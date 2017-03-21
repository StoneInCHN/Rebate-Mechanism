package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Order;
import org.rebate.dao.OrderDao;
import org.rebate.service.OrderService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order,Long> implements OrderService {

      @Resource(name="orderDaoImpl")
      public void setBaseDao(OrderDao orderDao) {
         super.setBaseDao(orderDao);
  }
}