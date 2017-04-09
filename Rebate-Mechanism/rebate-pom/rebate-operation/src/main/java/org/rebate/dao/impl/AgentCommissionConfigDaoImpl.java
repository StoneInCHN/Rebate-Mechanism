package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.AgentCommissionConfig;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.AgentCommissionConfigDao;
@Repository("agentCommissionConfigDaoImpl")
public class AgentCommissionConfigDaoImpl extends  BaseDaoImpl<AgentCommissionConfig,Long> implements AgentCommissionConfigDao {

}