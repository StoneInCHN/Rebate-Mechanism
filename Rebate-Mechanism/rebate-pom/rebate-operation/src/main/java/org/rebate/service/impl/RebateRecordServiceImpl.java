package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.RebateRecord;
import org.rebate.dao.RebateRecordDao;
import org.rebate.service.RebateRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("rebateRecordServiceImpl")
public class RebateRecordServiceImpl extends BaseServiceImpl<RebateRecord,Long> implements RebateRecordService {

      @Resource(name="rebateRecordDaoImpl")
      public void setBaseDao(RebateRecordDao rebateRecordDao) {
         super.setBaseDao(rebateRecordDao);
  }
}