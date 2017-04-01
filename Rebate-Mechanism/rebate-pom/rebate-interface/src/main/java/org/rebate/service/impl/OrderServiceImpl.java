package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.EndUserDao;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SnDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Order;
import org.rebate.entity.RebateRecord;
import org.rebate.entity.Seller;
import org.rebate.entity.Sn.Type;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


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

  @Resource(name = "userRecommendRelationDaoImpl")
  private UserRecommendRelationDao userRecommendRelationDao;

  @Resource(name = "leScoreRecordDaoImpl")
  private LeScoreRecordDao leScoreRecordDao;

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
    order.setSellerIncome(amount.multiply(seller.getDiscount().divide(new BigDecimal("10"))));
    order.setPaymentType(payType);
    order.setRemark(remark);
    order.setStatus(OrderStatus.UNPAID);
    order.setSn(snDao.generate(Type.ORDER));
    order.setIsBeanPay(isBeanPay);

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


  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Order updateOrderforPayCallBack(String orderSn) {
    Order order = orderDao.getOrderBySn(orderSn);
    order.setStatus(OrderStatus.PAID);

    EndUser endUser = order.getEndUser();
    Seller seller = order.getSeller();
    EndUser sellerEndUser = seller.getEndUser();

    seller.setTotalOrderNum(seller.getTotalOrderNum() + 1);
    seller.setTotalOrderAmount(seller.getTotalOrderAmount().add(order.getSellerIncome()));
    seller.setUnClearingAmount(seller.getUnClearingAmount().add(order.getSellerIncome()));
    sellerDao.merge(seller);

    /**
     * 消费后商家的直接收益
     */
    LeScoreRecord scoreRecord = new LeScoreRecord();
    scoreRecord.setEndUser(sellerEndUser);
    scoreRecord.setSeller(seller);
    scoreRecord.setLeScoreType(LeScoreType.CONSUME_SELLER);
    scoreRecord.setRemark(order.getPaymentType());
    scoreRecord.setAmount(order.getSellerIncome());
    scoreRecord.setUserCurLeScore(sellerEndUser.getCurLeScore().add(scoreRecord.getAmount()));
    sellerEndUser.setCurLeScore(sellerEndUser.getCurLeScore().add(scoreRecord.getAmount()));
    sellerEndUser.setTotalLeScore(sellerEndUser.getTotalLeScore().add(scoreRecord.getAmount()));
    sellerEndUser.setMerchantLeScore(sellerEndUser.getMerchantLeScore()
        .add(scoreRecord.getAmount()));
    sellerEndUser.getLeScoreRecords().add(scoreRecord);

    // endUserDao.merge(sellerEndUser);

    /**
     * 消费后用户积分返利 (乐豆消费无积分返利)
     */
    if (order.getUserScore() != null) {
      endUser.setCurScore(endUser.getCurScore().add(order.getUserScore()));
      endUser.setTotalScore(endUser.getTotalScore().add(order.getUserScore()));
      RebateRecord rebateRecord = new RebateRecord();
      rebateRecord.setEndUser(endUser);
      rebateRecord.setSeller(seller);
      rebateRecord.setAmount(order.getAmount());
      rebateRecord.setOrderId(order.getId());
      rebateRecord.setRebateScore(order.getUserScore());
      rebateRecord.setPaymentType(order.getPaymentType());
      rebateRecord.setUserCurScore(endUser.getCurScore());
      endUser.getRebateRecords().add(rebateRecord);
      endUserDao.merge(endUser);
    }
    /**
     * 消费后商家积分返利(乐豆消费无积分返利)
     */
    if (order.getSellerScore() != null) {
      sellerEndUser.setCurScore(sellerEndUser.getCurScore().add(order.getSellerScore()));
      sellerEndUser.setTotalScore(sellerEndUser.getTotalScore().add(order.getSellerScore()));
      RebateRecord rebateRecord = new RebateRecord();
      rebateRecord.setEndUser(sellerEndUser);
      rebateRecord.setSeller(seller);
      rebateRecord.setAmount(order.getAmount());
      rebateRecord.setOrderId(order.getId());
      rebateRecord.setRebateScore(order.getSellerScore());
      rebateRecord.setPaymentType(order.getPaymentType());
      rebateRecord.setUserCurScore(sellerEndUser.getCurScore());
      sellerEndUser.getRebateRecords().add(rebateRecord);
      // endUserDao.merge(sellerEndUser);
    }

    /**
     * 乐豆消费无推荐返利
     */
    BigDecimal rebateAmount = order.getAmount().subtract(order.getSellerIncome());
    if (!order.getIsBeanPay()) {

      SystemConfig leScorePerConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.LESCORE_PERCENTAGE);

      /**
       * 用户消费后推荐人返利乐分
       */
      UserRecommendRelation userRecommendRelation = userRecommendRelationDao.findByUser(endUser);
      SystemConfig directUserConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_DIRECT_USER);
      SystemConfig indirectUserConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_INDIRECT_USER);
      if (directUserConfig != null && indirectUserConfig != null && userRecommendRelation != null
          && leScorePerConfig != null) {
        List<EndUser> records =
            userRecommendIncome(userRecommendRelation, directUserConfig, indirectUserConfig,
                leScorePerConfig, 0, rebateAmount);
        if (!CollectionUtils.isEmpty(records)) {
          endUserDao.merge(records);
        }
      }

      /**
       * 商家消费收益后推荐人返利乐分
       */
      UserRecommendRelation sellerRecommendRelation =
          userRecommendRelationDao.findByUser(sellerEndUser);
      SystemConfig recommendSellerConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_SELLER);
      if (recommendSellerConfig != null && recommendSellerConfig.getConfigValue() != null
          && leScorePerConfig.getConfigValue() != null) {
        LeScoreRecord leScoreRecord = new LeScoreRecord();
        EndUser sellerRecommender = sellerRecommendRelation.getParent().getEndUser();
        leScoreRecord.setEndUser(sellerRecommender);
        leScoreRecord.setRecommender(sellerRecommendRelation.getEndUser().getNickName());
        leScoreRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
        leScoreRecord.setLeScoreType(LeScoreType.RECOMMEND_SELLER);
        BigDecimal income =
            rebateAmount.multiply(new BigDecimal(recommendSellerConfig.getConfigValue()));
        leScoreRecord.setAmount(income.multiply(new BigDecimal(leScorePerConfig.getConfigValue())));
        leScoreRecord.setUserCurLeScore(sellerRecommender.getCurLeScore().add(
            leScoreRecord.getAmount()));

        LeBeanRecord leBeanRecord = new LeBeanRecord();
        leBeanRecord.setAmount(income.subtract(leScoreRecord.getAmount()));
        leBeanRecord.setEndUser(sellerRecommender);
        leBeanRecord.setType(LeBeanChangeType.RECOMMEND_SELLER);
        leBeanRecord.setRecommender(sellerRecommendRelation.getEndUser().getNickName());
        leBeanRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
        leBeanRecord.setUserCurLeBean(sellerRecommender.getCurLeBean()
            .add(leBeanRecord.getAmount()));

        sellerRecommender.setCurLeBean(sellerRecommender.getCurLeBean().add(
            leBeanRecord.getAmount()));
        sellerRecommender.setTotalLeBean(sellerRecommender.getTotalLeBean().add(
            leBeanRecord.getAmount()));
        sellerRecommender.getLeBeanRecords().add(leBeanRecord);
        sellerRecommender.setCurLeScore(sellerRecommender.getCurLeScore().add(
            leScoreRecord.getAmount()));
        sellerRecommender.setTotalLeScore(sellerRecommender.getTotalLeScore().add(
            leScoreRecord.getAmount()));
        sellerRecommender.setRecommendLeScore(sellerRecommender.getRecommendLeScore().add(
            leScoreRecord.getAmount()));
        sellerRecommender.getLeScoreRecords().add(leScoreRecord);
        endUserDao.merge(sellerRecommender);
      }
    }


    /**
     * 分销商提成
     */
    SystemConfig agentCommissionConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.AGENT_COMMISSION);
    if (agentCommissionConfig != null && agentCommissionConfig.getConfigValue() != null) {
      BigDecimal agentAmount =
          rebateAmount.add(new BigDecimal(agentCommissionConfig.getConfigValue()));
      List<EndUser> agents = agentCommissionIncome(agentAmount, seller.getArea());
      endUserDao.merge(agents);
    }

    endUserDao.merge(sellerEndUser);
    orderDao.merge(order);
    return order;
  }

  private List<EndUser> agentCommissionIncome(BigDecimal agentAmount, Area area) {
    List<EndUser> records = new ArrayList<EndUser>();
    if (area != null) {
      EndUser agent = endUserDao.getAgentByArea(area);
      LeScoreRecord leScoreRecord = new LeScoreRecord();
      leScoreRecord.setEndUser(agent);
      leScoreRecord.setLeScoreType(LeScoreType.AGENT);
      leScoreRecord.setAmount(agentAmount);
      leScoreRecord.setUserCurLeScore(agent.getCurLeScore().add(leScoreRecord.getAmount()));
      agent.setAgentLeScore(agent.getAgentLeScore().add(agentAmount));
      agent.setCurLeScore(agent.getCurLeScore().add(agentAmount));
      agent.setTotalLeScore(agent.getTotalLeScore().add(agentAmount));
      agent.getLeScoreRecords().add(leScoreRecord);
      records.add(agent);
      records.addAll(agentCommissionIncome(agentAmount, area.getParent()));
    }
    return records;

  }

  private List<EndUser> userRecommendIncome(UserRecommendRelation userRecommendRelation,
      SystemConfig directUser, SystemConfig indirectUser, SystemConfig leScorePer, int i,
      BigDecimal rebateAmount) {

    List<EndUser> records = new ArrayList<EndUser>();
    if (userRecommendRelation.getParent() != null && directUser.getConfigValue() != null
        && indirectUser.getConfigValue() != null && leScorePer.getConfigValue() != null) {
      LeScoreRecord leScoreRecord = new LeScoreRecord();

      EndUser userRecommend = userRecommendRelation.getParent().getEndUser();
      leScoreRecord.setEndUser(userRecommend);
      leScoreRecord.setRecommender(userRecommendRelation.getEndUser().getNickName());
      leScoreRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
      leScoreRecord.setLeScoreType(LeScoreType.RECOMMEND_USER);
      BigDecimal income = new BigDecimal("0");
      if (i == 0) {
        income = rebateAmount.multiply(new BigDecimal(directUser.getConfigValue()));
        i++;
      } else {
        income = rebateAmount.multiply(new BigDecimal(indirectUser.getConfigValue()));
      }
      leScoreRecord.setAmount(income.multiply(new BigDecimal(leScorePer.getConfigValue())));
      leScoreRecord.setUserCurLeScore(userRecommend.getCurLeScore().add(leScoreRecord.getAmount()));

      LeBeanRecord leBeanRecord = new LeBeanRecord();
      leBeanRecord.setAmount(income.subtract(leScoreRecord.getAmount()));
      leBeanRecord.setEndUser(userRecommend);
      leBeanRecord.setType(LeBeanChangeType.RECOMMEND_USER);
      leBeanRecord.setRecommender(userRecommendRelation.getEndUser().getNickName());
      leBeanRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
      leBeanRecord.setUserCurLeBean(userRecommend.getCurLeBean().add(leBeanRecord.getAmount()));
      userRecommend.setCurLeBean(userRecommend.getCurLeBean().add(leBeanRecord.getAmount()));
      userRecommend.setTotalLeBean(userRecommend.getTotalLeBean().add(leBeanRecord.getAmount()));
      userRecommend.getLeBeanRecords().add(leBeanRecord);
      userRecommend.setCurLeScore(userRecommend.getCurLeScore().add(leScoreRecord.getAmount()));
      userRecommend.setTotalLeScore(userRecommend.getTotalLeScore().add(leScoreRecord.getAmount()));
      userRecommend.getLeScoreRecords().add(leScoreRecord);
      userRecommend.setRecommendLeScore(userRecommend.getRecommendLeScore().add(
          leScoreRecord.getAmount()));
      records.add(userRecommend);
      records.addAll(userRecommendIncome(userRecommendRelation.getParent(), directUser,
          indirectUser, leScorePer, i, rebateAmount));
    }
    return records;
  }

  @Override
  public Order getOrderBySn(String orderSn) {
    return orderDao.getOrderBySn(orderSn);
  }
}
