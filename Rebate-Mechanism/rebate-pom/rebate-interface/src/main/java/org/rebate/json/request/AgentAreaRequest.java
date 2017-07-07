package org.rebate.json.request;

import java.util.Date;

import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;
import org.rebate.json.base.BaseRequest;


public class AgentAreaRequest extends BaseRequest {

  /**
   * 地区ID
   */
  private Long areaId;

  /**
   * 地区代理级别
   */
  private AgencyLevel agencyLevel;
  /**
   * 开始查询日期
   */
  private Date startDate;
  /**
   * 结束查询日期
   */
  private Date endDate;
  
  public Long getAreaId() {
	return areaId;
  }
  public void setAreaId(Long areaId) {
	this.areaId = areaId;
  }
  public AgencyLevel getAgencyLevel() {
	return agencyLevel;
  }
  public void setAgencyLevel(AgencyLevel agencyLevel) {
	this.agencyLevel = agencyLevel;
  }
  public Date getStartDate() {
	return startDate;
  }
  public void setStartDate(Date startDate) {
	this.startDate = startDate;
  }
  public Date getEndDate() {
	return endDate;
  }
  public void setEndDate(Date endDate) {
	this.endDate = endDate;
  }




}
