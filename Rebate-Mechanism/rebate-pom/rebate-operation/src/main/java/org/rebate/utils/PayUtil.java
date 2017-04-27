package org.rebate.utils;

import java.net.InetAddress;
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

public class PayUtil {

  public static Setting setting = SettingUtils.get();
  // 秘钥
  public static final String wechat_Key = setting.getWechatKey();
  // 微信分配的公众账号ID（企业号corpid即为此appId）
  public static final String wechat_appid = setting.getWechatAppid();
  // 商户ID
  public static final String wechat_mch_id = setting.getWechatMchId();

  // 微信企业向个人付款接口
  public static final String wechat_transfersUrl = setting.getWechatTransfersUrl();

  /**
   * 企业付款
   * 
   * @param openid
   * @param amount
   * @param desc
   * @param partner_trade_no
   * @return
   * @throws DocumentException 步凑： 1、准备数据; 2、把所有的参数连接成一个字符串，然后进行MD5，把MD5得到的一个字符串做为最后一个参;
   *         3、把微信提供的安全证，封装到要提交的数据; 4、通过JAVA程序向微信提供的接口POST数据。微信接口返回处理结果。
   */
  public static ResponseOne<Map<String, Object>> wechatTransfers(String openid, int amount,
      String desc, String partner_trade_no) throws Exception {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Map<String, Object> signParams = new HashMap<String, Object>();

    InetAddress ia = InetAddress.getLocalHost();
    String ip = ia.getHostAddress(); // 获取本机IP地址
    // 随机字符串，不长于32位。推荐随机数生成算法
    String nonce_str = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

    // 设置支付参数
    signParams.put("mch_appid", wechat_appid); // 微信分配的公众账号ID（企业号corpid即为此appId）
    signParams.put("mchid", wechat_mch_id);// 微信支付分配的商户号
    signParams.put("nonce_str", nonce_str); // 随机字符串，不长于32位
    signParams.put("partner_trade_no", partner_trade_no); // 商户订单号，需保持唯一性
    signParams.put("openid", openid); // 商户appid下，某用户的openid
    signParams.put("check_name", "NO_CHECK"); // NO_CHECK：不校验真实姓名
                                              // FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
                                              // OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
    signParams.put("amount", amount); // 企业付款金额，单位为分
    signParams.put("desc", desc); // 企业付款操作说明信息。必填。
    signParams.put("spbill_create_ip", ip); // 调用接口的机器Ip地址

    String sign = WeixinUtil.getSign(signParams);

    String xml =
        "<xml>" + "<mch_appid>" + wechat_appid + "</mch_appid>" + "<mchid>" + wechat_mch_id
            + "</mchid>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<partner_trade_no>"
            + partner_trade_no + "</partner_trade_no>" + "<openid>" + openid + "</openid>"
            + "<check_name>NO_CHECK</check_name>"
            +
            // "<re_user_name>" + bean.getReUserName() + "</re_user_name>"+
            "<amount>" + amount + "</amount>" + "<desc>" + desc + "</desc>" + "<spbill_create_ip>"
            + ip + "</spbill_create_ip>" + "<sign>" + sign + "</sign>" + "</xml>";
    // 调接口
    System.out.println(xml);
    String xmlString = WeixinUtil.httpsRequest(wechat_transfersUrl, "POST", xml);
    LogUtil.debug(PayUtil.class, "wechatTransfers", "xmlString = %s", xmlString);
    // 解析xml
    // 返结果示例：https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
    Document dom = DocumentHelper.parseText(xmlString);
    Element root = dom.getRootElement();
    String return_code = root.elementText("return_code");
    // return_code 返回状态码 SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    if (return_code.equals("SUCCESS")) {
      // 业务结果 result_code 是 SUCCESS String(16) SUCCESS/FAIL
      String result_code = root.elementText("result_code");
      if (result_code.equals("SUCCESS")) {
        // 用户提现成功
        response.setCode(CommonAttributes.SUCCESS);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("partner_trade_no", root.elementText("partner_trade_no"));
        data.put("payment_no", root.elementText("payment_no"));
        data.put("payment_time", root.elementText("payment_time"));
        data.put("mch_appid", root.elementText("mch_appid"));
        data.put("mchid", root.elementText("mchid"));
        data.put("device_info", root.elementText("device_info"));
        data.put("nonce_str", root.elementText("nonce_str"));
        response.setMsg(data);
        LogUtil.debug(PayUtil.class, "wechatTransfers", "wechatTransfers payment_time = %s",
            root.elementText("payment_time"));
      } else {
        response.setCode(CommonAttributes.FAIL_COMMON);
        response.setDesc(root.elementText("err_code_des")
            + Message.success("rebate.withdraw.wechat.pay.fail").getContent());
        LogUtil.debug(PayUtil.class, "wechatTransfers", "err_code_des = %s",
            root.elementText("err_code_des"));
      }
    } else {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(root.elementText("return_msg")
          + Message.success("rebate.withdraw.wechat.pay.fail").getContent());
      LogUtil.debug(PayUtil.class, "wechatTransfers", "return_msg = %s",
          root.elementText("return_msg"));
    }
    return response;
  }
}
