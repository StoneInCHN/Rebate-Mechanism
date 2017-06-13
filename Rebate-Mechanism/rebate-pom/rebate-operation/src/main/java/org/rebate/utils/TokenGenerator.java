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


}
