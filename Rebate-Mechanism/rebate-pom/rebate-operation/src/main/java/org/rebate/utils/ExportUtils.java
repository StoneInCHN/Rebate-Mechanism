package org.rebate.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rebate.entity.Order;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;

public class ExportUtils {

  /**
   * 准备导出的Order数据
   * @param response
   * @param lists
   * @return
   */
  public static List<Map<String, String>> prepareExportOrder(List<Order> lists){
      List<Map<String, String>> mapList = new ArrayList<Map<String,String>>();
      for (Order order : lists) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sn", order.getSn());
        map.put("endUserPhone", order.getEndUser()!=null?order.getEndUser().getCellPhoneNum():"");
        map.put("endUserName", order.getEndUser()!=null?order.getEndUser().getNickName():"");
        map.put("sellerID", order.getSeller()!=null?order.getSeller().getId()+"":"");
        map.put("sellerName", order.getSeller()!=null?order.getSeller().getName():"");
        map.put("sellerCategory", order.getSeller()!=null&&order.getSeller().getSellerCategory()!=null?order.getSeller().getSellerCategory().getCategoryName():"");
        map.put("sellerDiscount", order.getSeller()!=null?order.getSeller().getDiscount()+"":"");
        map.put("createDate", DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", order.getCreateDate()));
        map.put("amount", order.getAmount()+"");
        map.put("paymentType", order.getPaymentType());
        map.put("paymentTime", DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", order.getPaymentTime()));
        map.put("status","");
        if (order.getStatus() != null) {
          if (order.getStatus() == OrderStatus.UNPAID) {
            map.put("status", "未支付");
          }
          if (order.getStatus() == OrderStatus.PAID) {
            map.put("status", "已支付");
          }
          if (order.getStatus() == OrderStatus.FINISHED) {
            map.put("status", "已评论");
          }
        }
        map.put("sellerIncome", order.getSellerIncome()+"");
        map.put("rebateAmount", order.getRebateAmount()+"");
        map.put("userScore", order.getUserScore()+"");
        map.put("sellerScore", order.getSellerScore()+"");
        map.put("isClearing", order.getIsClearing()!=null&&order.getIsClearing()?"已结算":"未结算");
        mapList.add(map);
      }
      return mapList;
  }
  
}
