package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;

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
   * 订单编号
   */
  private String sn;

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
   * 商家收入金额（乐分），打折后的金额
   */
  private BigDecimal sellerIncome;

  /**
   * 消费返利用户积分
   */
  private BigDecimal userScore;

  /**
   * 消费返利商户积分
   */
  private BigDecimal sellerScore;

  /**
   * 备注
   */
  private String remark;

  /**
   * 订单对应的用户评价
   */
  private SellerEvaluate evaluate;

  /**
   * 订单状态
   */
  private OrderStatus status;

  // /**
  // * 订单直接收益是否结算(提取)
  // */
  // private Boolean isClearing = false;


  public BigDecimal getSellerIncome() {
    return sellerIncome;
  }


  public void setSellerIncome(BigDecimal sellerIncome) {
    this.sellerIncome = sellerIncome;
  }


  // public Boolean getIsClearing() {
  // return isClearing;
  // }
  //
  //
  // public void setIsClearing(Boolean isClearing) {
  // this.isClearing = isClearing;
  // }


  public OrderStatus getStatus() {
    return status;
  }


  public void setStatus(OrderStatus status) {
    this.status = status;
  }


  @Column(length = 30)
  public String getSn() {
    return sn;
  }


  public void setSn(String sn) {
    this.sn = sn;
  }

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
  public BigDecimal getUserScore() {
    return userScore;
  }

  public void setUserScore(BigDecimal userScore) {
    this.userScore = userScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getSellerScore() {
    return sellerScore;
  }

  public void setSellerScore(BigDecimal sellerScore) {
    this.sellerScore = sellerScore;
  }

  @Column(length = 50)
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
