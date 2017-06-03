package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.AgentCommissionConfigDao;
import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("agentCommissionConfigDaoImpl")
public class AgentCommissionConfigDaoImpl extends BaseDaoImpl<AgentCommissionConfig, Long>
    implements AgentCommissionConfigDao {

  @Override
  public AgentCommissionConfig getConfigByArea(Area area) {
    if (area == null) {
      return null;
    }
    try {
      String jpql = "select config from AgentCommissionConfig config where config.area = :area";
      return entityManager.createQuery(jpql, AgentCommissionConfig.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("area", area).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
