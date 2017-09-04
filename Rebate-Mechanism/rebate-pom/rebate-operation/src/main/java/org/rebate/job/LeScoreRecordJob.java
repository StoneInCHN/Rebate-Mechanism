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
import org.rebate.beans.Setting;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.SpringUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capBatchQuery.BatchQueryReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 乐分提现 交易结果更新
 *
 */
@Component("leScoreRecordJob")
@Lazy(false)
public class LeScoreRecordJob {
	
	@Resource(name = "leScoreRecordServiceImpl")
	private LeScoreRecordService leScoreRecordService;

	@Resource(name = "endUserServiceImpl")
	private EndUserService endUserService;
	  
	@Resource(name = "bankCardServiceImpl")
	private BankCardService bankCardService;
	  
	@Resource(name="systemConfigServiceImpl") 
	private SystemConfigService systemConfigService; 
	  
	@Resource(name="paramConfigServiceImpl")
	private ParamConfigService paramConfigService;
	  
	/**
	 * [通联渠道]隔一段时间 异步更新 提现记录状态
	 * @param reqSn
	 */
    public void notifyWithdrawRecordByAllinpay(String reqSn){

      Timer timer=new Timer();
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
									LogUtil.debug(this.getClass(), "batchWithdrawal(handleQueryTradeNew)", "Catch Exception: %s", e.getMessage());
									e.printStackTrace();
								} finally{
									cancel();//结束
								}
							}
						}
				} catch (Exception e) {
					cancel();//结束
					LogUtil.debug(this.getClass(), "batchWithdrawal", "Catch Exception: %s", e.getMessage());
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, delay, period);//延迟20分钟后，每5分钟执行一次
  
  }
  /**
   * [九派渠道]隔一段时间 异步更新 提现记录状态
   * @param reqSn
   */
  public void notifyWithdrawRecordByJiuPai(String reqSn) {
      Timer timer=new Timer();
      long delay = getDelayVal();//延迟10分钟后
      long period = getPeriodVal();//每5分钟执行一次
     
		  TimerTask task = new TimerTask(){
			public void run(){
				  try {
					  GateWayService gateWayService = new GateWayService();
					  BatchQueryReq req = new BatchQueryReq();
					  req.setBatchNo(reqSn);//交易批次号
					  req.setPageNum(1);
					  req.setPageSize(200);
					  //代收付批量查询
					  Map<String, String> resMap = gateWayService.capBatchQuery(req);
					  if (resMap.get("tamtCapQueryList") != null) {
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
									  
				  					  LogUtil.debug(this.getClass(), "notifyWithdrawRecordByJiuPai", "merchantId: %s, batchNo: %s, mercOrdNo: %s,ordSts: %s, tamTxTyp: %s",
				  							merchantId, resBatchNo, mercOrdNo, ordSts, tamTxTyp);
									  if (!mercOrdNo.startsWith(merchantId)) {
										  LogUtil.debug(this.getClass(), "notifyWithdrawRecordByJiuPai", "merchantId + withDrawSn != 商户订单号(mercOrdNo)");
										  continue; 
									  }
									  String withDrawSn = mercOrdNo.replace(setting.getJiupaiMerchantId(), "");
									  LeScoreRecord record = findNeedLeScoreRecord(resBatchNo, null, withDrawSn);
									   if (record != null && ("S".equals(ordSts) || "处理成功".equals(ordSts) 
											   || "N".equals(ordSts) || "处理失败".equals(ordSts))) {//即有最终结果
								        	try {
												String msg = tamTxTyp + ordSts;
												if ("S".equals(ordSts) || "处理成功".equals(ordSts)) {//处理成功
													updateRecord(record, ClearingStatus.SUCCESS, msg);
												}else if ("N".equals(ordSts) || "处理失败".equals(ordSts)){//处理失败
													updateRecord(record, ClearingStatus.FAILED, msg);
												}
											} catch (Exception e) {
												LogUtil.debug(this.getClass(), "notifyWithdrawRecordByJiuPai", "(updateRecord)Catch Exception: %s", e.getMessage());
												e.printStackTrace();
											} finally{
												cancel();//结束
											}
									   }
									   LogUtil.debug(this.getClass(), "notifyWithdrawRecordByJiuPai", "batchNo: %s, mercOrdNo: %s", resBatchNo, mercOrdNo);
								  }
							  }
					   }
				  
				} catch (Exception e) {
					cancel();//结束
					LogUtil.debug(this.getClass(), "batchWithdrawal", "Catch Exception: %s", e.getMessage());
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, delay, period);//延迟20分钟后，每5分钟执行一次
    	
  }

  /**
   * 根据reqSn,sn,withDrawSn 获取需要更新提现状态的乐分提现记录
   * @return
   */
  private LeScoreRecord findNeedLeScoreRecord(String reqSn, String sn, String withDrawSn){
	  LogUtil.debug(this.getClass(), "findNeedLeScoreRecord", "查询条件 reqSn: %s, sn: %s, withDrawSn: %s", reqSn, sn, withDrawSn);
	  LeScoreRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  if (sn != null) {
		  filters.add(Filter.eq("sn", sn));//子单号
	  }
	  if (withDrawSn != null) {
		  filters.add(Filter.eq("withDrawSn", withDrawSn));//提现流水号
	  }
	  filters.add(Filter.eq("status", ClearingStatus.PROCESSING));//处理中
	  filters.add(Filter.eq("isWithdraw", false));//未提现
	  List<LeScoreRecord> records = leScoreRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(this.getClass(), "findNeedLeScoreRecord", "Find more than one record by req_sn: %s, sn: %s", reqSn, sn);
		  }
		  record = records.get(0);
	  }else {
		  LogUtil.debug(this.getClass(), "findNeedLeScoreRecord", "未找到乐分提现记录");
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
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
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
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
		  period = 300000;//5分钟
	  }
      return period;
  }
  /**
   * 处理通联已经处理完成(0000)了的交易结果
   * @param qtdetails
   * @param reqSn
   * @throws Exception
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void handleQueryTradeNew(Iterator<Element> qtdetails, String reqSn) throws Exception{
	    List<LeScoreRecord> mergeList = new ArrayList<LeScoreRecord>();
	  	while (qtdetails.hasNext()) {
			Element qtdetail = (Element) qtdetails.next();
			String sn = qtdetail.elementText("SN");
			String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
			String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
			//根据reqSn和sn，获取需要更新提现状态的乐分提现记录
			LeScoreRecord record = findNeedLeScoreRecord(reqSn, sn, null);
			if (record != null) {
				LogUtil.debug(this.getClass(), "handleQueryTradeNew","[%s:%s] 乐分提现记录Id: %s", qtdetail_ret_code, qtdetail_err_msg, record.getId());
		    	if ("0000".equals(qtdetail_ret_code) || "4000".equals(qtdetail_ret_code)) {//处理成功
		    		LeScoreRecord successRecord = updateRecord(record, ClearingStatus.SUCCESS, qtdetail_err_msg);
		    		mergeList.add(successRecord);
		    	}else {//处理失败
		    		LeScoreRecord failedRecord = updateRecord(record, ClearingStatus.FAILED, qtdetail_err_msg);
		    		mergeList.add(failedRecord);
		    	}
			}
			LogUtil.debug(this.getClass(), "handleQueryTradeNew", "req_sn: %s, sn: %s", reqSn, sn);
	  }
	  if (mergeList != null) {
	  	 leScoreRecordService.update(mergeList);
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
	  }else if (status == ClearingStatus.FAILED) {
	  		record.setIsWithdraw(false);
	  		record.setStatus(ClearingStatus.FAILED);
	  		record.setWithdrawMsg(msg);//提现返回消息
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

}
