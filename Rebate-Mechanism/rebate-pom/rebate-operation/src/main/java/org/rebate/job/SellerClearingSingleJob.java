package org.rebate.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.Order;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.OrderService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capOrderQueryReq.OrderQueryReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商家货款单笔提现 交易查询
 *
 */
@Component("sellerClearingSingleJob")
@Lazy(false)
public class SellerClearingSingleJob {
	
	  @Resource(name = "sellerClearingRecordServiceImpl")
	  private SellerClearingRecordService sellerClearingRecordService;
	  
	  @Resource(name="orderServiceImpl")
	  private OrderService orderService;
	  
	  @Resource(name="systemConfigServiceImpl") 
	  private SystemConfigService systemConfigService; 
	  
	  @Resource(name="paramConfigServiceImpl")
	  private ParamConfigService paramConfigService;
	  
	  @Resource(name="sellerServiceImpl")
	  private SellerService sellerService; 
	  
	  @Resource(name="clearingOrderRelationServiceImpl")
	  private ClearingOrderRelationService clearingOrderRelationService;
	  /**
	   * (通联渠道)更新单笔代付
	   * @param reqSn
	   */
	  public void updateRecordSingleByAllinpay(String reqSn){

	      Timer timer = new Timer();
	      long delay = getDelayVal();//延迟10分钟后
	      long period = getPeriodVal();//每5分钟执行一次
		  TimerTask task = new TimerTask(){
				public void run(){
					  try {
						    TranxServiceImpl tranxService = new TranxServiceImpl();
						    String xmlResponse = tranxService.queryTradeNew(reqSn, false);
						    if (xmlResponse != null) {
						        Document doc = DocumentHelper.parseText(xmlResponse);
						        Element root = doc.getRootElement();// AIPG
						        Element infoElement = root.element("INFO");
						        String ret_code = infoElement.elementText("RET_CODE");
						        if ("0000".equals(ret_code)) {//处理完毕
						        	@SuppressWarnings("unchecked")
						        	Iterator<Element> qtdetails = root.element("QTRANSRSP").elementIterator("QTDETAIL");
						        	try {
						        		handleQueryTradeNew(qtdetails, reqSn);
									} catch (Exception e) {
										LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByAllinpay", "(更新货款结算记录)Catch Exception: %s", e.getMessage());
										e.printStackTrace();
									} finally{
										cancel();//结束
									}
								}
							}
					} catch (Exception e) {
						cancel();//结束
						LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByAllinpay", "Catch Exception: %s", e.getMessage());
						e.printStackTrace();
					}
				}
		  };
		  timer.schedule(task, delay, period);//延迟10分钟后，每5分钟执行一次
  
	  }
	  /**
	   * (九派渠道)更新单笔代付
	   * @param reqSn 商户交易流水   mcTransDateTime 商户交易时间  orderNo 原交易订单号
	   */
	  public void updateRecordSingleByJiuPai(String reqSn, String mcTransDateTime, String orderNo){

	      Timer timer = new Timer();
	      long delay = getDelayVal();//延迟10分钟后
	      long period = getPeriodVal();//每5分钟执行一次
		  TimerTask task = new TimerTask(){
				public void run(){
					  try {
						  //根据reqSn，获取需要更新状态的实时货款提现(单笔)
						  SellerClearingRecord record = findNeedClearingRecord(reqSn);
						  if (record == null || record.getAmount() == null) {
							  LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByJiuPai", "不能通过:%s找到处理中货款记录，或者货款记录无金额", reqSn);
							  cancel();//结束
						  }
						  String amountPenny = getPennyStr(record.getAmount());//金额单位：分
						  OrderQueryReq req = new OrderQueryReq(reqSn, mcTransDateTime, orderNo, amountPenny);
					      GateWayService gateWayService = new GateWayService();
					      Map<String, String> resMap = gateWayService.capOrderQuery(req);
				          String rspCode = resMap.get("rspCode");//应答码  IPS00000正常返回
				          String rspMessage = resMap.get("rspMessage");//交易成功
				          String ordsts = resMap.get("ordsts");//订单状态  P处理中   S处理成功   F处理失败   R退汇   N待人工处理
				          LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByJiuPai", 
				        		  "rspCode:%s,rspMessage:%s,ordsts:%s,mcTransDateTime:%s,orderNo:%s,amountPenny:%s", 
				        		  rspCode,rspMessage,ordsts,mcTransDateTime,orderNo,amountPenny);
				          if ("IPS00000".equals(rspCode)) {//正常返回
				        	  //有最终结果  处理成功 或者 处理失败
//				        	  //test
//				        	  if ("P".equals(ordsts) && orderNo.indexOf("800002308510001") >= 0) { 
//				        		  ordsts = "S";
//				        	  }
//				        	  //test
				        	  if ("S".equals(ordsts) || "F".equals(ordsts) || "R".equals(ordsts)) { 
						        	try {
							        	  if ("S".equals(ordsts)) {//处理成功
							        		  updateRecord(record, ClearingStatus.SUCCESS, rspMessage);
										  }
							        	  if ("F".equals(ordsts) || "R".equals(ordsts)) {//处理失败 
							        		  String errMsg = rspMessage;
							        		  if ("F".equals(ordsts)) errMsg = "处理失败";
							        		  if ("R".equals(ordsts)) errMsg = "退汇";
							        		  updateRecord(record, ClearingStatus.FAILED, errMsg);
										  }
									} catch (Exception e) {
										LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByJiuPai", "(更新货款结算记录)Catch Exception: %s", e.getMessage());
										e.printStackTrace();
									} finally{
										cancel();//结束
									}
				        	  }
				          }
					} catch (Exception e) {
						cancel();//结束
						LogUtil.debug(SellerClearingSingleJob.class, "updateRecordSingleByJiuPai", "Catch Exception: %s", e.getMessage());
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, delay, period);//延迟10分钟后，每5分钟执行一次
	  }
	  

  /**
   * 处理通联已经处理完成(0000)了的交易结果
   * @param qtdetails
   * @param reqSn
   * @throws Exception
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void handleQueryTradeNew(Iterator<Element> qtdetails, String reqSn) throws Exception{
	  	while (qtdetails.hasNext()) {
			Element qtdetail = (Element) qtdetails.next();
			String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
			String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
			//根据reqSn，获取需要更新状态的实时货款提现(单笔)
			SellerClearingRecord record = findNeedClearingRecord(reqSn);
			if (record != null) {
				LogUtil.debug(SellerClearingSingleJob.class, "handleQueryTradeNew(Single)", qtdetail_err_msg +" for SellerClearingRecord(Id): %s", record.getId());
			    if ("0000".equals(qtdetail_ret_code)) {//处理成功
			    	updateRecord(record, ClearingStatus.SUCCESS, qtdetail_err_msg);
				}else {//处理失败
					updateRecord(record, ClearingStatus.FAILED, qtdetail_err_msg);
				}
			}
			LogUtil.debug(SellerClearingSingleJob.class, "handleQueryTradeNew(Single)", "req_sn: %s", reqSn);
			break;//单笔只有一单
	  }
  }

  /**
   * 更新商家货款记录等信息
   * @param record
   * @param status
   */
  private void updateRecord(SellerClearingRecord record, ClearingStatus status, String errMsg){
	  LogUtil.debug(SellerClearingSingleJob.class, "updateRecord", "Update SellerClearingRecord(Single)-->ClearingStatus to be: %s", status.toString());
	  if (status == ClearingStatus.SUCCESS) {
		  	//将货款的结算状态改为处理成功即结算成功，是否结算改为true
	    	record.setClearingStatus(ClearingStatus.SUCCESS);
	    	record.setIsClearing(true);
	    	record.setRemark(record.getRemark() + ";最终结果:" + errMsg);
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
					order.setIsClearing(true);
					orders.add(order);
				}
			}
      		orderService.update(orders);
      		
	  }else if (status == ClearingStatus.FAILED) {
		  	//将货款的结算状态改为处理失败即结算失败，是否结算改为false
	    	record.setClearingStatus(ClearingStatus.FAILED);
	    	record.setIsClearing(false);
	    	record.setRemark(record.getRemark() + ";" + errMsg);
	    	sellerClearingRecordService.update(record);
	  }
  }
  /**
   * 根据reqSn，获取需要更新状态的实时货款提现(单笔)
   * @return
   */
  private SellerClearingRecord findNeedClearingRecord(String reqSn){
	  SellerClearingRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//单号
	  filters.add(Filter.eq("clearingStatus", ClearingStatus.PROCESSING));
	  filters.add(Filter.eq("isClearing", false));//未结算
	  List<SellerClearingRecord> records = sellerClearingRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(SellerClearingSingleJob.class, "findNeedClearingRecord(Single)", "Find more than one record by req_sn: %s", reqSn);
		  }
		  record = records.get(0);
	  }
	  return record;
  }
  /**
   * 交易结果查询  延迟查询时间(延迟10分钟)
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
		  LogUtil.debug(SellerClearingSingleJob.class, "getDelayVal", "Catch Exception: %s", e.getMessage());
		  delay = 600000;//10分钟
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
		  LogUtil.debug(SellerClearingSingleJob.class, "getDelayVal", "Catch Exception: %s", e.getMessage());
		  period = 300000;//5分钟
	  }
      return period;
  }
  /**
   * 获取金额字符串，单位取分
   * @param amount
   * @return
   */
  private String getPennyStr(BigDecimal amount){
	  String pennyStr = null;
	  if (amount == null) {
		return pennyStr;
	  }
	  try {
		pennyStr = amount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
	  } catch (Exception e) {
		  LogUtil.debug(SellerClearingSingleJob.class, "getPennyStr", "Catch Exception: %s", e.getMessage());
		  return null;
	  }
	  return pennyStr;
  }
}
