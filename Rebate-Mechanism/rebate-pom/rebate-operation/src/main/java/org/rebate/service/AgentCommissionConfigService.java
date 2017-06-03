package org.rebate.service;

import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.framework.service.BaseService;

public interface AgentCommissionConfigService extends BaseService<AgentCommissionConfig, Long> {
  /**
   * 获取地区提成配置
   * 
   * @param area
   * @return
   */
  AgentCommissionConfig getConfigByArea(Area area);
}
