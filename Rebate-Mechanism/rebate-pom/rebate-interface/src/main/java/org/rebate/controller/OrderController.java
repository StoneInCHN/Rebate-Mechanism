package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.SellerEvaluateImage;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
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
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller - 订单
 * 
 * @author huyong
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

  /**
   * 立即下单
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/pay", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> pay(@RequestBody OrderRequest req) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    String token = req.getToken();
    String payType = req.getPayType();
    BigDecimal amount = req.getAmount();
    Long sellerId = req.getSellerId();
    String remark = req.getRemark();
    Boolean isBeanPay = req.getIsBeanPay();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }
    Order order = orderService.create(userId, payType, amount, sellerId, remark, isBeanPay);
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil.debug(OrderController.class, "pay",
          "pay order. userId: %s,payType: %s,amount: %s,sellerId: %s,remark: %s", userId, payType,
          amount, sellerId, remark);
    }

    if (isBeanPay) {
      orderService.updateOrderforPayCallBack(order.getSn());
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("out_trade_no", order.getSn());
    response.setMsg(map);
    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
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
  public @ResponseBody BaseResponse sellerReply(@RequestBody SellerRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long evaluateId = req.getEntityId();
    String reply = req.getSellerReply();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    SellerEvaluate evaluate = sellerEvaluateService.find(evaluateId);
    evaluate.setSellerReply(reply);
    sellerEvaluateService.update(evaluate);
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil.debug(OrderController.class, "sellerReply",
          "seller reply user evaluate. evaluateId: %s,reply: %s", evaluateId, reply);
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
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
  public @ResponseBody BaseResponse userEvaluateOrder(UserEvaluateOrderRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long orderId = req.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Order order = orderService.find(orderId);
    EndUser endUser = endUserService.find(userId);
    SellerEvaluate evaluate = new SellerEvaluate();
    evaluate.setEndUser(endUser);
    evaluate.setScore(req.getScore());
    evaluate.setOrder(order);
    evaluate.setContent(req.getContent());

    List<SellerEvaluateImage> sellerEvaluateImages = new ArrayList<SellerEvaluateImage>();
    if (req.getEvaluateImage() != null && req.getEvaluateImage().size() > 0) {
      for (MultipartFile file : req.getEvaluateImage()) {
        SellerEvaluateImage image = new SellerEvaluateImage();
        image.setSource(fileService.saveImage(file, ImageType.ORDER_EVALUATE));
        sellerEvaluateImages.add(image);
      }
    }

    order.setEvaluate(evaluate);
    order.setStatus(OrderStatus.FINISHED);
    orderService.update(order);
    if (LogUtil.isDebugEnabled(OrderController.class)) {
      LogUtil.debug(OrderController.class, "User evaluate",
          "user evaluate for order %s,content: %s", orderId, req.getContent());
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
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
  @RequestMapping(value = "/evaluateDetail", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> evaluateDetail(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    String token = req.getToken();
    Long orderId = req.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Order order = orderService.find(orderId);

    SellerEvaluate evaluate = order.getEvaluate();

    String[] propertys =
        {"id", "endUser.userName", "score", "content", "sellerReply", "evaluateImages.source"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, evaluate);

    response.setMsg(result);

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
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
  public @ResponseBody ResponseMultiple<Map<String, Object>> getOrderUnderSeller(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long entityId = request.getEntityId();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Seller seller = sellerService.find(entityId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter sellerFilter = new Filter("seller", Operator.eq, seller);
    filters.add(sellerFilter);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<Order> page = orderService.findPage(pageable);
    String[] propertys =
        {"id", "seller.name", "createDate", "endUser.nickName", "amount", "endUser.cellPhoneNum",
            "endUser.userPhoto", "sn", "remark", "userScore", "status", "evaluate.sellerReply"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
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
  public @ResponseBody ResponseOne<Map<String, Object>> getEvaluateByOrder(
      @RequestBody BaseRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long orderId = request.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

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

    String newtoken = TokenGenerator.generateToken(request.getToken());
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
  public @ResponseBody ResponseMultiple<Map<String, Object>> getOrderUnderUser(
      @RequestBody OrderRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageNumber = request.getPageNumber();
    Integer pageSize = request.getPageSize();
    EndUser endUser = endUserService.find(userId);

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    List<Filter> filters = new ArrayList<Filter>();
    Filter endUserFilter = new Filter("endUser", Operator.eq, endUser);
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
        {"id", "sn", "seller.name", "userScore", "amount", "createDate", "remark",
            "evaluate.content", "evaluate.sellerReply", "status", "seller.storePictureUrl",
            "seller.address"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, orderPage.getContent());

    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
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

  /**
   * 获取订单详情
   * 
   * @return
   */
  @RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> orderDetail(@RequestBody BaseRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long orderId = request.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Order order = orderService.find(orderId);
    String[] propertys = {"id", "seller.name", "userScore", "amount", "evaluate"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, order);

    response.setMsg(result);
    // String newtoken = TokenGenerator.generateToken(request.getToken());
    // endUserService.createEndUserToken(newtoken, userId);
    // response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}
