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
   * 累计分红乐心
   */
  private BigDecimal totalLeMind;
  /**
   * 累计消费
   */
  private BigDecimal totalConsume;
  /**
   * 分红乐分
   */
  private BigDecimal bonusLeScore;
  /**
   * 公益金额
   */
  private BigDecimal publicAmount;


  /**
   * 统计日期
   */
  private Date reportDate;

  /**
   * 备注
   */
  private String remark;


  @Column(scale = 2, precision = 10)
  public BigDecimal getConsumeTotalAmount() {
    return consumeTotalAmount;
  }

  public void setConsumeTotalAmount(BigDecimal consumeTotalAmount) {
    this.consumeTotalAmount = consumeTotalAmount;
  }

  public Integer getConsumePeopleNum() {
    return consumePeopleNum;
  }

  public void setConsumePeopleNum(Integer consumePeopleNum) {
    this.consumePeopleNum = consumePeopleNum;
  }

  public Integer getSellerNum() {
    return sellerNum;
  }

  public void setSellerNum(Integer sellerNum) {
    this.sellerNum = sellerNum;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getPublicTotalAmount() {
    return publicTotalAmount;
  }

  public void setPublicTotalAmount(BigDecimal publicTotalAmount) {
    this.publicTotalAmount = publicTotalAmount;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getTotalLeMind() {
    return totalLeMind;
  }

  public void setTotalLeMind(BigDecimal totalLeMind) {
    this.totalLeMind = totalLeMind;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getTotalConsume() {
    return totalConsume;
  }

  public void setTotalConsume(BigDecimal totalConsume) {
    this.totalConsume = totalConsume;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getBonusLeScore() {
    return bonusLeScore;
  }

  public void setBonusLeScore(BigDecimal bonusLeScore) {
    this.bonusLeScore = bonusLeScore;
  }

  @Column(scale = 2, precision = 10)
  public BigDecimal getPublicAmount() {
    return publicAmount;
  }

  public void setPublicAmount(BigDecimal publicAmount) {
    this.publicAmount = publicAmount;
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
