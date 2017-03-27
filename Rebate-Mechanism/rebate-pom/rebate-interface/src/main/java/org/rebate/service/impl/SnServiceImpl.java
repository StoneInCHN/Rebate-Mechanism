package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Sn;
import org.rebate.dao.SnDao;
import org.rebate.service.SnService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("snServiceImpl")
public class SnServiceImpl extends BaseServiceImpl<Sn,Long> implements SnService {

      @Resource(name="snDaoImpl")
      public void setBaseDao(SnDao snDao) {
         super.setBaseDao(snDao);
  }
}