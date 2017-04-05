package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.BonusByMindPerDay;
import org.rebate.dao.BonusByMindPerDayDao;
import org.rebate.service.BonusByMindPerDayService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("bonusByMindPerDayServiceImpl")
public class BonusByMindPerDayServiceImpl extends BaseServiceImpl<BonusByMindPerDay,Long> implements BonusByMindPerDayService {

      @Resource(name="bonusByMindPerDayDaoImpl")
      public void setBaseDao(BonusByMindPerDayDao bonusByMindPerDayDao) {
         super.setBaseDao(bonusByMindPerDayDao);
  }
}