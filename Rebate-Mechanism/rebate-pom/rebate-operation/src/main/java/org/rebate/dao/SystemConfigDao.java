package org.rebate.dao;

import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.dao.BaseDao;

public interface SystemConfigDao extends BaseDao<SystemConfig, Long> {
  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  SystemConfig getConfigByKey(SystemConfigKey key);
  
  /**
   * 根据key获取系统配置忽略IsEnabled
   * 
   * @param key
   * @return
   */
  SystemConfig getConfigByKeyIgnoreIsEnabled(SystemConfigKey key);
}
