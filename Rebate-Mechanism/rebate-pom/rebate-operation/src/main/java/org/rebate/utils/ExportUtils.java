package org.rebate.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
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
        map.put("endUserPhone", order.getEndUser() != null? order.getEndUser().getCellPhoneNum() : "");
        map.put("endUserName", order.getEndUser() != null? order.getEndUser().getNickName() : "");
        map.put("sellerID", order.getSeller() != null? order.getSeller().getId() + "" : "");
        map.put("sellerName", order.getSeller() != null? order.getSeller().getName() : "");
        map.put("sellerCategory", order.getSeller() != null && order.getSeller().getSellerCategory() != null? order.getSeller().getSellerCategory().getCategoryName() : "");
        map.put("sellerDiscount", order.getSeller() != null? order.getSeller().getDiscount() + "" : "");
        map.put("createDate", DateUtils.getDateFormatString("yyyy-MM-dd HH:mm:ss", order.getCreateDate()));
        map.put("amount", order.getAmount() + "");
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
        map.put("sellerIncome", order.getSellerIncome() + "");
        map.put("rebateAmount", order.getRebateAmount() + "");
        map.put("userScore", order.getUserScore() + "");
        map.put("sellerScore", order.getSellerScore() + "");
        map.put("isClearing", order.getIsClearing() != null&&order.getIsClearing()? "已结算" : "未结算");
        mapList.add(map);
      }
      return mapList;
  }

  public static List<Map<String, String>> prepareExportSeller(List<Seller> lists) {
    String[] headers = {"sellerId","sellerName","sellerCategory","contactPerson","contactCellPhone",
        "endUserCellPhone","endUserNickName","area","address","licenseNum",
        "rateScore","avgPrice","discount","limitAmountByDay","businessTime",
        "favoriteNum","totalOrderNum","accountStatus","description"}; // 需要导出的字段
    String[] headersName = {"商家编号","商家名字","商家类别","商家联系人","商家联系人手机",
        "申请人(用户)手机号","用户昵称","地区","商家地址","营业执照号",
        "评分","均价","折扣","每日营业额上限","营业时间",
        "商家被收藏数","总订单数","商家状态","店铺介绍"}; // 字段对应列的列名
    List<Map<String, String>> mapList = new ArrayList<Map<String,String>>();
    for (Seller seller : lists) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("sellerId", seller.getId() + "");
      map.put("sellerName", seller.getName());
      map.put("sellerCategory", seller.getSellerCategory() != null? seller.getSellerCategory().getCategoryName() : "");
      map.put("contactPerson", seller.getContactPerson());
      map.put("contactCellPhone", seller.getContactCellPhone());
      map.put("endUserCellPhone", seller.getEndUser() != null? seller.getEndUser().getCellPhoneNum() : "");
      map.put("endUserNickName", seller.getEndUser() != null? seller.getEndUser().getNickName() : "");
      map.put("area", seller.getArea() != null? seller.getArea().getFullName() : "");
      map.put("address", seller.getAddress());
      map.put("licenseNum", seller.getLicenseNum());
      map.put("rateScore", seller.getRateScore() != null? seller.getRateScore().toString() : "");
      map.put("avgPrice", seller.getAvgPrice() != null? seller.getAvgPrice().toString() : "");
      map.put("discount", seller.getDiscount() != null? seller.getDiscount().toString() : "");
      map.put("limitAmountByDay", seller.getLimitAmountByDay() != null? seller.getLimitAmountByDay().toString() : "");
      map.put("businessTime", seller.getBusinessTime());
      map.put("favoriteNum", seller.getFavoriteNum() + "");
      map.put("totalOrderNum", seller.getTotalOrderAmount() + "");
      map.put("accountStatus", "");
      if (seller.getAccountStatus() != null) {
        if (AccountStatus.ACTIVED == seller.getAccountStatus()) {
          map.put("accountStatus", "帐号正常");
        }
        if (AccountStatus.LOCKED == seller.getAccountStatus()) {
          map.put("accountStatus", "帐号禁用");
        }
        if (AccountStatus.DELETE == seller.getAccountStatus()) {
          map.put("accountStatus", "帐号删除");
        }        
      }
      map.put("description", seller.getDescription());
      mapList.add(map);
    }
    return mapList;
  }
  
}
