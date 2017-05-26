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
import org.rebate.service.SnService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
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
      
      @Resource(name="bankCardServiceImpl")
      private BankCardService bankCardService;
      
      @Resource(name="systemConfigServiceImpl") 
      private SystemConfigService systemConfigService; 
      
      @Resource(name="snServiceImpl") 
      private SnService snService;    
      
      @Resource(name="clearingOrderRelationServiceImpl")
      private ClearingOrderRelationService clearingOrderRelationService;  
    /**
     * 商家货款结算
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)  
	public void sellerClearing(Date startDate, Date endDate) {
    	
		Map<Seller, BigDecimal> sellerAmountMap = new HashMap<Seller, BigDecimal>();
		Map<Seller, Set<Order>> sellerOrdersMap = new HashMap<Seller, Set<Order>>();
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.ne("status", OrderStatus.UNPAID));
		filters.add(Filter.eq("isClearing", false));
        Date[] queryDates = {startDate, endDate};
        filters.add(Filter.between("paymentTime", queryDates));
		List<Order> orders = orderService.findList(null, filters, null);
		
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			Seller seller = order.getSeller();
			if (!sellerAmountMap.containsKey(seller)) {
				sellerAmountMap.put(seller, order.getAmount());
				Set<Order> orderSet = new HashSet<Order>();
				orderSet.add(order);
				sellerOrdersMap.put(seller, orderSet);
			}else {
				BigDecimal income = sellerAmountMap.get(seller);
				income = income.add(order.getAmount());
				sellerAmountMap.put(seller, income);
				
				Set<Order> orderSet = sellerOrdersMap.get(seller);
				orderSet.add(order);
			}
		}
		
		if (!sellerAmountMap.isEmpty()) {
			
			 List<SellerClearingRecord> records = new ArrayList<SellerClearingRecord>();
			 BigDecimal totalClearingAmount = new BigDecimal(0);//货款结算总金额
			 
			 for (Map.Entry<Seller, BigDecimal> entry : sellerAmountMap.entrySet()) {
				 Seller seller = entry.getKey();
				 BigDecimal totalOrderAmount = entry.getValue(); //订单总金额
				 EndUser endUser = seller.getEndUser();
				 
				 SellerClearingRecord record = new SellerClearingRecord();
				 record.setSeller(seller);
				 record.setEndUser(endUser);
				 record.setBankCardId(bankCardService.getDefaultCard(endUser.getId()).getId());
				 record.setAmount(endUser.getIncomeLeScore());
				 totalClearingAmount.add(endUser.getIncomeLeScore());
				 record.setTotalOrderAmount(totalOrderAmount);  
				 record.setHandlingCharge(getHandlingCharge(totalOrderAmount));
				 record.setClearingSn(snService.generate(Type.SELLER_CLEARING_RECORD));
				 record.setIsClearing(false);
				 save(record);//保存商家货款结算记录
				 
				 records.add(record);
				 
				 Set<Order> orderSet = sellerOrdersMap.get(seller);
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
						//tranxService.batchDaiFu(setting.getAllinpayUrl(), false, records.size()+"", totalClearingAmount.toString(), records);
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