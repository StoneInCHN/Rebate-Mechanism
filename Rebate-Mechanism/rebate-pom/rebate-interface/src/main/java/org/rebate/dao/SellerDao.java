package org.rebate.dao;

import org.rebate.entity.Seller;
import org.rebate.framework.dao.BaseDao;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;

public interface SellerDao extends BaseDao<Seller, Long> {
	/**
	 * 根据用户获取商户信息
	 * 
	 * @param userId
	 * @return
	 */
	Seller findSellerByUser(Long userId);


	Page<Seller> findFavoriteSellers(Pageable pageable, Long userId);
}
