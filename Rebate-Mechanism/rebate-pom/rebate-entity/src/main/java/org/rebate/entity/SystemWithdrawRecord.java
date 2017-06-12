package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;

/**
 * 系统提现记录（admin管理员系统内部提现）
 * 
 *
 */
@Entity
@Table(name = "rm_system_withdraw_record", indexes = {
    @Index(name = "createDateIndex", columnList = "createDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_system_withdraw_record_sequence")
public class SystemWithdrawRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 提现金额
   */
  private BigDecimal amount;

  /**
   * 提现人
   */
  private String operator;

  /**
   * 提现人手机号（验证码发送到的手机）
   */
  private String cellPhoneNum; 

  /**
   * 提现失败时返回的错误信息
   */
  private String withdrawMsg;
  /**
   * 提现状态
   */
  private ClearingStatus status;

  /**
   * 是否已提现
   */
  private Boolean isWithdraw;
  
  /**
   * 提现手续费
   */
  private BigDecimal handlingCharge;

  /**
   * 提现银行卡ID
   */
  private Long bankCardId;
  
  /**
   * 银行卡号
   */
  private String cardNum;
  
  /**
   * 交易批次号
   */
  private String reqSn;

  /**
   * 备注
   */
  private String remark;

  @Column(length = 50, nullable = false, updatable = false)
  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }
  
  @Column(length = 20, nullable = false, updatable = false)
  public String getCellPhoneNum() {
    return cellPhoneNum;
  }

  public void setCellPhoneNum(String cellPhoneNum) {
    this.cellPhoneNum = cellPhoneNum;
  }
  
  public Long getBankCardId() {
    return bankCardId;
  }

  public void setBankCardId(Long bankCardId) {
    this.bankCardId = bankCardId;
  }
  
  @Column(length = 50, nullable = false, updatable = false)
  public String getCardNum() {
    return cardNum;
  }

  public void setCardNum(String cardNum) {
    this.cardNum = cardNum;
  }

  @Column(scale = 4, precision = 12, updatable = false)
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Column(length = 100)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column(length = 100)
  public String getWithdrawMsg() {
    return withdrawMsg;
  }

  public void setWithdrawMsg(String withdrawMsg) {
    this.withdrawMsg = withdrawMsg;
  }

  public Boolean getIsWithdraw() {
    return isWithdraw;
  }

  public void setIsWithdraw(Boolean isWithdraw) {
    this.isWithdraw = isWithdraw;
  }
  @Column(scale = 4, precision = 12)
  public BigDecimal getHandlingCharge() {
	return handlingCharge;
  }
	
    public void setHandlingCharge(BigDecimal handlingCharge) {
      this.handlingCharge = handlingCharge;
    }
	public String getReqSn() {
		return reqSn;
	}
	
	public void setReqSn(String reqSn) {
		this.reqSn = reqSn;
	}


	public ClearingStatus getStatus() {
		return status;
	}

	public void setStatus(ClearingStatus status) {
		this.status = status;
	}
	
}
