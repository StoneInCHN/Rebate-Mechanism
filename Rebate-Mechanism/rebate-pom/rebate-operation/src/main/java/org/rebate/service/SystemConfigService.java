package org.rebate.service;

import org.rebate.beans.Message;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.BaseService;

public interface SystemConfigService extends BaseService<SystemConfig, Long> {
  /**
   * 根据key获取系统配置
   * 
   * @param key
   * @return
   */
  SystemConfig getConfigByKey(SystemConfigKey key);
  
  SystemConfig getConfigByKeyIgnoreIsEnabled(SystemConfigKey key);
  
  Message updateSystemConfig(SystemConfig systemConfig);
}
