package org.rebate.dao.impl;

import java.util.Date;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.ThirdApiReportDao;
import org.rebate.entity.ThirdApiReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("thirdApiReportDaoImpl")
public class ThirdApiReportDaoImpl extends BaseDaoImpl<ThirdApiReport, Long> implements
    ThirdApiReportDao {

  @Override
  public ThirdApiReport getApiReportByDate(Date curDate) {
    if (curDate == null) {
      return null;
    }
    try {
      String jpql =
          "select report from ThirdApiReport report where report.statisticsDate = :statisticsDate";
      return entityManager.createQuery(jpql, ThirdApiReport.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("statisticsDate", curDate)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
