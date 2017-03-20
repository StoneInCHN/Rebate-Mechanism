package org.rebate.framework.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.rebate.framework.entity.BaseEntity;


public class EntityListener {

  /**
   * 保存前处理
   * 
   * @param entity 基类
   */
  @PrePersist
  public void prePersist(BaseEntity entity) {
    entity.setCreateDate(new Date());
    entity.setModifyDate(new Date());
  }

  /**
   * 更新前处理
   * 
   * @param entity 基类
   */
  @PreUpdate
  public void preUpdate(BaseEntity entity) {
    entity.setModifyDate(new Date());
  }

}
