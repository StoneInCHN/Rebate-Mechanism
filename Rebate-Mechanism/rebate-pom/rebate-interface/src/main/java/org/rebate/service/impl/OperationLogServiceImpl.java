package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.OperationLog;
import org.rebate.dao.OperationLogDao;
import org.rebate.service.OperationLogService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("operationLogServiceImpl")
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLog,Long> implements OperationLogService {

      @Resource(name="operationLogDaoImpl")
      public void setBaseDao(OperationLogDao operationLogDao) {
         super.setBaseDao(operationLogDao);
  }
}