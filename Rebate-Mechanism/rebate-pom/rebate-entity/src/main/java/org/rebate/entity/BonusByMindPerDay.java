package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "rm_bonus_by_mind_per_day")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_bonus_by_mind_per_day_sequence")
public class BonusByMindPerDay extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 产生每日分红的乐心
   */
  private MindExchangeByDay mindExchangeByDay;

  /**
   * 分红乐分数量
   */
  private BigDecimal bonusAmount;


  /**
   * 分红产生日期
   */
  private Date bonusDate;

  /**
   * 备注
   */
  private String remark;


  @ManyToOne
  public MindExchangeByDay getMindExchangeByDay() {
    return mindExchangeByDay;
  }

  public void setMindExchangeByDay(MindExchangeByDay mindExchangeByDay) {
    this.mindExchangeByDay = mindExchangeByDay;
  }


  @Column(scale = 2, precision = 10)
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
