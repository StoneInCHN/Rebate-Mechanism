package org.rebate.dao.impl; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository; 
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.utils.DateUtils;
import org.rebate.dao.LeScoreRecordDao;
@Repository("leScoreRecordDaoImpl")
public class LeScoreRecordDaoImpl extends  BaseDaoImpl<LeScoreRecord,Long> implements LeScoreRecordDao {

	@Override
	public List<String> jiuPaiProcessingBatchNoList(Date endDate) {
		int jiupaiOrdinal = PaymentChannel.JIUPAI.ordinal();
		String dateStr = DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", endDate);
		String sql = "select distinct req_sn from rm_le_score_record where status = 0 and is_withdraw = 0 and sn is not null and payment_channel = " + jiupaiOrdinal + " and create_date <= '" + dateStr + "'";
		@SuppressWarnings("rawtypes")
		List list = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<String> batchNoList = new ArrayList<String>();
	    for (Object object : list) {
	    	 String entity = (String) object;
	    	 batchNoList.add(entity);
	    }
		return batchNoList;
	}

}