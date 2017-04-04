package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.LeBeanRecord;
import org.rebate.dao.LeBeanRecordDao;
import org.rebate.service.LeBeanRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("leBeanRecordServiceImpl")
public class LeBeanRecordServiceImpl extends BaseServiceImpl<LeBeanRecord,Long> implements LeBeanRecordService {

      @Resource(name="leBeanRecordDaoImpl")
      public void setBaseDao(LeBeanRecordDao leBeanRecordDao) {
         super.setBaseDao(leBeanRecordDao);
  }
}