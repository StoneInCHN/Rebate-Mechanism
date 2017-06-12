package org.rebate.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;

public class SystemWithdrawRecordReq {

  /**
   * 提现人
   */
  private String operator;
  /**
   * 提现人手机号（验证码发送到的手机）
   */
  private String cellPhoneNum; 
  /**
   * 提现状态
   */
  private ClearingStatus status;
  
  /**
   * 银行卡号
   */
  private String cardNum;
  
  /** 创建日期 */
  private Date beginDate;
  
  /** 创建日期 */
  private Date endDate;

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }

  public ClearingStatus getStatus() {
    return status;
  }

  public void setStatus(ClearingStatus status) {
    this.status = status;
  }

  public String getCardNum() {
    return cardNum;
  }

  public void setCardNum(String cardNum) {
    this.cardNum = cardNum;
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


 
  
}
