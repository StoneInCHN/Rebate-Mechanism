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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 每日全国分红统计
 * 
 * @author Andrea
 *
 */
@Entity
@Table(name = "rm_nation_bonus_report")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_nation_bonus_report_sequence")
public class NationBonusReport extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 全国消费总额
   */
  private BigDecimal consumeTotalAmount;
  /**
   * 全国消费人数
   */
  private Integer consumePeopleNum;

  /**
   * 当日累计分红乐心
   */
  private BigDecimal leMindByDay;
  /**
   * 当日累计消费
   */
  private BigDecimal consumeByDay;
  /**
   * 当日分红总额
   */
  private BigDecimal bonusLeScoreByDay;

  /**
   * 累计分红总额
   */
  private BigDecimal totalBonus;

  /**
   * 每日参与分红计算金额
   */
  private BigDecimal bonusByDay;

  /**
   * 每日让利
   */
  private BigDecimal ProfitByDay;
  /**
   * 平台收益
   */
  private BigDecimal platformIncome;
  /**
   * 统计日期
   */
  private Date reportDate;

  /**
   * 备注
   */
  private String remark;

  /**
   * 创业基金
   */
  private BigDecimal ventureFund;


  /**
   * 代理商提成金
   */
  private BigDecimal agentCommission;

  /**
   * 鼓励金
   */
  private BigDecimal encourageConsume;
  /**
   * 用户推荐提成金
   */
  private BigDecimal userRecommendCommission;

  /**
   * 商户推荐提成金
   */
  private BigDecimal sellerRecommendCommission;

  /**
   * 全国公益商家
   */
  private Integer sellerNum;


  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getTotalBonus() {
    return totalBonus;
  }

  public void setTotalBonus(BigDecimal totalBonus) {
    this.totalBonus = totalBonus;
  }

  @Column(scale = 4, precision = 18)
  @JsonProperty
  public BigDecimal getConsumeTotalAmount() {
    return consumeTotalAmount;
  }

  public void setConsumeTotalAmount(BigDecimal consumeTotalAmount) {
    this.consumeTotalAmount = consumeTotalAmount;
  }

  @JsonProperty
  public Integer getConsumePeopleNum() {
    return consumePeopleNum;
  }

  public void setConsumePeopleNum(Integer consumePeopleNum) {
    this.consumePeopleNum = consumePeopleNum;
  }



  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getLeMindByDay() {
    return leMindByDay;
  }

  public void setLeMindByDay(BigDecimal leMindByDay) {
    this.leMindByDay = leMindByDay;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getConsumeByDay() {
    return consumeByDay;
  }

  public void setConsumeByDay(BigDecimal consumeByDay) {
    this.consumeByDay = consumeByDay;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getBonusLeScoreByDay() {
    return bonusLeScoreByDay;
  }

  public void setBonusLeScoreByDay(BigDecimal bonusLeScoreByDay) {
    this.bonusLeScoreByDay = bonusLeScoreByDay;
  }


  @JsonProperty
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

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getPlatformIncome() {
    return platformIncome;
  }

  public void setPlatformIncome(BigDecimal platformIncome) {
    this.platformIncome = platformIncome;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getVentureFund() {
    return ventureFund;
  }

  public void setVentureFund(BigDecimal ventureFund) {
    this.ventureFund = ventureFund;
  }


  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getAgentCommission() {
    return agentCommission;
  }

  public void setAgentCommission(BigDecimal agentCommission) {
    this.agentCommission = agentCommission;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getUserRecommendCommission() {
    return userRecommendCommission;
  }

  public void setUserRecommendCommission(BigDecimal userRecommendCommission) {
    this.userRecommendCommission = userRecommendCommission;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getSellerRecommendCommission() {
    return sellerRecommendCommission;
  }

  public void setSellerRecommendCommission(BigDecimal sellerRecommendCommission) {
    this.sellerRecommendCommission = sellerRecommendCommission;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getBonusByDay() {
    return bonusByDay;
  }

  public void setBonusByDay(BigDecimal bonusByDay) {
    this.bonusByDay = bonusByDay;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getProfitByDay() {
    return ProfitByDay;
  }

  public void setProfitByDay(BigDecimal profitByDay) {
    ProfitByDay = profitByDay;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getEncourageConsume() {
    return encourageConsume;
  }

  public void setEncourageConsume(BigDecimal encourageConsume) {
    this.encourageConsume = encourageConsume;
  }

  @JsonProperty
  public Integer getSellerNum() {
    return sellerNum;
  }

  public void setSellerNum(Integer sellerNum) {
    this.sellerNum = sellerNum;
  }


}
