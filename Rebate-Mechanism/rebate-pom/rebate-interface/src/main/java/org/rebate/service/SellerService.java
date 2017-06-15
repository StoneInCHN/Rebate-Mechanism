package org.rebate.service;

import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.BaseService;
import org.rebate.json.request.SellerRequest;

public interface SellerService extends BaseService<Seller, Long> {

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

  /**
   * 修改商户信息
   * 
   * @param req
   * @return
   */
  Seller editInfo(SellerRequest req);

  /**
   * 获取用户收藏商铺
   * 
   * @param pageable
   * @param userId
   * @return
   */
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
