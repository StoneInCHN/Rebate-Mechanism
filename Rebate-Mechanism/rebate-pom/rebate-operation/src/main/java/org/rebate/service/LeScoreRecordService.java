package org.rebate.service; 

import org.rebate.beans.Message;
import org.rebate.entity.BankCard;
import org.rebate.entity.LeScoreRecord;
import org.rebate.framework.service.BaseService;

public interface LeScoreRecordService extends BaseService<LeScoreRecord,Long>{

  Message auditWithdraw(LeScoreRecord leScoreRecord);
  
  /**
   * 通联批量提现（代付）
   * @param ids
   * @return
   */
  String batchWithdrawal(Long[] ids);
  /**
   * 单笔实时提现
   * @param record
   * @param bankCard
   * @return
   */
  Message singlePay(LeScoreRecord record, BankCard bankCard);
  
  
}