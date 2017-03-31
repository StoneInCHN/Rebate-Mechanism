package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SettingConfigDao;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SettingConfigService;
import org.springframework.stereotype.Service;

@Service("settingConfigServiceImpl")
public class SettingConfigServiceImpl extends BaseServiceImpl<SettingConfig, Long> implements
    SettingConfigService {

  @Resource(name = "settingConfigDaoImpl")
  private SettingConfigDao settingConfigDao;

  @Resource(name = "settingConfigDaoImpl")
  public void setBaseDao(SettingConfigDao settingConfigDao) {
    super.setBaseDao(settingConfigDao);
  }

  @Override
  public SettingConfig getConfigsByKey(SettingConfigKey key) {
    return settingConfigDao.getConfigsByKey(key);
  }
}
