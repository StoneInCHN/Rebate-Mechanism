package org.rebate.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.JiupaiPayRequest;
import org.rebate.service.EndUserService;
import org.rebate.utils.JsonUtil;
import org.rebate.utils.TimeUtils;
import org.rebate.utils.TokenGenerator;
import org.rebate.utils.jiupaiPay.payTool.PayRpm;
import org.rebate.utils.jiupaiPay.utils.Base64Utils;
import org.rebate.utils.jiupaiPay.utils.HiDesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("jiupaiPayController")
@RequestMapping("/jiupaiPay")
public class JiupaiPayController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;


  /**
   * 九派支付-快捷绑卡
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmBindCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmBindCard(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String orderSn = req.getOrderSn();
    String cellPhoneNum = req.getCellPhoneNum();
    String idNo = req.getIdNo();
    String cardNo = req.getCardNo();
    String userName = req.getUserName();
    String token = req.getToken();
    Long userId = req.getUserId();

    LogUtil
        .debug(
            JiupaiPayController.class,
            "rpmBindCard",
            "jiupai pay rpmBindCard params. userId: %s,orderSn: %s,cellPhoneNum: %s,idNo: %s,userName: %s,cardNo: %s",
            userId, orderSn, cellPhoneNum, idNo, userName, cardNo);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());
    paramMap.put("orderId", orderSn);
    paramMap.put("idType", "00");// 00:身份证
    paramMap.put("idNo", HiDesUtils.desEnCode(idNo));
    paramMap.put("userName", userName);
    paramMap.put("phone", cellPhoneNum);
    paramMap.put("cardNo", HiDesUtils.desEnCode(cardNo));
    paramMap.put("cardType", "0");// 0:储蓄卡
    // paramMap.put("expireDate", "2512");
    // paramMap.put("cvn2", "210");

    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmBindCard(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmBindCard", "jiupai pay rpmBindCard. response: %s",
        resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 九派支付-解绑银行卡
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmUnbindCard", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse rpmUnbindCard(@RequestBody JiupaiPayRequest req) {
    BaseResponse response = new BaseResponse();
    String token = req.getToken();
    Long userId = req.getUserId();
    String contractId = req.getContractId();

    LogUtil.debug(JiupaiPayController.class, "rpmUnbindCard",
        "jiupai pay rpmUnbindCard params. userId: %s,contractId: %s", userId, contractId);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());
    paramMap.put("contractId", contractId);// 绑卡协议号


    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmUnbindCard(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmUnbindCard",
        "jiupai pay rpmUnbindCard. response: %s", resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 九派支付-查询支持绑卡的银行列表
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmBankList", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> rpmBankList(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    String token = req.getToken();
    Long userId = req.getUserId();

    LogUtil.debug(JiupaiPayController.class, "rpmBankList",
        "jiupai pay rpmBankList params. userId: %s", userId);

    Map<String, String> paramMap = new HashMap<String, String>();
    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmBankList(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmBankList", "jiupai pay rpmBankList. response: %s",
        resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    Map<String, Object> resulstMap = new HashMap<String, Object>();
    resulstMap.putAll(resMap);
    try {
      String bankList = resMap.get("bankList");
      String creditBankList = resMap.get("creditBankList");
      resulstMap.put("bankList",
          JsonUtil.getMapList4JsonStr(new String(Base64Utils.decode(bankList))));
      resulstMap.put("creditBankList",
          JsonUtil.getMapList4JsonStr(new String(Base64Utils.decode(creditBankList))));
    } catch (Exception e) {
      e.printStackTrace();
    }

    response.setMsg(resulstMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }



  /**
   * 查询用户绑卡信息
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmMemberCardList", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> rpmMemberCardList(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    String token = req.getToken();
    Long userId = req.getUserId();
    LogUtil.debug(JiupaiPayController.class, "rpmMemberCardList",
        "jiupai pay rpmMemberCardList params. userId: %s", userId);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());

    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmMemberCardList(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmMemberCardList",
        "jiupai pay rpmMemberCardList. response: %s", resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      String cardList = resMap.get("cardList");
      resultMap.put("cardList",
          JsonUtil.getMapList4JsonStr(new String(Base64Utils.decode(cardList))));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (resultMap.get("cardList") != null) {
      List<Map<String, Object>> bankList = (List<Map<String, Object>>) resultMap.get("cardList");
      for (Map<String, Object> map : bankList) {
        map.put("cardNo", HiDesUtils.desDeCode((String) map.get("cardNo")));
      }
    }

    response.setMsg(resultMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 九派支付-查询银行卡详细信息
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmCardInfo", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmCardInfo(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String cardNo = req.getCardNo();
    String token = req.getToken();
    Long userId = req.getUserId();

    LogUtil.debug(JiupaiPayController.class, "rpmCardInfo",
        "jiupai pay rpmCardInfo params. userId: %s,cardNo: %s", userId, cardNo);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("cardNo", HiDesUtils.desEnCode(cardNo));

    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmCardInfo(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmCardInfo", "jiupai pay rpmCardInfo. response: %s",
        resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 九派支付-查询银行卡签约状态
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmQueryCardBindStatus", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmQueryCardBindStatus(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String cardNo = req.getCardNo();
    String token = req.getToken();
    Long userId = req.getUserId();

    LogUtil.debug(JiupaiPayController.class, "rpmQueryCardBindStatus",
        "jiupai pay rpmQueryCardBindStatus params. userId: %s,cardNo: %s", userId, cardNo);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());
    paramMap.put("cardNo", HiDesUtils.desEnCode(cardNo));

    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmCardInfo(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmCardInfo", "jiupai pay rpmCardInfo. response: %s",
        resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 九派支付-快捷支付短信下发/重发(短信验证码6位随机数字,有效期60s,发送时间间隔60s)
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmQuickPaySms", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmQuickPaySms(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String contractId = req.getContractId();
    String token = req.getToken();
    Long userId = req.getUserId();

    LogUtil.debug(JiupaiPayController.class, "rpmQuickPaySms",
        "jiupai pay rpmQuickPaySms params. userId: %s,contractId: %s", userId, contractId);

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());
    paramMap.put("contractId", contractId);

    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmQuickPaySms(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmQuickPaySms",
        "jiupai pay rpmQuickPaySms. response: %s", resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 九派支付-快捷支付发起(九派验证短信)
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmQuickPayInit", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmQuickPayInit(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String orderSn = req.getOrderSn();
    String contractId = req.getContractId();
    String token = req.getToken();
    Long userId = req.getUserId();
    BigDecimal amount = req.getAmount();
    String clientIP = req.getClientIP();
    String goodsName = req.getGoodsName();

    LogUtil
        .debug(
            JiupaiPayController.class,
            "rpmQuickPayInit",
            "jiupai pay rpmQuickPayInit params. userId: %s,orderSn: %s,contractId: %s,amount: %s,clientIP: %s,goodsName: %s",
            userId, orderSn, contractId, amount, clientIP, goodsName);

    if (amount == null || !isCNYAmount(amount.toString())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.jiupai.pay.order.amount.invalid").getContent());
      return response;
    }

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("memberId", userId.toString());
    paramMap.put("orderId", orderSn);
    paramMap.put("contractId", contractId);
    paramMap.put("payType", "DQP");
    BigDecimal amountFen =
        amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
    paramMap.put("amount", amountFen.toString());
    paramMap.put("currency", "CNY");
    paramMap.put("orderTime", TimeUtils.getDateFormatString("yyyyMMddHHmmss", new Date()));
    paramMap.put("clientIP", clientIP != null ? clientIP : "127.0.0.1");
    paramMap.put("validUnit", "01");// 订单有效期单位 00-分 01-小时 02-日 03-月
    paramMap.put("validNum", "3");// 订单有效期数量
    paramMap.put("goodsName", goodsName);
    // paramMap.put("goodsDesc", "红米世代");
    paramMap.put("offlineNotifyUrl", setting.getJiupaiNotifyUrl());


    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmQuickPayInit(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmQuickPayInit",
        "jiupai pay rpmQuickPayInit. response: %s", resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 九派支付-快捷支付提交(九派验证短信)
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/rpmQuickPayCommit", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, String>> rpmQuickPayCommit(
      @RequestBody JiupaiPayRequest req) {
    ResponseOne<Map<String, String>> response = new ResponseOne<Map<String, String>>();
    String orderSn = req.getOrderSn();
    String contractId = req.getContractId();
    String token = req.getToken();
    Long userId = req.getUserId();
    BigDecimal amount = req.getAmount();
    String clientIP = req.getClientIP();
    String goodsName = req.getGoodsName();
    String checkCode = req.getCheckCode();

    LogUtil
        .debug(
            JiupaiPayController.class,
            "rpmQuickPayCommit",
            "jiupai pay rpmQuickPayCommit params. userId: %s,orderSn: %s,contractId: %s,amount: %s,checkCode: %s,clientIP: %s,goodsName: %s",
            userId, orderSn, contractId, amount, checkCode, clientIP, goodsName);

    if (amount == null || !isCNYAmount(amount.toString())) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.jiupai.pay.order.amount.invalid").getContent());
      return response;
    }

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("orderId", orderSn);
    paramMap.put("memberId", userId.toString());
    paramMap.put("contractId", contractId);
    paramMap.put("checkCode", checkCode);
    paramMap.put("payType", "DQP");
    paramMap.put("currency", "CNY");
    paramMap.put("orderTime", TimeUtils.getDateFormatString("yyyyMMddHHmmss", new Date()));
    paramMap.put("clientIP", clientIP != null ? clientIP : "127.0.0.1");
    paramMap.put("validUnit", "01");// 订单有效期单位 00-分 01-小时 02-日 03-月
    paramMap.put("validNum", "3");// 订单有效期数量
    paramMap.put("goodsName", goodsName);
    // paramMap.put("goodsDesc", "红米世代");
    BigDecimal amountFen =
        amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
    paramMap.put("amount", amountFen.toString());
    paramMap.put("offlineNotifyUrl", setting.getJiupaiNotifyUrl());


    PayRpm payRpm = new PayRpm();
    Map<String, String> resMap = payRpm.rpmQuickPayCommit(paramMap);
    LogUtil.debug(JiupaiPayController.class, "rpmQuickPayCommit",
        "jiupai pay rpmQuickPayCommit. response: %s", resMap);

    if (!"IPS00000".equals(resMap.get("rspCode")) || !"success".equals(resMap.get("verifySign"))) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(resMap.get("rspMessage") != null ? resMap.get("rspMessage") : Message.error(
          "rebate.jiupai.pay.request.fail").getContent());
      return response;
    }

    response.setMsg(resMap);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}
