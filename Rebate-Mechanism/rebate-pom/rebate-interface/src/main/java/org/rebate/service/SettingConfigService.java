package org.rebate.service;

import org.rebate.entity.SettingConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.framework.service.BaseService;

public interface SettingConfigService extends BaseService<SettingConfig, Long> {


  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  SettingConfig getConfigsByKey(SettingConfigKey key);
}
