package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public SystemConfig update(SystemConfig entity) {
    if(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE.equals(entity.getConfigKey()) &&  entity.getIsEnabled()){
      SystemConfig config = getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
      if(config!=null&&config.getIsEnabled()){
        config.setIsEnabled(false);
        super.update(config);
      }
    }else if (SystemConfigKey.TRANSACTION_FEE_PERTIME.equals(entity.getConfigKey()) && entity.getIsEnabled()) {
      SystemConfig config = getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
      if(config!=null&&config.getIsEnabled()){
        config.setIsEnabled(false);
        super.update(config);
      }
    }
    return super.update(entity);
  }
  
}
