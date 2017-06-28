package org.rebate.service.impl; 

import java.util.Date;
import java.util.List;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.rebate.entity.AreaConsumeReport;
import org.rebate.dao.AreaConsumeReportDao;
import org.rebate.service.AreaConsumeReportService;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.beans.AreaConsumeResult;

@Service("areaConsumeReportServiceImpl")
public class AreaConsumeReportServiceImpl extends BaseServiceImpl<AreaConsumeReport,Long> implements AreaConsumeReportService {
	
	@Resource(name="areaConsumeReportDaoImpl")
	private AreaConsumeReportDao areaConsumeReportDao;
	
    @Resource(name="areaConsumeReportDaoImpl")
    public void setBaseDao(AreaConsumeReportDao areaConsumeReportDao) {
        super.setBaseDao(areaConsumeReportDao);
    }

	@Override
	public List<AreaConsumeResult> getAreaConsumeResult(Date startDate,
			Date endDate) {
		return areaConsumeReportDao.getAreaConsumeResult(startDate, endDate);
	}
}