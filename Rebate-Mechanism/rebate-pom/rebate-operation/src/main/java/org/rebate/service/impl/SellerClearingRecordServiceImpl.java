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

import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.dao.SellerClearingRecordDao;
import org.rebate.entity.BankCard;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
		Map<Long, BigDecimal> sellerIncomeMap = new HashMap<Long, BigDecimal>();
		Map<Long, Set<Order>> sellerOrdersMap = new HashMap<Long, Set<Order>>();
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.ne("status", OrderStatus.UNPAID));
		filters.add(Filter.eq("isClearing", false));
        Date[] queryDates = {startDate, endDate};
        filters.add(Filter.between("paymentTime", queryDates));
		List<Order> orderList = orderService.findList(null, filters, null);
		List<Order> orders = new ArrayList<Order>();
		for (int i = 0; i < orderList.size(); i++) {
			Order order = orderList.get(i);
			long existCount = clearingOrderRelationService.count(Filter.eq("order", order));;
			if (existCount == 0) {//如果该订单结算记录已存在，则剔除该订单
				orders.add(order);
			}
		}
		
		//将orders以seller分组
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			Seller seller = order.getSeller();
			Long sellerId = seller.getId();
			if (!sellerAmountMap.containsKey(sellerId)) {
				sellerAmountMap.put(sellerId, order.getAmount());
				sellerIncomeMap.put(sellerId, order.getSellerIncome());
				Set<Order> orderSet = new HashSet<Order>();
				orderSet.add(order);
				sellerOrdersMap.put(sellerId, orderSet);
			}else {
				BigDecimal amount = sellerAmountMap.get(sellerId);
				amount = amount.add(order.getAmount());
				sellerAmountMap.put(sellerId, amount);
				
				BigDecimal income = sellerAmountMap.get(sellerId);
				income = income.add(order.getAmount());
				sellerIncomeMap.put(sellerId, income);
				
				Set<Order> orderSet = sellerOrdersMap.get(sellerId);
				orderSet.add(order);
			}
		}
		
		if (!sellerAmountMap.isEmpty() && !sellerIncomeMap.isEmpty() && !sellerOrdersMap.isEmpty()) {
			
			 List<SellerClearingRecord> records = new ArrayList<SellerClearingRecord>();
			 
			 BigDecimal totalClearingAmount = new BigDecimal(0);//货款结算总金额
			 BigDecimal totalHandlingCharge = new BigDecimal(0);//手续费总金额
			 
			 for (Map.Entry<Long, BigDecimal> entry : sellerAmountMap.entrySet()) {
				 
				 Seller seller = sellerService.find(entry.getKey());//根据sellerId获取Seller
				 BigDecimal totalOrderAmount = entry.getValue(); //当天订单总金额
				 BigDecimal totalSellerIncome = sellerIncomeMap.get(seller.getId()); //当天商家收入金额（折扣后的结算金额）
				 
				 EndUser endUser = seller.getEndUser();
				 
				 SellerClearingRecord record = new SellerClearingRecord();
				 record.setSeller(seller);
				 record.setEndUser(endUser);
				 
				 BankCard defaultCard = bankCardService.getDefaultCard(endUser);
				 if (defaultCard != null) {
					 record.setBankCardId(defaultCard.getId());
				 }else {
					LogUtil.debug(this.getClass(), "sellerClearing", "Cannot find default card for endUserId:", endUser != null? endUser.getId() : "");
					//是否需要短信或邮件通知商家用去配置默认银行卡？？
				 }
				 
				 record.setTotalOrderAmount(totalOrderAmount); //订单总金额
				 
				 record.setAmount(endUser.getIncomeLeScore());//结算金额
				 if (totalSellerIncome.compareTo(endUser.getIncomeLeScore()) != 0) {
					 LogUtil.debug(this.getClass(), "sellerClearing", "Total seller income is not equals endUser IncomeLeScore !!!");
					 record.setAmount(totalSellerIncome);
				 }
				 totalClearingAmount = totalClearingAmount.add(record.getAmount());//累加结算金额
				 
				 record.setHandlingCharge(getHandlingCharge(totalOrderAmount));//手续费
				 totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
				 
				 record.setClearingSn(snService.generate(Type.SELLER_CLEARING_RECORD));//结算货款单编号（用于显示）
				 record.setIsClearing(false);//未结算
				 
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
				    	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总共代付的金额
				    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0).toString();//金额单位：分
				    	String totalItem = records.size() + "";//单数量
				    	tranxService.init();//初始化通联基础数据
				    	//开始批量代付
				    	List<SellerClearingRecord> recordList = tranxService.batchDaiFu(false, totalItem,  totalPayStr, records, bankCardService);
						if (recordList == null) {
							/**
							 * 代付失败,回滚当前的事物
							 * 避免生成无用的货款结算记录（防止手动补跑job生成重复的结算记录）
							 * 手动补跑job调用 此货款结算（/console/job/manualClearingRecordJob.jhtml）
							 */
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						}else {
							save(recordList);//再次保存商家货款结算记录（已赋值了req_sn和sn）
						}
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.debug(this.getClass(), "clearingRecordJob", "Batch daifu failed, Catch exception: %s", e.getMessage());
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
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
	    //手续费，优先考虑 每笔提现固定手续费（通联固定每笔是1.5元？？），后考虑提现手续费占提现金额的百分比 
		BigDecimal handlingCharge = new BigDecimal(0);
		try {
			SystemConfig feePertime = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
			if (feePertime != null && feePertime.getConfigValue() != null) {
				handlingCharge = new BigDecimal(feePertime.getConfigValue());
			}else {
				SystemConfig feePercentage = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
				if (feePercentage != null && feePercentage.getConfigValue() != null) {
			    	handlingCharge = totalOrderAmount.multiply(new BigDecimal(feePercentage.getConfigValue()));
				}
			}
		} catch (Exception e) {
			LogUtil.debug(this.getClass(), "getHandlingCharge", "Catch exception: %s", e.getMessage());
			return handlingCharge;
		}
		return handlingCharge;
	}
    @Override
    public Message singlePay(Long id) {
      
      try {
        for (int i = 0; i < 5; i++) {
          Thread.sleep(1000);
          System.out.println(i);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      return Message.success("rebate.message.success");
    }
}