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

/**
 * 返利积分记录
 * 
 *
 */
@Entity
@Table(name = "rm_rebate_record", indexes = {@Index(name = "createDateIndex",
    columnList = "createDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_rebate_score_sequence")
public class RebateRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 消费返利的商家
   */
  private Seller seller;

  /**
   * 返利积分的用户
   */
  private EndUser endUser;

  /**
   * 订单ID
   */
  private Long orderId;

  /**
   * 消费金额
   */
  private BigDecimal amount;

  /**
   * 返利积分
   */
  private BigDecimal rebateScore;


  /**
   * 用户当前积分
   */
  private BigDecimal userCurScore;


  /**
   * 备注
   */
  private String remark;

  /**
   * 支付方式
   */
  private String paymentType;

  @Column(length = 20)
  public String getPaymentType() {
    return paymentType;
  }


  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getUserCurScore() {
    return userCurScore;
  }

  public void setUserCurScore(BigDecimal userCurScore) {
    this.userCurScore = userCurScore;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getRebateScore() {
    return rebateScore;
  }

  public void setRebateScore(BigDecimal rebateScore) {
    this.rebateScore = rebateScore;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }



  @Column(length = 500)
  public String getRemark() {
    return remark;
  }


  public void setRemark(String remark) {
    this.remark = remark;
  }



  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getEndUser() {
    return endUser;
  }


  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public Seller getSeller() {
    return seller;
  }


  public void setSeller(Seller seller) {
    this.seller = seller;
  }



}
