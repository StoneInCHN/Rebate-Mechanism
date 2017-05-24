package org.rebate.service;

import java.math.BigDecimal;
import java.util.List;

import org.rebate.entity.Order;
import org.rebate.entity.SellerOrderCart;
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
  Order create(Long userId, String payType, BigDecimal amount, Long sellerId, String remark,
      Boolean isBeanPay);


  // /**
  // * 普通订单支付成功回调更新订单数据
  // *
  // * @param orderSn
  // * @return
  // */
  // Order updateOrderforPayCallBack(Order order);
  //
  // Order updateOrderInfo(Order order);

  /**
   * 商家录单订单录单成功后,无论支付与否,先更新订单相关信息
   */
  void updateSellerOrderBeforePay(String sn);

  /**
   * 支付成功后回调方法
   * 
   * @param sn
   */
  void callbackAfterPay(String sn);


  /**
   * 根据订单号获取订单
   * 
   * @param orderSn
   * @return
   */
  Order getOrderBySn(String orderSn);

  /**
   * 根据批量订单流水号获取订单
   * 
   * @param orderSn
   * @return
   */
  List<Order> getOrderByBatchSn(String batchSn);

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


  Order create(Long userId, String payType, BigDecimal amount, Long sellerId, String remark,
      Boolean isBeanPay, Boolean isSallerOrder);


  Order createSellerOrder(Long userId, BigDecimal amount, Long sellerId);


  List<Order> createSellerOrder(List<SellerOrderCart> sellerOrderCarts);


}
