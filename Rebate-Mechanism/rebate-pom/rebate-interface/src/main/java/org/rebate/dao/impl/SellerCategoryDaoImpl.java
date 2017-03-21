package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerCategory;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerCategoryDao;
@Repository("sellerCategoryDaoImpl")
public class SellerCategoryDaoImpl extends  BaseDaoImpl<SellerCategory,Long> implements SellerCategoryDao {

}