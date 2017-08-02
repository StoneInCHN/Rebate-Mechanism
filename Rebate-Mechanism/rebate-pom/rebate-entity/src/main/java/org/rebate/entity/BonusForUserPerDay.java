package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;

/**
 * 用户乐心每日分红乐分记录
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_bonus_for_user_per_day", indexes = {@Index(name = "bonusDateIndex",
    columnList = "bonusDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_bonus_for_user_per_day_sequence")
public class BonusForUserPerDay extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 产生每日分红用户
   */
  private EndUser endUser;

  /**
   * 分红乐心数量
   */
  private Integer bonusMindCount;

  /**
   * 分红乐心数量
   */
  private Integer reduceMindCount;


  /**
   * 分红产生日期
   */
  private Date bonusDate;

  /**
   * 分红金额
   */
  private BigDecimal bonusAmount;


  /**
   * 备注
   */
  private String remark;

  public Integer getBonusMindCount() {
    return bonusMindCount;
  }

  public void setBonusMindCount(Integer bonusMindCount) {
    this.bonusMindCount = bonusMindCount;
  }

  public Integer getReduceMindCount() {
    return reduceMindCount;
  }

  public void setReduceMindCount(Integer reduceMindCount) {
    this.reduceMindCount = reduceMindCount;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }



  @Column(scale = 4, precision = 12)
  public BigDecimal getBonusAmount() {
    return bonusAmount;
  }


  public void setBonusAmount(BigDecimal bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  @Temporal(TemporalType.DATE)
  public Date getBonusDate() {
    return bonusDate;
  }


  public void setBonusDate(Date bonusDate) {
    this.bonusDate = bonusDate;
  }

  @Column(length = 100)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }



}
