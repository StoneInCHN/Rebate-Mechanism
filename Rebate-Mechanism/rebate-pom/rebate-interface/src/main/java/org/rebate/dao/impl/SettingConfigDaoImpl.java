package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SettingConfigDao;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("settingConfigDaoImpl")
public class SettingConfigDaoImpl extends BaseDaoImpl<SettingConfig, Long> implements
    SettingConfigDao {

  @Override
  public SettingConfig getConfigsByKey(SettingConfigKey key) {
    if (key == null) {
      return null;
    }
    try {
      String jpql =
          "select config from SettingConfig config where config.isEnabled = true and config.configKey = :key";
      return entityManager.createQuery(jpql, SettingConfig.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("key", key).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
