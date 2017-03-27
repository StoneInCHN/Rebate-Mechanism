package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SellerEvaluateDao;
import org.rebate.entity.SellerEvaluate;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("sellerEvaluateDaoImpl")
public class SellerEvaluateDaoImpl extends BaseDaoImpl<SellerEvaluate, Long> implements
    SellerEvaluateDao {

  @Transactional
  public SellerEvaluate getEvaluateByOrder(Long orderId) {

    if (orderId == null) {
      return null;
    }
    try {
      String jpql =
          "select evaluate from SellerEvaluate evaluate where evaluate.order.id = :orderId";
      return entityManager.createQuery(jpql, SellerEvaluate.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("orderId", orderId).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

}
