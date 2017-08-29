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
import org.rebate.entity.ParamConfig;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capOrderQueryReq.OrderQueryReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 平台提现记录 交易查询
 *
 */
@Component("systemWithdrawSingleJob")
@Lazy(false)
public class SystemWithdrawSingleJob {
	
	  @Resource(name = "systemWithdrawRecordServiceImpl")
	  private SystemWithdrawRecordService systemWithdrawRecordService;
	  
	  @Resource(name="systemConfigServiceImpl") 
	  private SystemConfigService systemConfigService; 
	  
	  @Resource(name="paramConfigServiceImpl")
	  private ParamConfigService paramConfigService;
	  
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
										LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByAllinpay", "(更新平台提现记录)Catch Exception: %s", e.getMessage());
										e.printStackTrace();
									} finally{
										cancel();//结束
									}
								}
							}
					} catch (Exception e) {
						cancel();//结束
						LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByAllinpay", "Catch Exception: %s", e.getMessage());
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
						  //获取需要更新状态的平台提现记录
						  SystemWithdrawRecord record = findNeedClearingRecord(reqSn);
						  if (record == null || record.getAmount() == null) {
							  LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByJiuPai", "不能通过:%s找到货款记录，或者货款记录无金额", reqSn);
							  cancel();//结束
						  }
						  String amountPenny = getPennyStr(record.getAmount());//金额单位：分
						  OrderQueryReq req = new OrderQueryReq(reqSn, mcTransDateTime, orderNo, amountPenny);
					      GateWayService gateWayService = new GateWayService();
					      Map<String, String> resMap = gateWayService.capOrderQuery(req);
				          String rspCode = resMap.get("rspCode");//应答码  IPS00000正常返回
				          String rspMessage = resMap.get("rspMessage");//交易成功
				          String ordsts = resMap.get("ordsts");//订单状态  P处理中   S处理成功   F处理失败   R退汇   N待人工处理
				          String amount = resMap.get("amount");
				          LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByJiuPai", 
				        		  "rspCode:%s,rspMessage:%s,ordsts:%s,mcTransDateTime:%s,orderNo:%s,amountPenny:%s,amount:%s", 
				        		  rspCode,rspMessage,ordsts,mcTransDateTime,orderNo,amountPenny,amount);
				          if ("IPS00000".equals(rspCode)) {//正常返回
//				        	  //test
//							   	//测试环境 预下单 当成处理成功
//					        	if ("P".equals(ordsts) && orderNo.indexOf("800002308510001") >= 0) { 
//					        		ordsts = "S";
//					        	}
//				        	  //test
				        	  //有最终结果  处理成功 或者 处理失败
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
										LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByJiuPai", "(更新平台提现记录)Catch Exception: %s", e.getMessage());
										e.printStackTrace();
									} finally{
										cancel();//结束
									}
				        	  }
				          }
					} catch (Exception e) {
						cancel();//结束
						LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecordSingleByJiuPai", "Catch Exception: %s", e.getMessage());
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
			SystemWithdrawRecord record = findNeedClearingRecord(reqSn);
			if (record != null) {
				LogUtil.debug(SystemWithdrawSingleJob.class, "handleQueryTradeNew(Single)", qtdetail_err_msg +" for SellerClearingRecord(Id): %s", record.getId());
			    if ("0000".equals(qtdetail_ret_code)) {//处理成功
			    	updateRecord(record, ClearingStatus.SUCCESS, qtdetail_err_msg);
				}else {//处理失败
					updateRecord(record, ClearingStatus.FAILED, qtdetail_err_msg);
				}
			}
			LogUtil.debug(SystemWithdrawSingleJob.class, "handleQueryTradeNew(Single)", "req_sn: %s", reqSn);
			break;//单笔只有一单
	  }
  }

  /**
   * 更新商家货款记录等信息
   * @param record
   * @param status
   */
  private void updateRecord(SystemWithdrawRecord record, ClearingStatus status, String errMsg){
	  LogUtil.debug(SystemWithdrawSingleJob.class, "updateRecord", "Update SystemWithdrawRecord(Single)-->ClearingStatus to be: %s", status.toString());
	  record.setWithdrawMsg(record.getWithdrawMsg()+";"+errMsg);
	  if (status == ClearingStatus.SUCCESS) {
		  record.setStatus(ClearingStatus.SUCCESS);
          record.setIsWithdraw(true);
	  }else if (status == ClearingStatus.FAILED) {
		  record.setStatus(ClearingStatus.FAILED);
          record.setIsWithdraw(false);
	  }
	  systemWithdrawRecordService.update(record);
  }
  /**
   * 根据reqSn,获取需要更新状态的平台提现记录
   * @return
   */
  private SystemWithdrawRecord findNeedClearingRecord(String reqSn){
	  SystemWithdrawRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//单号
	  List<SystemWithdrawRecord> records = systemWithdrawRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(SystemWithdrawSingleJob.class, "findNeedClearingRecord(Single)", "Find more than one record by req_sn: %s", reqSn);
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
		  LogUtil.debug(SystemWithdrawSingleJob.class, "getDelayVal", "Catch Exception: %s", e.getMessage());
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
		  LogUtil.debug(SystemWithdrawSingleJob.class, "getDelayVal", "Catch Exception: %s", e.getMessage());
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
		  LogUtil.debug(SystemWithdrawSingleJob.class, "getPennyStr", "Catch Exception: %s", e.getMessage());
		  return null;
	  }
	  return pennyStr;
  }
}
