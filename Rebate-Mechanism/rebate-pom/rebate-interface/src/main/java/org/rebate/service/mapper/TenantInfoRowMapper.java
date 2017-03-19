package org.rebate.service.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;


public class TenantInfoRowMapper implements RowMapper<Map<String, Object>> {

  public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
    Map<String, Object> tenantInfo = new HashMap<String, Object>();

    /**
     * 数据库字段映射
     */
    tenantInfo.put("id", rs.getLong("id"));
    // tenantInfo.put("service_id", rs.getLong("service_id"));
    tenantInfo.put("contactPhone", rs.getString("contact_phone"));
    tenantInfo.put("distance", String.format("%.1f", rs.getDouble("distance") / 1000));
    tenantInfo.put("latitude", rs.getBigDecimal("latitude"));
    tenantInfo.put("longitude", rs.getBigDecimal("longitude"));
    tenantInfo.put("address", rs.getString("address"));
    tenantInfo.put("tenantName", rs.getString("tenant_name"));
    tenantInfo.put("photo", rs.getString("photo"));
    tenantInfo.put("praiseRate", rs.getInt("praise_rate"));
    tenantInfo.put("rateCounts", rs.getInt("rate_counts"));

    // tenantInfo.put("service_category_name", rs.getString("category_name"));
    // tenantInfo.put("price", rs.getBigDecimal("price"));
    // tenantInfo.put("promotion_price", rs.getBigDecimal("promotion_price"));
    return tenantInfo;
  }


}
