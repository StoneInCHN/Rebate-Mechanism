package org.rebate.dao.impl; 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;

import org.rebate.dao.AreaConsumeReportDao;
import org.rebate.entity.AreaConsumeReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.json.beans.AreaConsumeResult;
import org.rebate.utils.DateUtils;
import org.springframework.stereotype.Repository;
@Repository("areaConsumeReportDaoImpl")
public class AreaConsumeReportDaoImpl extends  BaseDaoImpl<AreaConsumeReport,Long> implements AreaConsumeReportDao {

	@Override
	public List<AreaConsumeResult> getAreaConsumeResult(Date startDate,
			Date endDate) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select o.amount, (o.seller_income/o.amount)*10, seller.area from rm_order o,rm_seller seller");
		sqlBuffer.append(" where o.seller = seller.id and o.status <> 0 and o.is_saller_order = 0");
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

}