package org.rebate.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.beans.Setting;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Order;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.framework.filter.Filter;
import org.rebate.json.beans.SellerClearingOrders;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.OrderService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.SellerService;
import org.rebate.utils.CommonUtils;
import org.rebate.utils.DateUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.SpringUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capBatchQuery.BatchQueryReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 商户货款结算Job(T+1后结算)
 *
 */
@Component("sellerClearingBatchJob")
@Lazy(false)
public class SellerClearingBatchJob {

  public static Date date;

  @Resource(name = "sellerClearingRecordServiceImpl")
  private SellerClearingRecordService sellerClearingRecordService;
  
  @Resource(name="clearingOrderRelationServiceImpl")
  private ClearingOrderRelationService clearingOrderRelationService;
  
  @Resource(name="endUserServiceImpl")
  private EndUserService endUserService;
  
  @Resource(name="orderServiceImpl")
  private OrderService orderService;
 
  @Resource(name="paramConfigServiceImpl")
  private ParamConfigService paramConfigService;
  
  @Resource(name="sellerServiceImpl")
  private SellerService sellerService; 
  
  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;
  
  @Resource(name = "leScoreRecordJob")
  private LeScoreRecordJob leScoreRecordJob;
  
  //@Scheduled(cron="0 30 21 * * ?")
  @Scheduled(cron = "${job.daily_sellerClearing_cal.cron}")// 每天10点0分0秒执行 0 0 10 * * ?
  public void sellerClearingCalculate() {
    if (date == null) {
      date = new Date();
    }
    Date startDate = DateUtils.startOfDay(date, -1);//获取昨天的开始时间 00:00:00
    Date endDate = DateUtils.endOfDay(date, -1);//获取昨天的结束时间 23:59:59 999

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job Start! Time Period:"
        + startDate + " - " + endDate);
    try {
      //获取支付渠道
      PaymentChannel channel = CommonUtils.getPaymentChannel();
      //生成需要结算的商家货款记录
      List<SellerClearingOrders> records = sellerClearingRecordService.getNeedClearingRecords(startDate, endDate, channel);
      LogUtil.debug(this.getClass(), "sellerClearingCalculate", "需要结算的商家货款数量:"+records.size());
      //1. 通联支付渠道
      if (PaymentChannel.ALLINPAY == channel) {
    	  //商家货款批量代付(通联渠道)
          String reqSn = sellerClearingRecordService.sellerClearingByAllinPay(records);     
          if (reqSn != null) {
        	  //隔一段时间请求通联交易结果查询接口，自动更新商家货款记录的状态
        	  notifyClearingRecordByAllinpay(reqSn);
    	  }
	  }
      //2. 九派支付渠道
      else if(PaymentChannel.JIUPAI == channel) {
		//更新之前九派未更新状态的乐分提现记录和货款记录
    	updateWithdrawRecordByJiupai(endDate);
    	updateClearingRecordByJiupai(endDate);  
    	//商家货款批量代付(九派渠道)
    	if (records.size() == 0) {
    		LogUtil.debug(this.getClass(), "sellerClearingCalculate", "(九派渠道)无需要结算的商家货款记录");
    	}else {
    		int count = (records.size()/200) + 1;//结算次数(九派批量代付   一批次最多200条记录)
    		for (int i = 0; i < count; i++) {
    			int fromIndex = i*200;
    			int toIndex = (i+1)*200;
    			if (i + 1 == count) {
    				toIndex = records.size();
    			}
    			LogUtil.debug(this.getClass(), "sellerClearingCalculate", "(九派支付渠道) fromIndex=%s, toIndex=%s", fromIndex, toIndex);
    			List<SellerClearingOrders> recordList = records.subList(fromIndex, toIndex);
    			sellerClearingRecordService.sellerClearingByJiuPai(recordList); 	
    		}
		}
	  }

    } catch (Exception e) {
      date = null;
      LogUtil.debug(this.getClass(), "sellerClearingCalculate", "捕获异常:"+e.getMessage());
      e.printStackTrace();
    }

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job End! Time Period:"
        + startDate + " - " + endDate);
    date = null;
  }

  /**
   * 更新之前九派未更新状态的货款记录
   * @param startDate
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void updateClearingRecordByJiupai(Date endDate){
	  // -- 更新货款结算 --
	  //九派需要更新结算状态的交易批次号list
	  List<String> batchNoList = sellerClearingRecordService.jiuPaiProcessingBatchNoList(endDate);
	  if (batchNoList != null && batchNoList.size() > 0) {
		  for (int i = 0; i < batchNoList.size(); i++) {
			  String batchNo = batchNoList.get(i);//交易批次号
			  GateWayService gateWayService = new GateWayService();
			  BatchQueryReq req = new BatchQueryReq();
			  req.setBatchNo(batchNo);
			  req.setPageNum(1);
			  req.setPageSize(200);
			  //代收付批量查询
			  Map<String, String> resMap = gateWayService.capBatchQuery(req);
			  String queryListStr = resMap.get("tamtCapQueryList");
			  JSONArray jsonArray = JSON.parseArray(queryListStr);
			  if (jsonArray.size() > 0) {
				  for (int j = 0; j < jsonArray.size(); j++) {
					  JSONObject jsonObject = jsonArray.getJSONObject(j);
					  Setting setting = SettingUtils.get();
					  String merchantId = setting.getJiupaiMerchantId();
					  String resBatchNo = jsonObject.getString("batchNo");
					  String mercOrdNo = jsonObject.getString("mercOrdNo");
					  String ordSts = jsonObject.getString("ordSts");
					  String tamTxTyp = jsonObject.getString("tamTxTyp");
					  
  					  LogUtil.debug(this.getClass(), "updateClearingRecordByJiupai", "merchantId: %s, batchNo: %s, mercOrdNo: %s,ordSts: %s, tamTxTyp: %s",
  							merchantId, resBatchNo, mercOrdNo, ordSts, tamTxTyp);
					  if (!mercOrdNo.startsWith(merchantId)) {
						  LogUtil.debug(this.getClass(), "updateClearingRecordByJiupai", "merchantId + withDrawSn != 商户订单号(mercOrdNo)");
						  continue; 
					  }
					  String clearingSn = mercOrdNo.replace(setting.getJiupaiMerchantId(), "");
					  SellerClearingRecord record = findNeedClearingRecord(resBatchNo, null, clearingSn);
					   if (record != null) {
						   	String msg = tamTxTyp + ordSts;
//						   	//test
//						   	//测试环境 预下单 当成处理成功
//				        	if ("预下单".equals(ordSts) && mercOrdNo.indexOf("800002308510001") >= 0) { 
//				        		ordSts = "处理成功";
//				        	}
//						   	//test
						    if ("S".equals(ordSts) || "处理成功".equals(ordSts)) {//处理成功
						    	updateRecord(record, ClearingStatus.SUCCESS, msg);
							}else if ("N".equals(ordSts) || "处理失败".equals(ordSts)){//处理失败
								updateRecord(record, ClearingStatus.FAILED, msg);
							}else {
								//处理中 或 预下单 这种不管，等第二天10点会继续查询 批量代付 查询接口
							}
					   }
					   LogUtil.debug(this.getClass(), "updateClearingRecordByJiupai", "batchNo: %s, mercOrdNo: %s", batchNo, mercOrdNo);
				  }
			  }
			  
		  }
	  }else {
		  LogUtil.debug(this.getClass(), "updateWithdrawRecordByJiupai", "(九派渠道)无需要更新状态的货款记录");
	  }
  }
  /**
   * 更新之前九派未更新状态的乐分提现
   * @param startDate
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void updateWithdrawRecordByJiupai(Date endDate) throws Exception{
	  // -- 更新乐分提现 --
	  //九派需要更新提现状态的交易批次号list
	  List<String> batchNoList = leScoreRecordService.jiuPaiProcessingBatchNoList(endDate);
	  if (batchNoList != null && batchNoList.size() > 0) {
		  for (int i = 0; i < batchNoList.size(); i++) {
			  String batchNo = batchNoList.get(i);//交易批次号
			  GateWayService gateWayService = new GateWayService();
			  BatchQueryReq req = new BatchQueryReq();
			  req.setBatchNo(batchNo);
			  req.setPageNum(1);
			  req.setPageSize(200);
			  //代收付批量查询
			  Map<String, String> resMap = gateWayService.capBatchQuery(req);
			  String queryListStr = resMap.get("tamtCapQueryList");
			  JSONArray jsonArray = JSON.parseArray(queryListStr);
			  if (jsonArray.size() > 0) {
				  for (int j = 0; j < jsonArray.size(); j++) {
					  JSONObject jsonObject = jsonArray.getJSONObject(j);
					  Setting setting = SettingUtils.get();
					  String merchantId = setting.getJiupaiMerchantId();
					  String resBatchNo = jsonObject.getString("batchNo");
					  String mercOrdNo = jsonObject.getString("mercOrdNo");
					  String ordSts = jsonObject.getString("ordSts");
					  String tamTxTyp = jsonObject.getString("tamTxTyp");
					  
  					  LogUtil.debug(this.getClass(), "updateWithdrawRecordByJiupai", "merchantId: %s, batchNo: %s, mercOrdNo: %s,ordSts: %s, tamTxTyp: %s",
  							merchantId, resBatchNo, mercOrdNo, ordSts, tamTxTyp);
					  if (!mercOrdNo.startsWith(merchantId)) {
						  LogUtil.debug(this.getClass(), "updateWithdrawRecordByJiupai", "merchantId + withDrawSn != 商户订单号(mercOrdNo)");
						  continue; 
					  }
					  String withDrawSn = mercOrdNo.replace(setting.getJiupaiMerchantId(), "");
					  LeScoreRecord record = findNeedWithdrawRecord(resBatchNo, null, withDrawSn);
					   if (record != null) {
						   	String msg = tamTxTyp + ordSts;
//						   	//test
//						   	//测试环境 预下单 当成处理成功
//				        	if ("预下单".equals(ordSts) && mercOrdNo.indexOf("800002308510001") >= 0) { 
//				        		ordSts = "处理成功";
//				        	}
//						   	//test
						    if ("S".equals(ordSts) || "处理成功".equals(ordSts)) {//处理成功
//						    	LeScoreRecord successRecord = leScoreRecordJob.updateRecord(record, ClearingStatus.SUCCESS, msg);
//						    	leScoreRecordService.update(successRecord);//不能重用leScoreRecordJob的updateRecord方法，会报detached entity passed to persist
						    	updateRecord(record, ClearingStatus.SUCCESS, msg);
							}else if ("N".equals(ordSts) || "处理失败".equals(ordSts)){//处理失败
//								LeScoreRecord failedRecord = leScoreRecordJob.updateRecord(record, ClearingStatus.FAILED, msg);
//								leScoreRecordService.save(failedRecord);
								updateRecord(record, ClearingStatus.FAILED, msg);
							}else {
								//处理中 或 预下单 这种不管，等第二天10点会继续查询 批量代付 查询接口
							}
					   }
					   LogUtil.debug(this.getClass(), "updateWithdrawRecordByJiupai", "batchNo: %s, mercOrdNo: %s", batchNo, mercOrdNo);
				  }
			  }
			  
		  }
	  }else {
		  LogUtil.debug(this.getClass(), "updateWithdrawRecordByJiupai", "(九派渠道)无需要更新提现状态乐分提现记录");
	  }
  }
  /**
   * 更新乐分提现相关信息
   * @param record
   */
  public LeScoreRecord updateRecord(LeScoreRecord record, ClearingStatus status, String msg) throws Exception{
	  LogUtil.debug(this.getClass(), "updateRecord", "更新乐分提现记录  recordId:%s, status: %s, msg: %s", record.getId(), status.toString(), msg);
	  if (status == ClearingStatus.SUCCESS) {
	  		record.setIsWithdraw(true);
	  		record.setStatus(ClearingStatus.SUCCESS);
	  		record.setWithdrawMsg(msg);//提现返回消息
	  		leScoreRecordService.update(record);
	  }else if (status == ClearingStatus.FAILED) {
	  		record.setIsWithdraw(false);
	  		record.setStatus(ClearingStatus.FAILED);
	  		record.setWithdrawMsg(msg);//提现返回消息
	  		leScoreRecordService.update(record);
	  		// 提现失败 把 乐分还回到用户手中
	  		EndUser endUser = null;
	  		if (record.getEndUser().getId() != null) {
	  			endUser = endUserService.find(record.getEndUser().getId());
				}
	  		if (endUser != null) {
	  			refundLeScore(endUser, record);//退回乐分
			}
	  }
	  return record;
  }
  /**
   * 退回乐分给用户，并生成乐分退回记录
   * @param endUser
   * @param record
   */
  public void refundLeScore(EndUser endUser, LeScoreRecord record) throws Exception{
	    LogUtil.debug(this.getClass(), "refundLeScore", "退回乐分给用户Id:%s",endUser.getId());
  		// 当前乐分
  		BigDecimal curLeScore = record.getAmount();
  		// 激励乐分(包括乐心分红乐分，推荐获得乐分)
  		BigDecimal motivateLeScore = record.getMotivateLeScore();
  		// 业务员收益乐分
  		BigDecimal incomeLeScore = record.getIncomeLeScore();
  		// 代理商提成乐分
  		BigDecimal agentLeScore = record.getAgentLeScore();
  		if (endUser.getCurLeScore() != null && curLeScore != null) {
  			endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore.abs()));
		}
  		if (endUser.getMotivateLeScore() != null && motivateLeScore != null) {
  			endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore.abs()));
		}
  		if (endUser.getAgentLeScore() != null && agentLeScore != null) {
  			endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore.abs()));
		}
  		if (endUser.getIncomeLeScore() != null && incomeLeScore != null) {
  			endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore.abs()));
		}
  		endUserService.update(endUser);
    	LeScoreRecord refundRecord = new LeScoreRecord();//乐分退回记录
    	refundRecord.setLeScoreType(LeScoreType.REFUND);
    	refundRecord.setEndUser(endUser);
    	refundRecord.setAmount(record.getAmount() !=null? record.getAmount().abs():null);
    	refundRecord.setMotivateLeScore(record.getMotivateLeScore());
    	refundRecord.setAgentLeScore(record.getAgentLeScore());
    	refundRecord.setIncomeLeScore(record.getIncomeLeScore());
    	refundRecord.setSeller(record.getSeller());
    	refundRecord.setUserCurLeScore(endUser.getCurLeScore());
    	//例如：乐分退回来自提现流水号：2007010000044091500518983865
    	refundRecord.setWithdrawMsg(SpringUtils.getMessage("rebate.endUser.leScore.REFUND.msg", record.getReqSn()));
    	//例如：提现客户付款帐户余额不足乐分退回
    	refundRecord.setRemark(SpringUtils.getMessage("rebate.endUser.leScore.type.WITHDRAW") + record.getWithdrawMsg()
    	    		+SpringUtils.getMessage("rebate.endUser.leScore.type.REFUND"));
    	leScoreRecordService.save(refundRecord);
    	LogUtil.debug(this.getClass(), "refundLeScore", "并生成乐分退回记录Id:%s",refundRecord.getId());
  }
  /**
   * (异步)隔一段时间请求通联交易结果查询接口，自动更新商家货款记录的状态
   * @param reqSn
   */
  private void notifyClearingRecordByAllinpay(String reqSn){
	  TranxServiceImpl tranxService = new TranxServiceImpl();
      Timer timer=new Timer();
      long delay = getDelayVal();//延迟10分钟后
      long period = getPeriodVal();//每5分钟执行一次
     
		  TimerTask task = new TimerTask(){
			public void run(){
				  try {
					    LogUtil.debug(this.getClass(), "notifyClearingRecordByAllinpay", "开始(通联渠道|异步)隔一段时间请求通联交易结果查询接口");
					    String xmlResponse = tranxService.queryTradeNew(reqSn, false);
					    if (xmlResponse != null) {
					        Document doc = DocumentHelper.parseText(xmlResponse);
					        Element root = doc.getRootElement();// AIPG
					        Element infoElement = root.element("INFO");
					        String ret_code = infoElement.elementText("RET_CODE");
					        if ("0000".equals(ret_code)) {//处理完毕
					        	LogUtil.debug(this.getClass(), "notifyClearingRecordByAllinpay", "通联交易结果查询接口返回:%s 已处理完毕",reqSn);
					        	@SuppressWarnings("unchecked")
					        	Iterator<Element> qtdetails = root.element("QTRANSRSP").elementIterator("QTDETAIL");
					        	try {
					        		handleQueryTradeNew(qtdetails, reqSn);
					        	} catch (Exception e) {
					        		LogUtil.debug(this.getClass(), "handleQueryTradeNew", "Catch Exception: %s", e.getMessage());
					        		e.printStackTrace();
					        	} finally{
					        		cancel();//结束Timer
					        	}
							}
						}
				} catch (Exception e) {
					cancel();//结束Timer
					LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Catch Exception: %s", e.getMessage());
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, delay, period);//延迟10分钟后，每5分钟执行一次
  }
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void handleQueryTradeNew(Iterator<Element> qtdetails, String reqSn) throws Exception{
	while (qtdetails.hasNext()) {
		Element qtdetail = (Element) qtdetails.next();
		String sn = qtdetail.elementText("SN");
		String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
		String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
		SellerClearingRecord record = findNeedClearingRecord(reqSn, sn, null);
		if (record != null) {
		    if ("0000".equals(qtdetail_ret_code) || "4000".equals(qtdetail_ret_code)) {//处理成功
		    	updateRecord(record, ClearingStatus.SUCCESS, qtdetail_err_msg);
			}else {//处理失败
				updateRecord(record, ClearingStatus.FAILED, qtdetail_err_msg);
			}
		}
		LogUtil.debug(this.getClass(), "sellerClearingCalculate", "req_sn: %s, sn: %s", reqSn, sn);
	}
  }

  /**
   * 更新商家货款记录等信息
   * @param record
   * @param status
   */
  private void updateRecord(SellerClearingRecord record, ClearingStatus clearingStatus, String errMsg){
	  LogUtil.debug(this.getClass(), "updateRecord", "更新商家货款记  recordId:%s, clearingStatus: %s, errMsg: %s", record.getId(), clearingStatus.toString(), errMsg);
	  if (clearingStatus == ClearingStatus.SUCCESS) {
		  	//将货款的结算状态改为处理成功即结算成功，是否结算改为true
	    	record.setClearingStatus(ClearingStatus.SUCCESS);
	    	record.setIsClearing(true);
	    	record.setRemark(errMsg);
	    	sellerClearingRecordService.update(record);
	    	//结算成功后，将商家的未结算货款金额减去本次货款已结算金额
	    	if (record.getSeller() != null && record.getSeller().getId() != null) {
	    		Seller seller = sellerService.find(record.getSeller().getId());
	    		seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(record.getAmount()));
	    		sellerService.update(seller);
	    	}
	    	//将本次货款涉及到的所有订单是否结算状态改为true即已结算
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.eq("clearingRecId", record.getId()));
			List<ClearingOrderRelation> relations = clearingOrderRelationService.findList(null, filters, null);
			List<Order> orders = new ArrayList<Order>();
			for (ClearingOrderRelation relation : relations) {
				if (relation != null && relation.getOrder() != null && relation.getOrder().getId() != null) {
					Order order = orderService.find(relation.getOrder().getId());//不明白为啥直接用relation.getOrder()要报错-->could not initialize proxy - no Session
					orders.add(order);
				}
			}
      		for (Order order : orders) {
      			order.setIsClearing(true);
  			}
      		orderService.update(orders);
      		
	  }else if (clearingStatus == ClearingStatus.FAILED) {
		  	//将货款的结算状态改为处理失败即结算失败，是否结算改为false
	    	record.setClearingStatus(ClearingStatus.FAILED);
	    	record.setIsClearing(false);
	    	record.setRemark(errMsg);
	    	sellerClearingRecordService.update(record);
	  }
  }
  /**
   * 根据reqSn,sn,withDrawSn,获取需要更新提现状态的乐分提现记录
   * @return
   */
  private LeScoreRecord findNeedWithdrawRecord(String reqSn, String sn, String withDrawSn){
	  
	  LogUtil.debug(this.getClass(), "findNeedWithdrawRecord", "查询条件 req_sn: %s, sn: %s, withDrawSn: %s", reqSn, sn, withDrawSn);
	  
	  LeScoreRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  if (sn != null) {
		  filters.add(Filter.eq("sn", sn));//子单号
	  }
	  if (withDrawSn != null) {
		  filters.add(Filter.eq("withDrawSn", withDrawSn));//提现记录编号
	  }
	  filters.add(Filter.eq("status", ClearingStatus.PROCESSING));//处理中
	  filters.add(Filter.eq("isWithdraw", false));//未提现
	  List<LeScoreRecord> records = leScoreRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  LogUtil.debug(this.getClass(), "findNeedWithdrawRecord", "找到多个乐分提现记录");//这种情况不应该出现
		  }
		  record = records.get(0);
	  }else {
		  LogUtil.debug(this.getClass(), "findNeedWithdrawRecord", "未找到乐分提现记录");
	  }
	  return record;
  }
  /**
   * 根据reqSn,sn,clearingSn,获取需要更新结算状态的商家货款记录
   * @return
   */
  private SellerClearingRecord findNeedClearingRecord(String reqSn, String sn, String clearingSn){
	  
	  LogUtil.debug(this.getClass(), "findNeedClearingRecord", "查询条件 reqSn: %s, sn: %s, clearingSn: %s", reqSn, sn, clearingSn);
	  
	  SellerClearingRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  if (sn != null) {
		  filters.add(Filter.eq("sn", sn));//子单号
	  }
	  if (clearingSn != null) {
		  filters.add(Filter.eq("clearingSn", clearingSn));//结算货款单编号
	  }
	  filters.add(Filter.eq("clearingStatus", ClearingStatus.PROCESSING));//处理中
	  filters.add(Filter.eq("isClearing", false));//未结算
	  List<SellerClearingRecord> records = sellerClearingRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  LogUtil.debug(this.getClass(), "findNeedClearingRecord", "找到多个商家货款记录");//这种情况不应该出现
		  }
		  record = records.get(0);
	  }else {
		  LogUtil.debug(this.getClass(), "findNeedWithdrawRecord", "未找到商家货款记录");
	  }
	  return record;
  }
  /**
   * 交易结果查询  延迟查询时间
   * @return
   */
  private long getDelayVal(){
	  long delay = 600000;//10分钟
	  try {
	      ParamConfig config = paramConfigService.getConfigByKey(ParamConfigKey.ALLINPAY_QUERY_DELAY);
	      if (config != null && config.getConfigValue() != null) {
	    	  delay = new Long(config.getConfigValue());
	      }
	  } catch (Exception e) {
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
	  }
      return delay;
  }
  /**
   * 交易结果查询  间隔时间(每五分钟)
   * @return
   */
  private long getPeriodVal(){
	  long period = 300000;//5分钟
	  try {
	      ParamConfig periodConfig = paramConfigService.getConfigByKey(ParamConfigKey.ALLINPAY_QUERY_PERIOD);
	      if (periodConfig != null && periodConfig.getConfigValue() != null) {
	    	  period = new Long(periodConfig.getConfigValue());
	      } 
	  } catch (Exception e) {
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
	  }
      return period;
  }
}
