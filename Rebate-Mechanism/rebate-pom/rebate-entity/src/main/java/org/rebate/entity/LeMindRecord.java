package org.rebate.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;

/**
 * 乐心记录
 * 
 *
 */
@Entity
@Table(name = "rm_le_mind_record", indexes = {
    @Index(name = "createDateIndex", columnList = "createDate"),
    @Index(name = "statusIndex", columnList = "status")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_le_mind_record_sequence")
public class LeMindRecord extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 得到乐心的用户
   */
  private EndUser endUser;

  /**
   * 获得乐心数量
   */
  private BigDecimal amount;
  /**
   * 对应的积分数量
   */
  private BigDecimal score;

  /**
   * 用户当前乐心
   */
  private BigDecimal userCurLeMind;
  /**
   * 备注
   */
  private String remark;

  /**
   * 乐心状态（是否可以继续产生收益）
   */
  private CommonStatus status;

  /**
   * 乐心累计产生的分红 (最大不超过定义的1个乐心分红的最大值*乐心数量)
   */
  private BigDecimal totalBonus = new BigDecimal("0");

  /**
   * 根据乐心兑换时的配置，乐心应累计产生的最大分红
   */
  private BigDecimal maxBonus = new BigDecimal("0");;

  /**
   * 乐心每天产生的乐分收益
   */
  private Set<BonusByMindPerDay> bonusByDays = new HashSet<BonusByMindPerDay>();



  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getTotalBonus() {
    return totalBonus;
  }

  public void setTotalBonus(BigDecimal totalBonus) {
    this.totalBonus = totalBonus;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getMaxBonus() {
    return maxBonus;
  }

  public void setMaxBonus(BigDecimal maxBonus) {
    this.maxBonus = maxBonus;
  }

  @OneToMany(mappedBy = "leMindRecord", cascade = CascadeType.ALL)
  public Set<BonusByMindPerDay> getBonusByDays() {
    return bonusByDays;
  }

  public void setBonusByDays(Set<BonusByMindPerDay> bonusByDays) {
    this.bonusByDays = bonusByDays;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  @Column(scale = 4, precision = 12)
  public BigDecimal getUserCurLeMind() {
    return userCurLeMind;
  }

  public void setUserCurLeMind(BigDecimal userCurLeMind) {
    this.userCurLeMind = userCurLeMind;
  }

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

  @ManyToOne
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }


}
