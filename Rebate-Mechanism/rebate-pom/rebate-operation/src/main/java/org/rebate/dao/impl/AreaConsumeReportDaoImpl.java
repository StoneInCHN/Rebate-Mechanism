package org.rebate.dao.impl; 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;

import org.rebate.dao.AreaConsumeReportDao;
import org.rebate.dao.AreaDao;
import org.rebate.entity.Area;
import org.rebate.entity.AreaConsumeReport;
import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.json.beans.AreaConsumeResult;
import org.rebate.utils.DateUtils;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Repository;
@Repository("areaConsumeReportDaoImpl")
public class AreaConsumeReportDaoImpl extends  BaseDaoImpl<AreaConsumeReport,Long> implements AreaConsumeReportDao {

	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;	 	
	
	@Override
	public List<AreaConsumeResult> getAreaConsumeResult(Date startDate,
			Date endDate) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select o.amount, (o.seller_income/o.amount)*10, seller.area from rm_order o,rm_seller seller");
		//sqlBuffer.append(" where o.seller = seller.id and o.status <> 0 and o.is_saller_order = 0");
		sqlBuffer.append(" where o.seller = seller.id and o.status <> 0");//包含普通订单和录单
		sqlBuffer.append(" and o.create_date > '");
		sqlBuffer.append(DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", startDate));
		sqlBuffer.append("'");
		sqlBuffer.append(" and o.create_date < '");
		sqlBuffer.append(DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", endDate));
		sqlBuffer.append("'");
		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sqlBuffer.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<AreaConsumeResult> resultList = new ArrayList<AreaConsumeResult>();
	    for (Object object : list) {
	    	 Object[] entity = (Object[]) object;
	    	 AreaConsumeResult result = new AreaConsumeResult();
	    	 result.setAmount((BigDecimal)entity[0]);
	    	 result.setSellerDiscount((BigDecimal)entity[1]);
	    	 result.setAreaID(((BigInteger)entity[2]).longValue());
	    	 resultList.add(result);
	    }
		return resultList;
	}

	@Override
	public List<AreaConsumeReport> getCityProvinceReport(Set<Long> citeIds,
			Set<Long> provinceIds, Date reportDate) {
		List<AreaConsumeReport> reports = new ArrayList<AreaConsumeReport>();
		String dateStr = TimeUtils.getDateFormatString("yyyy-MM-dd", reportDate);
		for (Long citeId : citeIds) {
			Area city = areaDao.find(citeId);
			StringBuffer citySql = new StringBuffer();
			citySql.append("select sum(total_amount), seller_discount");
			citySql.append(" from rm_area_consume_report where agency_level = 2 and city = ");
			citySql.append(citeId);
			citySql.append(" and report_date = '");
			citySql.append(dateStr);
			citySql.append("'");
			citySql.append(" group by seller_discount");
			@SuppressWarnings("rawtypes")
			List cityReportlist = entityManager.createNativeQuery(citySql.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
		    for (Object object : cityReportlist) {
		    	Object[] entity = (Object[]) object;
		    	AreaConsumeReport cityReport = new AreaConsumeReport();
		    	cityReport.setAgencyLevel(AgencyLevel.CITY);
		    	cityReport.setCity(city);
		    	cityReport.setProvince(city.getParent());
		    	cityReport.setReportDate(reportDate);
		    	cityReport.setTotalAmount((BigDecimal)entity[0]);
		    	cityReport.setSellerDiscount((BigDecimal)entity[1]);
		    	reports.add(cityReport);
		    } 
		}
		for (Long provinceId : provinceIds) {
			Area province = areaDao.find(provinceId);
			StringBuffer provinceSql = new StringBuffer();
			provinceSql.append("select sum(total_amount), seller_discount");
			provinceSql.append(" from rm_area_consume_report where agency_level = 2 and province = ");
			provinceSql.append(provinceId);
			provinceSql.append(" and report_date = '");
			provinceSql.append(dateStr);
			provinceSql.append("'");
			provinceSql.append(" group by seller_discount");
			@SuppressWarnings("rawtypes")
			List cityReportlist = entityManager.createNativeQuery(provinceSql.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
		    for (Object object : cityReportlist) {
		    	Object[] entity = (Object[]) object;
		    	AreaConsumeReport provinceReport = new AreaConsumeReport();
		    	provinceReport.setAgencyLevel(AgencyLevel.PROVINCE);
		    	provinceReport.setProvince(province);
		    	provinceReport.setReportDate(reportDate);
		    	provinceReport.setTotalAmount((BigDecimal)entity[0]);
		    	provinceReport.setSellerDiscount((BigDecimal)entity[1]);
		    	reports.add(provinceReport);
		    } 
		}
		return reports;
	}

}