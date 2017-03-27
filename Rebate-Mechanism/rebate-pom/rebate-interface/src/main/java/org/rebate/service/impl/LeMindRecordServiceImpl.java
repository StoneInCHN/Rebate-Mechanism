package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.LeMindRecord;
import org.rebate.dao.LeMindRecordDao;
import org.rebate.service.LeMindRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("leMindRecordServiceImpl")
public class LeMindRecordServiceImpl extends BaseServiceImpl<LeMindRecord,Long> implements LeMindRecordService {

      @Resource(name="leMindRecordDaoImpl")
      public void setBaseDao(LeMindRecordDao leMindRecordDao) {
         super.setBaseDao(leMindRecordDao);
  }
}