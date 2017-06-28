package org.rebate.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.rebate.entity.Agent;
import org.rebate.entity.Area;
import org.rebate.entity.AreaConsumeReport;
import org.rebate.entity.Seller;
import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;
import org.rebate.framework.filter.Filter;
import org.rebate.json.beans.AreaConsumeResult;
import org.rebate.service.AgentService;
import org.rebate.service.AreaConsumeReportService;
import org.rebate.service.AreaService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.utils.DateUtils;
import org.rebate.utils.LogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 每日地区消费/交易额统计
 *
 */
@Component("areaConsumeReportJob")
@Lazy(false)
public class AreaConsumeReportJob {
	
	  public static Date date;
	  
	  @Resource(name = "areaConsumeReportServiceImpl")
	  private AreaConsumeReportService areaConsumeReportService;
	  
	  @Resource(name = "areaServiceImpl")
	  private AreaService areaService;	  
	  
	  
	  //@Scheduled(cron="0 30 21 * * ?")
	  @Scheduled(cron = "${job.daily_areaConsume_statistics.cron}")// 每天1点0分0秒执行 0 0 3 * * ?
	  public void areaConsumeReport() {
		if (date == null) {
		    date = new Date();
		}
	    Date startDate = DateUtils.startOfDay(date, -1);//获取昨天开始时间 00:00:00
	    Date endDate = DateUtils.endOfDay(date, -1);//获取昨天的结束时间 23:59:59 999

	    LogUtil.debug(this.getClass(), "areaConsumeReport", "AreaConsumeReport Job Start! Time Period:"
	        + startDate + " - " + endDate);
	    
	    Map<String, BigDecimal> mapAmount = new HashMap<String, BigDecimal>();
	    List<AreaConsumeResult> results = areaConsumeReportService.getAreaConsumeResult(startDate, endDate);
	    for (int i = 0; i < results.size(); i++) {
	    	AreaConsumeResult result = results.get(i);
	    	String key = result.getAreaID() + "_" + result.getSellerDiscount();
	    	if (!mapAmount.containsKey(key)) {
	    		mapAmount.put(key, result.getAmount());
			}else {
				BigDecimal existAmount = mapAmount.get(key);
				mapAmount.put(key, existAmount.add(result.getAmount()));
			}
		}
	    List<AreaConsumeReport> reports = new ArrayList<AreaConsumeReport>();
	    if (!mapAmount.isEmpty()) {
	    	for (Map.Entry<String, BigDecimal> entry : mapAmount.entrySet()) {
  				String key = entry.getKey();
  				String[] keyArray = key.split("_");
  				Area area = areaService.find(new Long(keyArray[0]));
  				int level = area.getTreePath().split(Area.TREE_PATH_SEPARATOR).length;
  				boolean exist = false;
  				if (level == 3) {//县区
  					exist = areaConsumeReportService.exists(Filter.eq("area", area), 
  	  						Filter.eq("reportDate", startDate));
  				}
  				if (level == 2) {//市
  					exist = areaConsumeReportService.exists(Filter.eq("city", area), 
  	  						Filter.eq("reportDate", startDate));
				}  				
  				if (level == 1) {//省
  					exist = areaConsumeReportService.exists(Filter.eq("province", area), 
  	  						Filter.eq("reportDate", startDate));
				}
  				if (exist) {
					continue;
				}
  				AreaConsumeReport report = new AreaConsumeReport();
  				if (level == 3) {//县区
  					report.setArea(area);
  	  				if (area.getParent() != null) {
  						report.setCity(area.getParent());
  						if (area.getParent().getParent() != null) {
  							report.setProvince(area.getParent().getParent());
  						}
  					}
				}
  				if (level == 2) {//市
  					report.setCity(area);
  					if (area.getParent() != null) {
  						report.setProvince(area.getParent());
  					}
				}  				
  				if (level == 1) {//省
  					report.setProvince(area);
				}   				
  				report.setSellerDiscount(new BigDecimal(keyArray[1]));
  				report.setReportDate(startDate);
  				report.setTotalAmount(entry.getValue());
  				reports.add(report);
	    	}
		}
	    if (reports.size() > 0) {
	    	areaConsumeReportService.save(reports);
		}
	    LogUtil.debug(this.getClass(), "areaConsumeReport", "AreaConsumeReport Job End! Time Period:"
		        + startDate + " - " + endDate);
	  }
}
