package org.rebate.service.impl; 

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.dao.SellerClearingRecordDao;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.EndUserService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerClearingRecordServiceImpl")
public class SellerClearingRecordServiceImpl extends BaseServiceImpl<SellerClearingRecord,Long> implements SellerClearingRecordService {

      @Resource(name="sellerClearingRecordDaoImpl")
      public void setBaseDao(SellerClearingRecordDao sellerClearingRecordDao) {
         super.setBaseDao(sellerClearingRecordDao);
      }
      
      @Resource(name="clearingOrderRelationServiceImpl")
      private ClearingOrderRelationService clearingOrderRelationService;
      
      @Resource(name="endUserServiceImpl")
      private EndUserService endUserService;
      
      @Resource(name="orderServiceImpl")
      private OrderService orderService;
      

	@Override
	public List<SellerClearingRecord> getListByReqSn(String reqSn) {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("reqSn", reqSn));
		filters.add(Filter.eq("isClearing", false));
		List<SellerClearingRecord> records = findList(null, filters, null);
		return records;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateClearingByReqSn(String reqSn) {
    	List<SellerClearingRecord> records = getListByReqSn(reqSn);
    	if (!CollectionUtils.isEmpty(records)) {
        	for (SellerClearingRecord record : records) {
        		EndUser endUser = record.getEndUser();
        		endUser.setIncomeLeScore(new BigDecimal(0));
        		endUserService.update(endUser);
        		record.setIsClearing(true);
        		record.setRemark("success");
        		update(record);
        		List<Order> orders = getOrderListByRecordId(record.getId());
        		for (Order order : orders) {
        			order.setIsClearing(true);
    			}
        		orderService.update(orders);
    		}
		}
	}
	
	@Override
	public List<Order> getOrderListByRecordId(Long recordId) {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("clearingRecId", recordId));
		List<ClearingOrderRelation> relations = clearingOrderRelationService.findList(null, filters, null);
		List<Order> orders = new ArrayList<Order>();
		for (ClearingOrderRelation relation : relations) {
			orders.add(relation.getOrder());
		}
		return orders;
	}
}