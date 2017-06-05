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
   * 全国公益商家
   */
  private Integer sellerNum;

  /**
   * 公益总金额
   */
  private BigDecimal publicTotalAmount;

  /**
   * 当日累计分红乐心
   */
  private BigDecimal leMindByDay;
  /**
   * 当日累计消费
   */
  private BigDecimal consumeByDay;
  /**
   * 当日分红乐分（改完乐豆）
   */
  private BigDecimal bonusLeScoreByDay;
  /**
   * 公益金额
   */
  private BigDecimal publicAmountByDay;

  /**
   * 平台收益
   */
  private BigDecimal platformIncome;
  /**
   * 每日让利总额
   */
  private BigDecimal profitByDay;
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
   * 奖池
   */
  private BigDecimal award;

  /**
   * 代理商提成金
   */
  private BigDecimal agentCommission;

  /**
   * 用户推荐提成金
   */
  private BigDecimal userRecommendCommission;

  /**
   * 商户推荐提成金
   */
  private BigDecimal sellerRecommendCommission;

  /**
   * 累计分红总额
   */
  private BigDecimal totalBonus;


  @Column(scale = 4, precision = 18)
  @JsonProperty
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
  public Integer getSellerNum() {
    return sellerNum;
  }

  public void setSellerNum(Integer sellerNum) {
    this.sellerNum = sellerNum;
  }

  @JsonProperty
  @Column(scale = 4, precision = 18)
  public BigDecimal getPublicTotalAmount() {
    return publicTotalAmount;
  }

  public void setPublicTotalAmount(BigDecimal publicTotalAmount) {
    this.publicTotalAmount = publicTotalAmount;
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
  @Column(scale = 4, precision = 18)
  public BigDecimal getPublicAmountByDay() {
    return publicAmountByDay;
  }

  public void setPublicAmountByDay(BigDecimal publicAmountByDay) {
    this.publicAmountByDay = publicAmountByDay;
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
  public BigDecimal getAward() {
    return award;
  }

  public void setAward(BigDecimal award) {
    this.award = award;
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
  public BigDecimal getProfitByDay() {
    return profitByDay;
  }

  public void setProfitByDay(BigDecimal profitByDay) {
    this.profitByDay = profitByDay;
  }


}
