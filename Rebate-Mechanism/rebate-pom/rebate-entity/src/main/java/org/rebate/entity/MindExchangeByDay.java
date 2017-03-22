package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;

/**
 * 用户每日积分换乐心记录
 * 
 * @author Andrea 兑换的乐心将每天产生收益，直到收益达到某个设置值
 *
 */
@Entity
@Table(name = "rm_mind_exchange_by_day")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_mind_exchange_by_day_sequence")
public class MindExchangeByDay extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 兑换乐心的用户
   */
  private EndUser endUser;


  /**
   * 可兑换的积分
   */
  private BigDecimal scoreAmount;

  /**
   * 兑换的乐心
   */
  private BigDecimal mindAmount;

  /**
   * 乐心状态（是否可以继续产生收益）
   */
  private CommonStatus status;

  /**
   * 兑换日期
   */
  private Date exChangeDate;

  /**
   * 备注
   */
  private String remark;

  /**
   * 乐心每天产生的乐分收益
   */
  private Set<BonusByMindForDay> bonusByDays = new HashSet<BonusByMindForDay>();


  @OneToMany(mappedBy = "mindExchangeByDay")
  public Set<BonusByMindForDay> getBonusByDays() {
    return bonusByDays;
  }

  public void setBonusByDays(Set<BonusByMindForDay> bonusByDays) {
    this.bonusByDays = bonusByDays;
  }

  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getScoreAmount() {
    return scoreAmount;
  }

  public void setScoreAmount(BigDecimal scoreAmount) {
    this.scoreAmount = scoreAmount;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getMindAmount() {
    return mindAmount;
  }

  public void setMindAmount(BigDecimal mindAmount) {
    this.mindAmount = mindAmount;
  }

  @Temporal(TemporalType.DATE)
  public Date getExChangeDate() {
    return exChangeDate;
  }

  public void setExChangeDate(Date exChangeDate) {
    this.exChangeDate = exChangeDate;
  }

  @Column(length = 100)
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

}
