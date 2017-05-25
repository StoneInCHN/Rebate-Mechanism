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
 * 商户货款结算记录
 * 
 *
 */
@Entity
@Table(name = "rm_seller_clearing_record", indexes = {
    @Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "clearingSnIndex", columnList = "clearingSn"),
    @Index(name = "isClearingIndex", columnList = "isClearing")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_le_score_record_sequence")
public class SellerClearingRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 结算货款的商家
   */
  private Seller seller;

  /**
   * 商家用户
   */
  private EndUser endUser;

  /**
   * 结算金额
   */
  private BigDecimal amount;

  /**
   * 订单总金额
   */
  private BigDecimal totalOrderAmount;
  /**
   * 备注
   */
  private String remark;

  /**
   * 是否已结算
   */
  private Boolean isClearing;


  /**
   * 结算银行卡ID
   */
  private Long bankCardId;

  /**
   * 结算货款单编号
   */
  private String clearingSn;



  @Column(scale = 4, precision = 12)
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

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalOrderAmount() {
    return totalOrderAmount;
  }

  public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
    this.totalOrderAmount = totalOrderAmount;
  }

  public Boolean getIsClearing() {
    return isClearing;
  }

  public void setIsClearing(Boolean isClearing) {
    this.isClearing = isClearing;
  }

  public Long getBankCardId() {
    return bankCardId;
  }

  public void setBankCardId(Long bankCardId) {
    this.bankCardId = bankCardId;
  }

  @Column(length = 50)
  public String getClearingSn() {
    return clearingSn;
  }

  public void setClearingSn(String clearingSn) {
    this.clearingSn = clearingSn;
  }

}
