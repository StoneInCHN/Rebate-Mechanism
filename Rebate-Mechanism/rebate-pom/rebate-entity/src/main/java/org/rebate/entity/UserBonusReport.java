package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;

/**
 * 每日用户分红统计
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_user_bonus_report")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_bonus_report_sequence")
public class UserBonusReport extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 分红乐分
   */
  private BigDecimal bonusLeScore;
  /**
   * 总消费金额
   */
  private BigDecimal consumeTotalAmount;
  /**
   * 最高分红乐分
   */
  private BigDecimal highBonusLeScore;

  /**
   * 积分
   */
  private BigDecimal score;

  /**
   * 乐心
   */
  private BigDecimal leMind;
  /**
   * 乐分
   */
  private BigDecimal leScore;
  /**
   * 乐豆
   */
  private BigDecimal leBean;


  /**
   * 统计日期
   */
  private Date reportDate;

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 备注
   */
  private String remark;


  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getConsumeTotalAmount() {
    return consumeTotalAmount;
  }

  public void setConsumeTotalAmount(BigDecimal consumeTotalAmount) {
    this.consumeTotalAmount = consumeTotalAmount;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getHighBonusLeScore() {
    return highBonusLeScore;
  }

  public void setHighBonusLeScore(BigDecimal highBonusLeScore) {
    this.highBonusLeScore = highBonusLeScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getLeMind() {
    return leMind;
  }

  public void setLeMind(BigDecimal leMind) {
    this.leMind = leMind;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getLeScore() {
    return leScore;
  }

  public void setLeScore(BigDecimal leScore) {
    this.leScore = leScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getLeBean() {
    return leBean;
  }

  public void setLeBean(BigDecimal leBean) {
    this.leBean = leBean;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getBonusLeScore() {
    return bonusLeScore;
  }

  public void setBonusLeScore(BigDecimal bonusLeScore) {
    this.bonusLeScore = bonusLeScore;
  }


  @Temporal(TemporalType.DATE)
  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }


  @Column(length = 100)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }



}
