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
import org.rebate.beans.VerifyBankcardBean;
import org.rebate.beans.VerifyBankcardResult;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.BankCard;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.BankCardRequest;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
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

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;


  /**
   * 银行卡四元素校验
   * 四元素:姓名、身份证、银行卡、手机号码
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
    
    //银行卡四元素,都不能为空
    if (ownerName == null || cardNum == null || idcard == null || reservedMobile == null) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing"));
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
      String result = ApiUtils.post(setting.getJuheVerifyBankcard4(),params);
      
      if (LogUtil.isDebugEnabled(this.getClass())) {
        LogUtil.debug(this.getClass(),
                "verifyCard", "request params: %s, result: %s", params.toString(), result);
      }
      
      ObjectMapper objectMapper = new ObjectMapper();
      VerifyBankcardBean verifyBankcardBean = objectMapper.readValue(result, VerifyBankcardBean.class);
      if (verifyBankcardBean == null) {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(message("rebate.request.failed"));
        return response;
      }
      
      if (verifyBankcardBean.getError_code() == 0 && verifyBankcardBean.getResult() != null) {
        VerifyBankcardResult verifyBankcardResult = verifyBankcardBean.getResult();
        if ("1".equals(verifyBankcardResult.getRes())) {//验证成功
          desc = verifyBankcardResult.getMessage();
        }else if ("2".equals(verifyBankcardResult.getRes())) {//验证不匹配
          response.setCode(CommonAttributes.FAIL_COMMON);
          response.setDesc(verifyBankcardResult.getMessage());
          return response;
        }
      }else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(verifyBankcardBean.getError_code() + ":" + verifyBankcardBean.getReason());
        return response;
      }
    } catch (Exception e) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(message("rebate.request.failed"));
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
    filters.add(userFilter);

    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("isDefault"));
    orderings.add(Ordering.desc("createDate"));

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    pageable.setFilters(filters);
    pageable.setOrders(orderings);

    Page<BankCard> page = bankCardService.findPage(pageable);
    String[] propertys = {"id", "cardNum", "bankName", "cardType", "bankLogo", "isDefault"};
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
