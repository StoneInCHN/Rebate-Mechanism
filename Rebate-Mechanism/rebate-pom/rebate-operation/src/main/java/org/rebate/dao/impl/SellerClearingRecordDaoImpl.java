package org.rebate.dao.impl; 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;

import org.rebate.dao.SellerClearingRecordDao;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.json.beans.SellerClearingResult;
import org.springframework.stereotype.Repository;
@Repository("sellerClearingRecordDaoImpl")
public class SellerClearingRecordDaoImpl extends  BaseDaoImpl<SellerClearingRecord,Long> implements SellerClearingRecordDao {

	@Override
	public List<SellerClearingResult> findClearingResult() {
		String sql = "select date_format(a.payment_time, '%Y-%m-%d'),b.name,sum(a.amount),sum(a.seller_income),sum(a.amount-a.seller_income),count(*) from rm_order as a,rm_seller as b where a.status<>0 and a.is_clearing<>0 and a.seller=b.id group by date_format(a.payment_time, '%Y-%m-%d'),b.name order by date_format(a.payment_time, '%Y-%m-%d') desc";
		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<SellerClearingResult> resultList = new ArrayList<SellerClearingResult>();
	    for (Object object : list) {
	    	 Object[] entity = (Object[]) object;
	    	 SellerClearingResult result = new SellerClearingResult();
	    	 result.setPaymentDate((String)entity[0]);
	    	 result.setSellerName((String)entity[1]);
	    	 result.setAmount((BigDecimal)entity[2]);
	    	 result.setSellerIncome((BigDecimal)entity[3]);
	    	 result.setProfit((BigDecimal)entity[4]);
	    	 result.setCount(((BigInteger)entity[5]).intValue());
	    	 resultList.add(result);
	    }
		return resultList;
	}

}