package org.rebate.service.impl; 

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.job.SellerClearingSingleJob;
import org.rebate.json.beans.SellerClearingOrders;
import org.rebate.json.beans.SellerClearingResult;
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
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferInfo;
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferReq;
import org.rebate.utils.jiupai.pojo.capSingleTransfer.SingleTransferReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
      
    @Resource(name="sellerClearingRecordDaoImpl")  
    private SellerClearingRecordDao sellerClearingRecordDao;
      
    @Resource(name = "sellerClearingSingleJob")
    private SellerClearingSingleJob sellerClearingSingleJob;
    
    private Setting setting = SettingUtils.get();
      
  	/**
  	 * 生成需要结算的商家货款记录
  	 */
  	@Override
  	public List<SellerClearingOrders> getNeedClearingRecords(Date startDate, Date endDate, PaymentChannel channel) {
  		
  		List<SellerClearingOrders> clearingOrdersList = new ArrayList<SellerClearingOrders>();//货款记录列表
  		
  		Map<Long, BigDecimal> sellerAmountMap = new HashMap<Long, BigDecimal>();//商家Id(KEY) : 订单金额 (VALUE)
  		Map<Long, BigDecimal> sellerIncomeMap = new HashMap<Long, BigDecimal>();//商家Id(KEY) : 结算金额 (VALUE)
  		Map<Long, Set<Order>> sellerOrdersMap = new HashMap<Long, Set<Order>>();//商家Id(KEY) : 订单列表 (VALUE)
  		
  		List<Filter> filters = new ArrayList<Filter>();
  		OrderStatus[] paidStatus = {OrderStatus.PAID,OrderStatus.FINISHED};//已支付 或 评价后
  		filters.add(Filter.in("status", paidStatus));
  		filters.add(Filter.eq("isClearing", false));//未结算订单
  		filters.add(Filter.eq("isSallerOrder", false));//普通订单(非录单)
  		filters.add(Filter.le("paymentTime", endDate));//支付时间在  截止日期之前
  		//filters.add(Filter.eq("paymentChannel", channel));//订单的支付渠道
  		List<Order> orderList = orderService.findList(null, filters, null);
  		List<Order> orders = new ArrayList<Order>();//实际需要结算的订单
  		for (int i = 0; i < orderList.size(); i++) {
  			Order order = orderList.get(i);
  			long existCount = clearingOrderRelationService.count(Filter.eq("order", order));;
  			if (existCount == 0) {
  				orders.add(order);
  			}
  		}
  		LogUtil.debug(this.getClass(), "getNeedClearingRecords", "总共订单个数: %s, 实际需要结算的订单个数: %s", orderList.size(), orders.size());
  		
  		//将orders以商家(seller)分组,以便 每个商家的订单列表组成一条货款结算记录
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
  				amount = amount.add(order.getAmount());//叠加金额
  				sellerAmountMap.put(sellerId, amount);
  				
  				BigDecimal income = sellerIncomeMap.get(sellerId);
  				income = income.add(order.getSellerIncome());//叠加金额
  				sellerIncomeMap.put(sellerId, income);
  				
  				Set<Order> orderSet = sellerOrdersMap.get(sellerId);
  				orderSet.add(order);//添加订单
  			}
  		}
  		//orders以商家分组完成后,遍历Map,商家货款记录
  		if (!sellerAmountMap.isEmpty() && !sellerIncomeMap.isEmpty() && !sellerOrdersMap.isEmpty()) {
  			
  			 for (Map.Entry<Long, BigDecimal> entry : sellerAmountMap.entrySet()) {
  				 
  				 Long sellerId = entry.getKey();//商家Id(KEY)
  				
  				 SellerClearingOrders clearingOrders = new SellerClearingOrders();
  				 SellerClearingRecord record = new SellerClearingRecord();//货款记录
  				 clearingOrders.setRecord(record);
  				 clearingOrders.setOrderSet(sellerOrdersMap.get(sellerId));
  				 
  				 
  				 Seller seller = sellerService.find(sellerId);//根据sellerId获取Seller
  				 record.setSeller(seller);
  				 
  				 LogUtil.debug(this.getClass(), "getNeedClearingRecords", "开始处理商家Id:%s 的货款记录", sellerId);

  				 BigDecimal totalSellerIncome = sellerIncomeMap.get(sellerId); //结算金额 (VALUE)
  				 totalSellerIncome = totalSellerIncome.setScale(2,BigDecimal.ROUND_HALF_UP);//保留两位小数，即精确到"分"			   				   				  				 
                 record.setAmount(totalSellerIncome);//结算金额       
                 
                 //结算金额不足 "结算最低金额限制"的，等下次凑够了金额再结算
                 SystemConfig clearingMinLimit = systemConfigService.getConfigByKey(SystemConfigKey.CLEARING_MINIMUM_LIMIT);//商家货款结算最低金额限制
     			 if (clearingMinLimit != null && clearingMinLimit.getConfigValue() != null) {
     				BigDecimal minLimit = new BigDecimal(clearingMinLimit.getConfigValue());
     				if (record.getAmount().compareTo(minLimit) == -1) {//货款金额 小于 最低限制
                        LogUtil.debug(this.getClass(), "getNeedClearingRecords", "商家ID:%s, 货款金额 :%s, 不足最低结算金额 %s, 等下次凑够了金额再结算", 
                        		seller.getId(), record.getAmount().toString(), clearingMinLimit.getConfigValue());
                        continue;//绕开货款金额不足 结算最低金额的商家货款结算
     				}
     			 }
     			 
  				 BigDecimal totalOrderAmount = entry.getValue(); //订单金额 (VALUE)
  				 record.setTotalOrderAmount(totalOrderAmount);
  				
  				 EndUser endUser = seller.getEndUser();//商家用户  				   				   				 
  				 record.setEndUser(endUser);
  				 
     			 //获取商家用户默认银行卡
                 BankCard defaultCard = bankCardService.getDefaultCard(endUser);
                 if (defaultCard != null) {
                      record.setBankCardId(defaultCard.getId());//银行卡Id
                      record.setOwnerName(defaultCard.getOwnerName());//持卡人姓名
                      record.setCardNum(defaultCard.getCardNum());//银行卡号
                 }else {
                     LogUtil.debug(this.getClass(), "getNeedClearingRecords", "未能找到商家用户默认银行卡,商家用户Id: %s", endUser != null? endUser.getId() : "");
                     record.setClearingStatus(ClearingStatus.FAILED);//处理失败
                     record.setIsClearing(false);//未结算
                     record.setValid(true);//有效
                     record.setRemark("商家未添加默认银行卡,待添加银行卡后执行单笔货款提现!");
                     save(record);//保存此货款记录(需要用户在页面点击货款提现)
                     Set<Order> orderSet = sellerOrdersMap.get(sellerId);
                     for (Order order : orderSet) {
                       ClearingOrderRelation relation = new ClearingOrderRelation();
                       relation.setClearingRecId(record.getId());
                       relation.setOrder(order);
                       clearingOrderRelationService.save(relation);//保存商家货款结算记录与订单的关系
                     }
                     LogUtil.debug(this.getClass(), "getNeedClearingRecords", "商家用户 %s 未添加默认银行卡,待添加银行卡后执行单笔货款提现!", endUser != null? endUser.getCellPhoneNum() : "");
                     continue;//绕开银行卡为空的商家货款结算
                 }

  				 record.setHandlingCharge(new BigDecimal(0));//XGG需求：商家货款代付不扣手续费了,即手续费为0
  				 
  				 record.setClearingSn(snService.generate(Type.SELLER_CLEARING_RECORD));//结算货款单编号（用于显示）
  				 record.setClearingStatus(ClearingStatus.PROCESSING);//处理中
  				 record.setIsClearing(false);//未结算
  				 record.setValid(true);//有效
  				 record.setPaymentChannel(channel);//支付渠道
  				 
  				 clearingOrdersList.add(clearingOrders);
  				 LogUtil.debug(this.getClass(), "getNeedClearingRecords", "已添加商家Id:%s 的货款记录", sellerId);
  			 }
  		}
  		return clearingOrdersList;
  	}  
    /**
     * 商家货款批量代付(通联渠道)
     * return reqSn
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)  
  	public String sellerClearingByAllinPay(List<SellerClearingOrders> clearingOrdersList) {
    	if (clearingOrdersList.size() == 0) {
    		LogUtil.debug(this.getClass(), "sellerClearingByAllinPay", "(通联渠道)无需要结算的商家货款记录");
    		return null;
    	}
	 	//货款单数量
	 	String totalItem = clearingOrdersList.size() + "";
	 	
		BigDecimal totalClearingAmount = new BigDecimal(0);//货款结算总金额
		BigDecimal totalHandlingCharge = new BigDecimal(0);//手续费总金额
		Map<String, Set<Order>> sellerOrdersMap = new HashMap<String, Set<Order>>();//商家货款编号(KEY) : 订单列表 (VALUE)
		List<SellerClearingRecord> records = new ArrayList<SellerClearingRecord>();
	 	for (SellerClearingOrders clearingOrders: clearingOrdersList) {
	 		SellerClearingRecord record = clearingOrders.getRecord();
	 		totalClearingAmount = totalClearingAmount.add(record.getAmount());//累加结算金额
			totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
			records.add(record);
			sellerOrdersMap.put(record.getClearingSn(), clearingOrders.getOrderSet());
		}
	 	
	 	//总代付的金额
	 	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总结算金额 - 总手续费  = 总代付的金额
	 	
	    TranxServiceImpl tranxService = new TranxServiceImpl();
	    
	    try {
	    	//总代付的金额(单位 分， 字符串)
	    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
	    	
	    	//开始批量代付
	    	List<SellerClearingRecord> recordList = tranxService.batchDaiFu(false, totalItem, totalPayStr, records);
	    	if (recordList != null && recordList.size() > 0) {
	    		
		    	save(recordList);//保存已经受理的货款结算记录(已赋值了reqSn和sn)
		    	
		    	//保存商家货款结算记录与订单的关系
		    	for (int i = 0; i < recordList.size(); i++) {
		    		 SellerClearingRecord record = recordList.get(i);
					 Set<Order> orderSet = sellerOrdersMap.get(record.getClearingSn());
					 for (Order order : orderSet) {
						ClearingOrderRelation relation = new ClearingOrderRelation();
						relation.setClearingRecId(record.getId());
						relation.setOrder(order);
						clearingOrderRelationService.save(relation);
					}
				}
		    	
		    	return recordList.get(0).getReqSn();
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.debug(this.getClass(), "sellerClearingByAllinPay", "商家货款批量代付(通联渠道)失败  捕获异常: %s", e.getMessage());
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
		}
	    return null;
  	}
    /**
     * 商家货款批量代付(九派渠道)
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
	public void sellerClearingByJiuPai(List<SellerClearingOrders> clearingOrdersList) {
    	if (clearingOrdersList.size() > 0) {
    		Map<String, Set<Order>> sellerOrdersMap = new HashMap<String, Set<Order>>();//商家货款编号(KEY) : 订单列表 (VALUE)
    		Map<String, SellerClearingRecord> snClearingRecordMap = new HashMap<String, SellerClearingRecord>();//结算货款单编号(KEY) : 结算货款记录 (VALUE)
    	 	for (SellerClearingOrders clearingOrders: clearingOrdersList) {
    	 		SellerClearingRecord record = clearingOrders.getRecord();
    			sellerOrdersMap.put(record.getClearingSn(), clearingOrders.getOrderSet());
    			snClearingRecordMap.put(record.getClearingSn(), record);
    		}
    	 	//组装请求参数
    	    BatchTransferReq req = new BatchTransferReq();
    	    List<BatchTransferInfo> reqInfoList = new ArrayList<BatchTransferInfo>();
    	    for (Map.Entry<String, SellerClearingRecord> entry : snClearingRecordMap.entrySet()) {
    	    	  SellerClearingRecord record = entry.getValue();
    	    	  BatchTransferInfo info = new BatchTransferInfo();
    	    	  BigDecimal payAmount = record.getAmount().subtract(record.getHandlingCharge());//结算金额 - 手续费 = 代付的金额
    	    	  String payStr = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
    	 	      info.setTxnAmt(payStr);//交易(代付)金额  分
    	 	      info.setCardNo(record.getCardNum());
    	 	      info.setUsrNm(record.getOwnerName());
    	 	      info.setMercOrdNo(record.getClearingSn());
    	 	      info.setRmk("九派 货款结算:" + record.getClearingSn());
    	 	      reqInfoList.add(info);
    		}
    	    req.setCount(reqInfoList.size());
    	    req.setInfoList(reqInfoList);
    	    try {
    	    	//开始批量代付
    	    	GateWayService gateWayService = new GateWayService();
    	    	Map<String, String> resMap = gateWayService.capBatchTransfer(req);
    	    	//获取响应 infoList
    	    	String resInfoList = resMap.get("infoList");
  			  	JSONArray jsonArray = JSON.parseArray(resInfoList);
  			  	if (jsonArray.size() > 0) {
  			  	  String merchantId = setting.getJiupaiMerchantId();
  				  for (int j = 0; j < jsonArray.size(); j++) {
  					  JSONObject jsonObject = jsonArray.getJSONObject(j);
  					  String batchNo = jsonObject.getString("batchNo");
  					  String jrnNo = jsonObject.getString("jrnNo");
  					  String mercOrdNo = jsonObject.getString("mercOrdNo");
  					  String errorCode = jsonObject.getString("errorCode");
  					  String errorMsg = jsonObject.getString("errorMsg");
  					  LogUtil.debug(this.getClass(), "sellerClearingByJiuPai", "merchantId: %s, batchNo: %s, jrnNo: %s, mercOrdNo: %s,errorCode: %s, errorMsg: %s",
  							merchantId, batchNo, jrnNo, mercOrdNo, errorCode, errorMsg);
					  if (!mercOrdNo.startsWith(merchantId) || !jrnNo.startsWith(batchNo)) {
						  LogUtil.debug(this.getClass(), "sellerClearingByJiuPai", "merchantId + withDrawSn != 商户订单号(mercOrdNo) 或   batchNo + sn != 交易流水 (jrnNo)");
						  continue; 
					  }
					  String clearingSn = mercOrdNo.replace(setting.getJiupaiMerchantId(), "");//结算货款单编号
					  String sn = jrnNo.replace(batchNo, "");//记录序号，例如：0001
  					  SellerClearingRecord record = snClearingRecordMap.get(clearingSn);
  					   if (record != null) {
  						   	if ("CAP00500".equals(errorCode)) {//CAP00500受理成功
  						    	record.setReqSn(batchNo);
  						    	record.setSn(sn);
  						   	}else {//未受理  例如CAP00530不支持此银行卡  先保存失败的记录  然后走货款单笔结算流程
  						    	record.setClearingStatus(ClearingStatus.FAILED);
  							}
					    	record.setRemark(errorMsg);
					    	save(record);
					    	//保存商家货款结算记录与订单的关系
							Set<Order> orderSet = sellerOrdersMap.get(record.getClearingSn());
							for (Order order : orderSet) {
								ClearingOrderRelation relation = new ClearingOrderRelation();
								relation.setClearingRecId(record.getId());
								relation.setOrder(order);
								clearingOrderRelationService.save(relation);
							}
  					   }
  				   }
  			  	}
    	    	
    	    } catch (Exception e) {
				e.printStackTrace();
				LogUtil.debug(this.getClass(), "sellerClearingByJiuPai", "商家货款批量代付(九派渠道)失败  捕获异常: %s", e.getMessage());
    	    }
    	      
    	}else {
    		LogUtil.debug(this.getClass(), "sellerClearingByJiuPai", "(九派渠道)无需要结算的商家货款记录");
		}
	}
  	/**
  	 * (通联渠道)单笔实时付款（代付）
  	 */
  	@Override
  	public Message singlePayByAllinpay(SellerClearingRecord record, BankCard bankCard) {
  	      try {
  			    TranxServiceImpl tranxService = new TranxServiceImpl();
  			    BigDecimal handlingCharge = new BigDecimal(0);//XGG需求：商家货款代付不扣手续费了，即手续费为0
  			    record.setHandlingCharge(handlingCharge);
  			    BigDecimal payAmount = record.getAmount().abs().subtract(handlingCharge);
    			BigDecimal amountPenny = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
    			
  	          	//预先申请 新的货款单编号
  	          	String newClearingSn = snService.generate(Type.SELLER_CLEARING_RECORD);
    			//发起单笔代付
  			    Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getOwnerName(), bankCard.getCardNum(), amountPenny.toString());
  			    if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
  			    	String status = resultMap.get("status");
	  			    String req_sn = resultMap.get("req_sn");
	  			    String err_msg = resultMap.get("err_msg");
	  			    
  			    	LogUtil.debug(this.getClass(), "singlePayByAllinpay", "(通联渠道)单笔实时付款返回  status:%s, req_sn:%s, err_msg:%s", status, req_sn, err_msg);
  			    	
  			    	if ("success".equals(status) || "error".equals(status)){//最终结果
  	  			    	singlePayHandle(record, status, req_sn, newClearingSn, err_msg, handlingCharge, PaymentChannel.ALLINPAY);
  	  			    	return Message.success("rebate.message.success");
  			    	}
  			    	if ("wait".equals(status)){//中间状态，还需要等待查询
  	  			    	singlePayWait(record, status, req_sn, newClearingSn, err_msg, handlingCharge, PaymentChannel.ALLINPAY, null, null);
  	  			    	return Message.success(err_msg);
  			    	}
  				}
  				return Message.error("rebate.common.system.error");				
  	        } catch (Exception e) {
  	          e.printStackTrace();
  	          return Message.error("rebate.common.system.error");
  	        }
  	}
  	/**
  	 * (九派渠道)单笔实时付款（代付）
  	 */
	@Override
	public Message singlePayByJiuPai(SellerClearingRecord record, BankCard bankCard) {
	    	BigDecimal handlingCharge = new BigDecimal(0);//XGG需求：商家货款代付不扣手续费了，即手续费为0
	    	record.setHandlingCharge(handlingCharge);
	    	BigDecimal payAmount = record.getAmount().abs().subtract(handlingCharge);
	    	String amountPenny = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();	
	      try {
	          GateWayService gateWayService = new GateWayService();
	          SingleTransferReq req = new SingleTransferReq();
	          req.setAmount(amountPenny);//交易金额  单位：分
	          req.setCardNo(bankCard.getCardNum());//银行卡号
	          req.setAccName(bankCard.getOwnerName());//账户户名
	          req.setRemark("九派 单笔货款结算");//订单备注
	          req.setCallBackUrl("/console/jiupai/notifyClearingRecord.jhtml");//回调地址URL
	          
	          //预先申请 新的货款单编号
	          String newClearingSn = snService.generate(Type.SELLER_CLEARING_RECORD);
	          //发起单笔代付
	          Map<String, String> resMap = gateWayService.capSingleTransfer(req, newClearingSn);
	          String rspCode = resMap.get("rspCode");//应答码  IPS00000正常返回
	          String rspMessage = resMap.get("rspMessage");//交易成功
	          String orderSts = resMap.get("orderSts");//订单状态  U订单初始化  P处理中   S处理成功   F处理失败   R退汇   N待人工处理
	          String mcSequenceNo = resMap.get("mcSequenceNo");//商户交易流水
	          String mcTransDateTime = resMap.get("mcTransDateTime");//商户交易时间
	          String orderNo = resMap.get("orderNo");//原交易订单号
	          if ("IPS00000".equals(rspCode) && mcSequenceNo != null) {//正常返回
//	        	  //test
//	        	  if ("P".equals(orderSts) && orderNo.indexOf("800002308510001") >= 0) { 
//	        		  orderSts = "S";
//	        	  }
//	        	  //test
	        	  if ("S".equals(orderSts)) {//处理成功
	        		  singlePayHandle(record, "success", mcSequenceNo, newClearingSn, rspMessage, handlingCharge, PaymentChannel.JIUPAI);
	        		  return Message.success(rspMessage);
				  }
	        	  if ("F".equals(orderSts) || "R".equals(orderSts)) {//处理失败 
	        		  String errMsg = rspMessage;
	        		  if ("F".equals(orderSts)) errMsg = "处理失败";
	        		  if ("R".equals(orderSts)) errMsg = "退汇";
	        		  singlePayHandle(record, "error", mcSequenceNo, newClearingSn, errMsg, handlingCharge, PaymentChannel.JIUPAI);
	        		  return Message.error(rspMessage);
				  }
	        	  if ("U".equals(orderSts) || "P".equals(orderSts) || "N".equals(orderSts)) {//处理中 
	        		  String errMsg = rspMessage;
	        		  if ("U".equals(orderSts)) errMsg = "订单初始化";
	        		  if ("P".equals(orderSts)) errMsg = "处理中";
	        		  if ("N".equals(orderSts)) errMsg = "待人工处理";
	        		  singlePayWait(record, "wait", mcSequenceNo, newClearingSn, errMsg, handlingCharge, PaymentChannel.JIUPAI, mcTransDateTime, orderNo);
	        		  return Message.error(rspMessage);
				  }
			  }
	          return Message.error("rebate.common.system.error");	
	        } catch (Exception e) {
	          e.printStackTrace();
	          return Message.error("rebate.common.system.error");
	        }
	}
  	/**
  	 * 单笔结算等待查询，将以前的记录作废，新建一个新的记录(处理中...)
  	 */
  	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  	private void singlePayWait(SellerClearingRecord oldRecord, String status, String reqSn, String newClearingSn, 
  			String errMsg, BigDecimal handlingCharge, PaymentChannel channel, String mcTransDateTime, String orderNo){
  		oldRecord.setValid(false);//标记旧的货款记录无效
  		update(oldRecord);//更新旧的货款记录
  		LogUtil.debug(this.getClass(), "singlePayWait", "旧的货款单%s已作废", oldRecord.getClearingSn());
  		
  		SellerClearingRecord newRecord = new SellerClearingRecord();
  		newRecord.setPaymentChannel(channel);//支付渠道
  		newRecord.setAmount(oldRecord.getAmount());
  		newRecord.setBankCardId(oldRecord.getBankCardId());
  		newRecord.setClearingSn(newClearingSn);//结算货款单编号（用于显示）
  		newRecord.setEndUser(oldRecord.getEndUser());
  		newRecord.setSeller(oldRecord.getSeller());
  		newRecord.setTotalOrderAmount(oldRecord.getTotalOrderAmount());
  		newRecord.setHandlingCharge(handlingCharge);
  		newRecord.setValid(true);//标记新的货款记录有效
  		newRecord.setReqSn(reqSn);
  		newRecord.setSn(null);//单笔结算不像批量结算，没有sn号
  		newRecord.setRemark(errMsg);
	    if ("wait".equals(status)) {
	  		newRecord.setClearingStatus(ClearingStatus.PROCESSING);//处理中...
	  		newRecord.setIsClearing(false);//暂时未结算
		    save(newRecord);//保存新的货款记录
		    LogUtil.debug(this.getClass(), "singlePayWait", "新的货款单%s已生成", newRecord.getClearingSn());
		    
	  		oldRecord.setRemark(oldRecord.getRemark() + ";已作废请参考:"+newClearingSn);
	  		update(oldRecord);//更新旧的货款记录
	  		//把旧的货款记录和订单关系解除，建立新的货款记录和订单关系
	  		List<ClearingOrderRelation> relations = getRelationListByRecordId(oldRecord.getId());
			for (ClearingOrderRelation relation : relations) {
				relation.setClearingRecId(newRecord.getId());
				clearingOrderRelationService.update(relation);//保存商家货款结算记录与订单的关系
			}
			if (reqSn != null) {
				//过十分钟后发起异步单笔查询请求(通联渠道)
			    if (channel == PaymentChannel.ALLINPAY) {
			    	sellerClearingSingleJob.updateRecordSingleByAllinpay(reqSn);
			    }
			    //过十分钟后发起异步订单查询请求(九派渠道)
			    else if (channel == PaymentChannel.JIUPAI) {
			    	sellerClearingSingleJob.updateRecordSingleByJiuPai(reqSn, mcTransDateTime, orderNo);
				}
			}

		}
  	}  	
  	/**
  	 * 单笔结算完成，将以前的记录作废，新建一个新的记录(处理成功 或者 处理失败)
  	 */
  	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  	private void singlePayHandle(SellerClearingRecord oldRecord, String status, String req_sn, String newClearingSn, String err_msg, BigDecimal handlingCharge, PaymentChannel channel){
  		oldRecord.setValid(false);//标记旧的货款记录无效
  		update(oldRecord);//更新旧的货款记录
  		LogUtil.debug(this.getClass(), "singlePayHandle", "旧的货款单%s已作废", oldRecord.getClearingSn());
  		
  		SellerClearingRecord newRecord = new SellerClearingRecord();
  		newRecord.setPaymentChannel(channel);//设置新的货款记录的支付渠道
  		newRecord.setAmount(oldRecord.getAmount());
  		newRecord.setBankCardId(oldRecord.getBankCardId());
  		
  		newRecord.setClearingSn(newClearingSn);//结算货款单编号（用于显示）
  		newRecord.setEndUser(oldRecord.getEndUser());
  		newRecord.setSeller(oldRecord.getSeller());
  		newRecord.setTotalOrderAmount(oldRecord.getTotalOrderAmount());
  		newRecord.setHandlingCharge(handlingCharge);
  		newRecord.setValid(true);//标记新的货款记录有效
  		newRecord.setReqSn(req_sn);
  		newRecord.setSn(null);//单笔结算不像批量结算，没有sn号
  		newRecord.setRemark(err_msg);
	    if ("success".equals(status)) {
	    	if (PaymentChannel.JIUPAI == channel) {//如果是九派渠道，还需要等待回调才是最终结果，因此暂时设置为处理中,货款未结算
		  		newRecord.setClearingStatus(ClearingStatus.PROCESSING);
		  		newRecord.setIsClearing(false);
		  		newRecord.setRemark(newRecord.getRemark() + ",等待回调通知");
			}else {
		  		newRecord.setClearingStatus(ClearingStatus.SUCCESS);
		  		newRecord.setIsClearing(true);
		        //结算成功后，从商家的未结算货款金额减去本次货款已结算金额
	            if (newRecord.getSeller() != null) {
	                Seller seller = newRecord.getSeller();
	                seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(newRecord.getAmount()));
	                sellerService.update(seller);
	            }
			}
		}else if ("error".equals(status)) {
	  		newRecord.setClearingStatus(ClearingStatus.FAILED);
	  		newRecord.setIsClearing(false);
		}
	    save(newRecord);//保存新的货款记录
	    LogUtil.debug(this.getClass(), "singlePayHandle", "新的货款单%s已生成", newRecord.getClearingSn());
	    
  		oldRecord.setRemark(oldRecord.getRemark() + ";已作废请参考:"+newClearingSn);
  		update(oldRecord);//更新旧的货款记录
  		//把旧的货款记录和订单关系解除，建立新的货款记录和订单关系
  		List<ClearingOrderRelation> relations = getRelationListByRecordId(oldRecord.getId());
		for (ClearingOrderRelation relation : relations) {
			relation.setClearingRecId(newRecord.getId());
			clearingOrderRelationService.update(relation);//保存商家货款结算记录与订单的关系
			//通联处理成功的货款涉及的订单改为已结算，反之，如果是九派渠道，还需要等待回调才是最终结果
			if (PaymentChannel.ALLINPAY == channel && "success".equals(status)) {
				Order order = relation.getOrder();
				order.setIsClearing(true);
				orderService.update(order);
			}
		}

  	}
	@Override
	public List<ClearingOrderRelation> getRelationListByRecordId(Long recordId) {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("clearingRecId", recordId));
		List<ClearingOrderRelation> relations = clearingOrderRelationService.findList(null, filters, null);
		return relations;
	}
	@Override
	public List<SellerClearingResult> findClearingResult() {
		return sellerClearingRecordDao.findClearingResult();
	}
	@Override
	public List<String> jiuPaiProcessingBatchNoList(Date endDate) {
		return sellerClearingRecordDao.jiuPaiProcessingBatchNoList(endDate);
	}
	
//  /**
//  * 获取通联货款结算手续费
//  * @param totalOrderAmount
//  * @return
//  */
//	private BigDecimal getAllinpayHandlingCharge(BigDecimal totalOrderAmount) {
//	    //手续费，优先考虑 每笔提现固定手续费（通联固定每笔是1.5元？？），后考虑提现手续费占提现金额的百分比 
//		BigDecimal handlingCharge = new BigDecimal(0);
//		try {
//			SystemConfig feePertime = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
//			if (feePertime != null && feePertime.getConfigValue() != null) {
//				handlingCharge = new BigDecimal(feePertime.getConfigValue());
//			}else {
//				SystemConfig feePercentage = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
//				if (feePercentage != null && feePercentage.getConfigValue() != null) {
//			    	handlingCharge = totalOrderAmount.multiply(new BigDecimal(feePercentage.getConfigValue()));
//				}
//			}
//		} catch (Exception e) {
//			LogUtil.debug(this.getClass(), "getHandlingCharge", "Catch exception: %s", e.getMessage());
//			return handlingCharge;
//		}
//		return handlingCharge;
//	}


}