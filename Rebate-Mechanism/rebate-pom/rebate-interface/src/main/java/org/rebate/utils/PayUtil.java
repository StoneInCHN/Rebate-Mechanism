package org.rebate.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.json.base.ResponseOne;
import org.rebate.utils.alipay.config.AlipayConfig;
import org.rebate.utils.alipay.sign.RSA;
import org.rebate.utils.allinpay.HttpConnectionUtil;
import org.rebate.utils.allinpay.SunMd5;
import org.rebate.utils.allinpay.SybUtil;
import org.rebate.utils.wechat.WeixinUtil;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.common.MD5;



public class PayUtil {

  public static Setting setting = SettingUtils.get();

  // 秘钥
  public static final String wechat_Key = setting.getWechatKey();
  // 微信分配的公众账号ID（企业号corpid即为此appId）
  public static final String wechat_appid = setting.getWechatAppid();
  // 商户ID
  public static final String wechat_mch_id = setting.getWechatMchId();
  // 交易类型
  public static final String wechat_trade_type = setting.getWechatTradeType();
  // 通知地址
  public static final String wechat_notify_url = setting.getWechatNotifyUrl();
  // 微信下订单接口
  public static final String wechat_AddOrderUrl = setting.getWechatAddOrderUrl();

  // // 翼支付下单url
  // private static final String yi_payOrder = setting.getYiPayOrder();
  // // 翼支付商户代码
  // private static final String yi_merchantId = setting.getYiMerchantId();
  // // 翼支付商户key
  // private static final String yi_merchantKey = setting.getYiMerchantKey();
  // // 翼支付回调url
  // private static final String yi_notify_url = setting.getYiPayNotifyUrl();



  // 通联统一下单url
  private static final String allinpay_order_url = setting.getTlPayOrder();
  // 通联微信支付宝扫码支付商户号
  private static final String allinpay_merchantPayId = setting.getTlMerchantPayId();
  // 通联支付appId
  private static final String allinpay_appId = setting.getTlAppId();
  // 通联支付商户md5 key
  private static final String allinpay_merchantMD5Key = setting.getTlMerchantKey();
  // 通联支付回调url
  private static final String allinpay_notify_url = setting.getTlPayNotifyUrl();


  // 通联支付H5支付后同步跳转url
  private static final String allinpay_pickup_url = setting.getTlPickupUrl();
  // 通联支付用户注册url
  private static final String allinpay_regUser_url = setting.getTlRegUserUrl();
  // 通联支付商户H5支付编号
  private static final String allinpay_merchantH5Id = setting.getTlMerchantH5Id();

  // 通联支付商户H5支付 md5key
  private static final String allinpay_merchantH5Key = setting.getTlMerchantH5Key();

  // 银行卡支付H5回调url
  private static final String allinpay_H5notify_Url = setting.getTlPayNotifyH5Url();
  // H5页面提交订单接口url
  private static final String allinpay_H5orderUrl = setting.getTlH5OrderUrl();

  // /**
  // * 电信翼支付接口
  // *
  // * @param order_sn 商户订单号
  // * @param body 商品介绍
  // * @param ip
  // * @param product_id 商品ID
  // * @param total_fee 商品价格（分）
  // * @return
  // * @throws Exception
  // */
  // public static ResponseOne<Map<String, Object>> yiPay(String order_sn, String body, String ip,
  // String product_id, BigDecimal amount, String userId) throws Exception {
  //
  // ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
  // String orderTime = TimeUtils.format("yyyyMMddHHmmss", new Date().getTime());
  // String orderReqTranSeq = orderTime + "000001"; // 订单流水号(当前时间+000001)(格式如：yyyymmddhhmmss000001)
  // String temp =
  // "MERCHANTID=" + yi_merchantId + "&ORDERSEQ=" + order_sn + "&ORDERREQTRANSEQ="
  // + orderReqTranSeq + "&ORDERREQTIME=" + orderTime + "&KEY=" + yi_merchantKey;
  // String mac = CryptTool.md5Digest(temp);
  // Map<String, Object> map = new HashMap<String, Object>();
  // map.put("MERCHANTID", yi_merchantId);
  // map.put("ORDERSEQ", order_sn);
  // map.put("ORDERREQTRANSEQ", orderReqTranSeq);
  // map.put("ORDERREQTIME", orderTime);
  // BigDecimal total_fee = amount.multiply(new BigDecimal(100));
  // map.put("ORDERAMT", total_fee);
  // map.put("TRANSCODE", "01");
  // map.put("MAC", mac);
  // String result = ApiUtils.post(yi_payOrder, map);
  // String res[] = result.split("&");
  // if ("00".equals(res[0])) {
  // Map<String, Object> resMap = new HashMap<String, Object>();
  // resMap.put("out_trade_no", order_sn);
  // resMap.put("MERCHANTID", yi_merchantId);
  // resMap.put("ORDERSEQ", order_sn);
  // resMap.put("ORDERREQTRANSEQ", orderReqTranSeq);
  // String orderAmount = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
  // resMap.put("ORDERAMOUNT", orderAmount);
  // resMap.put("ORDERTIME", orderTime);
  //
  // resMap.put("PRODUCTDESC", body);
  // resMap.put("BACKMERCHANTURL", yi_notify_url);
  // resMap.put("PRODUCTAMOUNT", orderAmount);
  // resMap.put("ATTACHAMOUNT", "0");
  // resMap.put("CURTYPE", "RMB");
  // resMap.put("CUSTOMERID", userId);
  // resMap.put("USERIP", ip);
  // resMap.put("ACCOUNTID", "");
  // resMap.put("BUSITYPE", "04");
  //
  // String tempStr =
  // "MERCHANTID=" + yi_merchantId + "&ORDERSEQ=" + order_sn + "&ORDERREQTRNSEQ="
  // + orderReqTranSeq + "&ORDERTIME=" + orderTime + "&KEY=" + yi_merchantKey;
  // String macStr = CryptTool.md5Digest(tempStr);
  // resMap.put("MAC", macStr);
  // response.setCode(CommonAttributes.SUCCESS);
  // response.setMsg(resMap);
  // } else {
  // response.setCode(CommonAttributes.FAIL_COMMON);
  // response.setDesc(Message.success("rebate.payOrder.create.fail").getContent() + result);
  // }
  // return response;
  // }

  /**
   * create the order info for alipay. 创建订单信息
   */
  public static String getOrderInfo(String subject, String order_sn, String body, String price) {
    StringBuffer stringBuffer = new StringBuffer();

    // 签约合作者身份ID
    stringBuffer.append("partner=" + "\"" + AlipayConfig.partner + "\"");

    // 签约卖家支付宝账号
    stringBuffer.append("&seller_id=" + "\"" + AlipayConfig.sellerId + "\"");

    // 商户网站唯一订单号
    stringBuffer.append("&out_trade_no=" + "\"" + order_sn + "\"");

    // 商品名称
    stringBuffer.append("&subject=" + "\"" + subject + "\"");

    // 商品详情
    stringBuffer.append("&body=" + "\"" + body + "\"");

    // 商品金额
    stringBuffer.append("&total_fee=" + "\"" + price + "\"");

    // 服务器异步通知页面路径
    stringBuffer.append("&notify_url=" + "\"" + AlipayConfig.ali_notify_url + "\"");

    // 服务接口名称， 固定值 mobile.securitypay.pay
    stringBuffer.append("&service=\"mobile.securitypay.pay\"");

    // 支付类型， 固定值 1
    stringBuffer.append("&payment_type=\"1\"");

    // 参数编码， 固定值 utf-8
    stringBuffer.append("&_input_charset=\"utf-8\"");

    // 设置未付款交易的超时时间
    // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    // 取值范围：1m～15d。
    // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    // 该参数数值不接受小数点，如1.5h，可转换为90m。
    // orderInfo += "&it_b_pay=\"30m\"";
    stringBuffer.append("&it_b_pay=\"2d\"");

    // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
    // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

    // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
    // orderInfo += "&return_url=\"m.alipay.com\"";

    // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
    // orderInfo += "&paymethod=\"expressGateway\"";

    return stringBuffer.toString();
  }

  /**
   * 支付宝支付接口
   * 
   * @return
   * @throws Exception
   */
  public static String alipay(String order_sn, String product_name, String product_detail,
      String total_fee) throws Exception {

    // Map<String, String> paramMap = new HashMap<String, String>();
    // // 签约合作者身份ID
    // paramMap.put("partner", AlipayConfig.partner);
    // // 签约卖家支付宝账号
    // paramMap.put("seller_id", AlipayConfig.sellerId);
    // // 商户网站唯一订单号
    // paramMap.put("out_trade_no", order_sn);
    // // 商品名称
    // paramMap.put("subject", product_name);
    // // 商品详情
    // paramMap.put("body", product_detail);
    // // 商品金额
    // paramMap.put("total_fee", total_fee);
    // // 服务器异步通知地址
    // paramMap.put("notify_url", AlipayConfig.ali_notify_url);
    // // 服务接口名称， 固定值mobile.securitypay.pay
    // paramMap.put("service", "mobile.securitypay.pay");
    // // 支付类型， 固定值 1
    // paramMap.put("payment_type", "1");
    // // 参数编码， 固定值utf-8
    // paramMap.put("_input_charset", "utf-8");
    // // 固定值2d
    // paramMap.put("it_b_pay", "2d");
    String data = getOrderInfo(product_name, order_sn, product_detail, total_fee);
    String rsa_sign =
        URLEncoder.encode(RSA.sign(data, AlipayConfig.private_key, AlipayConfig.input_charset),
            AlipayConfig.input_charset);
    // 把签名得到的sign和签名类型sign_type拼接在待签名字符串后面。
    data = data + "&sign=\"" + rsa_sign + "\"&sign_type=\"" + AlipayConfig.sign_type + "\"";
    return data;
  }


  /**
   * 微信支付接口
   * 
   * @param order_sn 商户订单号
   * @param body 商品介绍
   * @param ip
   * @param product_id 商品ID
   * @param total_fee 商品价格（分）
   * @return
   * @throws DocumentException
   */
  public static ResponseOne<Map<String, Object>> wechat(String order_sn, String body, String ip,
      String product_id, String total_fee) throws DocumentException {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    /**
     * 微信开发签名流程 //详情 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序如下： stringA=
     * "appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA"
     * ; 第二步：拼接API密钥： stringSignTemp="stringA&key=192006250b4c09247ec02edce69f6a2d"
     * sign=MD5(stringSignTemp).toUpperCase()="9A0A8659F005D6984697E2CA0A9CF3B7" 最终得到最终发送的数据：
     */


    // 随机字符串，不长于32位。推荐随机数生成算法
    String nonce_str = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    String stringSignTemp =
        "appid=" + wechat_appid + "&body=" + body + "&mch_id=" + wechat_mch_id + "&nonce_str="
            + nonce_str + "&notify_url=" + wechat_notify_url + "&out_trade_no=" + order_sn
            + "&product_id=" + product_id + "&spbill_create_ip=" + ip + "&total_fee=" + total_fee
            + "&trade_type=APP&key=" + wechat_Key;
    // System.out.println(stringSignTemp);
    String sign = MD5.MD5Encode(stringSignTemp).toUpperCase();
    // System.out.println(sign);
    String xml =
        "<xml>" + "<appid>" + wechat_appid + "</appid>" + "<body>" + body + "</body>" + "<mch_id>"
            + wechat_mch_id + "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>"
            + "<notify_url>" + wechat_notify_url + "</notify_url>" + "<out_trade_no>" + order_sn
            + "</out_trade_no>" + "<product_id>" + product_id + "</product_id>"
            + "<spbill_create_ip>" + ip + "</spbill_create_ip>" + "<total_fee>" + total_fee
            + "</total_fee>" + "<trade_type>APP</trade_type>" + "<sign>" + sign + "</sign>"
            + "</xml>";
    // 调接口
    System.out.println(xml);
    String xmlString = WeixinUtil.httpsRequest(wechat_AddOrderUrl, "POST", xml);
    // 解析xml
    String prepay_id = null;
    Document dom = DocumentHelper.parseText(xmlString);
    Element root = dom.getRootElement();
    String return_code = root.elementText("return_code");
    if (return_code.equals("SUCCESS")) {
      String result_code = root.elementText("result_code");
      if (result_code.equals("SUCCESS")) {

        // 预支付交易会话标识
        prepay_id = root.elementText("prepay_id");
        Map<String, String> data = new HashMap<String, String>();
        data.put("prepayid", prepay_id);
        data.put("noncestr", UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());

        // data.put("sign", sign);
        // data.put("sign", root.elementText("sign"));
        // type 1:红包余额支付；2：在线支付
        // data.put("type", "2");
        // data.put("channel", "wx");
        data.put("appid", wechat_appid);
        data.put("partnerid", wechat_mch_id);
        data.put("package", "Sign=WXPay");
        data.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        data.put("sign", getSignForWX(data));
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("out_trade_no", order_sn);
        res.putAll(data);
        response.setCode(CommonAttributes.SUCCESS);
        response.setMsg(res);
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(root.elementText("err_code_des")
            + Message.success("rebate.payOrder.create.fail").getContent());
      }
    } else {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(root.elementText("return_msg")
          + Message.success("rebate.payOrder.create.fail").getContent());
    }
    return response;
  }



  /**
   * 通联支付接口(银行卡快捷支付H5)
   * 
   * @param order_sn 商户订单号
   * @param body 商品介绍
   * @param ip
   * @param product_id 商品ID
   * @param total_fee 商品价格（分）
   * @return
   * @throws Exception
   */
  public static ResponseOne<Map<String, Object>> allinpayH5(String order_sn, String body,
      String userId, String product_id, String total_fee) throws Exception {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    if ("5".equals(userId)) {// 特殊处理手机号15339598777 userId=5
      userId = userId + "xgg";
    }
    String allinpay_userId =
        SunMd5.allinpayRegister(allinpay_merchantH5Id, userId.toLowerCase(),
            allinpay_merchantH5Key, allinpay_regUser_url);
    if (!StringUtils.isEmpty(allinpay_userId)) {
      LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

      params.put("inputCharset", "1");
      params.put("pickupUrl", allinpay_pickup_url);
      params.put("receiveUrl", allinpay_H5notify_Url);
      params.put("version", "v1.0");
      params.put("language", "1");
      params.put("signType", "0");
      params.put("merchantId", allinpay_merchantH5Id);
      params.put("orderNo", order_sn);
      params.put("orderAmount", total_fee);
      params.put("orderCurrency", "0");
      params.put("orderDatetime", TimeUtils.format("yyyyMMddHHmmss", new Date().getTime()));
      // params.put("productName", body);
      params.put("ext1", "<USER>" + allinpay_userId + "</USER>");
      params.put("payType", "33");

      // String tempStr =
      // "inputCharset=" + resMap.get("inputCharset") + "&pickupUrl=" + resMap.get("pickupUrl")
      // + "&receiveUrl=" + resMap.get("receiveUrl") + "&version=" + resMap.get("version")
      // + "&language=" + resMap.get("language") + "&signType=" + resMap.get("signType")
      // + "&merchantId=" + resMap.get("merchantId") + "&orderNo=" + resMap.get("orderNo")
      // + "&orderAmount=" + resMap.get("orderAmount") + "&orderCurrency="
      // + resMap.get("orderCurrency") + "&orderDatetime=" + resMap.get("orderDatetime")
      // + "&productName=" + resMap.get("productName") + "&ext1=" + resMap.get("ext1")
      // + "&payType=" + resMap.get("payType") + "&key=" + allinpay_merchantMD5Key;
      String signMsg = SybUtil.sign(params, allinpay_merchantH5Key);
      Map<String, Object> resMap = new LinkedHashMap<String, Object>();
      resMap.putAll(params);
      resMap.put("signMsg", signMsg);
      resMap.put("payH5orderUrl", allinpay_H5orderUrl);
      response.setCode(CommonAttributes.SUCCESS);
      response.setMsg(resMap);
    } else {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.success("rebate.payOrder.create.fail").getContent());
    }
    return response;
  }


  /**
   * 通联支付接口(支付宝支付)
   * 
   * @param order_sn 商户订单号
   * @param body 商品介绍
   * @param ip
   * @param product_id 商品ID
   * @param total_fee 商品价格（分）
   * @return
   * @throws Exception
   */
  public static ResponseOne<Map<String, Object>> allinPay(String order_sn, String body,
      String total_fee) throws Exception {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    HttpConnectionUtil http = new HttpConnectionUtil(allinpay_order_url + "/pay");
    http.init();
    // 随机字符串，不长于32位。推荐随机数生成算法
    String nonce_str = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

    TreeMap<String, String> data = new TreeMap<String, String>();
    data.put("cusid", allinpay_merchantPayId);
    data.put("appid", allinpay_appId);
    // data.put("version", "11");
    data.put("trxamt", total_fee);
    data.put("reqsn", order_sn);
    data.put("paytype", "A01");

    // data.put("paytype", "W01");
    data.put("randomstr", nonce_str);
    data.put("body", body);
    data.put("notify_url", allinpay_notify_url);

    data.put("sign", SybUtil.sign(data, allinpay_merchantMD5Key));
    byte[] bys = http.postParams(data, true);
    String result = new String(bys, "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> map = objectMapper.readValue(result, Map.class);
    System.out.println(map.toString());
    if (map == null) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.success("rebate.payOrder.create.fail").getContent());
      return response;
    }
    if ("SUCCESS".equals(map.get("retcode"))) {// 通信成功
      String trxstatus = map.get("trxstatus");
      if ("0000".equals(trxstatus)) {// 下单成功
        TreeMap tmap = new TreeMap();
        tmap.putAll(map);
        String sign = tmap.remove("sign").toString();
        String signLocal = SybUtil.sign(tmap, allinpay_merchantMD5Key);
        if (signLocal.toLowerCase().equals(sign.toLowerCase())) {
          response.setCode(CommonAttributes.SUCCESS);
          Map<String, Object> res = new HashMap<String, Object>();
          res.put("payinfo", map.get("payinfo"));
          res.put("out_trade_no", map.get("reqsn"));
          response.setMsg(res);
          System.out.println(res.toString());
        } else {
          response.setCode(CommonAttributes.FAIL_COMMON);
          response.setDesc(Message.success("rebate.pay.sign.error").getContent());
        }
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(Message.success("rebate.payOrder.create.fail").getContent());
      }
    } else {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc((map.get("retmsg") != null ? map.get("retmsg") : "")
          + Message.success("rebate.payOrder.create.fail").getContent());
    }
    return response;

  }


  public static String getSignForWX(Map<String, String> map) {
    ArrayList<String> list = new ArrayList<String>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (!"sign".equals(entry.getKey()) && null != entry.getValue()
          && !"".equals(entry.getValue())) {
        list.add(entry.getKey() + "=" + entry.getValue() + "&");
      }
    }
    int size = list.size();
    String[] arrayToSort = list.toArray(new String[size]);
    Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      sb.append(arrayToSort[i]);
    }
    String result = sb.toString();
    result += "key=" + wechat_Key;
    System.out.println(result);
    result = MD5.MD5Encode(result).toUpperCase();

    return result;
  }

  // private static String createSign(Map<String, Object> map) {
  // SortedMap<String, Object> parameters = new TreeMap<String, Object>();
  // parameters.putAll(map);
  // StringBuffer sb = new StringBuffer();
  // Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
  // Iterator it = es.iterator();
  // while (it.hasNext()) {
  // Map.Entry entry = (Map.Entry) it.next();
  // String k = (String) entry.getKey();
  // Object v = entry.getValue();
  // if (null != v && !"".equals(v) && !"out_trade_no".equals(k)) {
  // sb.append(k + "=" + v + "&");
  // }
  // }
  // sb.append("key=" + wechat_Key);
  // System.out.println(sb.toString());
  // String sign = MD5Util.MD5Encode(sb.toString()).toUpperCase();
  // return sign;
  //
  // }
}
