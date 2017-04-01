package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.AreaDao;
import org.rebate.entity.Area;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.AreaService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {

  @Resource(name = "areaDaoImpl")
  private AreaDao areaDao;

  @Resource(name = "areaDaoImpl")
  public void setBaseDao(AreaDao areaDao) {
    super.setBaseDao(areaDao);
  }

  @Override
  public String genAreaSql(Area area) {
    String areaSql = area.getId() + ",";
    if (!CollectionUtils.isEmpty(area.getChildren())) {
      for (Area childArea : area.getChildren()) {
        areaSql += genAreaSql(childArea);
      }
    }
    return areaSql;
  }
}
