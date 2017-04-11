package org.rebate.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import org.rebate.utils.alipay.util.AlipayCore;
import org.rebate.utils.wechat.WeixinUtil;

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



  /**
   * 支付宝支付接口
   * 
   * @return
   * @throws UnsupportedEncodingException
   * @throws AlipayApiException
   */
  public static String alipay(String order_sn, String product_name, String product_detail,
      String total_fee) throws Exception {

    Map<String, String> paramMap = new HashMap<String, String>();
    // 签约合作者身份ID
    paramMap.put("partner", AlipayConfig.partner);
    // 签约卖家支付宝账号
    paramMap.put("seller_id", AlipayConfig.sellerId);
    // 商户网站唯一订单号
    paramMap.put("out_trade_no", order_sn);
    // 商品名称
    paramMap.put("subject", product_name);
    // 商品详情
    paramMap.put("body", product_detail);
    // 商品金额
    paramMap.put("total_fee", total_fee);
    // 服务器异步通知地址
    paramMap.put("notify_url", AlipayConfig.ali_notify_url);
    // 服务接口名称， 固定值mobile.securitypay.pay
    paramMap.put("service", "mobile.securitypay.pay");
    // 支付类型， 固定值 1
    paramMap.put("payment_type", "1");
    // 参数编码， 固定值utf-8
    paramMap.put("_input_charset", "utf-8");
    // 固定值2d
    paramMap.put("it_b_pay", "2d");
    String data = AlipayCore.createLinkString(paramMap);
    // String rsa_sign =
    // AlipaySignature.rsaSign(paramMap, AlipayConfig.private_key, AlipayConfig.input_charset);
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
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("prepay_id", prepay_id);
        data.put("nonce_str", nonce_str);
        data.put("out_trade_no", order_sn);
        data.put("sign", sign);
        // type 1:红包余额支付；2：在线支付
        // data.put("type", "2");
        // data.put("channel", "wx");
        data.put("appId", wechat_appid);
        data.put("partnerId", wechat_mch_id);
        data.put("package", "Sign=WXPay");
        data.put("timeStamp", TimeUtils.format("MMddHHmmss", new Date().getTime()));
        response.setCode(CommonAttributes.SUCCESS);
        response.setMsg(data);
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
}
