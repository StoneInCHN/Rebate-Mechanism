package org.rebate.utils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.Setting;
import org.rebate.utils.alipay.config.AlipayConfig;
import org.rebate.utils.alipay.sign.RSA;
import org.rebate.utils.allinpay.PayNotify;
import org.rebate.utils.wechat.WeixinUtil;



public class test {


  public static void main(String[] args) throws Exception {
    Setting setting = SettingUtils.get();
    String serverPrivateKey = setting.getServerPrivateKey();
    String serverPublicKey = setting.getServerPublicKey();
    KeyPair keyPair = KeyGenerator.generateKeys();
    // System.out.println(RSAHelper.getKeyString(keyPair.getPublic()));
    // System.out.println(RSAHelper.getKeyString(keyPair.getPrivate()));
    // String pwd = KeyGenerator.encrypt("111111", RSAHelper.getPublicKey(serverPublicKey));
    // System.out.println(pwd);
    // String pwd1 = KeyGenerator.encrypt("111111", RSAHelper.getPublicKey(serverPublicKey));
    // System.out.println(pwd1);
    System.out.println(DigestUtils.md5Hex("luyBL3"));
    System.out.println(KeyGenerator.encrypt("111111", RSAHelper.getPublicKey(serverPublicKey)));
    String password =
        "Od3XgLxsVHCeIPzx4oikeT3HH02iOzAHimS8xQTDr8Veq66O2u8ZCskmq5ewXQD3PD7xHwCTOLJfPpIMKXJMhUrogJLVXvDMqUOm7u5L2TF7fIGAYjzS6RRIgzSUtZE+3o+sejo5PvDoRqLPDy1s9svUznLYZkyPIIhv8VhGguU=";
    System.out.println(KeyGenerator.decrypt(password, RSAHelper.getPrivateKey(serverPrivateKey)));
    BigDecimal a = new BigDecimal("22.00");
    System.out.println(a.setScale(0, BigDecimal.ROUND_DOWN));
    // System.out.println(ToolsUtils.createUserName());
    // ToolsUtils.sendSmsMsg("17381945036;17708077387", "翼享生活: 您的验证码是1111，如非本人操作请忽略。");
    String key =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4Lx8cgCyPVKxjdr3IBch3JG+/AreSbjhLqN0pM4HlPEkQGmoihzptSVe+o1ywALgkul/1e5fUljqAGAbtyhmxk9dRs3QddKposOzStoX9ReTWmMJPtC5cNXbclg7QbYKrzw25sUkxGHMP9KInZV7qAnhwFg5TDmk87Lab5JrmPAgMBAAECgYBJ0uWuEmKBbuMo66Slkq4zp9W0UpK6RTrxWg5UTHy+YtrjlfUdsk1BxMAhMuMy8nbvlivwfpaxnf9DZlHx8NEKSY+vpj/jZyir1Nscf9Dya7ak5EgvBPBE/5KtJqpx7WD1qJjAoaumTh0ljy9B5G88RcLFNA2U27NjchCO9SjmIQJBAPn/g8omfRGHJoBJHXsIBYripnCld7J1V/Ekyjb8r4rQtH8bS/NK30CR1ToPD0gjcEDTgYkLkONqUUmJ9EWBwOcCQQDCm80BkFTt0TxjxqLSpyEJcsZdp7dHFVUDaHEci/WVVwldcaPdw9THkHybB3KvwzIVzuClq2QYvRptZ+qN6qUZAkA+sHYp0PD33j4nWS5NVbueEivOf4++bnJ5A9K5ay/RzXgVj5DCF3pYRLmFb5VTb5+Mgf0vknjorhZoLHHWpCztAkEAosXuEwDGCKSZ/lqGlet0lpKJmIxPoAUXtmIFOftWzjKegqoqhbLmpoUTtBfmtVxu6A7Bl9BjSM3i7N+eMFWzAQJAIfUnYgTQ8RKBZ0gkK98FuGcsYU6aPvKzyGaPeczKdsp4xZgEdlppCzwxn/Kd9kKRrL2PKkgBzQ5Vaygvu8dqNg==";
    System.out.println(key.length());
    payAlipayNotify();
    Calendar startTime = Calendar.getInstance();
    startTime.setTime(new Date());
    startTime.add(Calendar.DATE, -1);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);
    System.err.println(startTime.getTime());

    String toMail = "464709367@qq.com";
    String[] toMails = toMail.split(",");
    for (String string : toMails) {
      System.out.println(string);
    }

    InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
    System.out.println(address);// PC-20140317PXKX/192.168.0.121
    System.out.println(address.getHostAddress());// 192.168.0.121

    // System.out.println(new DecimalFormat("#.00").format(f));
    // for (int i = 0; i < 50; i++) {
    // Double smsCode = Math.random() * 4 + 8;
    // BigDecimal code =
    // new BigDecimal(smsCode).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
    // System.out.println(code.toString());
    // }
    BigDecimal aBigDecimal = new BigDecimal("10");
    BigDecimal bBigDecimal = new BigDecimal("100");
    System.out.println(bBigDecimal.add(aBigDecimal).toString());


    System.out.println(new Date(new Long("1494273600000")));
    // URLEncoder.encode("");
    String url = "";
    // String res = ApiUtils.get(url);
    // ObjectMapper mapper = new ObjectMapper();
    // Map<String, Object> resMap = (Map<String, Object>) mapper.readValue(res, Map.class);

    String sn = "90000343434343";
    if (sn.startsWith("90000")) {
      System.out.println(sn);
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("111", "1212");
    System.out.println(map);
    System.out.println("" + "11");
    // PayUtil.allinPay("90000343434343", "导辅导辅导报告", "100");
    // String allinpay_userId =
    // SunMd5.allinpayRegister("008510154113610", "1".toLowerCase(), "1234567890",
    // "https://cashier.allinpay.com/usercenter/merchant/UserInfo/reg.do");
    // System.out.println(allinpay_userId);
    Map<String, String> params = new HashMap<String, String>();
    params.put("merchantId", "008510154113610");
    params.put("version", "v1.0");
    params.put("language", "1");
    params.put("signType", "0");
    params.put("payType", "33");
    params.put("issuerId", "");
    params.put("paymentOrderId", "201705281152510386");
    params.put("orderNo", "201705288411");
    params.put("orderDatetime", "20170528115249");
    params.put("orderAmount", "1");
    params.put("payDatetime", "20170528115342");
    params.put("payAmount", "1");
    params.put("ext1", "<USER>170525864078656</USER>");
    params.put("ext2", "");
    params.put("payResult", "1");
    params.put("errorCode", "");
    params.put("returnDatetime", "20170528115342");

    params.put("signMsg", "292915306352B007DCD043DFBF3FBB9A");
    System.out.println(params);
    System.out.println(PayNotify.verifySignH5(params));

    System.out.println(new Date().getTime());

  }

  public static void payAlipayNotify() {
    try {
      String rsa_sign = RSA.sign("a=123", AlipayConfig.private_key, AlipayConfig.input_charset);
      System.out.println(rsa_sign);
      String data = PayUtil.alipay("201703292828", "1", "sdsdsdsdsd", "100.5");
      System.out.println(data);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void payWeChatNotify() {
    Map<String, Object> xmlMap = new HashMap<String, Object>();
    xmlMap.put("transaction_id", "4002542001201605185980297118");
    xmlMap.put("nonce_str", "0BA2FD63FD3345178FB4C1EDCCF6F3F3");
    xmlMap.put("bank_type", "CMBC_DEBIT");
    xmlMap.put("openid", "oWP5ws14_TVPyBKKOY0s3ZZDy1sU");
    xmlMap.put("sign", "F8FC5AE2F80AD8F0D69BDF38DF10F444");
    xmlMap.put("fee_type", "CNY");
    xmlMap.put("mch_id", "1249451301");
    xmlMap.put("cash_fee", "10");
    xmlMap.put("out_trade_no", "201605181006200000217539003_0620");
    xmlMap.put("appid", "wx75b0585936937e4a");
    xmlMap.put("total_fee", "10");
    xmlMap.put("trade_type", "APP");
    xmlMap.put("result_code", "SUCCESS");
    xmlMap.put("time_end", "20160518100634");
    xmlMap.put("is_subscribe", "N");
    xmlMap.put("return_code", "SUCCESS");

    String xmlReturn = "";
    if ("SUCCESS".equals(xmlMap.get("return_code"))) {
      // 验证签名
      String sign = xmlMap.get("sign") + "";
      xmlMap.put("sign", "");
      if (sign.equals(WeixinUtil.getSign(xmlMap))) {
        // 查询是否处理过
        // 处理回调结果
        if ("SUCCESS".equals(xmlMap.get("result_code"))) {
          // 系统订单编号
          String out_trade_no = xmlMap.get("out_trade_no").toString();
          // 支付的金额
          String total_fee = xmlMap.get("total_fee").toString();
          // 货币种类
          String fee_type = xmlMap.get("fee_type").toString();
          // 微信支付订单号
          String transaction_id = xmlMap.get("transaction_id").toString();

          String payment = "wx";

        } else {
          // 交易失败情况记录
        }
      }
      // 返回处理结果xml
      xmlReturn = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
    } else {
      // 返回处理结果xml
      xmlReturn = "<xml><return_code>FAIL</return_code><return_msg>签名错误</return_msg></xml>";
    }
  }
}
