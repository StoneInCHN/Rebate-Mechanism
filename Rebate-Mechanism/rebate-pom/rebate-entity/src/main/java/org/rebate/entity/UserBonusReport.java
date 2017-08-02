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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 每日用户分红统计
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_user_bonus_report", indexes = {@Index(name = "reportDateIndex",
    columnList = "reportDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_bonus_report_sequence")
public class UserBonusReport extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 分红乐分（改完乐豆）
   */
  private BigDecimal bonusLeScore;
  /**
   * 总消费金额
   */
  private BigDecimal consumeTotalAmount;
  /**
   * 最高分红乐分（改完乐豆）
   */
  private BigDecimal highBonusLeScore;

  /**
   * 统计日期
   */
  private Date reportDate;

  /**
   * 用户ID
   */
  private EndUser userId;

  /**
   * 备注
   */
  private String remark;


  @ManyToOne(fetch = FetchType.LAZY)
  @JsonProperty
  public EndUser getUserId() {
    return userId;
  }

  public void setUserId(EndUser userId) {
    this.userId = userId;
  }

  @Column(scale = 4, precision = 12)
  @JsonProperty
  public BigDecimal getConsumeTotalAmount() {
    return consumeTotalAmount;
  }

  public void setConsumeTotalAmount(BigDecimal consumeTotalAmount) {
    this.consumeTotalAmount = consumeTotalAmount;
  }

  @Column(scale = 4, precision = 12)
  @JsonProperty
  public BigDecimal getHighBonusLeScore() {
    return highBonusLeScore;
  }

  public void setHighBonusLeScore(BigDecimal highBonusLeScore) {
    this.highBonusLeScore = highBonusLeScore;
  }


  @Column(scale = 4, precision = 12)
  @JsonProperty
  public BigDecimal getBonusLeScore() {
    return bonusLeScore;
  }

  public void setBonusLeScore(BigDecimal bonusLeScore) {
    this.bonusLeScore = bonusLeScore;
  }


  @Temporal(TemporalType.DATE)
  @JsonProperty
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
