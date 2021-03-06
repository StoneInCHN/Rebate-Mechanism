package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;

/**
 * 用户消费订单
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_order", indexes = {@Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "snIndex", columnList = "sn")})
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
   * 支付方式ID
   */
  private String paymentTypeId;


  /**
   * 支付时间
   */
  private Date paymentTime;

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

  /**
   * 是否为乐豆消费
   */
  private Boolean isBeanPay;

  // /**
  // * 订单直接收益是否结算(提取)
  // */
  // private Boolean isClearing = false;

  /**
   * 返利金额
   */
  private BigDecimal rebateAmount;

  /**
   * 鼓励金额
   */
  private BigDecimal encourageAmount;
  

  /**
   * 是否为录单订单
   */
  private Boolean isSallerOrder = false;

  /**
   * 商家折扣
   */
  private BigDecimal sellerDiscount;

  /**
   * 订单直接收益是否结算(提取)
   */
  private Boolean isClearing = false;

  /**
   * 批量录单时流水号
   */
  private String batchSn;


  /**
   * 抵扣金额
   */
  private BigDecimal deductAmount;

  @Column(length = 5)
  public String getPaymentTypeId() {
    return paymentTypeId;
  }

  public void setPaymentTypeId(String paymentTypeId) {
    this.paymentTypeId = paymentTypeId;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getDeductAmount() {
    return deductAmount;
  }

  public void setDeductAmount(BigDecimal deductAmount) {
    this.deductAmount = deductAmount;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getEncourageAmount() {
    return encourageAmount;
  }

  public void setEncourageAmount(BigDecimal encourageAmount) {
    this.encourageAmount = encourageAmount;
  }

  @Transient
  public BigDecimal getRebateAmount() {
    if (amount != null && sellerIncome != null) {
      rebateAmount = amount.subtract(sellerIncome);
    }
    return rebateAmount;
  }

  public void setRebateAmount(BigDecimal rebateAmount) {
    this.rebateAmount = rebateAmount;
  }


  public Date getPaymentTime() {
    return paymentTime;
  }

  public void setPaymentTime(Date paymentTime) {
    this.paymentTime = paymentTime;
  }

  public Boolean getIsBeanPay() {
    return isBeanPay;
  }

  public void setIsBeanPay(Boolean isBeanPay) {
    this.isBeanPay = isBeanPay;
  }

  @Column(scale = 4, precision = 12)
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

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  public SellerEvaluate getEvaluate() {
    return evaluate;
  }


  public void setEvaluate(SellerEvaluate evaluate) {
    this.evaluate = evaluate;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getAmount() {
    return amount;
  }


  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getUserScore() {
    return userScore;
  }

  public void setUserScore(BigDecimal userScore) {
    this.userScore = userScore;
  }

  @Column(scale = 4, precision = 12)
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

  public Boolean getIsSallerOrder() {
	return isSallerOrder;
  }

  public void setIsSallerOrder(Boolean isSallerOrder) {
	this.isSallerOrder = isSallerOrder;
  }
  @Transient
  public BigDecimal getSellerDiscount() {
    sellerDiscount = sellerIncome.divide(amount, 1, BigDecimal.ROUND_HALF_UP);
    return sellerDiscount;
  }

  public void setSellerDiscount(BigDecimal sellerDiscount) {
    this.sellerDiscount = sellerDiscount;
  }
  public Boolean getIsClearing() {
	return isClearing;
  }

  public void setIsClearing(Boolean isClearing) {
	this.isClearing = isClearing;
  }
  @Column(length = 40)
  public String getBatchSn() {
    return batchSn;
  }

  public void setBatchSn(String batchSn) {
    this.batchSn = batchSn;
  }  
}
