package org.rebate.utils.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.rebate.utils.PayUtil;

import com.tencent.common.MD5;


/**
 * 发送https请求的通用工具类
 * 
 * @author jack
 * @version 1.0
 * 
 */
public class WeixinUtil {
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
   * 获取签名
   * 
   * @param map
   * @return
   */
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
