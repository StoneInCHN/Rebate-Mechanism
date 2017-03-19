package org.rebate.utils;

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
   * 支付宝支付接口 生成系统交易流水号。返回APP
   * 
   * @return
   */
  // public Map<String, Object> alipay(String order_sn) {
  // Map<String, Object> data = new HashMap<String, Object>();
  // // 2：在线支付
  // data.put("type", "2");
  // data.put("channel", "alipay");
  // data.put("out_trade_no", order_sn);
  // return data;
  // }



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
        data.put("type", "2");
        data.put("channel", "wx");
        response.setCode(CommonAttributes.SUCCESS);
        response.setMsg(data);
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(root.elementText("err_code_des")
            + Message.success("csh.buyRecord.create.fail").getContent());
      }
    } else {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(root.elementText("return_msg")
          + Message.success("csh.buyRecord.create.fail").getContent());
    }
    return response;
  }
}
