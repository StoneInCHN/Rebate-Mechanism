package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.BonusParamPerDay;
import org.rebate.dao.BonusParamPerDayDao;
import org.rebate.service.BonusParamPerDayService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("bonusParamPerDayServiceImpl")
public class BonusParamPerDayServiceImpl extends BaseServiceImpl<BonusParamPerDay,Long> implements BonusParamPerDayService {

      @Resource(name="bonusParamPerDayDaoImpl")
      public void setBaseDao(BonusParamPerDayDao bonusParamPerDayDao) {
         super.setBaseDao(bonusParamPerDayDao);
  }
}