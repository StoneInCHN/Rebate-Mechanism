package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.MindExchangeByDay;
import org.rebate.dao.MindExchangeByDayDao;
import org.rebate.service.MindExchangeByDayService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("mindExchangeByDayServiceImpl")
public class MindExchangeByDayServiceImpl extends BaseServiceImpl<MindExchangeByDay,Long> implements MindExchangeByDayService {

      @Resource(name="mindExchangeByDayDaoImpl")
      public void setBaseDao(MindExchangeByDayDao mindExchangeByDayDao) {
         super.setBaseDao(mindExchangeByDayDao);
  }
}