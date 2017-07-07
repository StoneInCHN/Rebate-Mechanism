package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.ThirdApiReport;
import org.rebate.dao.ThirdApiReportDao;
import org.rebate.service.ThirdApiReportService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("thirdApiReportServiceImpl")
public class ThirdApiReportServiceImpl extends BaseServiceImpl<ThirdApiReport,Long> implements ThirdApiReportService {

      @Resource(name="thirdApiReportDaoImpl")
      public void setBaseDao(ThirdApiReportDao thirdApiReportDao) {
         super.setBaseDao(thirdApiReportDao);
  }
}