package org.rebate.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
	  
	  public void updateRecordStatus(String reqSn){

      Timer timer=new Timer();
      long delay = getDelayVal();//延迟10分钟后
      long period = getPeriodVal();//每5分钟执行一次
     
		  TimerTask task = new TimerTask(){
			public void run(){
				  try {
					    TranxServiceImpl tranxService = new TranxServiceImpl();
					    tranxService.init();//初始化通联基础数据
					    String xmlResponse = tranxService.queryTradeNew(reqSn, false);
					    if (xmlResponse != null) {
					    	//LogUtil.debug(this.getClass(), "sellerClearingCalculate", "queryTradeNew: %s", xmlResponse);
					        Document doc = DocumentHelper.parseText(xmlResponse);
					        Element root = doc.getRootElement();// AIPG
					        Element infoElement = root.element("INFO");
					        String ret_code = infoElement.elementText("RET_CODE");
					        String req_sn = infoElement.elementText("REQ_SN");
					        if ("0000".equals(ret_code)) {//处理完毕
					        List<LeScoreRecord> mergeList = new ArrayList<LeScoreRecord>();
					        @SuppressWarnings("unchecked")
							Iterator<Element> qtdetails = root.element("QTRANSRSP").elementIterator("QTDETAIL");
					        	while (qtdetails.hasNext()) {
					        		Element qtdetail = (Element) qtdetails.next();
					        		String sn = qtdetail.elementText("SN");
					        		String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
					        		String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
					        		LeScoreRecord record = findNeedLeScoreRecord(reqSn, sn);
					        		if (record != null) {
			  					    	if ("0000".equals(qtdetail_ret_code) || "4000".equals(qtdetail_ret_code)) {//处理成功
			  					    		record.setIsWithdraw(true);
  			  					    	record.setWithdrawMsg(qtdetail_err_msg);//提现返回消息
  			  					    	mergeList.add(record);
			  					    	}else {//处理失败
			  					    		LogUtil.debug(this.getClass(), "batchWithdrawal", qtdetail_err_msg +" for LeScoreRecord(Id): %s", record.getId());
			  					    		record.setIsWithdraw(false);
  			  					    	record.setWithdrawMsg(qtdetail_err_msg);//提现返回消息
  			  					    	mergeList.add(record);
			  					    		// 提现失败 把 乐分还回到用户手中
			  					    		EndUser endUser = null;
			  					    		if (record.getEndUser().getId() != null) {
			  					    			endUser = endUserService.find(record.getEndUser().getId());
											}
			  					    		if (endUser != null) {
  			  					    		// 当前乐分
  			  					    		BigDecimal curLeScore = record.getAmount();
  			  					    		// 激励乐分(包括乐心分红乐分，推荐获得乐分)
  			  					    		BigDecimal motivateLeScore = record.getMotivateLeScore();
  			  					    		// 商家直接收益乐分
  			  					    		// BigDecimal incomeLeScore = record.getIncomeLeScore();
  			  					    		// 代理商提成乐分
  			  					    		BigDecimal agentLeScore = record.getAgentLeScore();
  			  					    		if (endUser.getCurLeScore() != null) {
  			  					    			endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore));
												}
  			  					    		if (endUser.getMotivateLeScore() != null) {
  			  					    			endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore));
												}
  			  					    		if (endUser.getAgentLeScore() != null) {
  			  					    			endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore));
												}
  			  					    		// endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore));
  			  					    		endUserService.update(endUser);
											}
			  					    	}
					        		}
					        		LogUtil.debug(this.getClass(), "batchWithdrawal", "req_sn: %s, sn: %s", req_sn, sn);
					            }
					        	leScoreRecordService.update(mergeList);
					        	cancel();//结束
							}
						}
				} catch (Exception e) {
					LogUtil.debug(this.getClass(), "batchWithdrawal", "Catch Exception: %s", e.getMessage());
				}
			}
		};
		timer.schedule(task, delay, period);//延迟20分钟后，每5分钟执行一次
  
  }

  /**
   * 根据reqSn和sn，获取需要更新提现状态的乐分提现记录
   * @return
   */
  private LeScoreRecord findNeedLeScoreRecord(String reqSn, String sn){
	  LeScoreRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  filters.add(Filter.eq("sn", sn));//子单号
	  //filters.add(Filter.eq("isWithdraw", false));//未结算
	  List<LeScoreRecord> records = leScoreRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(this.getClass(), "findNeedLeScoreRecord", "Find more than one record by req_sn: %s, sn: %s", reqSn, sn);
		  }
		  record = records.get(0);
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
