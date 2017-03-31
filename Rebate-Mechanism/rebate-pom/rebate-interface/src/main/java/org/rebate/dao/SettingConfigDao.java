package org.rebate.dao;

import org.rebate.entity.SettingConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.framework.dao.BaseDao;

public interface SettingConfigDao extends BaseDao<SettingConfig, Long> {
  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  SettingConfig getConfigsByKey(SettingConfigKey key);
}
