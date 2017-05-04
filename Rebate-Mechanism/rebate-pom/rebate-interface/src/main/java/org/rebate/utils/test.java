package org.rebate.utils;



public class test {

  public static void main(String[] args) throws Exception {

    System.out.println(ToolsUtils.createUserName());
    ToolsUtils.sendSmsMsg("17381945036", "【翼享生活】短信验证码是1256");
  }

  // public static void payWeChatNotify() {
  // Map<String, Object> xmlMap = new HashMap<String, Object>();
  // xmlMap.put("transaction_id", "4002542001201605185980297118");
  // xmlMap.put("nonce_str", "0BA2FD63FD3345178FB4C1EDCCF6F3F3");
  // xmlMap.put("bank_type", "CMBC_DEBIT");
  // xmlMap.put("openid", "oWP5ws14_TVPyBKKOY0s3ZZDy1sU");
  // xmlMap.put("sign", "F8FC5AE2F80AD8F0D69BDF38DF10F444");
  // xmlMap.put("fee_type", "CNY");
  // xmlMap.put("mch_id", "1249451301");
  // xmlMap.put("cash_fee", "10");
  // xmlMap.put("out_trade_no", "201605181006200000217539003_0620");
  // xmlMap.put("appid", "wx75b0585936937e4a");
  // xmlMap.put("total_fee", "10");
  // xmlMap.put("trade_type", "APP");
  // xmlMap.put("result_code", "SUCCESS");
  // xmlMap.put("time_end", "20160518100634");
  // xmlMap.put("is_subscribe", "N");
  // xmlMap.put("return_code", "SUCCESS");
  //
  // String xmlReturn = "";
  // if ("SUCCESS".equals(xmlMap.get("return_code"))) {
  // // 验证签名
  // String sign = xmlMap.get("sign") + "";
  // xmlMap.put("sign", "");
  // if (sign.equals(WeixinUtil.getSign(xmlMap))) {
  // // 查询是否处理过
  // // 处理回调结果
  // if ("SUCCESS".equals(xmlMap.get("result_code"))) {
  // // 系统订单编号
  // String out_trade_no = xmlMap.get("out_trade_no").toString();
  // // 支付的金额
  // String total_fee = xmlMap.get("total_fee").toString();
  // // 货币种类
  // String fee_type = xmlMap.get("fee_type").toString();
  // // 微信支付订单号
  // String transaction_id = xmlMap.get("transaction_id").toString();
  //
  // String payment = "wx";
  //
  // } else {
  // // 交易失败情况记录
  // }
  // }
  // // 返回处理结果xml
  // xmlReturn = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
  // } else {
  // // 返回处理结果xml
  // xmlReturn = "<xml><return_code>FAIL</return_code><return_msg>签名错误</return_msg></xml>";
  // }
  // }
}
