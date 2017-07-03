package org.rebate.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.FeaturedService;
import org.rebate.entity.commonenum.CommonEnum.SortType;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.AreaService;
import org.rebate.service.SellerJdbcService;
import org.rebate.service.mapper.SellerRowMapper;
import org.rebate.utils.LatLonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service("sellerJdbcServiceImpl")
public class SellerJdbcServiceImpl extends BaseServiceImpl<EndUser, Long> implements
    SellerJdbcService {

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;


  @Override
  public Page<Map<String, Object>> getSellerList(String longitude, String latitude,
      Pageable pageable, int radius, Long categoryId, String areaIds,
      FeaturedService featuredService, SortType sortType, String keyWord) {

    StringBuffer seller_sql = new StringBuffer();
    StringBuffer total_count_sql = new StringBuffer();
    Page<Map<String, Object>> page = new Page<>();
    if (latitude != null && longitude != null) {
      double[] aroundGps =
          LatLonUtil.getAround(Double.valueOf(latitude), Double.valueOf(longitude), radius);
      seller_sql
          .append("SELECT distinct(rs.id),rs.create_date,rs.name,rs.is_bean_pay,rs.latitude,rs.longitude,rs.address,rs.description,rs.discount,rs.avg_price,rs.business_time,rs.favorite_num,rs.featured_service,rs.rate_score,rs.store_picture_url,rs.store_phone,rsc.category_name,");
      seller_sql.append("round(6378.138*2*asin(sqrt(pow(sin((" + latitude);
      seller_sql.append("*pi()/180-latitude*pi()/180)/2),2)+cos(" + latitude);
      seller_sql.append("*pi()/180)*cos(latitude*pi()/180)*pow(sin((" + longitude);
      seller_sql.append("*pi()/180-longitude*pi()/180)/2),2)))*1000,2) as distance");
      seller_sql.append(" FROM rm_seller rs");
      seller_sql.append(" LEFT JOIN rm_seller_category rsc ON rsc.id = rs.seller_category");
      seller_sql.append(" WHERE rs.account_status = 0");
      if (categoryId != null) {
        seller_sql.append(" AND rsc.id = " + categoryId);
      }
      if (areaIds != null) {
        seller_sql.append(" AND rs.area in (" + areaIds + ")");
      }
      if (featuredService != null) {
        if (featuredService.ordinal() == 1) {
          seller_sql.append(" AND rs.featured_service in (0,1)");
        } else if (featuredService.ordinal() == 2) {
          seller_sql.append(" AND rs.featured_service in (0,2)");
        } else {
          seller_sql.append(" AND rs.featured_service in (0,1,2)");
        }
      }
      if (keyWord != null) {
        seller_sql.append(" AND rs.name like '%" + keyWord + "%'");
      }

      seller_sql.append(" AND longitude > " + aroundGps[1]);
      seller_sql.append(" AND longitude <" + aroundGps[3]);
      seller_sql.append(" AND latitude > " + aroundGps[0]);
      seller_sql.append(" AND latitude < " + aroundGps[2]);
      if (SortType.DISTANCEASC.equals(sortType)) {// 距离优先
        seller_sql.append(" ORDER BY distance asc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");

      } else if (SortType.SCOREDESC.equals(sortType)) {// 好评优先
        seller_sql.append(" ORDER BY rs.rate_score desc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      } else if (SortType.COLLECTDESC.equals(sortType)) {// 收藏最多
        seller_sql.append(" ORDER BY rs.favorite_num desc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      } else {// 默认排序（时间先后顺序）
        seller_sql.append(" ORDER BY rs.create_date asc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      }



      total_count_sql.append("SELECT COUNT(*) AS total FROM (SELECT distinct(rs.id),");
      total_count_sql.append(" round(6378.138*2*asin(sqrt(pow(sin((" + latitude);
      total_count_sql.append("*pi()/180-latitude*pi()/180)/2),2)+cos(" + latitude);
      total_count_sql.append("*pi()/180)*cos(latitude*pi()/180)*pow(sin((" + longitude);
      total_count_sql.append("*pi()/180-longitude*pi()/180)/2),2)))*1000,2) as distance");
      total_count_sql.append(" FROM rm_seller rs");
      total_count_sql.append(" LEFT JOIN rm_seller_category rsc ON rsc.id = rs.seller_category");
      total_count_sql.append(" WHERE rs.account_status = 0");
      if (categoryId != null) {
        total_count_sql.append(" AND rsc.id = " + categoryId);
      }
      if (areaIds != null) {
        total_count_sql.append(" AND rs.area in (" + areaIds + ")");
      }
      if (featuredService != null) {
        if (featuredService.ordinal() == 1) {
          total_count_sql.append(" AND rs.featured_service in (0,1)");
        } else if (featuredService.ordinal() == 2) {
          total_count_sql.append(" AND rs.featured_service in (0,2)");
        } else {
          total_count_sql.append(" AND rs.featured_service in (0,1,2)");
        }
      }
      if (keyWord != null) {
        total_count_sql.append(" AND rs.name like '%" + keyWord + "%'");
      }

      total_count_sql.append(" AND longitude > " + aroundGps[1]);
      total_count_sql.append(" AND longitude <" + aroundGps[3]);
      total_count_sql.append(" AND latitude > " + aroundGps[0]);
      total_count_sql.append(" AND latitude < " + aroundGps[2]);
      total_count_sql.append(" ) AS seller_total;");


      SystemConfig rebateScore = systemConfigDao.getConfigByKey(SystemConfigKey.REBATESCORE_USER);
      SystemConfig unitConsume = systemConfigDao.getConfigByKey(SystemConfigKey.UNIT_CONSUME);
      SystemConfig leBeanPayConfig = systemConfigDao.find(Long.valueOf(4));// 乐豆抵扣是否开启
      List<Map<String, Object>> sellerList =
          jdbcTemplate.query(seller_sql.toString(), new SellerRowMapper(
              rebateScore != null ? rebateScore.getConfigValue() : null,
              unitConsume != null ? unitConsume.getConfigValue() : null,
              leBeanPayConfig != null ? leBeanPayConfig.getIsEnabled() : null));
      Map<String, Object> totalMap = jdbcTemplate.queryForMap(total_count_sql.toString());
      Long total = (Long) totalMap.get("total");

      // int pageTotal =
      // Integer.valueOf(String.valueOf((total + pageable.getPageSize() - 1)
      // / pageable.getPageSize()));

      // pageable.setPageSize(tenantInfoList != null ? tenantInfoList.size() : 0);
      page = new Page<Map<String, Object>>(sellerList, total, pageable);

    } else if (latitude == null && longitude == null && areaIds != null) {// 手机未开启定位，经纬度丢失

      seller_sql
          .append("SELECT distinct(rs.id),rs.create_date,rs.name,rs.is_bean_pay,rs.latitude,rs.longitude,rs.address,rs.description,rs.discount,rs.avg_price,rs.business_time,rs.favorite_num,rs.featured_service,rs.rate_score,rs.store_picture_url,rs.store_phone,rsc.category_name");
      seller_sql.append(" FROM rm_seller rs");
      seller_sql.append(" LEFT JOIN rm_seller_category rsc ON rsc.id = rs.seller_category");
      seller_sql.append(" WHERE rs.account_status = 0");
      if (categoryId != null) {
        seller_sql.append(" AND rsc.id = " + categoryId);
      }
      if (areaIds != null) {
        seller_sql.append(" AND rs.area in (" + areaIds + ")");
      }
      if (featuredService != null) {
        if (featuredService.ordinal() == 1) {
          seller_sql.append(" AND rs.featured_service in (0,1)");
        } else if (featuredService.ordinal() == 2) {
          seller_sql.append(" AND rs.featured_service in (0,2)");
        } else {
          seller_sql.append(" AND rs.featured_service in (0,1,2)");
        }
      }
      if (keyWord != null) {
        seller_sql.append(" AND rs.name like '%" + keyWord + "%'");
      }

      if (SortType.DISTANCEASC.equals(sortType)) {// 距离优先(由于没有经纬度，无法计算距离，按照默认排序)
        // seller_sql.append(" ORDER BY distance asc LIMIT " + (pageable.getPageNumber() - 1)
        // * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
        seller_sql.append(" ORDER BY rs.create_date asc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");

      } else if (SortType.SCOREDESC.equals(sortType)) {// 好评优先
        seller_sql.append(" ORDER BY rs.rate_score desc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      } else if (SortType.COLLECTDESC.equals(sortType)) {// 收藏最多
        seller_sql.append(" ORDER BY rs.favorite_num desc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      } else {// 默认排序（时间先后顺序）
        seller_sql.append(" ORDER BY rs.create_date asc LIMIT " + (pageable.getPageNumber() - 1)
            * pageable.getPageSize() + "," + pageable.getPageSize() + ";");
      }

      total_count_sql.append("SELECT COUNT(*) AS total FROM (SELECT distinct(rs.id)");
      total_count_sql.append(" FROM rm_seller rs");
      total_count_sql.append(" LEFT JOIN rm_seller_category rsc ON rsc.id = rs.seller_category");
      total_count_sql.append(" WHERE rs.account_status = 0");
      if (categoryId != null) {
        total_count_sql.append(" AND rsc.id = " + categoryId);
      }
      if (areaIds != null) {
        total_count_sql.append(" AND rs.area in (" + areaIds + ")");
      }
      if (featuredService != null) {
        if (featuredService.ordinal() == 1) {
          total_count_sql.append(" AND rs.featured_service in (0,1)");
        } else if (featuredService.ordinal() == 2) {
          total_count_sql.append(" AND rs.featured_service in (0,2)");
        } else {
          total_count_sql.append(" AND rs.featured_service in (0,1,2)");
        }
      }
      if (keyWord != null) {
        total_count_sql.append(" AND rs.name like '%" + keyWord + "%'");
      }
      total_count_sql.append(" ) AS seller_total;");


      SystemConfig rebateScore = systemConfigDao.getConfigByKey(SystemConfigKey.REBATESCORE_USER);
      SystemConfig unitConsume = systemConfigDao.getConfigByKey(SystemConfigKey.UNIT_CONSUME);
      SystemConfig leBeanPayConfig = systemConfigDao.find(Long.valueOf(4));// 乐豆抵扣是否开启
      List<Map<String, Object>> sellerList =
          jdbcTemplate.query(seller_sql.toString(), new SellerRowMapper(
              rebateScore != null ? rebateScore.getConfigValue() : null,
              unitConsume != null ? unitConsume.getConfigValue() : null,
              leBeanPayConfig != null ? leBeanPayConfig.getIsEnabled() : null));
      Map<String, Object> totalMap = jdbcTemplate.queryForMap(total_count_sql.toString());
      Long total = (Long) totalMap.get("total");

      page = new Page<Map<String, Object>>(sellerList, total, pageable);
    }
    return page;
  }


}
