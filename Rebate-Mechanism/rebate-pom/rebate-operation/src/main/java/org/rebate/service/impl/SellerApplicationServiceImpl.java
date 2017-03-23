package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SellerApplicationDao;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerApplicationService;
import org.springframework.stereotype.Service;

@Service("sellerApplicationServiceImpl")
public class SellerApplicationServiceImpl extends BaseServiceImpl<SellerApplication, Long>
    implements SellerApplicationService {

  @Resource(name = "sellerApplicationDaoImpl")
  private SellerApplicationDao baseDao;
  
  @Resource(name = "sellerApplicationDaoImpl")
  public void setBaseDao(SellerApplicationDao baseDao) {
    super.setBaseDao(baseDao);
  }

}
