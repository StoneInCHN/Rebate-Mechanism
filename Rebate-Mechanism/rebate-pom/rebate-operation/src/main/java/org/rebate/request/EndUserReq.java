package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;

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
   * 消费店铺名
   */
  private String sellerName;

  /**
   * 注册时间from
   */
  private Date regDateFrom;

  /**
   * 注册时间to
   */
  private Date regDateTo;

  /**
   * 记录时间from
   */
  private Date recordTimeFrom;

  /**
   * 记录时间to
   */
  private Date recordTimeTo;

  /**
   * 乐心状态（是否可以继续产生收益）
   */
  private CommonStatus status;

  /**
   * 乐分类型
   */
  private LeScoreType leScoreType;


  /**
   * 乐豆变化类型
   */
  private LeBeanChangeType type;

  /**
   * 是否为业务员
   */
  private Boolean isSalesman ;

  public LeBeanChangeType getType() {
    return type;
  }

  public void setType(LeBeanChangeType type) {
    this.type = type;
  }

  public LeScoreType getLeScoreType() {
    return leScoreType;
  }

  public void setLeScoreType(LeScoreType leScoreType) {
    this.leScoreType = leScoreType;
  }

  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }

  public Date getRecordTimeFrom() {
    return recordTimeFrom;
  }

  public void setRecordTimeFrom(Date recordTimeFrom) {
    this.recordTimeFrom = recordTimeFrom;
  }

  public Date getRecordTimeTo() {
    return recordTimeTo;
  }

  public void setRecordTimeTo(Date recordTimeTo) {
    this.recordTimeTo = recordTimeTo;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
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

  public Boolean getIsSalesman() {
    return isSalesman;
  }

  public void setIsSalesman(Boolean isSalesman) {
    this.isSalesman = isSalesman;
  }

  
}
