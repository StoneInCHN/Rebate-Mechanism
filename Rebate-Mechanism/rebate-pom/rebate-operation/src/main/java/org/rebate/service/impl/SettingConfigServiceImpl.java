package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SettingConfig;
import org.rebate.dao.SettingConfigDao;
import org.rebate.service.SettingConfigService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("settingConfigServiceImpl")
public class SettingConfigServiceImpl extends BaseServiceImpl<SettingConfig,Long> implements SettingConfigService {

      @Resource(name="settingConfigDaoImpl")
      public void setBaseDao(SettingConfigDao settingConfigDao) {
         super.setBaseDao(settingConfigDao);
  }
}