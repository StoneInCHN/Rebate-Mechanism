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
 * 录单购物车
 * 
 * @author huyong
 *
 */
@Entity
@Table(name = "rm_seller_order_cart", indexes = {@Index(name = "createDateIndex",
    columnList = "createDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_seller_order_cart_sequence")
public class SellerOrderCart extends BaseEntity {


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
   * 消费金额
   */
  private BigDecimal amount;


  /**
   * 返利金额
   */
  private BigDecimal rebateAmount;

  /**
   * 备注
   */
  private String remark;


  @Column(scale = 4, precision = 12)
  public BigDecimal getRebateAmount() {
    return rebateAmount;
  }

  public void setRebateAmount(BigDecimal rebateAmount) {
    this.rebateAmount = rebateAmount;
  }


  @Column(scale = 4, precision = 12)
  public BigDecimal getAmount() {
    return amount;
  }


  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }



  @Column(length = 50)
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
