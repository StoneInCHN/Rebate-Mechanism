package org.rebate.dao;

import org.rebate.entity.EndUser;
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

  /**
   * 根据营业执照号获取商户信息
   * 
   * @param license
   * @return
   */
  Seller findSellerBylicense(String license);


  Page<Seller> findFavoriteSellers(Pageable pageable, Long userId);

  /**
   * 用户是否收藏该商铺
   * 
   * @param userId
   * @param sellerId
   * @return
   */
  EndUser userCollectSeller(Long userId, Long sellerId);
}
