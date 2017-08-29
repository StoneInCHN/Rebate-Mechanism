package org.rebate.service; 

import java.util.Date;
import java.util.List;

import org.rebate.beans.Message;
import org.rebate.entity.BankCard;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.framework.service.BaseService;
import org.rebate.json.beans.SellerClearingOrders;
import org.rebate.json.beans.SellerClearingResult;

public interface SellerClearingRecordService extends BaseService<SellerClearingRecord,Long>{
	/**
	 * 生成需要结算的商家货款记录
	 */
	List<SellerClearingOrders> getNeedClearingRecords(Date startDate, Date endDate, PaymentChannel channel);
    /**
     * 商家货款批量代付(通联渠道)
     */
	String sellerClearingByAllinPay(List<SellerClearingOrders> records);
	/**
	 * 货款单笔代付(通联渠道)
	 */
	Message singlePayByAllinpay(SellerClearingRecord sellerClearingRecord, BankCard bankCard);
    /**
     * 商家货款批量代付(九派渠道)
     */
	void sellerClearingByJiuPai(List<SellerClearingOrders> records);
	/**
	 * 货款单笔代付(九派渠道)
	 */
	Message singlePayByJiuPai(SellerClearingRecord sellerClearingRecord, BankCard bankCard);	

	
	List<ClearingOrderRelation> getRelationListByRecordId(Long recordId);

	List<SellerClearingResult> findClearingResult();
	/**
	 * 九派需要更新结算状态的交易批次号list
	 */
	List<String> jiuPaiProcessingBatchNoList(Date endDate);


}