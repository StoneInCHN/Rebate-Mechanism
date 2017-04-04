package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.LeScoreRecord;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.service.LeScoreRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("leScoreRecordServiceImpl")
public class LeScoreRecordServiceImpl extends BaseServiceImpl<LeScoreRecord,Long> implements LeScoreRecordService {

      @Resource(name="leScoreRecordDaoImpl")
      public void setBaseDao(LeScoreRecordDao leScoreRecordDao) {
         super.setBaseDao(leScoreRecordDao);
  }
}