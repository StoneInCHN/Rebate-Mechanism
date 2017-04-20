package org.rebate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;

/**
 * 每日乐分分红相关参数记录
 * 
 *
 */
@Entity
@Table(name = "rm_bonus_param_per_day", indexes = {@Index(name = "bonusDateIndex",
    columnList = "bonusDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_bonus_param_per_day_sequence")
public class BonusParamPerDay extends BaseEntity {


  private static final long serialVersionUID = 1L;


  /**
   * 每日分红总金额占平台每日总收益的比例
   */
  private String totalBonusPerConfig;

  /**
   * 收益后乐分乐豆比例
   */
  private String leScorePerConfig;

  /**
   * 当日订单数
   */
  private Integer orderCount;
  /**
   * 当日平台总收益金额
   */
  private String rebateTotalAmount;
  /**
   * 每日用于分红计算的总金额
   */
  private String bonusCalAmount;
  /**
   * 每日乐心大于等于1的用户
   */
  private Integer leMindUserCount;
  /**
   * 当天乐心换算乐分的value(当天总收益的分红金额/当天消费乐心大于等于1的用户人数)
   */
  private String calValue;
  /**
   * 当日平台可产生用户分红的乐心
   */
  private Integer avlLeMindCount;
  /**
   * 每日分红金额
   */
  private String bonusAmount;
  /**
   * 分红产生日期
   */
  private Date bonusDate;

  /**
   * 备注
   */
  private String remark;


  @Column(length = 30)
  public String getTotalBonusPerConfig() {
    return totalBonusPerConfig;
  }

  public void setTotalBonusPerConfig(String totalBonusPerConfig) {
    this.totalBonusPerConfig = totalBonusPerConfig;
  }

  @Column(length = 30)
  public String getLeScorePerConfig() {
    return leScorePerConfig;
  }

  public void setLeScorePerConfig(String leScorePerConfig) {
    this.leScorePerConfig = leScorePerConfig;
  }

  public Integer getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(Integer orderCount) {
    this.orderCount = orderCount;
  }


  @Column(length = 30)
  public String getRebateTotalAmount() {
    return rebateTotalAmount;
  }

  public void setRebateTotalAmount(String rebateTotalAmount) {
    this.rebateTotalAmount = rebateTotalAmount;
  }

  @Column(length = 30)
  public String getBonusCalAmount() {
    return bonusCalAmount;
  }

  public void setBonusCalAmount(String bonusCalAmount) {
    this.bonusCalAmount = bonusCalAmount;
  }

  public Integer getLeMindUserCount() {
    return leMindUserCount;
  }

  public void setLeMindUserCount(Integer leMindUserCount) {
    this.leMindUserCount = leMindUserCount;
  }

  @Column(length = 30)
  public String getCalValue() {
    return calValue;
  }

  public void setCalValue(String calValue) {
    this.calValue = calValue;
  }

  public Integer getAvlLeMindCount() {
    return avlLeMindCount;
  }

  public void setAvlLeMindCount(Integer avlLeMindCount) {
    this.avlLeMindCount = avlLeMindCount;
  }

  @Column(length = 30)
  public String getBonusAmount() {
    return bonusAmount;
  }

  public void setBonusAmount(String bonusAmount) {
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
