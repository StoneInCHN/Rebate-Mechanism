package org.rebate.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.rebate.dao.EndUserDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SnDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.Sn.Type;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

  @Resource(name = "orderDaoImpl")
  private OrderDao orderDao;

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "snDaoImpl")
  private SnDao snDao;

  @Resource(name = "orderDaoImpl")
  public void setBaseDao(OrderDao orderDao) {
    super.setBaseDao(orderDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Order create(Long userId, String payType, BigDecimal amount, Long sellerId, String remark,
      Boolean isBeanPay) {
    Order order = new Order();
    EndUser endUser = endUserDao.find(userId);
    Seller seller = sellerDao.find(sellerId);

    order.setEndUser(endUser);
    order.setSeller(seller);
    order.setAmount(amount);
    order.setPaymentType(payType);
    order.setRemark(remark);
    order.setStatus(OrderStatus.UNPAID);
    order.setSn(snDao.generate(Type.ORDER));

    if (!isBeanPay) {
      BigDecimal rebateUserScoreConfig =
          new BigDecimal(systemConfigDao.getConfigByKey(SystemConfigKey.REBATESCORE_USER)
              .getConfigValue().toString());
      BigDecimal rebateSellerScoreConfig =
          new BigDecimal(systemConfigDao.getConfigByKey(SystemConfigKey.REBATESCORE_SELLER)
              .getConfigValue().toString());

      BigDecimal rebateUserScore =
          amount.subtract(seller.getDiscount().divide(new BigDecimal("10")).multiply(amount))
              .multiply(rebateUserScoreConfig);

      BigDecimal rebateSellerScore =
          amount.subtract(seller.getDiscount().divide(new BigDecimal("10")).multiply(amount))
              .multiply(rebateSellerScoreConfig);
      order.setUserScore(rebateUserScore);
      order.setSellerScore(rebateSellerScore);
    }

    orderDao.persist(order);
    return order;
  }
}
