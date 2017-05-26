package org.rebate.service; 

import java.util.Date;

import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.service.BaseService;

public interface SellerClearingRecordService extends BaseService<SellerClearingRecord,Long>{
    /**
     * 商家货款结算
     */
	void sellerClearing(Date startDate, Date endDate);
}