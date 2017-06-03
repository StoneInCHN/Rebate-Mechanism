package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.ParamConfigDao;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("paramConfigDaoImpl")
public class ParamConfigDaoImpl extends BaseDaoImpl<ParamConfig, Long> implements ParamConfigDao {


  public ParamConfig getConfigByKey(ParamConfigKey key) {

    if (key == null) {
      return null;
    }
    try {
      String jpql =
          "select config from ParamConfig config where config.isEnabled = true and config.configKey = :key";
      return entityManager.createQuery(jpql, ParamConfig.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("key", key).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  @Override
  public ParamConfig getConfigByKeyIgnoreIsEnabled(ParamConfigKey key) {
    if (key == null) {
      return null;
    }
    try {
      String jpql =
          "select config from ParamConfig config where  config.configKey = :key";
      return entityManager.createQuery(jpql, ParamConfig.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("key", key).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
