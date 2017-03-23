package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;

/**
 * 代理商
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_agent")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_agent_sequence")
public class Agent extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 地区
   */
  private Area area;

  /**
   * 代理级别
   */
  private AgencyLevel agencyLevel;

  /**
   * 用户
   */
  private EndUser endUser;


  @OneToOne(mappedBy = "agent")
  public EndUser getEndUser() {
    return endUser;
  }

  public void setEndUser(EndUser endUser) {
    this.endUser = endUser;
  }

  @ManyToOne
  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  public AgencyLevel getAgencyLevel() {
    return agencyLevel;
  }

  public void setAgencyLevel(AgencyLevel agencyLevel) {
    this.agencyLevel = agencyLevel;
  }

}
