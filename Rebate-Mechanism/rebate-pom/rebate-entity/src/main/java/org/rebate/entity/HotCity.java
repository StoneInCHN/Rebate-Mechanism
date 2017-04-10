package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 热门城市
 * 
 *
 */
@Entity
@Table(name = "rm_hot_city")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_hot_city_sequence")
public class HotCity extends BaseEntity {


  private static final long serialVersionUID = 1L;

  /**
   * 城市ID(关联areaId)
   */
  private Long cityId;

  /**
   * 城市名称
   */
  private String cityName;

  /**
   * 显示排序
   */
  private Integer cityOrder;


  public Integer getCityOrder() {
    return cityOrder;
  }

  public void setCityOrder(Integer cityOrder) {
    this.cityOrder = cityOrder;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  @Column(length = 100)
  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }



}
