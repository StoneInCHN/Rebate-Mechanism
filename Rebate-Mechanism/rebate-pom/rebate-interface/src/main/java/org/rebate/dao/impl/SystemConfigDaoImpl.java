package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("systemConfigDaoImpl")
public class SystemConfigDaoImpl extends BaseDaoImpl<SystemConfig, Long> implements SystemConfigDao {


  @Transactional
  public SystemConfig getConfigByKey(SystemConfigKey key) {

    if (key == null) {
      return null;
    }
    try {
      String jpql =
          "select config from SystemConfig config where config.isEnabled = true and config.configKey = :key";
      return entityManager.createQuery(jpql, SystemConfig.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("key", key).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

}
