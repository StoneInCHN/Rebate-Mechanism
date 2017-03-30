package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.OrderDao;
import org.rebate.entity.Order;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("orderDaoImpl")
public class OrderDaoImpl extends BaseDaoImpl<Order, Long> implements OrderDao {

  @Override
  public Order getOrderBySn(String orderSn) {
    if (orderSn == null) {
      return null;
    }
    try {
      String jpql = "select order from Order order where order.sn = :orderSn";
      return entityManager.createQuery(jpql, Order.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("orderSn", orderSn).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
