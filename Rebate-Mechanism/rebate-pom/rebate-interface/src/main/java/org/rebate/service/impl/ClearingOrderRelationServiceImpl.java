package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.ClearingOrderRelationDao;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.ClearingOrderRelationService;
import org.springframework.stereotype.Service;

@Service("clearingOrderRelationServiceImpl")
public class ClearingOrderRelationServiceImpl extends BaseServiceImpl<ClearingOrderRelation, Long>
    implements ClearingOrderRelationService {

  @Resource(name = "clearingOrderRelationDaoImpl")
  private ClearingOrderRelationDao clearingOrderRelationDao;

  @Resource(name = "clearingOrderRelationDaoImpl")
  public void setBaseDao(ClearingOrderRelationDao clearingOrderRelationDao) {
    super.setBaseDao(clearingOrderRelationDao);
  }

  @Override
  public Page<ClearingOrderRelation> getOrdersByClearingId(Long clearingId, Integer pageSize,
      Integer pageNum) {

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNum);
    pageable.setPageSize(pageSize);

    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("clearingRecId", clearingId));
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");
    Page<ClearingOrderRelation> page = clearingOrderRelationDao.findPage(pageable);
    return page;

  }
}
