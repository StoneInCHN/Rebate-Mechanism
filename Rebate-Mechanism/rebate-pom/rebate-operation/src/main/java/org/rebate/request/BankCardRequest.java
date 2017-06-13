package org.rebate.request;

import java.util.Date;


public class BankCardRequest {
  /**
   * 持卡人姓名
   */
  private String ownerName;

  /**
   * 银行卡号
   */
  private String cardNum;

  /**
   * 银行类别
   */
  private String bankName;


  /**
   * 银行卡类型
   */
  private String cardType;

  /**
   * 预留手机号
   */
  private String reservedMobile;

  /**
   * 时间from
   */
  private Date dateFrom;

  /**
   * 时间to
   */
  private Date dateTo;

  /**
   * 是否删除
   */
  private Boolean delStatus;

  /**
   * 是否为默认银行卡
   */
  private Boolean isDefault;


  /**
   * 身份证号
   */
  private String idCard;

  private String cellPhoneNum;

  /**
   * 昵称
   */
  private String nickName;

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

  public String getReservedMobile() {
    return reservedMobile;
  }

  public void setReservedMobile(String reservedMobile) {
    this.reservedMobile = reservedMobile;
  }

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  public Boolean getDelStatus() {
    return delStatus;
  }

  public void setDelStatus(Boolean delStatus) {
    this.delStatus = delStatus;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

}
