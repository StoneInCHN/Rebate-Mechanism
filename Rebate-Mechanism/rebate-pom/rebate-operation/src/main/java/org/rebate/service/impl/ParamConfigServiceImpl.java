package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.ParamConfigDao;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.ParamConfigService;
import org.springframework.stereotype.Service;

@Service("paramConfigServiceImpl")
public class ParamConfigServiceImpl extends BaseServiceImpl<ParamConfig, Long> implements
    ParamConfigService {

  @Resource(name = "paramConfigDaoImpl")
  private ParamConfigDao paramConfigDao;

  @Resource(name = "paramConfigDaoImpl")
  public void setBaseDao(ParamConfigDao paramConfigDao) {
    super.setBaseDao(paramConfigDao);
  }

  @Override
  public ParamConfig getConfigByKey(ParamConfigKey key) {
    return paramConfigDao.getConfigByKey(key);
  }
  
  @Override
  public ParamConfig getConfigByKeyIgnoreIsEnabled(ParamConfigKey key) {
    return paramConfigDao.getConfigByKeyIgnoreIsEnabled(key);
  }

}
