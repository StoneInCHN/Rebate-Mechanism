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
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
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
import org.rebate.utils.SpringUtils;
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
       * return req_sn
       */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)  
  	public String sellerClearing(Date startDate, Date endDate) {
      	
  		Map<Long, BigDecimal> sellerAmountMap = new HashMap<Long, BigDecimal>();
  		Map<Long, BigDecimal> sellerIncomeMap = new HashMap<Long, BigDecimal>();
  		Map<Long, Set<Order>> sellerOrdersMap = new HashMap<Long, Set<Order>>();
  		
  		List<Filter> filters = new ArrayList<Filter>();
  		filters.add(Filter.ne("status", OrderStatus.UNPAID));//订单不等于未支付，即已支付 或者 评价后
  		filters.add(Filter.eq("isClearing", false));//订单未结算
  		filters.add(Filter.eq("isSallerOrder", false));//不是录单订单
        Date[] queryDates = {startDate, endDate};
        filters.add(Filter.between("paymentTime", queryDates));//订单支付时间在开始查询和结束查询之间
  		List<Order> orderList = orderService.findList(null, filters, null);
  		List<Order> orders = new ArrayList<Order>();
  		for (int i = 0; i < orderList.size(); i++) {
  			Order order = orderList.get(i);
  			long existCount = clearingOrderRelationService.count(Filter.eq("order", order));;
  			if (existCount == 0) {//如果该订单结算记录已存在，则剔除该订单
  				orders.add(order);
  			}
  		}
  		LogUtil.debug(this.getClass(), "sellerClearing", "orderList size: %s, orders size: %s", orderList.size(), orders.size());
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
  				
  				BigDecimal income = sellerIncomeMap.get(sellerId);
  				income = income.add(order.getSellerIncome());
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
  				 BigDecimal totalOrderAmount = entry.getValue(); //当天商家订单总金额
  				 BigDecimal totalSellerIncome = sellerIncomeMap.get(seller.getId()); //当天商家收入金额（折扣后的结算金额）
  				 
  				 EndUser endUser = seller.getEndUser();
  				 
  				 SellerClearingRecord record = new SellerClearingRecord();
  				 record.setSeller(seller);
  				 record.setEndUser(endUser);
  				 
  				 record.setTotalOrderAmount(totalOrderAmount); //订单总金额
  				 
//  				 record.setAmount(endUser.getIncomeLeScore());//结算金额
//                 if (totalSellerIncome.compareTo(endUser.getIncomeLeScore()) != 0) {
//                   LogUtil.debug(this.getClass(), "sellerClearing", "Total seller income is not equals endUser IncomeLeScore !!!");
//                   record.setAmount(totalSellerIncome);
//                 }
                 record.setAmount(totalSellerIncome);//结算金额
                 
                 BankCard defaultCard = bankCardService.getDefaultCard(endUser);
                 if (defaultCard != null) {
                      record.setBankCardId(defaultCard.getId());
                 }else {
                     LogUtil.debug(this.getClass(), "sellerClearing", "Cannot find default card for endUserId:", endUser != null? endUser.getId() : "");
                     //是否需要短信或邮件通知商家用户去配置默认银行卡？？
                     record.setClearingStatus(ClearingStatus.FAILED);
                     record.setIsClearing(false);
                     record.setValid(true);//标记货款记录是否有效
                     record.setRemark("商家未添加默认银行卡,待添加银行卡后执行单笔货款提现!");
                     save(record);
                     Set<Order> orderSet = sellerOrdersMap.get(record.getSeller().getId());
                     for (Order order : orderSet) {
                       ClearingOrderRelation relation = new ClearingOrderRelation();
                       relation.setClearingRecId(record.getId());
                       relation.setOrder(order);
                       clearingOrderRelationService.save(relation);//保存商家货款结算记录与订单的关系
                     }
                     LogUtil.debug(this.getClass(), "sellerClearing", "商家未添加默认银行卡,待添加银行卡后执行单笔货款提现!");
                     continue;//绕开银行卡为空的商家货款结算
                 }

  				 BigDecimal handingCharge = getAllinpayHandlingCharge(totalOrderAmount);
  				 if (record.getAmount() != null && handingCharge != null) {
  					 //因为手续费要在结算金额里面扣除，所以结算金额应该至少多余手续费一分钱
  					 //否者放弃代付此单，同时将其设置为处理失败，标明备注：结算金额不够支付手续费！方便后台手动处理
  					 BigDecimal payAmount = record.getAmount().subtract(handingCharge);
  					 if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
  						 record.setClearingStatus(ClearingStatus.FAILED);
  						 record.setIsClearing(false);
  						 record.setRemark(SpringUtils.getMessage("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge"));
  						 save(record);
  						 Set<Order> orderSet = sellerOrdersMap.get(record.getSeller().getId());
						 for (Order order : orderSet) {
							ClearingOrderRelation relation = new ClearingOrderRelation();
							relation.setClearingRecId(record.getId());
							relation.setOrder(order);
							clearingOrderRelationService.save(relation);//保存商家货款结算记录与订单的关系
						 }
						 LogUtil.debug(this.getClass(), "sellerClearing", "Income Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handingCharge);
  						 continue;
  					 }
  					 
  				 }
  				 totalClearingAmount = totalClearingAmount.add(record.getAmount());//累加结算金额
  				 record.setHandlingCharge(handingCharge);//手续费
  				 totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
  				 
  				 record.setClearingSn(snService.generate(Type.SELLER_CLEARING_RECORD));//结算货款单编号（用于显示）
  				 record.setClearingStatus(ClearingStatus.PROCESSING);
  				 record.setIsClearing(false);
  				 record.setValid(true);//标记货款记录是否有效
  				 
  				 records.add(record);
  			 }
  			 
  			 if (records.size() > 0) {
  				    TranxServiceImpl tranxService = new TranxServiceImpl();
  				    try {
  				    	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总共代付的金额
  				    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
  				    	String totalItem = records.size() + "";//单数量
  				    	tranxService.init();//初始化通联基础数据
  				    	//开始批量代付
  				    	List<SellerClearingRecord> recordList = tranxService.batchDaiFu(false, totalItem,  totalPayStr, records, bankCardService);
//  						if (recordList == null) {
//  							/**
//  							 * 代付失败,回滚当前的事物
//  							 * 避免生成无用的货款结算记录（防止手动补跑job生成重复的结算记录）
//  							 * 手动补跑job调用 此货款结算（/console/job/manualClearingRecordJob.jhtml）
//  							 */
//  							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//  						}else {
//  							update(recordList);//更新商家货款结算记录（已赋值了req_sn和sn）
//  						}
  				    	if (recordList != null && recordList.size() > 0) {
  					    	save(recordList);//保存已经受理的货款结算记录（已赋值了req_sn和sn）
  					    	for (int i = 0; i < recordList.size(); i++) {
  					    		SellerClearingRecord record = recordList.get(i);
  								 Set<Order> orderSet = sellerOrdersMap.get(record.getSeller().getId());
  								 for (Order order : orderSet) {
  									ClearingOrderRelation relation = new ClearingOrderRelation();
  									relation.setClearingRecId(record.getId());
  									relation.setOrder(order);
  									clearingOrderRelationService.save(relation);//保存商家货款结算记录与订单的关系
  								}
  							}
  					    	return recordList.get(0).getReqSn();
  				    	}
  					} catch (Exception e) {
  						e.printStackTrace();
  						LogUtil.debug(this.getClass(), "clearingRecordJob", "Batch daifu failed, Catch exception: %s", e.getMessage());
  						//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
  					}
  			}
  		}
  		return null;
  	}
  	/**
  	 * 单笔实时付款（代付）
  	 */
  	@Override
  	public Message singlePay(SellerClearingRecord record, BankCard bankCard) {
  	      try {
  			    TranxServiceImpl tranxService = new TranxServiceImpl();
  			    tranxService.init();//初始化通联基础数据
  			    BigDecimal handlingCharge = getAllinpayHandlingCharge(record.getAmount());//手续费
  			    record.setHandlingCharge(handlingCharge);
  			    BigDecimal payAmount = record.getAmount().abs().subtract(handlingCharge);
  			    //因为手续费要在结算金额里面扣除，所以结算金额应该至少多余手续费一分钱
  			    //否者放弃代付此单，同时将其设置为处理失败，标明备注：结算金额不够支付手续费！方便后台手动处理
  			    if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
//  			    	record.setClearingStatus(ClearingStatus.FAILED);
//  			    	record.setIsClearing(false);
  			    	record.setRemark(SpringUtils.getMessage("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge"));
  			    	update(record);
  			    	LogUtil.debug(this.getClass(), "singlePay", "Income Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handlingCharge);
  			    	return Message.success("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge");
  			    }
    			BigDecimal amountPenny = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);		 
  			    Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getOwnerName(), bankCard.getCardNum(), amountPenny.toString());
  			    if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
  			    	String status = resultMap.get("status");
  			    	if ("success".equals(status) || "error".equals(status)){
  	  			    	String req_sn = resultMap.get("req_sn");
  	  			    	String err_msg = resultMap.get("err_msg");
  	  			    	singlePayHandle(record, status, req_sn, err_msg, handlingCharge);
  			    	}
  				}
  	        } catch (Exception e) {
  	          e.printStackTrace();
  	          return Message.error("rebate.common.system.error");
  	        }
  	        return Message.success("rebate.message.success");
  	}
  	/**
  	 * 单笔结算成功，将以前的记录作废，新建一个新的记录
  	 */
  	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  	private void singlePayHandle(SellerClearingRecord oldRecord, String status, String req_sn, String err_msg, BigDecimal handlingCharge){
  		SellerClearingRecord newRecord = new SellerClearingRecord();
  		newRecord.setAmount(oldRecord.getAmount());
  		newRecord.setBankCardId(oldRecord.getBankCardId());
  		String clearingSn = snService.generate(Type.SELLER_CLEARING_RECORD);
  		newRecord.setClearingSn(clearingSn);//结算货款单编号（用于显示）
  		newRecord.setEndUser(oldRecord.getEndUser());
  		newRecord.setSeller(oldRecord.getSeller());
  		newRecord.setTotalOrderAmount(oldRecord.getTotalOrderAmount());
  		newRecord.setHandlingCharge(handlingCharge);
  		newRecord.setValid(true);//标记新的货款记录有效
  		newRecord.setReqSn(req_sn);
  		newRecord.setSn(null);//单笔结算不像批量结算，没有sn号
  		newRecord.setRemark(err_msg);
	    if ("success".equals(status)) {
	  		newRecord.setClearingStatus(ClearingStatus.SUCCESS);
	  		newRecord.setIsClearing(true);
		}else if ("error".equals(status)) {
	  		newRecord.setClearingStatus(ClearingStatus.FAILED);
	  		newRecord.setIsClearing(false);
			//update(oldRecord);
		}
	    save(newRecord);//保存新的货款记录
  		oldRecord.setValid(false);//标记旧的货款记录无效
  		oldRecord.setRemark(oldRecord.getRemark() + " 该结算记录已作废,请参考新的货款单号:"+clearingSn);
  		update(oldRecord);//更新旧的货款记录
  		//把旧的货款记录和订单关系解除，建立新的货款记录和订单关系
  		List<ClearingOrderRelation> relations = getRelationListByRecordId(oldRecord.getId());
		for (ClearingOrderRelation relation : relations) {
			relation.setClearingRecId(newRecord.getId());
			clearingOrderRelationService.update(relation);//保存商家货款结算记录与订单的关系
			if ("success".equals(status)) {
				Order order = relation.getOrder();
				order.setIsClearing(true);
				orderService.update(order);
			}
		}

  	}
    /**
     * 获取通联货款结算手续费
     * @param totalOrderAmount
     * @return
     */
	private BigDecimal getAllinpayHandlingCharge(BigDecimal totalOrderAmount) {
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
	public List<ClearingOrderRelation> getRelationListByRecordId(Long recordId) {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("clearingRecId", recordId));
		List<ClearingOrderRelation> relations = clearingOrderRelationService.findList(null, filters, null);
		return relations;
	}
}