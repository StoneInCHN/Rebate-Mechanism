package org.rebate.dao; 
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.rebate.entity.AreaConsumeReport;
import org.rebate.framework.dao.BaseDao;
import org.rebate.json.beans.AreaConsumeResult;

public interface AreaConsumeReportDao extends  BaseDao<AreaConsumeReport,Long>{

	List<AreaConsumeResult> getAreaConsumeResult(Date startDate, Date endDate);

	List<AreaConsumeReport> getCityProvinceReport(Set<Long> citeIds,
			Set<Long> provinceIds, Date reportDate);

}