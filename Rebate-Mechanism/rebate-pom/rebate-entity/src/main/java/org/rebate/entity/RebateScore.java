package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.RebateType;

/**
 * 用户消费返利积分记录
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_rebate_score")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_rebate_score_sequence")
public class RebateScore extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 返利积分所属商家
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
   * 返利类型
   */
  private RebateType rebateType;

  /**
   * 备注
   */
  private String remark;



  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getRebateScore() {
    return rebateScore;
  }

  public void setRebateScore(BigDecimal rebateScore) {
    this.rebateScore = rebateScore;
  }

  public RebateType getRebateType() {
    return rebateType;
  }

  public void setRebateType(RebateType rebateType) {
    this.rebateType = rebateType;
  }

  @Column(scale = 2, precision = 10)
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



  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }


  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @ManyToOne
  public Seller getSeller() {
    return seller;
  }


  public void setSeller(Seller seller) {
    this.seller = seller;
  }



}
