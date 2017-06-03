package org.rebate.dao;

import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.framework.dao.BaseDao;

public interface AgentCommissionConfigDao extends BaseDao<AgentCommissionConfig, Long> {
  /**
   * 获取地区提成配置
   * 
   * @param area
   * @return
   */
  AgentCommissionConfig getConfigByArea(Area area);
}
