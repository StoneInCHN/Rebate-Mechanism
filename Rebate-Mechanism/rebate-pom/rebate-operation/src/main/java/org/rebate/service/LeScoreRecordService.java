package org.rebate.service; 

import org.rebate.beans.Message;
import org.rebate.entity.LeScoreRecord;
import org.rebate.framework.service.BaseService;

public interface LeScoreRecordService extends BaseService<LeScoreRecord,Long>{

  Message auditWithdraw(LeScoreRecord leScoreRecord);
  
}