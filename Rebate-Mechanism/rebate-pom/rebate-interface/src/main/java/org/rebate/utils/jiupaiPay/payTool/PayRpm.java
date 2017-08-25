package org.rebate.utils.jiupaiPay.payTool;

import java.util.HashMap;
import java.util.Map;

import org.rebate.utils.jiupaiPay.utils.HiDesUtils;
import org.rebate.utils.jiupaiPay.utils.RSASignUtil;


public class PayRpm extends PayCase {


  public PayRpm() {

  }

  public void main(String[] args) {
    System.out.print(HiDesUtils.desDeCode("4ff3401d425d0a756a1e80486f391746710ef975a9577d9b"));
    PayRpm test = new PayRpm();
    // test.testRpmCardSignStatusQuery();
    // test.testRpmBankList();
    // test.testRpmBindCard();
    // test.testRpmUnbindCard();
    // test.testRpmQuickPaySms();
    // test.testRpmQuickPay();
    // test.testRpmPayQuery();
    // test.testRpmRefund();
    // test.testRpmRefundQuery();
    // test.testRpmMemberCardList();
    // test.testRpmCardInfo();
    // test.testRpmQuickPayInit();
    // test.testRpmQuickPayCommit();
  }

  public Map<String, String> rpmCardSignStatusQuery(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmQueryCardBindStatus");// 接口类型
    // 1、具体业务请求参数
    // paramMap.put("memberId", "12345678");
    // paramMap.put("cardNo", HiDesUtils.desEnCode("6222080200011462691"));
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("hasSign", RSASignUtil.getValue(res, "hasSign"));
    retMap.put("contractId", RSASignUtil.getValue(res, "contractId"));
    return validRetSign(retMap, res);
  }


  /**
   * 快捷绑卡
   * 
   * @return
   */
  public Map<String, String> rpmBindCard(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmBindCard");// 接口类型
    // paramMap.put("memberId", "123456789");
    // paramMap.put("orderId", "asd");
    // paramMap.put("idType", "00");
    // paramMap.put("idNo", HiDesUtils.desEnCode("110103198306200619"));
    // paramMap.put("userName", "张三");
    // paramMap.put("phone", "18910891027");
    // paramMap.put("cardNo", HiDesUtils.desEnCode("6222080200011462691"));
    // paramMap.put("cardType", "0");
    // paramMap.put("expireDate", "2512");
    // paramMap.put("cvn2", "210");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("contractId", RSASignUtil.getValue(res, "contractId"));
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("bankName", RSASignUtil.getValue(res, "bankName"));
    retMap.put("bankAbbr", RSASignUtil.getValue(res, "bankAbbr"));
    retMap.put("cardType", RSASignUtil.getValue(res, "cardType"));
    retMap.put("extension", RSASignUtil.getValue(res, "extension"));
    return validRetSign(retMap, res);
  }

  /**
   * 解绑银行卡
   * 
   * @return
   */
  public Map<String, String> rpmUnbindCard(Map<String, String> paramMap) {
    paramMap.put("service", "rpmUnbindCard");// 接口类型
    // 1、具体业务请求参数
    // paramMap.put("contractId", "201704200000013920");
    // paramMap.put("memberId", "123456789");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("memberId", RSASignUtil.getValue(res, "memberId"));
    return validRetSign(retMap, res);
  }

  /**
   * 快捷支付短信下发/重发(短信验证码6位随机数字,有效期60s,发送时间间隔60s)
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmQuickPaySms(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmQuickPaySms");// 接口类型
    // 1、具体业务请求参数
    // paramMap.put("contractId", "201704230000016578");
    // paramMap.put("memberId", "110000002601");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("phone", RSASignUtil.getValue(res, "phone"));
    return validRetSign(retMap, res);
  }


  public Map<String, String> rpmQuickPay(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmQuickPay");// 接口类型/商户自验短信
    // 1、具体业务请求参数
    paramMap.put("orderId", org.apache.commons.lang3.time.DateFormatUtils.format(
        new java.util.Date(), "yyyyMMddHHmmss"));
    paramMap.put("contractId", "201704070000013094");
    paramMap.put("memberId", "123456789");
    paramMap.put("checkCode", "");
    paramMap.put("payType", "DQP");
    paramMap.put("amount", "1189");
    paramMap.put("currency", "CNY");
    paramMap.put("orderTime", org.apache.commons.lang3.time.DateFormatUtils.format(
        new java.util.Date(), "yyyyMMddHHmmss"));
    paramMap.put("clientIP", "100.202.113.87");
    paramMap.put("validUnit", "01");
    paramMap.put("validNum", "1");
    paramMap.put("goodsName", "手机");
    paramMap.put("goodsDesc", "国产手机 华为P10");
    paramMap.put("offlineNotifyUrl", "http://100.66.155.60:8090/payNotice.jsp");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("orderSts", RSASignUtil.getValue(res, "orderSts"));
    retMap.put("payOrderId", RSASignUtil.getValue(res, "payOrderId"));
    retMap.put("acDate", RSASignUtil.getValue(res, "acDate"));
    return validRetSign(retMap, res);
  }

  public void rpmPayQuery() {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmPayQuery");// 接口类型
    // 1、具体业务请求参数
    paramMap.put("orderId", "20170414153125");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("memberId", RSASignUtil.getValue(res, "memberId"));
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("amount", RSASignUtil.getValue(res, "amount"));
    retMap.put("orderTime", RSASignUtil.getValue(res, "orderTime"));
    retMap.put("payResult", RSASignUtil.getValue(res, "payResult"));
    retMap.put("bankAbbr", RSASignUtil.getValue(res, "bankAbbr"));
    retMap.put("payTime", RSASignUtil.getValue(res, "payTime"));
    retMap.put("payOrderId", RSASignUtil.getValue(res, "payOrderId"));
    retMap.put("acDate", RSASignUtil.getValue(res, "acDate"));
    retMap.put("fee", RSASignUtil.getValue(res, "fee"));
    validRetSign(retMap, res);
  }

  public void rpmRefund() {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmRefund");// 接口类型
    // 1、具体业务请求参数
    paramMap.put("refundAmount", "10");
    paramMap.put("oriOrderId", "20170427176427119");
    paramMap.put("orderId", "20170428222403001");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("refundAmount", RSASignUtil.getValue(res, "refundAmount"));
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("orderSts", RSASignUtil.getValue(res, "orderSts"));
    validRetSign(retMap, res);
  }

  public void rpmRefundQuery() {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmRefundQuery");// 接口类型
    // 1、具体业务请求参数
    paramMap.put("oriOrderId", "1493391197196120502");
    paramMap.put("orderId", "20170428222433001");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("refundAmount", RSASignUtil.getValue(res, "refundAmount"));
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("orderSts", RSASignUtil.getValue(res, "orderSts"));
    validRetSign(retMap, res);
  }

  /**
   * 查询支持绑卡的银行列表
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmBankList(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmBankList");// 接口类型
    // 1、具体业务请求参数

    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("bankList", RSASignUtil.getValue(res, "bankList"));
    retMap.put("creditBankList", RSASignUtil.getValue(res, "creditBankList"));// 网关返回的还是加密的
    return validRetSign(retMap, res);
  }

  /**
   * 查询用户绑卡信息
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmMemberCardList(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmMemberCardList");// 接口类型
    // 1、具体业务请求参数
    // paramMap.put("memberId", "123456789");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("memberId", RSASignUtil.getValue(res, "memberId"));
    retMap.put("cardList", RSASignUtil.getValue(res, "cardList"));
    return validRetSign(retMap, res);
  }

  /**
   * 查询银行卡详细信息
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmCardInfo(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmCardInfo");// 接口类型
    // 1、具体业务请求参数
    // paramMap.put("cardNo", HiDesUtils.desEnCode("6225880154901171"));
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("cardNo", RSASignUtil.getValue(res, "cardNo"));
    retMap.put("cardType", RSASignUtil.getValue(res, "cardType"));
    retMap.put("bankName", RSASignUtil.getValue(res, "bankName"));
    retMap.put("bankAbbr", RSASignUtil.getValue(res, "bankAbbr"));
    return validRetSign(retMap, res);
  }

  /**
   * 快捷支付发起(九派验证短信)
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmQuickPayInit(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmQuickPayInit");// 接口类型、快捷支付发起（我方验证短信）
    // 1、具体业务请求参数
    // paramMap.put("memberId", "123456789");
    // paramMap.put("orderId", "123456789");
    // paramMap.put("contractId", "201704070000013094");
    // paramMap.put("payType", "DQP");
    // paramMap.put("amount", "1000");
    // paramMap.put("currency", "CNY");
    // paramMap.put("orderTime", "20170704190500");
    // paramMap.put("clientIP", "100.202.103.87");
    // paramMap.put("validUnit", "01");
    // paramMap.put("validNum", "3");
    // paramMap.put("goodsName", "手机");
    // paramMap.put("goodsDesc", "红米世代");
    // paramMap.put("offlineNotifyUrl", "http://100.66.155.60:8090/payNotice.jsp");

    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("payOrderId", RSASignUtil.getValue(res, "payOrderId"));
    retMap.put("acDate", RSASignUtil.getValue(res, "acDate"));
    return validRetSign(retMap, res);
  }

  /**
   * 快捷支付提交(九派验证短信)
   * 
   * @param paramMap
   * @return
   */
  public Map<String, String> rpmQuickPayCommit(Map<String, String> paramMap) {
    // Map<String, String> paramMap = new HashMap<>();
    paramMap.put("service", "rpmQuickPayCommit");// 接口类型、快捷支付发起（我方验证短信）
    // 1、具体业务请求参数
    // paramMap.put("orderId", "123456789");
    // paramMap.put("memberId", "123456789");
    // paramMap.put("contractId", "201704070000013094");
    // paramMap.put("checkCode", "111111");
    // paramMap.put("payType", "DQP");
    // paramMap.put("currency", "CNY");
    // paramMap.put("orderTime", "20170704190709");
    // paramMap.put("clientIP", "100.202.103.87");
    // paramMap.put("validUnit", "01");
    // paramMap.put("validNum", "3");
    // paramMap.put("goodsName", "手机");
    // paramMap.put("goodsDesc", "红米世代");
    // paramMap.put("amount", "1000");
    // paramMap.put("offlineNotifyUrl", "http://100.66.155.60:8090/payNotice.jsp");
    // 2、请求流程
    String res = bizService(paramMap);
    // 3、验证返回签名
    Map<String, String> retMap = new HashMap<>();
    retMap.put("orderId", RSASignUtil.getValue(res, "orderId"));
    retMap.put("orderSts", RSASignUtil.getValue(res, "orderSts"));
    retMap.put("payOrderId", RSASignUtil.getValue(res, "payOrderId"));
    retMap.put("acDate", RSASignUtil.getValue(res, "acDate"));
    return validRetSign(retMap, res);
  }
}
