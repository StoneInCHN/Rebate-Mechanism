package org.rebate.service.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;



public class SellerRowMapper implements RowMapper<Map<String, Object>> {

  private String unitConsume;

  private String rebateScoreUser;

  public SellerRowMapper(String rebateScore, String unitConsume) {
    super();
    this.unitConsume = unitConsume;
    this.rebateScoreUser = rebateScore;
  }

  public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
    Map<String, Object> sellerInfo = new HashMap<String, Object>();

    /**
     * 数据库字段映射
     */
    sellerInfo.put("sellerId", rs.getLong("id"));
    sellerInfo.put("sellerName", rs.getString("name"));
    sellerInfo.put("storePhone", rs.getString("store_phone"));
    if (isExistColumn(rs, "distance")) {
      sellerInfo.put("distance", String.format("%.2f", rs.getDouble("distance") / 1000));
    } else {
      sellerInfo.put("distance", null);
    }
    sellerInfo.put("latitude", rs.getBigDecimal("latitude"));
    sellerInfo.put("longitude", rs.getBigDecimal("longitude"));
    sellerInfo.put("rateScore", rs.getBigDecimal("rate_score"));
    sellerInfo.put("avg_price", rs.getBigDecimal("avg_price"));
    sellerInfo.put("address", rs.getString("address"));
    sellerInfo.put("categoryName", rs.getString("category_name"));
    sellerInfo.put("description", rs.getString("description"));
    sellerInfo.put("business_time", rs.getString("business_time"));
    sellerInfo.put("storePictureUrl", rs.getString("store_picture_url"));
    sellerInfo.put("favorite_num", rs.getInt("favorite_num"));
    sellerInfo.put("featured_service", rs.getString("featured_service"));

    if (!StringUtils.isEmpty(unitConsume) && !StringUtils.isEmpty(rebateScoreUser)) {
      BigDecimal unit = new BigDecimal(unitConsume);
      sellerInfo.put("unitConsume", unit);
      BigDecimal rebateUserScore =
          unit.subtract(rs.getBigDecimal("discount").divide(new BigDecimal("10")).multiply(unit))
              .multiply(new BigDecimal(rebateScoreUser));
      sellerInfo.put("rebateUserScore", rebateUserScore);
    }

    // sellerInfo.put("service_category_name", rs.getString("category_name"));
    // sellerInfo.put("price", rs.getBigDecimal("price"));
    // sellerInfo.put("promotion_price", rs.getBigDecimal("promotion_price"));
    return sellerInfo;
  }


  /**
   * 判断查询结果集中是否存在某列
   * 
   * @param rs 查询结果集
   * @param columnName 列名
   * @return true 存在; false 不存咋
   */
  public boolean isExistColumn(ResultSet rs, String columnName) {
    try {
      if (rs.findColumn(columnName) > 0) {
        return true;
      }
    } catch (SQLException e) {
      return false;
    }

    return false;
  }


}
