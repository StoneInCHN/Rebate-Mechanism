package org.rebate.beans;

import java.io.Serializable;


/**
 * 短信验证码实体
 * 
 * @author shijun
 *
 */
public class SMSVerificationCode implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 验证码
   */
  private String smsCode;

  /** 短信验证码过期时间token */
  private String timeoutToken;

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }

  public String getTimeoutToken() {
    return timeoutToken;
  }

  public void setTimeoutToken(String timeoutToken) {
    this.timeoutToken = timeoutToken;
  }

}
