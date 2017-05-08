package org.rebate.dao; 
import org.rebate.entity.Agent;
import org.rebate.framework.dao.BaseDao;

public interface AgentDao extends  BaseDao<Agent,Long>{
  boolean areaExists(Long areaId);
}