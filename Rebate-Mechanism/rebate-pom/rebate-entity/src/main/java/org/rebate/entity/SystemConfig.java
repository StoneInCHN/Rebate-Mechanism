package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.entity.lucene.LowCaseBridgeImpl;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 数据字典配置
 * 
 * @author sujinxuan
 *
 */
@Entity
@Table(name = "rm_system_config", indexes = {@Index(name = "configKeyIndex",
    columnList = "configKey")})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_system_config_sequence")
public class SystemConfig extends BaseEntity {


  private static final long serialVersionUID = -1684057707764082356L;

  /**
   * 配置项值
   */
  private SystemConfigKey configKey;
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


  public SystemConfigKey getConfigKey() {
    return configKey;
  }

  public void setConfigKey(SystemConfigKey configKey) {
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
  @Column(length = 20)
  @Field(store = Store.NO, index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.NO)
  @FieldBridge(impl = LowCaseBridgeImpl.class)
  public String getConfigValue() {
    return configValue;
  }

  public void setConfigValue(String configValue) {
    this.configValue = configValue;
  }

}
