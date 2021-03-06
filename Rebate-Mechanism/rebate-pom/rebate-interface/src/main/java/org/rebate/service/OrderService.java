package org.rebate.service;

import java.math.BigDecimal;
import java.util.List;

import org.rebate.entity.Order;
import org.rebate.framework.service.BaseService;
import org.springframework.web.multipart.MultipartFile;


public interface OrderService extends BaseService<Order, Long> {

  /**
   * 支付订单
   * 
   * @param userId
   * @param payType
   * @param amount
   * @param sellerId
   * @param remark
   * @return
   */
  Order create(Long userId, String payTypeId, String payType, BigDecimal amount, Long sellerId,
      String remark, Boolean isBeanPay, BigDecimal deductAmount);


  /**
   * 支付成功回调更新订单数据
   * 
   * @param orderSn
   * @return
   */
  Order updateOrderforPayCallBack(String orderSn);


  /**
   * 根据订单号获取订单
   * 
   * @param orderSn
   * @return
   */
  Order getOrderBySn(String orderSn);

  /**
   * 用户评价
   * 
   * @param order
   * @param userId
   * @param score
   * @param content
   * @param evaluateImages
   * @return
   */
  Order evaluateOrder(Long orderId, Long userId, Integer score, String content,
      List<MultipartFile> evaluateImages);

  /**
   * 验证商家当天营业额是否超过规定的营业额度
   * 
   * @param seller
   * @param amount
   * @return
   */
  Boolean isOverSellerLimitAmount(Long sellerId, BigDecimal amount);
}
