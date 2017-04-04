package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.NationBonusReport;
import org.rebate.dao.NationBonusReportDao;
import org.rebate.service.NationBonusReportService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("nationBonusReportServiceImpl")
public class NationBonusReportServiceImpl extends BaseServiceImpl<NationBonusReport,Long> implements NationBonusReportService {

      @Resource(name="nationBonusReportDaoImpl")
      public void setBaseDao(NationBonusReportDao nationBonusReportDao) {
         super.setBaseDao(nationBonusReportDao);
  }
}