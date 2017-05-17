package org.rebate.utils.yipay;


public class CommModel {

  private String MerchantID = "02510103030438531"; // 商户代码

  private String CommKey = "A33B9C17EFD2CDB6303A4512A0373154AD9F9C067937BA7A"; // 商户key

  private String CommPwd = "123321"; // 商户密码

  private String MerchantUrl = "http://webpay.bestpay.com.cn/pageTest.do"; // 前台通知地址

  private String BackMerchantUrl = "http://webpay.bestpay.com.cn/test.do"; // 后台通知地址

  /*
   * private String OrderID="20120620132345"; //订单号(当前时间)(格式如：yyyymmddhhmmss)
   * 
   * private String OrderReqTranSeq="20120620132345000001";
   * //订单流水号(当前时间+000001)(格式如：yyyymmddhhmmss000001)
   * 
   * private String OrderDate="20120620"; //订单日期
   */
  private String OrderID = CryptTool.getCurrentDate(); // 订单号(当前时间)(格式如：yyyymmddhhmmss)

  private String OrderReqTranSeq = CryptTool.getCurrentDate() + "000001"; // 订单流水号(当前时间+000001)(格式如：yyyymmddhhmmss000001)

  private String OrderDate = CryptTool.getTodayDate2(); // 订单日期

  private String ActionUrlSelectBank = "https://wappaywg.bestpay.com.cn/payWap.do"; // 请求网关平台地址(选择银行再进行交易)

  private String ActionUrlBank = "https://wappaywg.bestpay.com.cn/payWapDirect.do "; // 请求网关平台地址(直接进行交易)

  private String ReFundUrl = "http://telepay.bestpay.com.cn/services"; // 退款地址

  // private String ReqTime = "20120620143550"; //退款请求时间，格式yyyyMMddHHmmss
  private String ReqTime = CryptTool.getCurrentDate(); // 退款请求时间，格式yyyyMMddHHmmss

  private String OrderRefundID = "REFUNDID" + System.currentTimeMillis(); // 退款请求流水号

  public String getReqTime() {
    return ReqTime;
  }

  public void setReqTime(String reqTime) {
    ReqTime = reqTime;
  }

  public String getOrderRefundID() {
    return OrderRefundID;
  }

  public void setOrderRefundID(String orderRefundID) {
    OrderRefundID = orderRefundID;
  }

  public String getReFundUrl() {
    return ReFundUrl;
  }

  public void setReFundUrl(String reFundUrl) {
    ReFundUrl = reFundUrl;
  }

  public String getActionUrlSelectBank() {
    return ActionUrlSelectBank;
  }

  public void setActionUrlSelectBank(String actionUrlSelectBank) {
    ActionUrlSelectBank = actionUrlSelectBank;
  }

  public String getActionUrlBank() {
    return ActionUrlBank;
  }

  public void setActionUrlBank(String actionUrlBank) {
    ActionUrlBank = actionUrlBank;
  }


  public String getMerchantID() {
    return MerchantID;
  }

  public void setMerchantID(String merchantID) {
    MerchantID = merchantID;
  }

  public String getCommKey() {
    return CommKey;
  }

  public void setCommKey(String commKey) {
    CommKey = commKey;
  }

  public String getCommPwd() {
    return CommPwd;
  }

  public void setCommPwd(String commPwd) {
    CommPwd = commPwd;
  }

  public String getMerchantUrl() {
    return MerchantUrl;
  }

  public void setMerchantUrl(String merchantUrl) {
    MerchantUrl = merchantUrl;
  }

  public String getBackMerchantUrl() {
    return BackMerchantUrl;
  }

  public void setBackMerchantUrl(String backMerchantUrl) {
    BackMerchantUrl = backMerchantUrl;
  }

  public String getOrderID() {
    return OrderID;
  }

  public void setOrderID(String orderID) {
    OrderID = orderID;
  }

  public String getOrderReqTranSeq() {
    return OrderReqTranSeq;
  }

  public void setOrderReqTranSeq(String orderReqTranSeq) {
    OrderReqTranSeq = orderReqTranSeq;
  }

  public String getOrderDate() {
    return OrderDate;
  }

  public void setOrderDate(String orderDate) {
    OrderDate = orderDate;
  }


}
