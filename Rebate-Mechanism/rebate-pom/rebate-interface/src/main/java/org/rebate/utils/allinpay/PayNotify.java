package org.rebate.utils.allinpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;

public class PayNotify {

  public static Setting setting = SettingUtils.get();

  /**
   * 验证消息是否是合法消息
   * 
   * @param params 通知返回来的参数数组
   * @return 验证结果
   */
  public static boolean verify(Map<String, String> params) {

    String sign = "";
    if (params.get("signMsg") != null) {
      sign = params.get("signMsg");
    }
    boolean isSign = getSignVeryfy(params, sign, setting.getTlMerchantH5Key());

    return isSign;
  }

  /**
   * 根据反馈回来的信息，生成签名结果
   * 
   * @param Params 通知返回来的参数数组
   * @param sign 比对的签名结果
   * @return 生成的签名结果
   */
  private static boolean getSignVeryfy(Map<String, String> Params, String sign, String md5Key) {
    // 过滤空值、sign与sign_type参数
    Map<String, String> sParaNew = paraFilter(Params);
    // 获取待签名字符串
    String preSignStr = createLinkString(sParaNew, md5Key);
    // 获得签名验证结果
    boolean isSign = false;
    String verifySign = SunMd5.md5(preSignStr).toUpperCase().trim();
    if (sign.equals(verifySign)) {
      isSign = true;
    }
    return isSign;
  }

  /**
   * 除去数组中的空值和签名参数
   * 
   * @param sArray 签名参数组
   * @return 去掉空值与签名参数后的新签名参数组
   */
  public static Map<String, String> paraFilter(Map<String, String> sArray) {

    Map<String, String> result = new HashMap<String, String>();

    if (sArray == null || sArray.size() <= 0) {
      return result;
    }

    for (String key : sArray.keySet()) {
      String value = sArray.get(key);
      if (value == null || value.equals("") || key.equalsIgnoreCase("signMsg")) {
        continue;
      }
      result.put(key, value);
    }

    return result;
  }


  /**
   * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
   * 
   * @param params 需要排序并参与字符拼接的参数组
   * @return 拼接后字符串
   */
  public static String createLinkString(Map<String, String> params, String md5Key) {

    List<String> keys = new ArrayList<String>(params.keySet());

    String prestr = "";

    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      String value = params.get(key);

      if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
        prestr = prestr + key + "=" + value;
      } else {
        prestr = prestr + key + "=" + value + "&";
      }
    }
    prestr += "&key=" + md5Key;
    return prestr;
  }
}
