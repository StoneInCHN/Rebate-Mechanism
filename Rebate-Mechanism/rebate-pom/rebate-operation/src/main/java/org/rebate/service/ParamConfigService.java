package org.rebate.service;

import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.service.BaseService;

public interface ParamConfigService extends BaseService<ParamConfig, Long> {
  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  ParamConfig getConfigByKey(ParamConfigKey key);
  
  ParamConfig getConfigByKeyIgnoreIsEnabled(ParamConfigKey key);
  
}
