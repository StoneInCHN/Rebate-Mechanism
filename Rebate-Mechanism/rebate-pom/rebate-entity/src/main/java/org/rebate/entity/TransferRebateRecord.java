package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.RebateType;

/**
 * 
 * 转账乐心记录
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_transfer_rebate_record", indexes = {
    @Index(name = "transIdIndex", columnList = "transId"),
    @Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "transMobileIndex", columnList = "transMobile")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_transfer_rebate_record_sequence")
public class TransferRebateRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 转账人ID
   */
  private Long transId;

  /**
   * 转账人手机号
   */
  private String transMobile;

  /**
   * 转账额度
   */
  private BigDecimal amount;

  /**
   * 转账类型
   */
  private RebateType transType;


  /**
   * 接受用户
   */
  private EndUser receiver;


  /**
   * 备注
   */
  private String remark;


  public Long getTransId() {
    return transId;
  }


  public void setTransId(Long transId) {
    this.transId = transId;
  }


  @Column(length = 20)
  public String getTransMobile() {
    return transMobile;
  }


  public void setTransMobile(String transMobile) {
    this.transMobile = transMobile;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getAmount() {
    return amount;
  }


  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }


  public RebateType getTransType() {
    return transType;
  }


  public void setTransType(RebateType transType) {
    this.transType = transType;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getReceiver() {
    return receiver;
  }


  public void setReceiver(EndUser receiver) {
    this.receiver = receiver;
  }

  @Column(length = 200)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }



}
