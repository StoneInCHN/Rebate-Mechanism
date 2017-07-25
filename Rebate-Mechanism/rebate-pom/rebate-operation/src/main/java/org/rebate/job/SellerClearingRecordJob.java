package org.rebate.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.EndUserService;
import org.rebate.service.OrderService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.SellerService;
import org.rebate.utils.DateUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户货款结算Job(T+1后结算)
 *
 */
@Component("sellerClearingRecordJob")
@Lazy(false)
public class SellerClearingRecordJob {

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

  // @Scheduled(cron="0 30 21 * * ?")
  @Scheduled(cron = "${job.daily_sellerClearing_cal.cron}")// 每天1点0分0秒执行 0 0 1 * * ?
  public void sellerClearingCalculate() {
    if (date == null) {
      date = new Date();
    }
    Date startDate = DateUtils.startOfDay(date, -1);//获取昨天的开始时间 00:00:00
    Date endDate = DateUtils.endOfDay(date, -1);//获取昨天的结束时间 23:59:59 999

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job Start! Time Period:"
        + startDate + " - " + endDate);

    try {
      //生成商家货款记录+批量代付
      String reqSn = sellerClearingRecordService.sellerClearing(startDate, endDate);
      //隔一段时间请求交易结果查询接口，去更新商家货款记录的状态
      if (reqSn != null) {
    	  TranxServiceImpl tranxService = new TranxServiceImpl();
    	  tranxService.init();//初始化通联基础数据
          Timer timer=new Timer();
          long delay = getDelayVal();//延迟10分钟后
          long period = getPeriodVal();//每5分钟执行一次
         
  		  TimerTask task = new TimerTask(){
  			public void run(){
  				  try {
  					    String xmlResponse = tranxService.queryTradeNew(reqSn, false);
  					    if (xmlResponse != null) {
  					    	//LogUtil.debug(this.getClass(), "sellerClearingCalculate", "queryTradeNew: %s", xmlResponse);
  					        Document doc = DocumentHelper.parseText(xmlResponse);
  					        Element root = doc.getRootElement();// AIPG
  					        Element infoElement = root.element("INFO");
  					        String ret_code = infoElement.elementText("RET_CODE");
  					        String req_sn = infoElement.elementText("REQ_SN");
  					        if ("0000".equals(ret_code)) {//处理完毕
  					        	@SuppressWarnings("unchecked")
								Iterator<Element> qtdetails = root.element("QTRANSRSP").elementIterator("QTDETAIL");
//  					        	while (qtdetails.hasNext()) {
//  					        		Element qtdetail = (Element) qtdetails.next();
//  					        		String sn = qtdetail.elementText("SN");
//  					        		String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
//  					        		String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
//  					        		SellerClearingRecord record = findNeedClearingRecord(reqSn, sn);
//  					        		if (record != null) {
//  			  					    	if ("0000".equals(qtdetail_ret_code) || "4000".equals(qtdetail_ret_code)) {//处理成功
//  			  					    		updateRecord(record, ClearingStatus.SUCCESS, qtdetail_err_msg);
//										}else {//处理失败
//											updateRecord(record, ClearingStatus.FAILED, qtdetail_err_msg);
//										}
//									}
//  					        		LogUtil.debug(this.getClass(), "sellerClearingCalculate", "req_sn: %s, sn: %s", req_sn, sn);
//  					            }
//  					        	cancel();//结束
  					        	try {
  					        		handleQueryTradeNew(qtdetails, reqSn);
								} catch (Exception e) {
									LogUtil.debug(this.getClass(), "handleQueryTradeNew", "Catch Exception: %s", e.getMessage());
									e.printStackTrace();
								} finally{
									cancel();//结束
								}
  							}
  						}
  				} catch (Exception e) {
  					cancel();//结束
  					LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Catch Exception: %s", e.getMessage());
  					e.printStackTrace();
  				}
  			}
  		};
  		timer.schedule(task, delay, period);//延迟10分钟后，每5分钟执行一次
	  }

    } catch (Exception e) {
      date = null;
      e.printStackTrace();
    }

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job End! Time Period:"
        + startDate + " - " + endDate);
    date = null;
  }
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void handleQueryTradeNew(Iterator<Element> qtdetails, String reqSn) throws Exception{
	while (qtdetails.hasNext()) {
		Element qtdetail = (Element) qtdetails.next();
		String sn = qtdetail.elementText("SN");
		String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
		String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
		SellerClearingRecord record = findNeedClearingRecord(reqSn, sn);
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
//  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) 
  private void updateRecord(SellerClearingRecord record, ClearingStatus status, String errMsg){
	  LogUtil.debug(this.getClass(), "updateRecord", "Update SellerClearingRecord-->ClearingStatus to be: %s", status.toString());
	  if (status == ClearingStatus.SUCCESS) {
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
      		
	  }else if (status == ClearingStatus.FAILED) {
		  	//将货款的结算状态改为处理失败即结算失败，是否结算改为false
	    	record.setClearingStatus(ClearingStatus.FAILED);
	    	record.setIsClearing(false);
	    	record.setRemark(errMsg);
	    	sellerClearingRecordService.update(record);
	  }
//  	  EndUser endUser = record.getEndUser();
//  	  if (endUser != null && endUser.getId() != null) {
//  		  EndUser sellerEndUser = endUserService.find(endUser.getId());//不明白为啥直接用record.getEndUser()要报错-->could not initialize proxy - no Session,不应该是懒加载的原因啊。。。。。
//  		  sellerEndUser.setIncomeLeScore(new BigDecimal(0));
//      	  endUserService.update(sellerEndUser);
//  	  }
  }
  /**
   * 根据reqSn和sn，获取需要更新结算状态的商家货款记录
   * @return
   */
  private SellerClearingRecord findNeedClearingRecord(String reqSn, String sn){
	  SellerClearingRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  filters.add(Filter.eq("sn", sn));//子单号
	  filters.add(Filter.eq("clearingStatus", ClearingStatus.PROCESSING));//处理中
	  filters.add(Filter.eq("isClearing", false));//未结算
	  List<SellerClearingRecord> records = sellerClearingRecordService.findList(null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(this.getClass(), "findNeedClearingRecord", "Find more than one record by req_sn: %s, sn: %s", reqSn, sn);
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
