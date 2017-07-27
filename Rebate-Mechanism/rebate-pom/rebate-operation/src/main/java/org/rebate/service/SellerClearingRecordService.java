package org.rebate.service; 

import java.util.Date;
import java.util.List;

import org.rebate.beans.Message;
import org.rebate.entity.BankCard;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.service.BaseService;
import org.rebate.json.beans.SellerClearingResult;

public interface SellerClearingRecordService extends BaseService<SellerClearingRecord,Long>{
    /**
     * 商家货款结算
     */
	String sellerClearing(Date startDate, Date endDate);
	
	/**
	 * 货款单笔支付
	 * @return
	 */
	Message singlePay(SellerClearingRecord sellerClearingRecord,
			BankCard bankCard);
	
	List<ClearingOrderRelation> getRelationListByRecordId(Long recordId);

	List<SellerClearingResult> findClearingResult();
}