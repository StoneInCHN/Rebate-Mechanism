package org.rebate.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 调用第三方API次数统计
 * 
 */
@Entity
@Table(name = "rm_third_report", indexes = {@Index(name = "statisticsDateIndex",
    columnList = "statisticsDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_third_report_sequence")
public class ThirdApiReport extends BaseEntity {

  private static final long serialVersionUID = 1L;



  /**
   * 银行卡四元素校验当日调用次数
   */
  private Integer verifyBankCardCount;

  /**
   * 银行卡四元素校验累计调用次数
   */
  private Integer verifyBankCardTotalCount;

  /**
   * 统计日期
   */
  private Date statisticsDate;


  @Temporal(TemporalType.DATE)
  @JsonProperty
  public Date getStatisticsDate() {
    return statisticsDate;
  }


  public void setStatisticsDate(Date statisticsDate) {
    this.statisticsDate = statisticsDate;
  }


  public Integer getVerifyBankCardCount() {
    return verifyBankCardCount;
  }


  public void setVerifyBankCardCount(Integer verifyBankCardCount) {
    this.verifyBankCardCount = verifyBankCardCount;
  }


  public Integer getVerifyBankCardTotalCount() {
    return verifyBankCardTotalCount;
  }


  public void setVerifyBankCardTotalCount(Integer verifyBankCardTotalCount) {
    this.verifyBankCardTotalCount = verifyBankCardTotalCount;
  }


}
