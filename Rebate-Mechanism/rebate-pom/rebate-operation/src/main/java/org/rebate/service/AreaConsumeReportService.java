package org.rebate.service; 

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.rebate.entity.AreaConsumeReport;
import org.rebate.framework.service.BaseService;
import org.rebate.json.beans.AreaConsumeResult;

public interface AreaConsumeReportService extends BaseService<AreaConsumeReport,Long>{

	List<AreaConsumeResult> getAreaConsumeResult(Date startDate, Date endDate);

	List<AreaConsumeReport> getCityProvinceReport(Set<Long> citeIds,
			Set<Long> provinceIds, Date reportDate);

}