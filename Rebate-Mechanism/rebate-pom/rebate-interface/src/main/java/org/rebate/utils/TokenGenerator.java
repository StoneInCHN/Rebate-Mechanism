package org.rebate.utils;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class TokenGenerator {

  public static String generateToken() {
    String token = UUID.randomUUID().toString() + "__" + new Date().getTime();
    return token;
  }

  public static String generateToken(String token) {
    String previousToken = token.split("__")[0];
    String newToken = null;
    if (!StringUtils.isEmpty(previousToken)) {
      newToken = previousToken + "__" + new Date().getTime();
    }
    return newToken;
  }

  /**
   * 手机短信验证码超时验证
   * 
   * @param token
   * @param timeout
   * @return
   */
  public static boolean smsCodeTokenTimeOut(String token, Integer timeout) {
    Date date = new Date(Long.valueOf(token));
    if ((new Date().getTime() - date.getTime()) / 1000 / 60 >= timeout) {
      return true;
    }
    return false;
  }

  /**
   * 手机token超时验证
   * 
   * @param token
   * @param timeout
   * @return
   */
  public static boolean tokenTimeOut(String token, Integer timeout) {
    String tokenDate = token.split("__")[1];
    Date date = new Date(Long.valueOf(tokenDate));
    if ((new Date().getTime() - date.getTime()) / 1000 / 60 >= timeout) {
      return true;
    }
    return false;
  }


  /**
   * 验证登录token
   * 
   * @param token 手机端的token
   * @param userToken redis中存放的token
   * @return
   */
  public static boolean isValiableToken(String token, String userToken) {

    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userToken)) {
      return false;
    }

    Integer timeout = SettingUtils.get().getTokenTimeOut();
    String tokenFromClient = token.split("__")[0];
    String tokenFromServer = userToken.split("__")[0];

    if (StringUtils.isEmpty(tokenFromServer) || StringUtils.isEmpty(tokenFromClient)
        || !tokenFromClient.equals(tokenFromServer) || tokenTimeOut(userToken, timeout)) {
      return false;
    }
    return true;
  }


  /**
   * 验证登录token：是否登录超时
   * 
   * @param token 手机端的token
   * @param userToken redis中存放的token
   * @return
   */
  public static boolean isTokenTimeout(String token, String userToken) {

    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userToken)) {
      return false;
    }

    Integer timeout = SettingUtils.get().getTokenTimeOut();
    String tokenFromClient = token.split("__")[0];
    String tokenFromServer = userToken.split("__")[0];

    if (StringUtils.isEmpty(tokenFromServer) || StringUtils.isEmpty(tokenFromClient)
        || tokenTimeOut(userToken, timeout)) {
      return false;
    }
    return true;
  }


  /**
   * 验证登录token：账号是否在其它设备登录
   * 
   * @param token 手机端的token
   * @param userToken redis中存放的token
   * @return
   */
  public static boolean isTokenAuth(String token, String userToken) {

    if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userToken)) {
      return false;
    }

    // Integer timeout = SettingUtils.get().getTokenTimeOut();
    String tokenFromClient = token.split("__")[0];
    String tokenFromServer = userToken.split("__")[0];

    if (StringUtils.isEmpty(tokenFromServer) || StringUtils.isEmpty(tokenFromClient)
        || !tokenFromClient.equals(tokenFromServer)) {
      return false;
    }
    return true;
  }

}
