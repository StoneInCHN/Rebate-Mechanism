package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.Seller;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("salesmanSellerRelationDaoImpl")
public class SalesmanSellerRelationDaoImpl extends BaseDaoImpl<SalesmanSellerRelation, Long>
    implements SalesmanSellerRelationDao {

  @Override
  public SalesmanSellerRelation getRelationBySeller(Seller seller) {
    if (seller == null) {
      return null;
    }
    try {
      String jpql =
          "select relation from SalesmanSellerRelation relation where relation.seller = :seller";
      return entityManager.createQuery(jpql, SalesmanSellerRelation.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("seller", seller).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
