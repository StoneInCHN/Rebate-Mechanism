package org.rebate.dao; 
import java.util.Date;
import java.util.List;

import org.rebate.entity.LeScoreRecord;
import org.rebate.framework.dao.BaseDao;

public interface LeScoreRecordDao extends  BaseDao<LeScoreRecord,Long>{
	/**
	 * 九派需要更新提现状态的交易批次号list
	 * @return
	 */
	List<String> jiuPaiProcessingBatchNoList(Date endDate);
}