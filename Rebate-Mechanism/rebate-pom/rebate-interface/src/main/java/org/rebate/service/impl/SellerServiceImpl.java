package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SellerDao;
import org.rebate.entity.Seller;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerService;
import org.springframework.stereotype.Service;

@Service("sellerServiceImpl")
public class SellerServiceImpl extends BaseServiceImpl<Seller, Long> implements SellerService {

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "sellerDaoImpl")
  public void setBaseDao(SellerDao sellerDao) {
    super.setBaseDao(sellerDao);
  }

  @Override
  public Seller findSellerByUser(Long userId) {
    return sellerDao.findSellerByUser(userId);
  }
}
