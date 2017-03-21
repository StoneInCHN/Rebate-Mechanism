package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Area;
import org.rebate.dao.AreaDao;
import org.rebate.service.AreaService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseServiceImpl<Area,Long> implements AreaService {

      @Resource(name="areaDaoImpl")
      public void setBaseDao(AreaDao areaDao) {
         super.setBaseDao(areaDao);
  }
}