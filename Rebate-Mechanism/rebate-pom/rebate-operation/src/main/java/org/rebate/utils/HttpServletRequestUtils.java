package org.rebate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
/**
 * 处理 HttpServletRequest 的工具类
 *
 */
public class HttpServletRequestUtils {
  
  /**
   * 工具类一般不允许实例化
   */
  private HttpServletRequestUtils(){}
  
  /**
   * 获取请求参数
   * @param request
   * @return
   * @throws IOException
   */
  public static String getRequestParam(HttpServletRequest request, String charset) throws IOException {
    
    if (charset == null) {
    	charset = "UTF-8";
    }
    if (request.getMethod().equalsIgnoreCase("GET") && request.getQueryString() != null) {//Get请求
      return new String(request.getQueryString().getBytes("iso-8859-1"), charset);
    }
    
    StringBuffer sb = new StringBuffer();
    
    if (request.getMethod().equalsIgnoreCase("POST")) {//Post请求
      
      try {
        InputStreamReader isr = new InputStreamReader(request.getInputStream(), charset);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        
        try {
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        } catch (IOException e) {
        	
          e.printStackTrace();
          
        } finally{
        	
          if (br != null) {
            br.close();
          }
          if (isr != null) {
        	  isr.close();
          }
        }
        
        return sb.toString();
        
      } catch (Exception e) {
    	  
        return null;
        
      }
    }
    
    return null;
  }
}
