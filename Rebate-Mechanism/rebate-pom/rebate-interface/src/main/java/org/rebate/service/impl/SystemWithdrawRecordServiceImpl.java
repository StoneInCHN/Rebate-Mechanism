package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.dao.SystemWithdrawRecordDao;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("systemWithdrawRecordServiceImpl")
public class SystemWithdrawRecordServiceImpl extends BaseServiceImpl<SystemWithdrawRecord,Long> implements SystemWithdrawRecordService {

      @Resource(name="systemWithdrawRecordDaoImpl")
      public void setBaseDao(SystemWithdrawRecordDao systemWithdrawRecordDao) {
         super.setBaseDao(systemWithdrawRecordDao);
  }
}