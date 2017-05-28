package org.rebate.service.impl; 

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.rebate.beans.Setting;
import org.rebate.dao.SellerClearingRecordDao;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.Sn.Type;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.BankCardService;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.SellerService;
import org.rebate.service.SnService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("sellerClearingRecordServiceImpl")
public class SellerClearingRecordServiceImpl extends BaseServiceImpl<SellerClearingRecord,Long> implements SellerClearingRecordService {

      @Resource(name="sellerClearingRecordDaoImpl")
      public void setBaseDao(SellerClearingRecordDao sellerClearingRecordDao) {
         super.setBaseDao(sellerClearingRecordDao);
      }
      
      @Resource(name="orderServiceImpl")
      private OrderService orderService;
      
      @Resource(name="sellerServiceImpl")
      private SellerService sellerService;
      
      @Resource(name="bankCardServiceImpl")
      private BankCardService bankCardService;
      
      @Resource(name="systemConfigServiceImpl") 
      private SystemConfigService systemConfigService; 
      
      @Resource(name="snServiceImpl") 
      private SnService snService;    
      
      @Resource(name="clearingOrderRelationServiceImpl")
      private ClearingOrderRelationService clearingOrderRelationService;  
      
      @Autowired
      private SellerClearingRecordDao sellerClearingRecordDao;
    /**
     * 商家货款结算
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)  
	public void sellerClearing(Date startDate, Date endDate) {
    	
		Map<Long, BigDecimal> sellerAmountMap = new HashMap<Long, BigDecimal>();
		Map<Long, Set<Order>> sellerOrdersMap = new HashMap<Long, Set<Order>>();
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.ne("status", OrderStatus.UNPAID));
		filters.add(Filter.eq("isClearing", false));
        Date[] queryDates = {startDate, endDate};
        filters.add(Filter.between("paymentTime", queryDates));
		List<Order> orders = orderService.findList(null, filters, null);
		
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			Seller seller = order.getSeller();
			Long sellerId = seller.getId();
			if (!sellerAmountMap.containsKey(sellerId)) {
				sellerAmountMap.put(sellerId, order.getAmount());
				Set<Order> orderSet = new HashSet<Order>();
				orderSet.add(order);
				sellerOrdersMap.put(sellerId, orderSet);
			}else {
				BigDecimal income = sellerAmountMap.get(sellerId);
				income = income.add(order.getAmount());
				sellerAmountMap.put(sellerId, income);
				
				Set<Order> orderSet = sellerOrdersMap.get(sellerId);
				orderSet.add(order);
			}
		}
		
		if (!sellerAmountMap.isEmpty()) {
			
			 List<SellerClearingRecord> records = new ArrayList<SellerClearingRecord>();
			 BigDecimal totalClearingAmount = new BigDecimal(0);//货款结算总金额
			 
			 for (Map.Entry<Long, BigDecimal> entry : sellerAmountMap.entrySet()) {
				 Seller seller = sellerService.find(entry.getKey());
				 BigDecimal totalOrderAmount = entry.getValue(); //订单总金额
				 EndUser endUser = seller.getEndUser();
				 
				 SellerClearingRecord record = new SellerClearingRecord();
				 record.setSeller(seller);
				 record.setEndUser(endUser);
				 record.setBankCardId(bankCardService.getDefaultCard(endUser.getId()).getId());
				 record.setAmount(endUser.getIncomeLeScore());
				 totalClearingAmount = totalClearingAmount.add(endUser.getIncomeLeScore());
				 record.setTotalOrderAmount(totalOrderAmount);  
				 record.setHandlingCharge(getHandlingCharge(totalOrderAmount));
				 record.setClearingSn(snService.generate(Type.SELLER_CLEARING_RECORD));
				 record.setIsClearing(false);
				 save(record);//保存商家货款结算记录
				 
				 records.add(record);
				 
				 Set<Order> orderSet = sellerOrdersMap.get(seller.getId());
				 for (Order order : orderSet) {
					ClearingOrderRelation relation = new ClearingOrderRelation();
					relation.setClearingRecId(record.getId());
					relation.setOrder(order);
					clearingOrderRelationService.save(relation);//保存商家货款结算记录与订单的关系
				}
			 }
			 if (records.size() > 0) {
				    TranxServiceImpl tranxService = new TranxServiceImpl();
				    try {
				    	Setting setting = SettingUtils.get();
				    	String totalAmount = totalClearingAmount.multiply(new BigDecimal(100)).setScale(0).toString();
						tranxService.batchDaiFu(setting.getAllinpayUrl(), false, records.size()+"", 
						    totalAmount, records, bankCardService, sellerClearingRecordDao);
					} catch (Exception e) {
						LogUtil.debug(this.getClass(), "clearingRecordJob", "Batch daifu failed, Catch exception: %s", e.getMessage());
						e.printStackTrace();
					}
			}
		}
	}
    /**
     * 获取货款结算手续费
     * @param totalOrderAmount
     * @return
     */
	private BigDecimal getHandlingCharge(BigDecimal totalOrderAmount) {
		BigDecimal handlingCharge = null;
		try {
		    SystemConfig feePercentage = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
		    if (feePercentage != null && feePercentage.getConfigValue() != null) {
		    	handlingCharge = totalOrderAmount.multiply(new BigDecimal(feePercentage.getConfigValue()));
			}else {
				SystemConfig feePertime = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
				if (feePercentage != null && feePertime.getConfigValue() != null) {
					handlingCharge = new BigDecimal(feePertime.getConfigValue());
				}
			}
		} catch (Exception e) {
			LogUtil.debug(this.getClass(), "getHandlingCharge", "Catch exception: %s", e.getMessage());
			return null;
		}
		return handlingCharge;
	}
}