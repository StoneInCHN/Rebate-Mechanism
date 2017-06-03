package org.rebate.dao;

import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.dao.BaseDao;

public interface ParamConfigDao extends BaseDao<ParamConfig, Long> {
  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  ParamConfig getConfigByKey(ParamConfigKey key);
  
  /**
   * 根据key获取系统配置忽略IsEnabled
   * 
   * @param key
   * @return
   */
  ParamConfig getConfigByKeyIgnoreIsEnabled(ParamConfigKey key);
}
