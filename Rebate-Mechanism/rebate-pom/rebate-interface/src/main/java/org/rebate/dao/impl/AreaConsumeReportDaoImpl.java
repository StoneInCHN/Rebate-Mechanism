package org.rebate.dao.impl; 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;

import org.rebate.dao.AreaConsumeReportDao;
import org.rebate.entity.AreaConsumeReport;
import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.AgentAreaRequest;
import org.rebate.json.response.AreaAmountResponse;
import org.rebate.json.response.AreaCountResponse;
import org.rebate.json.response.DiscountAmount;
import org.rebate.json.response.SalesManReport;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Repository;

@Repository("areaConsumeReportDaoImpl")
public class AreaConsumeReportDaoImpl extends  BaseDaoImpl<AreaConsumeReport,Long> implements AreaConsumeReportDao {

	@Override
	public ResponseMultiple<AreaAmountResponse> getConsumeAmountReport(AgentAreaRequest request,
			ResponseMultiple<AreaAmountResponse> response) {
		
		String areaStr = null;
		if (request.getAgencyLevel() == AgencyLevel.PROVINCE) areaStr = " province = ";
		if (request.getAgencyLevel() == AgencyLevel.CITY) areaStr = " city = ";
		if (request.getAgencyLevel() == AgencyLevel.COUNTY)  areaStr = " area = ";
		
		//按日期分页
		StringBuffer sqlDatePage = new StringBuffer();
		sqlDatePage.append("select SQL_CALC_FOUND_ROWS distinct report_date from rm_area_consume_report where");
		sqlDatePage.append(areaStr);
		sqlDatePage.append(request.getAreaId());
		if (request.getStartDate() != null) {
			sqlDatePage.append(" and report_date >= '");
			sqlDatePage.append(TimeUtils.getDateFormatString("yyyy-MM-dd", request.getStartDate()));
			sqlDatePage.append("'");
		}
		if (request.getEndDate() != null) {
			sqlDatePage.append(" and report_date <= '");
			sqlDatePage.append(TimeUtils.getDateFormatString("yyyy-MM-dd", request.getEndDate()));
			sqlDatePage.append("'");
		}
		sqlDatePage.append(" order by report_date desc");
		sqlDatePage.append(" limit ");
		sqlDatePage.append((request.getPageNumber() - 1) * request.getPageSize());
		sqlDatePage.append(",");
		sqlDatePage.append(request.getPageSize());
		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sqlDatePage.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<String> dateList = new ArrayList<String>();
	    for (Object object : list) {
	    	dateList.add(TimeUtils.getDateFormatString("yyyy-MM-dd", (Date) object));
	    } 
	    //总页数
		BigInteger total = (BigInteger)entityManager.createNativeQuery("select FOUND_ROWS()").setFlushMode(FlushModeType.COMMIT).getSingleResult();
		response.getPage().setTotal(total.intValue());
		
		//msg信息
		List<AreaAmountResponse> areaAmountResponses = new ArrayList<AreaAmountResponse>();

		if (dateList.size() > 0) {
			for (String date : dateList) {
				StringBuffer sqlDisAmount = new StringBuffer();
				sqlDisAmount.append("select total_amount, seller_discount");
				sqlDisAmount.append(" from rm_area_consume_report where 1 = 1");
				sqlDisAmount.append(" and agency_level = ");
				sqlDisAmount.append(request.getAgencyLevel().ordinal());
				sqlDisAmount.append(" and ");
				sqlDisAmount.append(areaStr);
				sqlDisAmount.append(request.getAreaId());
				sqlDisAmount.append(" and report_date = '");
				sqlDisAmount.append(date);
				sqlDisAmount.append("'");
				
				@SuppressWarnings("rawtypes")
				List discountlist = entityManager.createNativeQuery(sqlDisAmount.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
				List<DiscountAmount> discountAmounts = new ArrayList<DiscountAmount>();
				BigDecimal totalAmount = new BigDecimal(0);
			    for (Object object : discountlist) {
			    	Object[] entity = (Object[]) object;
			    	DiscountAmount discountAmount = new DiscountAmount();
			    	discountAmount.setAmount((BigDecimal)entity[0]);
			    	totalAmount = totalAmount.add(discountAmount.getAmount());
			    	discountAmount.setDiscount((BigDecimal)entity[1]);
			    	discountAmounts.add(discountAmount);
			    }  
				AreaAmountResponse areaAmount = new AreaAmountResponse();
				areaAmount.setAreaId(request.getAreaId());
				areaAmount.setDate(date);
				areaAmount.setTotalAmount(totalAmount);
				areaAmount.setDiscountAmounts(discountAmounts);
				areaAmountResponses.add(areaAmount);
			}
		}
		response.setMsg(areaAmountResponses);
		
		return response;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseMultiple<AreaCountResponse> getSellerCountReport(
			AgentAreaRequest request, ResponseMultiple<AreaCountResponse> response) {
		
//		String father = "";
//		String son = "";
//		if (request.getAgencyLevel() == AgencyLevel.PROVINCE){
//			father = "province";
//			son = "city";
//		}
//		if (request.getAgencyLevel() == AgencyLevel.CITY){
//			father = "city";
//			son = "area";
//		}
//		if (request.getAgencyLevel() == AgencyLevel.COUNTY){
//			father = "area";
//			son = "area";
//		}
//		StringBuffer sqlBuff = new StringBuffer();
//		sqlBuff.append(" select SQL_CALC_FOUND_ROWS count(*), seller.");
//		sqlBuff.append(son);
//		sqlBuff.append(", area.name  from rm_seller seller, rm_area area");
//		sqlBuff.append(" where seller.account_status = 0");
//		sqlBuff.append(" and seller.");
//		sqlBuff.append(son);
//		sqlBuff.append(" = area.id");
//		sqlBuff.append(" and seller.");
//		sqlBuff.append(father);
//		sqlBuff.append(" = ");
//		sqlBuff.append(request.getAreaId());
//		sqlBuff.append(" group by seller.");
//		sqlBuff.append(son);
//		sqlBuff.append(" order by count(*) desc ");
//		sqlBuff.append(" limit ");
//		sqlBuff.append((request.getPageNumber() - 1) * request.getPageSize());
//		sqlBuff.append(",");
//		sqlBuff.append(request.getPageSize());
		
		String sql = "";
		
		if (request.getAgencyLevel() == AgencyLevel.PROVINCE){
			sql = "select count(*), seller.city, area.name  from rm_seller seller, rm_area area where seller.account_status = 0 and seller.city = area.id and seller.province = %s group by seller.city order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.CITY){
			sql = "select count(*), seller.area, area.name  from rm_seller seller, rm_area area where seller.account_status = 0 and seller.area = area.id and seller.city = %s group by seller.area order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.COUNTY){
			sql = "select count(*), seller.area, area.name  from rm_seller seller, rm_area area where seller.account_status = 0 and seller.area = area.id and seller.area = %s group by seller.area order by count(*) desc limit %s, %s";
		}
		sql = String.format(sql, request.getAreaId(), (request.getPageNumber() - 1) * request.getPageSize(), request.getPageSize());
		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sql.toString()).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<AreaCountResponse> areaCountResponses = new ArrayList<AreaCountResponse>();
		Long totalCount = 0L;
	    for (Object object : list) {
	    	Object[] arrays = (Object[] )object;
	    	AreaCountResponse areaCountResponse = new AreaCountResponse();
	    	Long count = ((BigInteger)arrays[0]).longValue();
	    	totalCount += count;
	    	areaCountResponse.setCount(count);
	    	areaCountResponse.setId(((BigInteger)arrays[1]).longValue());
	    	areaCountResponse.setName((String)arrays[2]);
	    	areaCountResponses.add(areaCountResponse);
	    } 
	    
	    //Msg
	    response.setMsg(areaCountResponses);
	    //总页数
	    response.setDesc(totalCount.toString());
	    //总页数
		BigInteger total = (BigInteger)entityManager.createNativeQuery("select FOUND_ROWS()").setFlushMode(FlushModeType.COMMIT).getSingleResult();
		response.getPage().setTotal(total.intValue());
		
		return response;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseMultiple<AreaCountResponse> getEndUserCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse> response) {
		String sql = "";
		
		if (request.getAgencyLevel() == AgencyLevel.PROVINCE){
			sql = "select count(*), endUser.city, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.city = area.id and endUser.province = %s group by endUser.city order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.CITY){
			sql = "select count(*), endUser.area, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.area = area.id and endUser.city = %s group by endUser.area order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.COUNTY){
			sql = "select count(*), endUser.area, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.area = area.id and endUser.area = %s group by endUser.area order by count(*) desc limit %s, %s";
		}
		sql = String.format(sql, request.getAreaId(), (request.getPageNumber() - 1) * request.getPageSize(), request.getPageSize());

		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<AreaCountResponse> areaCountResponses = new ArrayList<AreaCountResponse>();
		Long totalCount = 0L;
	    for (Object object : list) {
	    	Object[] arrays = (Object[] )object;
	    	AreaCountResponse areaCountResponse = new AreaCountResponse();
	    	Long count = ((BigInteger)arrays[0]).longValue();
	    	totalCount += count;
	    	areaCountResponse.setCount(count);
	    	areaCountResponse.setId(((BigInteger)arrays[1]).longValue());
	    	areaCountResponse.setName((String)arrays[2]);
	    	areaCountResponses.add(areaCountResponse);
	    } 
	    //Msg
	    response.setMsg(areaCountResponses);
	    //总页数
	    response.setDesc(totalCount.toString());
	    //总页数
		BigInteger total = (BigInteger)entityManager.createNativeQuery("select FOUND_ROWS()").setFlushMode(FlushModeType.COMMIT).getSingleResult();
		response.getPage().setTotal(total.intValue());
		
		return response;
	}

	@Override
	public ResponseMultiple<AreaCountResponse<SalesManReport>> getSalesmanCountReport(
			AgentAreaRequest request,
			ResponseMultiple<AreaCountResponse<SalesManReport>> response) {
		String sql = "";
		
		if (request.getAgencyLevel() == AgencyLevel.PROVINCE){
			sql = "select count(*), endUser.city, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.is_salesman = 1 and endUser.city = area.id and endUser.province = %s group by endUser.city order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.CITY){
			sql = "select count(*), endUser.area, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.is_salesman = 1 and endUser.area = area.id and endUser.city = %s group by endUser.area order by count(*) desc limit %s, %s";
		}
		if (request.getAgencyLevel() == AgencyLevel.COUNTY){
			sql = "select count(*), endUser.area, area.name  from rm_end_user endUser, rm_area area where endUser.account_status = 0 and endUser.is_salesman = 1 and endUser.area = area.id and endUser.area = %s group by endUser.area order by count(*) desc limit %s, %s";
		}
		sql = String.format(sql, request.getAreaId(), (request.getPageNumber() - 1) * request.getPageSize(), request.getPageSize());

		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<AreaCountResponse<SalesManReport>> areaCountResponses = new ArrayList<AreaCountResponse<SalesManReport>>();
		Long totalCount = 0L;
	    for (Object object : list) {
	    	Object[] arrays = (Object[] )object;
	    	AreaCountResponse<SalesManReport> areaCountResponse = new AreaCountResponse<SalesManReport>();
	    	Long count = ((BigInteger)arrays[0]).longValue();
	    	totalCount += count;
	    	areaCountResponse.setCount(count);
	    	areaCountResponse.setId(((BigInteger)arrays[1]).longValue());
	    	areaCountResponse.setName((String)arrays[2]);
	    	if (request.getAgencyLevel() == AgencyLevel.COUNTY) {//区业务员，要显示详情
	    		String salesSql = "select id,user_name,nick_name,user_photo,cell_phone_num from rm_end_user where account_status = 0 and is_salesman = 1 and area = ";
	    		salesSql += request.getAreaId();
	    		@SuppressWarnings("rawtypes")
	    		List saleslist = entityManager.createNativeQuery(salesSql).setFlushMode(FlushModeType.COMMIT).getResultList();
	    		List<SalesManReport> salesManReports = new ArrayList<SalesManReport>();
	    	    for (Object sales : saleslist) {
	    	    	SalesManReport salesManReport = new SalesManReport();
	    	    	Object[] salesInfo = (Object[] )sales;
	    	    	salesManReport.setId(((BigInteger)salesInfo[0]).longValue());
	    	    	salesManReport.setUserName((String)salesInfo[1]);
	    	    	salesManReport.setNickName((String)salesInfo[2]);
	    	    	salesManReport.setUserPhoto((String)salesInfo[3]);
	    	    	salesManReport.setCellPhoneNum((String)salesInfo[4]);
	    	    	salesManReports.add(salesManReport);
	    	    }	
	    		areaCountResponse.setList(salesManReports);
			}
	    	areaCountResponses.add(areaCountResponse);
	    } 
	    //Msg
	    response.setMsg(areaCountResponses);
	    //总页数
	    response.setDesc(totalCount.toString());
	    //总页数
		BigInteger total = (BigInteger)entityManager.createNativeQuery("select FOUND_ROWS()").setFlushMode(FlushModeType.COMMIT).getSingleResult();
		response.getPage().setTotal(total.intValue());
		
		return response;
	}


}