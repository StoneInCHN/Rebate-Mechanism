package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.HolidayConfig;
import org.rebate.dao.HolidayConfigDao;
import org.rebate.service.HolidayConfigService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("holidayConfigServiceImpl")
public class HolidayConfigServiceImpl extends BaseServiceImpl<HolidayConfig,Long> implements HolidayConfigService {

      @Resource(name="holidayConfigDaoImpl")
      public void setBaseDao(HolidayConfigDao holidayConfigDao) {
         super.setBaseDao(holidayConfigDao);
  }
}