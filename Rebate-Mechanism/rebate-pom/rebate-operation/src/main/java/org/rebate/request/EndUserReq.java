package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.AccountStatus;

public class EndUserReq {
  /**
   * 用户手机号
   */
  private String cellPhoneNum;

  /**
   * 账号状态
   */
  private AccountStatus accountStatus;
  /**
   * 昵称
   */
  private String nickName;

  /**
   * 注册时间from
   */
  private Date regDateFrom;

  /**
   * 注册时间to
   */
  private Date regDateTo;


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

  public Date getRegDateFrom() {
    return regDateFrom;
  }

  public void setRegDateFrom(Date regDateFrom) {
    this.regDateFrom = regDateFrom;
  }

  public Date getRegDateTo() {
    return regDateTo;
  }

  public void setRegDateTo(Date regDateTo) {
    this.regDateTo = regDateTo;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }


}
