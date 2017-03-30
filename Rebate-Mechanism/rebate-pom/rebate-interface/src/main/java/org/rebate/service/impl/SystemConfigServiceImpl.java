package org.rebate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Service;

@Service("systemConfigServiceImpl")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements
    SystemConfigService {

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "systemConfigDaoImpl")
  public void setBaseDao(SystemConfigDao systemConfigDao) {
    super.setBaseDao(systemConfigDao);
  }

  @Override
  public SystemConfig getConfigByKey(SystemConfigKey key) {
    return systemConfigDao.getConfigByKey(key);
  }

  @Override
  public List<SystemConfig> getConfigsByKey(SystemConfigKey key) {
    return systemConfigDao.getConfigsByKey(key);
  }
}
