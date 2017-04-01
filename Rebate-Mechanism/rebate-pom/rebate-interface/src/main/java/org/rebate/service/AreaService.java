package org.rebate.service;

import org.rebate.entity.Area;
import org.rebate.framework.service.BaseService;

public interface AreaService extends BaseService<Area, Long> {
  /**
   * 组装地区查询sql
   * 
   * @param areaId
   * @return
   */
  String genAreaSql(Area area);
}
