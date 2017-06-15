package org.rebate.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SellerApplicationDao;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository("sellerApplicationDaoImpl")
public class SellerApplicationDaoImpl extends BaseDaoImpl<SellerApplication, Long> implements
    SellerApplicationDao {

  @Override
  public SellerApplication findSellerApplicationBylicense(String license) {

    if (license == null) {
      return null;
    }
    try {
      String jpql =
          "select application from SellerApplication application where application.applyStatus!=1 and application.licenseNum = :licenseNum";
      List<SellerApplication> list =
          entityManager.createQuery(jpql, SellerApplication.class)
              .setFlushMode(FlushModeType.COMMIT).setParameter("licenseNum", license)
              .getResultList();
      if (CollectionUtils.isEmpty(list)) {
        return null;
      } else {
        return list.get(0);
      }

    } catch (NoResultException e) {
      return null;
    }


  }

}
