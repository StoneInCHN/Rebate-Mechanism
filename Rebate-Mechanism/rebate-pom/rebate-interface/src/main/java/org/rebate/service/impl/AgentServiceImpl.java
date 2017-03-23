package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Agent;
import org.rebate.dao.AgentDao;
import org.rebate.service.AgentService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("agentServiceImpl")
public class AgentServiceImpl extends BaseServiceImpl<Agent,Long> implements AgentService {

      @Resource(name="agentDaoImpl")
      public void setBaseDao(AgentDao agentDao) {
         super.setBaseDao(agentDao);
  }
}