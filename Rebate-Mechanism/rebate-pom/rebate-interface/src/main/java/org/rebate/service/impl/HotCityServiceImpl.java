package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.HotCity;
import org.rebate.dao.HotCityDao;
import org.rebate.service.HotCityService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("hotCityServiceImpl")
public class HotCityServiceImpl extends BaseServiceImpl<HotCity,Long> implements HotCityService {

      @Resource(name="hotCityDaoImpl")
      public void setBaseDao(HotCityDao hotCityDao) {
         super.setBaseDao(hotCityDao);
  }
}