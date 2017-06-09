package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.ParamConfig;
import org.rebate.dao.ParamConfigDao;
import org.rebate.service.ParamConfigService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("paramConfigServiceImpl")
public class ParamConfigServiceImpl extends BaseServiceImpl<ParamConfig,Long> implements ParamConfigService {

      @Resource(name="paramConfigDaoImpl")
      public void setBaseDao(ParamConfigDao paramConfigDao) {
         super.setBaseDao(paramConfigDao);
  }
}