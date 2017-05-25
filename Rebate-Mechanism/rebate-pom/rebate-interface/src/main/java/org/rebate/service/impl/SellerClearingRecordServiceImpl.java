package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerClearingRecord;
import org.rebate.dao.SellerClearingRecordDao;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerClearingRecordServiceImpl")
public class SellerClearingRecordServiceImpl extends BaseServiceImpl<SellerClearingRecord,Long> implements SellerClearingRecordService {

      @Resource(name="sellerClearingRecordDaoImpl")
      public void setBaseDao(SellerClearingRecordDao sellerClearingRecordDao) {
         super.setBaseDao(sellerClearingRecordDao);
  }
}