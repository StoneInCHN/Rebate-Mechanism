package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.TransferRebateRecord;
import org.rebate.dao.TransferRebateRecordDao;
import org.rebate.service.TransferRebateRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("transferRebateRecordServiceImpl")
public class TransferRebateRecordServiceImpl extends BaseServiceImpl<TransferRebateRecord,Long> implements TransferRebateRecordService {

      @Resource(name="transferRebateRecordDaoImpl")
      public void setBaseDao(TransferRebateRecordDao transferRebateRecordDao) {
         super.setBaseDao(transferRebateRecordDao);
  }
}