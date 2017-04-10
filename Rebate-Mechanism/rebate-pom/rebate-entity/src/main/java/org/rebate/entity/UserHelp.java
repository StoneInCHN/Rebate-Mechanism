package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rebate.entity.base.BaseEntity;

/**
 * 用户帮助配置
 * 
 */
@Entity
@Table(name = "rm_user_help")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_user_help_sequence")
public class UserHelp extends BaseEntity {


  private static final long serialVersionUID = -1684057707764082356L;

  /**
   * 标题
   */
  private String title;
  /**
   * 内容
   */
  private String content;

  /**
   * 排序
   */
  private Integer configOrder;

  /**
   * 是否启用
   */
  private Boolean isEnabled;

  /**
   * 备注
   */
  private String remark;

  @Column(length = 100)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(length = 5000)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getConfigOrder() {
    return configOrder;
  }

  public void setConfigOrder(Integer configOrder) {
    this.configOrder = configOrder;
  }

  public Boolean getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  @Column(length = 200)
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


}
