package org.rebate.json.request;

import org.rebate.json.base.BaseRequest;

public class BankCardRequest extends BaseRequest {

  /**
   * 持卡人姓名
   */
  private String ownerName;

  /**
   * 银行卡号
   */
  private String cardNum;

  /**
   * 银行名称
   */
  private String bankName;

  /**
   * 银行卡类型
   */
  private String cardType;


  /**
   * 是否为默认银行卡
   */
  private Boolean isDefault;

  /**
   * 预留手机号
   */
  private String reservedMobile;

  /**
   * 验证码
   */
  private String smsCode;

  /**
   * 银行卡图标
   */
  private String bankLogo;


  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public String getCardNum() {
    return cardNum;
  }

  public void setCardNum(String cardNum) {
    this.cardNum = cardNum;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getCardType() {
    return cardType;
  }

  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }

  public String getReservedMobile() {
    return reservedMobile;
  }

  public void setReservedMobile(String reservedMobile) {
    this.reservedMobile = reservedMobile;
  }

  public String getBankLogo() {
    return bankLogo;
  }

  public void setBankLogo(String bankLogo) {
    this.bankLogo = bankLogo;
  }


}
