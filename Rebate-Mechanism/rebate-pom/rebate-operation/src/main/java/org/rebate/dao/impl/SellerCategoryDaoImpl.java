package org.rebate.dao.impl;

import org.rebate.dao.SellerCategoryDao;
import org.rebate.entity.SellerCategory;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("sellerCategoryDaoImpl")
public class SellerCategoryDaoImpl extends BaseDaoImpl<SellerCategory, Long> implements
    SellerCategoryDao {
  
}
