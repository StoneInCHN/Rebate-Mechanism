package org.rebate.dao.impl;

import java.util.Date;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.UserRegReportDao;
import org.rebate.entity.UserRegReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("userRegReportDaoImpl")
public class UserRegReportDaoImpl extends BaseDaoImpl<UserRegReport, Long> implements
    UserRegReportDao {

  @Override
  public UserRegReport getRegReportByDate(Date date) {
    if (date == null) {
      return null;
    }
    try {
      String jpql =
          "select report from UserRegReport report where report.statisticsDate = :statisticsDate";
      return entityManager.createQuery(jpql, UserRegReport.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("statisticsDate", date)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
