package org.rebate.json.request;

import org.rebate.json.base.BaseRequest;


public class SmsCodeRequest extends BaseRequest {

  /**
   * 验证码
   */
  private String smsCode;

  /**
   * 推荐人手机号
   */
  private String cellPhoneNum;


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


}
