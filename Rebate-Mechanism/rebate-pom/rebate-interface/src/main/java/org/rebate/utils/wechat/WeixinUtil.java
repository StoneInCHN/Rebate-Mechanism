package org.rebate.utils.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.rebate.beans.Setting;
import org.rebate.utils.PayUtil;
import org.rebate.utils.SettingUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.common.MD5;


/**
 * 发送https请求的通用工具类
 * 
 * @author jack
 * @version 1.0
 * 
 */
public class WeixinUtil {
  // private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
  // https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
  public static Setting setting = SettingUtils.get();

  public static final String token_url = setting.getWechatTokenUrl();

  /**
   * 发送https请求
   * 
   * @param requestUrl //提交的URL
   * @param requestMethod //提交方式
   * @param outputStr //ID
   * @return
   */
  public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    String obj = null;
    // 创建SSLContext对象，并使用我们指定的信任管理器初始化
    TrustManager[] tm = {new MyX509TrustManager()};
    // 安全套接字的上下文
    SSLContext sslContext;
    try {
      sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new java.security.SecureRandom());
      // 从上述SSLContext对象中得到SSLSocketFactory对象
      SSLSocketFactory ssf = sslContext.getSocketFactory();
      // 建立连接
      URL url = new URL(requestUrl);
      // 打开连接
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setSSLSocketFactory(ssf);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      // 设置请求方式
      conn.setRequestMethod(requestMethod);

      if (null != outputStr) {
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }
      // 从输入流中获取返回的内容
      InputStream inputStream = conn.getInputStream();
      InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
      BufferedReader bufferReader = new BufferedReader(reader);
      String str = null;
      StringBuffer buffer = new StringBuffer();
      while ((str = bufferReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferReader.close();
      reader.close();
      inputStream.close();
      inputStream = null;

      conn.disconnect();
      System.out.println(buffer.toString());
      obj = buffer.toString();
      // jsonObject = JSONObject.fromObject(buffer.toString());
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return obj;
  }

  /**
   * 
   * 获取接口访问凭证
   * 
   * @param appid
   * @param appSecret
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  public static Token getToken(String appid, String appSecret) throws Exception {
    Token token = null;

    // APPID&secret=APPSECRET
    // 将请求路径中的APPID和APPSECRET替换调
    String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appSecret);
    // 返回json对象
    String Str = httpsRequest(requestUrl, "GET", null);
    // 转JSON
    ObjectMapper mapper = new ObjectMapper();
    token = mapper.readValue(Str, Token.class);

    // JSONObject jsonObject = JSONObject.fromObject(Str.toString());
    // if (null != jsonObject) {
    // token = new Token();
    // // 从json对象中获取access_token和expires_in
    // token.setAccess_token(jsonObject.getString("access_token"));
    // token.setExpires_in(jsonObject.getString("expires_in"));
    // }
    return token;
  }

  /**
   * 根据内容类型来判断返回文件的扩展名
   * 
   * @param contentType 内容类型
   * @return
   */
  public static String getFileExt(String contentType) {
    String fileExt = "";

    if ("image/jpeg".equals(contentType))
      fileExt = ".jpg";
    else if ("audio/mpeg".equals(contentType))
      fileExt = ".mp3";
    else if ("audio/amr".equals(contentType))
      fileExt = ".amr";
    else if ("video/mp4".equals(contentType))
      fileExt = ".mp4";
    else if ("video/mpeg4".equals(contentType))
      fileExt = ".mp4";

    return fileExt;
  }

  /**
   * url编码
   * */
  public static String urlEncodeUTF8(String source) {
    String str = null;
    try {
      str = URLEncoder.encode(source, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return str;
  }

  public static String getSign(Map<String, Object> map) {
    ArrayList<String> list = new ArrayList<String>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() != "") {
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
    result += "key=" + PayUtil.wechat_Key;
    // Util.log("Sign Before MD5:" + result);
    result = MD5.MD5Encode(result).toUpperCase();
    // Util.log("Sign Result:" + result);
    return result;
  }

}
