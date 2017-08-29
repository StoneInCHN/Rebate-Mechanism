package org.rebate.service; 

import java.util.Date;
import java.util.List;

import org.rebate.beans.Message;
import org.rebate.entity.LeScoreRecord;
import org.rebate.framework.service.BaseService;

public interface LeScoreRecordService extends BaseService<LeScoreRecord,Long>{
  /**
   * 提现记录审核
   * @param leScoreRecord
   * @return
   */
  Message auditWithdraw(LeScoreRecord leScoreRecord);
  /**
   * (通联渠道)批量提现
   * @param ids
   * @return
   */
  String batchWithdrawalByAllinpay(Long[] ids);
  /**
   * (九派渠道)批量提现
   * @param ids
   * @return
   */
  void batchWithdrawalByJiuPai(Long[] ids);

	/**
	 * 九派需要更新提现状态的交易批次号list
	 * @return
	 */
  List<String> jiuPaiProcessingBatchNoList(Date endDate);
  
//  /**
//   * 单笔实时提现
//   * @param record
//   * @param bankCard
//   * @return
//   */
//  Message singlePay(LeScoreRecord record, BankCard bankCard);
  
  
}