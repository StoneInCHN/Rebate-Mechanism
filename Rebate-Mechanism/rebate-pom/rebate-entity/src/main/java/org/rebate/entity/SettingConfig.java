package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户设置配置
 * 
 */
@Entity
@Table(name = "rm_setting_config", indexes = {@Index(name = "configKeyIndex",
    columnList = "configKey")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_setting_config_sequence")
public class SettingConfig extends BaseEntity {


  private static final long serialVersionUID = -1684057707764082356L;

  /**
   * 配置项值
   */
  private SettingConfigKey configKey;
  /**
   * 配置项值
   */
  private String configValue;

  /**
   * 排序
   */
  private Integer configOrder;

  /**
   * 是否启用
   */
  private Boolean isEnabled;

  /**
   * 描述
   */
  private String remark;

  public SettingConfigKey getConfigKey() {
    return configKey;
  }

  public void setConfigKey(SettingConfigKey configKey) {
    this.configKey = configKey;
  }

  @JsonProperty
  public Boolean getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public Integer getConfigOrder() {
    return configOrder;
  }

  public void setConfigOrder(Integer configOrder) {
    this.configOrder = configOrder;
  }

  @JsonProperty
  @Column(length = 5000)
  public String getConfigValue() {
    return configValue;
  }

  public void setConfigValue(String configValue) {
    this.configValue = configValue;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  
}
