package org.rebate.dao.impl; 

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository; 
import org.rebate.entity.Agent;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.AgentDao;
@Repository("agentDaoImpl")
public class AgentDaoImpl extends  BaseDaoImpl<Agent,Long> implements AgentDao {

  @Override
  public boolean areaExists(Long areaId) {
    if (areaId == null) {
      return false;
    }
    String jpql = "select count(*) from Agent agent where agent.area = :areaId";
    Long count =
        entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
            .setParameter("areaId", areaId).getSingleResult();
    return count > 0;
  }

}