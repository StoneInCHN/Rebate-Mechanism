package org.rebate.dao; 
import java.util.Date;
import java.util.List;

import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.dao.BaseDao;
import org.rebate.json.beans.SellerClearingResult;

public interface SellerClearingRecordDao extends  BaseDao<SellerClearingRecord,Long>{

	List<SellerClearingResult> findClearingResult();
	/**
	 * 九派需要更新结算状态的交易批次号list
	 * @return
	 */
	List<String> jiuPaiProcessingBatchNoList(Date endDate);

}