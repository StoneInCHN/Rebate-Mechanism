package org.rebate.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.rebate.beans.Setting;
import org.rebate.common.log.LogUtil;

public class ToolsUtils {


  public static String sendReq(String smsUrl, String parameters) {

    StringBuffer response = new StringBuffer();
    try {
      if (LogUtil.isDebugEnabled(ApiUtils.class)) {
        LogUtil.debug(ApiUtils.class, "Request API URL is : %s", smsUrl + parameters);
      }

      URL url = new URL(smsUrl);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      // add reuqest header
      con.setRequestMethod("GET");
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.write(parameters.getBytes());
      // wr.writeBytes(parameters);
      wr.flush();
      wr.close();

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    if (LogUtil.isDebugEnabled(ApiUtils.class)) {
      LogUtil.debug(ApiUtils.class, "Response", "Response from API is : %s", response.toString());
    }
    return response.toString();
  }

  public static String sendSmsMsg(String mobile, String msg) {
    try {
      Setting setting = SettingUtils.get();
      String smsUrl = setting.getSmsUrl();
      String smsOrgId = setting.getSmsOrgId();
      String smsUserName = setting.getSmsUserName();
      String smsPwd = setting.getSmsPwd();
      String message = URLEncoder.encode(msg, "UTF-8");
      String url =
          "Id=" + smsOrgId + "&Name=" + smsUserName + "&Psw=" + smsPwd + "&Message=" + message
              + "&Phone=" + mobile + "&Timestamp=0";
      String rs = sendReq(smsUrl, url);
      return rs;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String createUserName() {
    String id = UUID.randomUUID().toString();

    id = DEKHash(id) + "";

    int diff = 12 - id.length();
    String randStr = RandomStringUtils.randomAlphabetic(12);
    for (int i = 0; i < diff; i++) {
      int randIndex = (int) (Math.random() * randStr.length());
      int index = (int) (Math.random() * id.length());
      id = id.substring(0, index) + randStr.charAt(randIndex) + id.substring(index, id.length());
    }
    return id;
  }

  private static int DEKHash(String str) {
    int hash = str.length();

    for (int i = 0; i < str.length(); i++) {
      hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
    }

    return (hash & 0x7FFFFFFF);
  }


  private static Integer baseNo = 1;


  /**
   * 生成账单号 格式 yyyyMMddHHmmss-组织机构代码(6位)-随机4位数-(1-1000累加) 27位
   * 
   * @return
   */
  public synchronized static String generateRecordNo(String orgCode) {
    StringBuffer strBuffer = new StringBuffer();
    String strDate = TimeUtils.getLongDateStr(new Date());
    strBuffer.append(strDate);
    strBuffer.append(orgCode);
    strBuffer.append((int) ((Math.random() * 9 + 1) * 1000));
    // 订单尾号增自999时重置
    if (baseNo == 999) {
      baseNo = 1;
    }
    if (baseNo.toString().length() == 1) {
      strBuffer.append("00");
    }
    if (baseNo.toString().length() == 2) {
      strBuffer.append("0");
    }
    strBuffer.append(baseNo++);
    return strBuffer.toString();
  }


  /**
   * 生成账单号 格式 param+随机4位数
   * 
   * @return
   */
  public synchronized static String generateRecordNoByParam(String param) {
    StringBuffer strBuffer = new StringBuffer();
    strBuffer.append(param);
    strBuffer.append((int) ((Math.random() * 9 + 1) * 1000));
    // 订单尾号增自999时重置
    if (baseNo == 999) {
      baseNo = 1;
    }
    if (baseNo.toString().length() == 1) {
      strBuffer.append("00");
    }
    if (baseNo.toString().length() == 2) {
      strBuffer.append("0");
    }
    strBuffer.append(baseNo++);
    return strBuffer.toString();
  }

  public static Map<String, Object> convertStrToJson(String str) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JavaType javaType =
          mapper.getTypeFactory()
              .constructParametricType(HashMap.class, String.class, Object.class);
      Map<String, Object> map = mapper.readValue(str, javaType);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  public static String[] convertList2String(List<String> list) {
    String[] arr = (String[]) list.toArray(new String[list.size()]);
    return arr;
  }


  /**
   * 检查对象obj的成员变量是否都为null
   * 
   * @param obj
   * @return
   */
  public static boolean checkObjAllFieldNull(Object obj) {
    for (Field f : obj.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        if (f.get(obj) != null) {
          return false;
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  /**
   * 对象字段toString显示方法
   * 
   * @param obj
   * @return
   */
  public static String entityToString(Object obj) {

    if (obj == null)
      return "null";
    StringBuffer sb = new StringBuffer();
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();

    sb.append(clazz.getName() + "{");
    try {
      for (Field field : fields) {
        field.setAccessible(true);
        sb.append("\n  " + field.getName() + ":" + field.get(obj));
      }
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    sb.append("\n}");
    return sb.toString();
  }
}
