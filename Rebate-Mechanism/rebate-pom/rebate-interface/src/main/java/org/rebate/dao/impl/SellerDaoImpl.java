package org.rebate.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.SellerDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
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
      String jpql =
          "select seller from Seller seller where seller.accountStatus=0 and seller.endUser.id = :userId";
      return entityManager.createQuery(jpql, Seller.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("userId", userId).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public Page<Seller> findFavoriteSellers(Pageable pageable, Long userId) {

    String jpql =
        "select seller from Seller seller join seller.favoriteEndUsers u where u.id =:userId";
    Map<String, Long> paramMap = new HashMap<>();
    paramMap.put("userId", userId);
    return findPageCustomized(pageable, jpql, paramMap);
  }

  @Override
  public EndUser userCollectSeller(Long userId, Long sellerId) {
    if (userId == null || sellerId == null) {
      return null;
    }
    try {
      String jpql =
          "select user from EndUser user join user.favoriteSellers u where user.id = :userId and u.id = :sellerId";
      return entityManager.createQuery(jpql, EndUser.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("userId", userId).setParameter("sellerId", sellerId).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
