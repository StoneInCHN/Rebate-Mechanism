package org.rebate.dao; 
import java.util.List;

import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.dao.BaseDao;
import org.rebate.json.beans.SellerClearingResult;

public interface SellerClearingRecordDao extends  BaseDao<SellerClearingRecord,Long>{

	List<SellerClearingResult> findClearingResult();

}