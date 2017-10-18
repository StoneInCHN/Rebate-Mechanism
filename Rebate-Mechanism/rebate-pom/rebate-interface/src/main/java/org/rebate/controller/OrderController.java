package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.SellerEvaluateImage;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.OrderRequest;
import org.rebate.json.request.SellerRequest;
import org.rebate.json.request.UserEvaluateOrderRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.FileService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerEvaluateService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.PayUtil;
import org.rebate.utils.TokenGenerator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 订单
 * 
 *
 */
@Controller("orderController")
@RequestMapping("/order")
public class OrderController extends MobileBaseController {

  @Resource(name = "orderServiceImpl")
  private OrderService orderService;
  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;
  @Resource(name = "sellerEvaluateServiceImpl")
  private SellerEvaluateService sellerEvaluateService;
  @Resource(name = "fileServiceImpl")
  private FileService fileService;
  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;
  @Resource(name = "taskExecutor")
  private TaskExecutor taskExecutor;


  /**
   * 普通支付获取支付信息
   * 
   * @return
   */
  @RequestMapping(value = "/getPayInfo", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> getPayInfo(@RequestBody OrderRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    SystemConfigKey configKey = request.getConfigKey();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("isBeanPay", false);
    List<SystemConfig> configList = systemConfigService.getConfigsByKey(configKey);
    List<SystemConfig> payTypes = new ArrayList<SystemConfig>();
    for (SystemConfig systemConfig : configList) {
      if (systemConfig.getId().intValue() == 6) {// 乐豆支付id固定为6
        map.put("isBeanPay", true);
      } else {
        payTypes.add(systemConfig);
      }
    }
    String[] propertys = {"id", "configValue"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, payTypes);
    map.put("payType", result);
    EndUser endUser = endUserService.find(userId);
    map.put("userCurLeScore", endUser.getCurLeScore());
    map.put("userCurLeBean", endUser.getCurLeBean());
    response.setMsg(map);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 计算用户可抵扣的乐豆
   * 
   * @return
   */
  @RequestMapping(value = "/calLeBeanDeduction", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> calLeBeanDeduction(
      @RequestBody OrderRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    Long sellerId = request.getSellerId();
    BigDecimal amount = request.getAmount();

    EndUser endUser = endUserService.find(userId);
    Seller seller = sellerService.find(sellerId);
    BigDecimal curLeBean = endUser.getCurLeBean();
    BigDecimal deductLeBean =
        amount.multiply(
            new BigDecimal(10).subtract(seller.getDiscount()).divide(new BigDecimal(10))).setScale(
            4, BigDecimal.ROUND_HALF_UP);

    Map<String, Object> map = new HashMap<String, Object>();
    if (curLeBean.compareTo(deductLeBean) >= 0) {
      map.put("deductLeBean", deductLeBean);
    } else {
      map.put("deductLeBean", curLeBean);
    }

    response.setMsg(map);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 立即下单
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/pay", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> pay(@RequestBody OrderRequest req,
      HttpServletRequest httpReq) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    String token = req.getToken();
    String payType = req.getPayType();
    String payTypeId = req.getPayTypeId();
    BigDecimal amount = req.getAmount();
    Long sellerId = req.getSellerId();
    String remark = req.getRemark();
    Boolean isBeanPay = req.getIsBeanPay();
    // String password = req.getPayPwd();
    BigDecimal deductLeBean = req.getDeductLeBean();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    if (orderService.isOverSellerLimitAmount(sellerId, amount)) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.payOrder.seller.limitAmount").getContent());
      return response;
    }

    BigDecimal payAmount = req.getAmount();
    if (isBeanPay) {// 乐豆抵扣
      if (deductLeBean.compareTo(amount) < 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.payOrder.deductLeBean.error").getContent());
        return response;
      }
      payAmount = amount.subtract(deductLeBean);
      EndUser endUser = endUserService.find(userId);
      if (endUser.getCurLeBean().compareTo(deductLeBean) < 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.payOrder.curLeBean.insufficient").getContent());
        return response;
      }
    }

    if ("5".equals(payTypeId)) {// 乐分支付固定id为5
      EndUser endUser = endUserService.find(userId);
      if (endUser.getCurLeScore().compareTo(payAmount) < 0) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.payOrder.curLeScore.insufficient",
            endUser.getCurLeScore()).getContent());
        return response;
      }
    }

    final Order order =
        orderService.create(userId, payTypeId, payType, amount, sellerId, remark, isBeanPay,
            deductLeBean);
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil
          .debug(
              OrderController.class,
              "pay",
              "pay order. userId: %s,payType: %s,payTypeId: %s,amount: %s,sellerId: %s,remark: %s,isBeanPay: %s",
              userId, payType, payTypeId, amount, sellerId, remark, isBeanPay);
    }

    try {
      if ("1".equals(payTypeId)) {// 微信支付
        BigDecimal weChatPrice = payAmount.multiply(new BigDecimal(100));
        response =
            PayUtil.wechat(order.getSn(), order.getSeller().getName(), httpReq.getRemoteAddr(),
                order.getId().toString(), weChatPrice.intValue() + "");
        response.getMsg().put("encourageAmount", order.getEncourageAmount());
      } else if ("2".equals(payTypeId)) {// 支付宝支付
        Map<String, Object> map = new HashMap<String, Object>();
        String orderStr =
            PayUtil.alipay(order.getSn(), order.getSeller().getName(), order.getSeller().getName(),
                payAmount.toString());
        map.put("orderStr", orderStr);
        map.put("out_trade_no", order.getSn());
        map.put("encourageAmount", order.getEncourageAmount());
        response.setMsg(map);
        response.setCode(CommonAttributes.SUCCESS);
      } else if ("3".equals(payTypeId)) {// 翼支付
        response =
            PayUtil.yiPay(order.getSn(), order.getSeller().getName(), httpReq.getRemoteAddr(),
                order.getId().toString(), payAmount, userId.toString());
        response.getMsg().put("encourageAmount", order.getEncourageAmount());
      } else if ("4".equals(payTypeId)) {// 通联银行卡快捷支付
        BigDecimal weChatPrice = payAmount.multiply(new BigDecimal(100));
        response =
            PayUtil.allinpayH5(order.getSn(), order.getSeller().getName(), userId.toString(), order
                .getId().toString(), weChatPrice.intValue() + "");
        response.getMsg().put("encourageAmount", order.getEncourageAmount());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // orderService.updateOrderforPayCallBack(order.getSn());
    if (isBeanPay) {// 乐豆支付
      taskExecutor.execute(new Runnable() {
        public void run() {
          orderService.updateOrderforPayCallBack(order.getSn());
        }
      });
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("out_trade_no", order.getSn());
      map.put("user_cur_leBean", order.getEndUser().getCurLeBean());
      response.setMsg(map);
      response.setCode(CommonAttributes.SUCCESS);
    }
    response.getMsg().put("orderId", order.getId());

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 商家回复用户评价
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/sellerReply", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody BaseResponse sellerReply(@RequestBody SellerRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long evaluateId = req.getEntityId();
    String reply = req.getSellerReply();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    SellerEvaluate evaluate = sellerEvaluateService.find(evaluateId);
    evaluate.setSellerReply(reply);
    sellerEvaluateService.update(evaluate);
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil.debug(OrderController.class, "sellerReply",
          "seller reply user evaluate. evaluateId: %s,reply: %s", evaluateId, reply);
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 用户评价订单
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/userEvaluateOrder", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse userEvaluateOrder(UserEvaluateOrderRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long orderId = req.getEntityId();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    orderService.evaluateOrder(orderId, userId, req.getScore(), req.getContent(),
        req.getEvaluateImage());
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil.debug(OrderController.class, "User evaluate",
          "user evaluate for orderId: %s,score: %s,content: %s", orderId, req.getScore(),
          req.getContent());
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }


  /**
   * 用户订单详情
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/detail", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> detail(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long orderId = req.getEntityId();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    Order order = orderService.find(orderId);

    String[] propertys =
        {"id", "sn", "seller.name", "seller.id", "userScore", "amount", "createDate", "remark",
            "evaluate.content", "evaluate.sellerReply", "status", "seller.storePictureUrl",
            "seller.address"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, order);
    response.setMsg(result);

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 获取商户订单
   * 
   * @return
   */
  @RequestMapping(value = "/getOrderUnderSeller", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getOrderUnderSeller(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long entityId = request.getEntityId();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    List<Filter> filters = new ArrayList<Filter>();
    Filter sellerFilter = new Filter("seller", Operator.eq, entityId);
    filters.add(sellerFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<Order> page = orderService.findPage(pageable);
    String[] propertys =
        {"id", "seller.name", "createDate", "endUser.nickName", "sellerScore", "amount",
            "endUser.cellPhoneNum", "endUser.userPhoto", "sn", "remark", "userScore", "status",
            "evaluate.sellerReply"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取订单评价内容
   * 
   * @return
   */
  @RequestMapping(value = "/getEvaluateByOrder", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> getEvaluateByOrder(
      @RequestBody BaseRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long orderId = request.getEntityId();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    SellerEvaluate evaluate = sellerEvaluateService.getEvaluateByOrder(orderId);
    String[] propertys =
        {"id", "endUser.userPhoto", "endUser.nickName", "order.amount", "score", "content",
            "sellerReply"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, evaluate);
    List<String> evaluateImgs = new ArrayList<String>();
    for (SellerEvaluateImage evaluateImage : evaluate.getEvaluateImages()) {
      evaluateImgs.add(evaluateImage.getSource());
    }
    result.put("evaluateImages", evaluateImgs);
    response.setMsg(result);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取用户订单
   * 
   * @return
   */
  @RequestMapping(value = "/getOrderUnderUser", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getOrderUnderUser(
      @RequestBody OrderRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageNumber = request.getPageNumber();
    Integer pageSize = request.getPageSize();

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    List<Filter> filters = new ArrayList<Filter>();
    Filter endUserFilter = new Filter("endUser", Operator.eq, userId);
    filters.add(endUserFilter);

    if (request.getOrderStatus() != null) {
      Filter statusFilter = new Filter("status", Operator.eq, request.getOrderStatus());
      filters.add(statusFilter);
    } else {
      Filter statusFilter = new Filter("status", Operator.ne, OrderStatus.UNPAID);
      filters.add(statusFilter);
    }


    pageable.setFilters(filters);

    Page<Order> orderPage = orderService.findPage(pageable);
    String[] propertys =
        {"id", "sn", "seller.name", "seller.id", "userScore", "amount", "createDate", "remark",
            "evaluate.content", "evaluate.sellerReply", "status", "seller.storePictureUrl",
            "seller.address"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, orderPage.getContent());

    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) orderPage.getTotal());
    response.setPage(pageInfo);

    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}
