package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 用户消费订单
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_order_sequence")
public class Order extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 订单所属商家
   */
  private Seller seller;

  /**
   * 订单所属用户
   */
  private EndUser endUser;

  /**
   * 支付方式
   */
  private String paymentType;

  /**
   * 消费金额
   */
  private BigDecimal amount;

  /**
   * 消费赠送积分
   */
  private BigDecimal score;

  /**
   * 备注
   */
  private String remark;

  /**
   * 订单对应的用户评价
   */
  private SellerEvaluate evaluate;


  @OneToOne
  public SellerEvaluate getEvaluate() {
    return evaluate;
  }


  public void setEvaluate(SellerEvaluate evaluate) {
    this.evaluate = evaluate;
  }


  @Column(scale = 2, precision = 10)
  public BigDecimal getAmount() {
    return amount;
  }


  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }


  @Column(scale = 2, precision = 10)
  public BigDecimal getScore() {
    return score;
  }


  public void setScore(BigDecimal score) {
    this.score = score;
  }

  @Column(length = 500)
  public String getRemark() {
    return remark;
  }


  public void setRemark(String remark) {
    this.remark = remark;
  }


  @Column(length = 20)
  public String getPaymentType() {
    return paymentType;
  }


  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
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
