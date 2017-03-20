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
 * 返利记录(包括消费返利，推荐返利，提成返利)
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_rebate_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_rebate_score_sequence")
public class RebateRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 消费返利的商家
   */
  private Seller seller;

  /**
   * 返利积分的用户（用户可以是endUser或seller）
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
   * 返利乐心
   */
  private BigDecimal rebateLeMind;

  /**
   * 返利乐分
   */
  private BigDecimal rebateLeScore;
  /**
   * 返利类型
   */
  private RebateType rebateType;

  /**
   * 用户当前积分
   */
  private BigDecimal userCurScore;

  /**
   * 用户当前乐心
   */
  private BigDecimal userCurLeMind;

  /**
   * 用户当前乐分
   */
  private BigDecimal userCurLeScore;
  /**
   * 备注
   */
  private String remark;


  @Column(scale = 2, precision = 10)
  public BigDecimal getRebateLeMind() {
    return rebateLeMind;
  }

  public void setRebateLeMind(BigDecimal rebateLeMind) {
    this.rebateLeMind = rebateLeMind;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getRebateLeScore() {
    return rebateLeScore;
  }

  public void setRebateLeScore(BigDecimal rebateLeScore) {
    this.rebateLeScore = rebateLeScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUserCurLeMind() {
    return userCurLeMind;
  }

  public void setUserCurLeMind(BigDecimal userCurLeMind) {
    this.userCurLeMind = userCurLeMind;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getUserCurLeScore() {
    return userCurLeScore;
  }

  public void setUserCurLeScore(BigDecimal userCurLeScore) {
    this.userCurLeScore = userCurLeScore;
  }

  @Column(scale = 2, precision = 10)
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
