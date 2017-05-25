package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.ClearingOrderRelation;
import org.rebate.dao.ClearingOrderRelationDao;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("clearingOrderRelationServiceImpl")
public class ClearingOrderRelationServiceImpl extends BaseServiceImpl<ClearingOrderRelation,Long> implements ClearingOrderRelationService {

      @Resource(name="clearingOrderRelationDaoImpl")
      public void setBaseDao(ClearingOrderRelationDao clearingOrderRelationDao) {
         super.setBaseDao(clearingOrderRelationDao);
  }
}