package org.rebate.service; 

import java.util.List;

import org.rebate.entity.Order;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.service.BaseService;

public interface SellerClearingRecordService extends BaseService<SellerClearingRecord,Long>{

	List<SellerClearingRecord> getListByReqSn(String reqSn);

	void updateClearingByReqSn(String reqSn);
	
	List<Order> getOrderListByRecordId(Long recordId);
}