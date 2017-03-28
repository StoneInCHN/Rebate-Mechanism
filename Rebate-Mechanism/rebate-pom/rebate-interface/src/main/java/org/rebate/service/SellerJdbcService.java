package org.rebate.service;

import java.util.Map;

import org.rebate.entity.commonenum.CommonEnum.FeaturedService;
import org.rebate.entity.commonenum.CommonEnum.SortType;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;



public interface SellerJdbcService {

  /**
   * 获取商家列表
   * 
   * @param longitude
   * @param latitude
   * @param pageable
   * @param radius
   * @param categoryId
   * @param areaId
   * @param featuredService
   * @param sortType
   * @param keyWord
   * @return
   */
  Page<Map<String, Object>> getSellerList(String longitude, String latitude, Pageable pageable,
      int radius, Long categoryId, Long areaId, FeaturedService featuredService, SortType sortType,
      String keyWord);

}
