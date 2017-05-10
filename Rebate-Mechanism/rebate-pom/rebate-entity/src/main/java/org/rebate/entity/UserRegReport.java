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
 * 手机app注册用户统计
 * 
 */
@Entity
@Table(name = "rm_user_reg_report", indexes = {@Index(name = "statisticsDateIndex",
    columnList = "statisticsDate")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_reg_report_sequence")
public class UserRegReport extends BaseEntity {

  private static final long serialVersionUID = 1L;



  /**
   * 注册用户数
   */
  private Integer regNum;

  /**
   * 统计日期
   */
  private Date statisticsDate;


  @JsonProperty
  public Integer getRegNum() {
    return regNum;
  }

  public void setRegNum(Integer regNum) {
    this.regNum = regNum;
  }

  @Temporal(TemporalType.DATE)
  @JsonProperty
  public Date getStatisticsDate() {
    return statisticsDate;
  }

  public void setStatisticsDate(Date statisticsDate) {
    this.statisticsDate = statisticsDate;
  }

}
