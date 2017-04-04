package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Agent;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.AgentDao;
@Repository("agentDaoImpl")
public class AgentDaoImpl extends  BaseDaoImpl<Agent,Long> implements AgentDao {

}