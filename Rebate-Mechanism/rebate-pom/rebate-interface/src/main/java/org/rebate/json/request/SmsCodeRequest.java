package org.rebate.json.request;

import org.rebate.entity.commonenum.CommonEnum.SmsCodeType;
import org.rebate.json.base.BaseRequest;


public class SmsCodeRequest extends BaseRequest {

  /**
   * 验证码
   */
  private String smsCode;

  /**
   * 手机号
   */
  private String cellPhoneNum;

  /**
   * 验证码类型
   */
  private SmsCodeType smsCodeType;


  public SmsCodeType getSmsCodeType() {
    return smsCodeType;
  }

  public void setSmsCodeType(SmsCodeType smsCodeType) {
    this.smsCodeType = smsCodeType;
  }

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
