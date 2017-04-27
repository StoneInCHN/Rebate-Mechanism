package org.rebate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;

/**
 * 节假日配置
 * 
 *
 */
@Entity
@Table(name = "rm_holiday_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_holiday_config_sequence")
public class HolidayConfig extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 节假日名称
   */
  private String holidayName;

  /**
   * 开始日期
   */
  private Date startDate;

  /**
   * 结束日期
   */
  private Date endDate;

  /**
   * 是否启用
   */
  private CommonStatus status;


  public CommonStatus getStatus() {
    return status;
  }

  public void setStatus(CommonStatus status) {
    this.status = status;
  }

  @Column(length = 30)
  public String getHolidayName() {
    return holidayName;
  }

  public void setHolidayName(String holidayName) {
    this.holidayName = holidayName;
  }

  @Temporal(TemporalType.DATE)
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Temporal(TemporalType.DATE)
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }


}
