package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.AgentCommissionConfig;
import org.rebate.dao.AgentCommissionConfigDao;
import org.rebate.service.AgentCommissionConfigService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("agentCommissionConfigServiceImpl")
public class AgentCommissionConfigServiceImpl extends BaseServiceImpl<AgentCommissionConfig,Long> implements AgentCommissionConfigService {

      @Resource(name="agentCommissionConfigDaoImpl")
      public void setBaseDao(AgentCommissionConfigDao agentCommissionConfigDao) {
         super.setBaseDao(agentCommissionConfigDao);
  }
}