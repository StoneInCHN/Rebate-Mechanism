package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;

public class LeScoreRecordReq {

  /**
   * 商家名字
   */
  private String userName;
  /**
   * 乐分类型
   */
  private LeScoreType leScoreType;
  
  /**
   * 手机号
   */
  private String cellPhoneNum;
  
  /** 创建日期 */
  private Date beginDate;
  
  /** 创建日期 */
  private Date endDate;
  
  /**
   * 提现状态
   */
  private ApplyStatus withdrawStatus;

  /**
   * 支付渠道 (通联 ALLINPAY,九派 IUPAI)
   */
  private PaymentChannel paymentChannel;
  
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public LeScoreType getLeScoreType() {
    return leScoreType;
  }

  public void setLeScoreType(LeScoreType leScoreType) {
    this.leScoreType = leScoreType;
  }

  public ApplyStatus getWithdrawStatus() {
    return withdrawStatus;
  }

  public void setWithdrawStatus(ApplyStatus withdrawStatus) {
    this.withdrawStatus = withdrawStatus;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public PaymentChannel getPaymentChannel() {
	return paymentChannel;
  }

  public void setPaymentChannel(PaymentChannel paymentChannel) {
	this.paymentChannel = paymentChannel;
  }
  
}
