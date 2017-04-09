package org.rebate.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 代理商提成配置
 * 
 *
 */
@Entity
@Table(name = "rm_agent_commission_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_agent_commission_config_sequence")
public class AgentCommissionConfig extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 地区
   */
  private Area area;

  /**
   * 地区代理词提成百分比
   */
  private BigDecimal commissionRate;


  @Column(scale = 5, precision = 6)
  public BigDecimal getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(BigDecimal commissionRate) {
    this.commissionRate = commissionRate;
  }

  @OneToOne
  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }



}
