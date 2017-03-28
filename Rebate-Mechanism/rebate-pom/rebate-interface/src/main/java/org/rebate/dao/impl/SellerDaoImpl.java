package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SellerDao;
import org.rebate.entity.Seller;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("sellerDaoImpl")
public class SellerDaoImpl extends BaseDaoImpl<Seller, Long> implements SellerDao {

  @Transactional
  public Seller findSellerByUser(Long userId) {
    if (userId == null) {
      return null;
    }
    try {
      String jpql = "select seller from Seller seller where seller.endUser.id = :userId";
      return entityManager.createQuery(jpql, Seller.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("userId", userId).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
