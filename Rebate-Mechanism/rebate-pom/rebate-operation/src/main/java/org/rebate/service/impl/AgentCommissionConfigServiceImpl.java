package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.AgentCommissionConfigDao;
import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.AgentCommissionConfigService;
import org.springframework.stereotype.Service;

@Service("agentCommissionConfigServiceImpl")
public class AgentCommissionConfigServiceImpl extends BaseServiceImpl<AgentCommissionConfig, Long>
    implements AgentCommissionConfigService {

  @Resource(name = "agentCommissionConfigDaoImpl")
  private AgentCommissionConfigDao agentCommissionConfigDao;

  @Resource(name = "agentCommissionConfigDaoImpl")
  public void setBaseDao(AgentCommissionConfigDao agentCommissionConfigDao) {
    super.setBaseDao(agentCommissionConfigDao);
  }

  @Override
  public AgentCommissionConfig getConfigByArea(Area area) {
    return agentCommissionConfigDao.getConfigByArea(area);
  }
}
