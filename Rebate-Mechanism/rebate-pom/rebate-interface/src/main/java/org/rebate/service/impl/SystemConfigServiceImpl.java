package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SystemConfig;
import org.rebate.dao.SystemConfigDao;
import org.rebate.service.SystemConfigService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("systemConfigServiceImpl")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig,Long> implements SystemConfigService {

      @Resource(name="systemConfigDaoImpl")
      public void setBaseDao(SystemConfigDao systemConfigDao) {
         super.setBaseDao(systemConfigDao);
  }
}