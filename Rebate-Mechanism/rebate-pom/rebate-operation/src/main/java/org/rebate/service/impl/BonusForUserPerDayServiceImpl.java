package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.BonusForUserPerDay;
import org.rebate.dao.BonusForUserPerDayDao;
import org.rebate.service.BonusForUserPerDayService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("bonusForUserPerDayServiceImpl")
public class BonusForUserPerDayServiceImpl extends BaseServiceImpl<BonusForUserPerDay,Long> implements BonusForUserPerDayService {

      @Resource(name="bonusForUserPerDayDaoImpl")
      public void setBaseDao(BonusForUserPerDayDao bonusForUserPerDayDao) {
         super.setBaseDao(bonusForUserPerDayDao);
  }
}