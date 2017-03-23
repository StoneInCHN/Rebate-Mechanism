package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Order;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.OrderDao;
@Repository("orderDaoImpl")
public class OrderDaoImpl extends  BaseDaoImpl<Order,Long> implements OrderDao {

}