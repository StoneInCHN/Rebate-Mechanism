package org.rebate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.BankCard;
import org.rebate.entity.UserAuth;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.beans.VerifyBankcardBean;
import org.rebate.json.beans.VerifyBankcardResult;
import org.rebate.json.request.BankCardRequest;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.UserAuthService;
import org.rebate.utils.ApiUtils;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller - BankCard
 * 
 *
 */
@Controller("bankCardController")
@RequestMapping("/bankCard")
public class BankCardController extends MobileBaseController {

  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;

  @Resource(name = "userAuthServiceImpl")
  private UserAuthService userAuthService;


  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;



  /**
   * 获取当前用户姓名和身份证
   * 
   * @return
   */
  @RequestMapping(value = "/getIdCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> getIdCard(
      @RequestBody BankCardRequest request) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = request.getUserId();
    String token = request.getToken();
    UserAuth userAuth = userAuthService.getUserAuth(userId, true);
    String[] propertys = {"id", "realName", "idCardNo"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, userAuth);

    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setDesc(message("rebate.find.success"));
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 获取用户默认银行卡
   * 
   * @return
   */
  @RequestMapping(value = "/getDefaultCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> getDefaultCard(
      @RequestBody BankCardRequest request) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = request.getUserId();
    String token = request.getToken();
    if (userId == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing") + ":userId");
      return response;
    }
    BankCard bankCard = bankCardService.getDefaultCard(userId);
    if (bankCard == null) {
      LogUtil.debug(this.getClass(), "getDefaultCard", "Cannot find default bankcard for user: %s",
          userId);
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(message("rebate.bankCard.cannot.find.default"));
      return response;
    }
    String[] propertys =
        {"id", "cardNum", "bankName", "cardType", "idCard", "bankLogo", "isDefault"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, bankCard);

    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setDesc(message("rebate.find.success"));
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 设置默认银行卡
   * 
   * @return
   */
  @RequestMapping(value = "/updateCardDefault", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse updateCardDefault(@RequestBody BankCardRequest request) {
    BaseResponse response = new BaseResponse();
    Long userId = request.getUserId();
    String token = request.getToken();

    Long entityId = request.getEntityId();
    if (entityId == null || userId == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing"));
      return response;
    }
    BankCard bankCard = bankCardService.find(entityId);
    if (bankCard == null) {
      LogUtil.debug(this.getClass(), "updateCard", "Cannot find bankcard for entityId: %s",
          entityId);
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(message("rebate.find.failed"));
      return response;
    }
    try {
      bankCardService.updateCardDefault(bankCard, userId);
    } catch (Exception e) {
      e.printStackTrace();
      LogUtil.debug(this.getClass(), "updateCard", "Update default bank card failed:",
          e.getMessage());
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(message("rebate.update.failed") + ":" + e.getMessage());
      return response;
    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setDesc(message("rebate.update.success"));
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 银行卡四元素校验 四元素:姓名、身份证、银行卡、手机号码
   * 
   * @return
   */
  @RequestMapping(value = "/verifyCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse verifyCard(@RequestBody BankCardRequest request) {

    BaseResponse response = new BaseResponse();

    Long userId = request.getUserId();
    String token = request.getToken();

    String ownerName = request.getOwnerName();
    String cardNum = request.getCardNum();
    String idcard = request.getIdCard();
    String reservedMobile = request.getReservedMobile();

    // 银行卡四元素,都不能为空
    if (ownerName == null || cardNum == null || idcard == null || reservedMobile == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing"));
      return response;
    }

    // 验证失败记录缓存，相同参数的失败记录不向第三方发送请求
    String localResult =
        bankCardService.isVerifyFailedRecord(ownerName, cardNum, idcard, reservedMobile);
    if (localResult != null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(localResult);
      return response;
    }

    // 如果存在，表示以前已经验证过了，不用再验证
    boolean exist =
        bankCardService.exists(Filter.eq("cardNum", cardNum), Filter.eq("ownerName", ownerName),
            Filter.eq("idCard", idcard), Filter.eq("reservedMobile", reservedMobile));
    if (exist) {
      String newtoken = TokenGenerator.generateToken(token);
      endUserService.createEndUserToken(newtoken, userId);
      response.setToken(newtoken);
      response.setDesc(message("rebate.bankCard.verify.success"));
      response.setCode(CommonAttributes.SUCCESS);
      return response;
    }

    Map<String, Object> params = new HashMap<String, Object>();
    params.put("key", setting.getJuheKey());
    params.put("bankcard", cardNum);
    params.put("realname", ownerName);
    params.put("idcard", idcard);
    params.put("mobile", reservedMobile);

    String desc = null;
    try {
      String result = ApiUtils.post(setting.getJuheVerifyBankcard4(), params);

      LogUtil.debug(this.getClass(), "verifyCard", "request params: %s, result: %s",
          params.toString(), result);

      ObjectMapper objectMapper = new ObjectMapper();
      VerifyBankcardBean verifyBankcardBean =
          objectMapper.readValue(result, VerifyBankcardBean.class);
      if (verifyBankcardBean == null) {
        LogUtil.debug(this.getClass(), "verifyCard",
            "Cannot convert from result to VerifyBankcardBean!!");
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(message("rebate.request.failed"));
        return response;
      }

      if (verifyBankcardBean.getError_code() == 0 && verifyBankcardBean.getResult() != null) {
        VerifyBankcardResult verifyBankcardResult = verifyBankcardBean.getResult();
        if ("1".equals(verifyBankcardResult.getRes())) {// 验证成功
          desc = verifyBankcardResult.getMessage();
        } else if ("2".equals(verifyBankcardResult.getRes())) {// 验证不匹配
          bankCardService.genVerifyFailedRecord(ownerName, cardNum, idcard, reservedMobile,
              verifyBankcardResult.getMessage());
          response.setCode(CommonAttributes.FAIL_COMMON);
          response.setDesc(verifyBankcardResult.getMessage());
          return response;
        }
      } else {
        bankCardService.genVerifyFailedRecord(ownerName, cardNum, idcard, reservedMobile,
            verifyBankcardBean.getReason());
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(verifyBankcardBean.getError_code() + ":" + verifyBankcardBean.getReason());
        return response;
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(message("rebate.request.failed") + ":" + e.getMessage());
      return response;
    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setDesc(desc);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 添加银行卡
   * 
   * @return
   */
  @RequestMapping(value = "/addCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse addCard(@RequestBody BankCardRequest request) {

    BaseResponse response = new BaseResponse();

    Long userId = request.getUserId();
    String token = request.getToken();
    String smsCode = request.getSmsCode();
    String reservedMobile = request.getReservedMobile();

    String ownerName = request.getOwnerName();
    String cardNum = request.getCardNum();
    String idcard = request.getIdCard();

    // 添加银行卡,四元素,都不能为空
    if (ownerName == null || cardNum == null || idcard == null || reservedMobile == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing"));
      return response;
    }

    SMSVerificationCode smsVerficationCode = endUserService.getSmsCode(reservedMobile);
    if (smsVerficationCode == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.sms.invaliable").getContent());
      return response;
    } else {
      String code = smsVerficationCode.getSmsCode();
      String timeoutToken = smsVerficationCode.getTimeoutToken();
      if (timeoutToken != null
          && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
        if (!smsCode.equals(code)) {
          response.setCode(CommonAttributes.FAIL_COMMON);
          response.setDesc(Message.error("rebate.sms.token.error").getContent());
          return response;
        } else {
          boolean existBankCard =
              bankCardService.exists(Filter.eq("cardNum", request.getCardNum()),
                  Filter.eq("delStatus", false));
          if (existBankCard) {// 不能添加重复的银行卡
            response.setCode(CommonAttributes.FAIL_COMMON);
            response.setDesc(Message.error("rebate.bankCard.exists.error").getContent());
            return response;
          }
          endUserService.deleteSmsCode(reservedMobile);
          bankCardService.addCard(request);
          if (LogUtil.isDebugEnabled(BankCardController.class)) {
            LogUtil
                .debug(
                    BankCardController.class,
                    "addCard",
                    "endUser add the bank card. userId: %s, ownerName: %s, cardNum: %s, bankName: %s, cardType: %s, isDefault: %s, reservedMobile: %s, bankLogo: %s",
                    userId, request.getOwnerName(), request.getCardNum(), request.getBankName(),
                    request.getCardType(), request.getIsDefault(), request.getReservedMobile(),
                    request.getBankLogo());
          }
        }
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.error("rebate.sms.token.timeout").getContent());
        return response;
      }

    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }



  /**
   * 删除银行卡
   * 
   * @return
   */
  @RequestMapping(value = "/delCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse delCard(@RequestBody BaseRequest request) {

    BaseResponse response = new BaseResponse();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long entityId = request.getEntityId();

    bankCardService.delBankCardById(entityId);
    if (LogUtil.isDebugEnabled(BankCardController.class)) {
      LogUtil.debug(BankCardController.class, "delCard",
          "endUser delete the bank card. userId: %s, cardId: %s", userId, entityId);
    }
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户银行卡列表
   * 
   * @return
   */
  @RequestMapping(value = "/myCardList", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<Map<String, Object>> myCardList(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();

    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("endUser", Operator.eq, userId);
    Filter statusFilter = new Filter("delStatus", Operator.eq, false);
    filters.add(userFilter);
    filters.add(statusFilter);

    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("isDefault"));
    orderings.add(Ordering.desc("createDate"));

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrders(orderings);

    Page<BankCard> page = bankCardService.findPage(pageable);
    String[] propertys =
        {"id", "cardNum", "bankName", "cardType", "idCard", "bankLogo", "isDefault"};
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

}
