package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.rebate.beans.Message;
import org.rebate.dao.AgentCommissionConfigDao;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.LeMindRecord;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Order;
import org.rebate.entity.RebateRecord;
import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.Seller;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.OrderService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

  @Resource(name = "orderDaoImpl")
  private OrderDao orderDao;

  @Resource(name = "salesmanSellerRelationDaoImpl")
  private SalesmanSellerRelationDao salesmanSellerRelationDao;

  @Resource(name = "userRecommendRelationDaoImpl")
  private UserRecommendRelationDao userRecommendRelationDao;

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "agentCommissionConfigDaoImpl")
  private AgentCommissionConfigDao agentCommissionConfigDao;

  @Resource(name = "orderDaoImpl")
  public void setBaseDao(OrderDao orderDao) {
    super.setBaseDao(orderDao);
  }

  @Override
  public BigDecimal getPayOrderBeanDeductForSeller(Long sellerId) {
    Date curDate = new Date();
    Date startTime = TimeUtils.formatDate2Day0(curDate);
    Date endTime = TimeUtils.formatDate2Day59(curDate);
    List<Order> orders = getPayOrderForSerllerByDay(startTime, endTime, sellerId);
    BigDecimal beanDeductAmount = new BigDecimal(0);
    if (CollectionUtils.isEmpty(orders)) {
      return beanDeductAmount;
    }
    for (Order order : orders) {
      if (BooleanUtils.isTrue(order.getIsBeanPay()) && order.getDeductAmount() != null) {
        beanDeductAmount = beanDeductAmount.add(order.getDeductAmount());
      }

    }
    return beanDeductAmount;
  }

  @Override
  public BigDecimal getPayOrderAmountForSeller(Long sellerId) {
    Date curDate = new Date();
    Date startTime = TimeUtils.formatDate2Day0(curDate);
    Date endTime = TimeUtils.formatDate2Day59(curDate);
    List<Order> orders = getPayOrderForSerllerByDay(startTime, endTime, sellerId);
    BigDecimal amount = new BigDecimal(0);
    if (CollectionUtils.isEmpty(orders)) {
      return amount;
    }
    for (Order order : orders) {
      amount = amount.add(order.getAmount());
    }
    return amount;
  }

  public List<Order> getPayOrderForSerllerByDay(Date startTime, Date endTime, Long sellerId) {

    List<Filter> filters = new ArrayList<Filter>();
    Filter startFilter = new Filter("paymentTime", Operator.ge, startTime);
    Filter endFilter = new Filter("paymentTime", Operator.le, endTime);
    Filter statusFilter = new Filter("status", Operator.ne, OrderStatus.UNPAID);
    Filter sellerFilter = new Filter("seller", Operator.eq, sellerId);
    filters.add(statusFilter);
    filters.add(startFilter);
    filters.add(endFilter);
    filters.add(sellerFilter);
    List<Order> orders = orderDao.findList(null, null, filters, null);
    return orders;

  }


  @Override
  public Order updateSallerOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderDao.find(orderId);
    if (OrderStatus.PAID.equals(status)) {
      if (!OrderStatus.UNPAID.equals(order.getStatus())) {
        return order;
      }
      order.setStatus(status);
      order.setPaymentTime(new Date());
      // order.setPaymentType("线下支付");
    }

    return updateOrderInfo(order);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Order updateOrderInfo(Order order) {

    Long orderId = order.getId();
    EndUser endUser = order.getEndUser();
    Seller seller = order.getSeller();
    EndUser sellerEndUser = seller.getEndUser();

    if (BooleanUtils.isFalse(order.getIsSallerOrder())) {
      seller
          .setTotalOrderNum((seller.getTotalOrderNum() != null ? seller.getTotalOrderNum() : 0) + 1);
      seller.setTotalOrderAmount(seller.getTotalOrderAmount().add(order.getSellerIncome()));
      seller.setUnClearingAmount(seller.getUnClearingAmount().add(order.getSellerIncome()));
      sellerDao.merge(seller);
    }

    if (BooleanUtils.isTrue(order.getIsSallerOrder())) {
      seller.setTotalSellerOrderNum((seller.getTotalSellerOrderNum() != null ? seller
          .getTotalSellerOrderNum() : 0) + 1);
      seller.setTotalSellerOrderAmount((seller.getTotalSellerOrderAmount() != null ? seller
          .getTotalSellerOrderAmount() : new BigDecimal(0)).add(order.getRebateAmount()));
      sellerDao.merge(seller);
    }
    /**
     * 消费后商家的直接收益
     */
    // LeScoreRecord scoreRecord = new LeScoreRecord();
    // scoreRecord.setOrderId(orderId);
    // scoreRecord.setEndUser(sellerEndUser);
    // scoreRecord.setSeller(seller);
    // scoreRecord.setLeScoreType(LeScoreType.CONSUME_SELLER);
    // scoreRecord.setRemark(order.getPaymentType());
    // scoreRecord.setAmount(order.getSellerIncome());
    // scoreRecord.setUserCurLeScore(sellerEndUser.getCurLeScore().add(scoreRecord.getAmount()));
    // sellerEndUser.setCurLeScore(sellerEndUser.getCurLeScore().add(order.getSellerIncome()));
    // sellerEndUser.setTotalLeScore(sellerEndUser.getTotalLeScore().add(order.getSellerIncome()));
    // sellerEndUser.setIncomeLeScore(sellerEndUser.getIncomeLeScore().add(order.getSellerIncome()));
    // sellerEndUser.getLeScoreRecords().add(scoreRecord);

    // endUserDao.merge(sellerEndUser);


    if (order.getIsBeanPay() && order.getDeductAmount().compareTo(new BigDecimal(0)) > 0) {// 支付中含有乐豆抵扣
      LeBeanRecord leBeanRecord = new LeBeanRecord();
      leBeanRecord.setOrderId(orderId);
      leBeanRecord.setAmount(order.getDeductAmount().negate());
      leBeanRecord.setEndUser(endUser);
      leBeanRecord.setSeller(seller);

      leBeanRecord.setType(LeBeanChangeType.CONSUME);
      leBeanRecord.setUserCurLeBean(endUser.getCurLeBean().add(leBeanRecord.getAmount()));
      endUser.setCurLeBean(leBeanRecord.getUserCurLeBean());
      endUser.getLeBeanRecords().add(leBeanRecord);
      endUserDao.merge(endUser);
    }

    SystemConfig mindDivideConfig = systemConfigDao.getConfigByKey(SystemConfigKey.MIND_DIVIDE);
    // SystemConfig maxBonusPerConfig =
    // systemConfigDao.getConfigByKey(SystemConfigKey.BONUS_MAXIMUM);



    /**
     * 消费后用户积分返利 (乐豆消费无积分返利)
     */
    if (order.getUserScore() != null) {
      /**
       * 鼓励金收益,直接转化为乐豆
       */
      if (order.getEncourageAmount() != null) {
        // LeScoreRecord leScoreRecord = new LeScoreRecord();
        // leScoreRecord.setOrderId(orderId);
        // leScoreRecord.setEndUser(endUser);
        // leScoreRecord.setLeScoreType(LeScoreType.ENCOURAGE);
        // leScoreRecord.setAmount(order.getEncourageAmount());
        // leScoreRecord.setSeller(seller);
        // leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
        // endUser.getLeScoreRecords().add(leScoreRecord);
        // endUser.setCurLeScore(leScoreRecord.getUserCurLeScore());
        // endUser.setTotalLeScore(endUser.getTotalLeScore().add(leScoreRecord.getAmount()));
        // endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(leScoreRecord.getAmount()));
        LeBeanRecord leBeanRecord = new LeBeanRecord();
        leBeanRecord.setOrderId(orderId);
        leBeanRecord.setEndUser(endUser);
        leBeanRecord.setType(LeBeanChangeType.ENCOURAGE);
        leBeanRecord.setAmount(order.getEncourageAmount());
        leBeanRecord.setSeller(seller);
        leBeanRecord.setUserCurLeBean(endUser.getCurLeBean().add(leBeanRecord.getAmount()));
        endUser.getLeBeanRecords().add(leBeanRecord);
        endUser.setCurLeBean(leBeanRecord.getUserCurLeBean());
        endUser.setTotalLeBean(endUser.getTotalLeBean().add(leBeanRecord.getAmount()));
      }

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

      if (mindDivideConfig != null && mindDivideConfig.getConfigValue() != null) {
        BigDecimal divideMind = new BigDecimal(mindDivideConfig.getConfigValue());
        BigDecimal mind = endUser.getCurScore().divide(divideMind, 0, BigDecimal.ROUND_DOWN);
        if (mind.compareTo(new BigDecimal(1)) >= 0) {
          LeMindRecord leMindRecord = new LeMindRecord();
          leMindRecord.setEndUser(endUser);
          leMindRecord.setAmount(mind);
          leMindRecord.setScore(mind.multiply(divideMind));
          leMindRecord.setStatus(CommonStatus.ACITVE);
          leMindRecord.setUserCurLeMind(endUser.getCurLeMind().add(mind));

          // leMindRecord.setMaxBonus(mind.multiply(divideMind));
          // if (maxBonusPerConfig != null && maxBonusPerConfig.getConfigValue() != null) {
          // leMindRecord.setMaxBonus(mind.multiply(new BigDecimal(maxBonusPerConfig
          // .getConfigValue())));
          // }

          endUser.getLeMindRecords().add(leMindRecord);
          endUser.setCurLeMind(leMindRecord.getUserCurLeMind());
          endUser.setTotalLeMind(endUser.getTotalLeMind().add(mind));

          RebateRecord deductScore = new RebateRecord();
          deductScore.setRemark(Message.success("rebate.endUser.score.mind", divideMind.toString())
              .getContent());
          deductScore.setEndUser(endUser);
          deductScore.setRebateScore(mind.multiply(divideMind).negate());
          deductScore.setUserCurScore(endUser.getCurScore().add(deductScore.getRebateScore()));
          endUser.getRebateRecords().add(deductScore);
          endUser.setCurScore(deductScore.getUserCurScore());
        }
      }
      endUserDao.merge(endUser);
    }
    /**
     * 消费后商家积分返利
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

      if (mindDivideConfig != null && mindDivideConfig.getConfigValue() != null) {
        BigDecimal divideMind = new BigDecimal(mindDivideConfig.getConfigValue());
        BigDecimal mind = sellerEndUser.getCurScore().divide(divideMind, 0, BigDecimal.ROUND_DOWN);
        if (mind.compareTo(new BigDecimal(1)) >= 0) {
          LeMindRecord leMindRecord = new LeMindRecord();
          leMindRecord.setEndUser(sellerEndUser);
          leMindRecord.setAmount(mind);
          leMindRecord.setScore(mind.multiply(divideMind));
          leMindRecord.setStatus(CommonStatus.ACITVE);
          leMindRecord.setUserCurLeMind(sellerEndUser.getCurLeMind().add(mind));

          // leMindRecord.setMaxBonus(mind.multiply(divideMind));
          // if (maxBonusPerConfig != null && maxBonusPerConfig.getConfigValue() != null) {
          // leMindRecord.setMaxBonus(mind.multiply(new BigDecimal(maxBonusPerConfig
          // .getConfigValue())));
          // }
          sellerEndUser.getLeMindRecords().add(leMindRecord);
          sellerEndUser.setCurLeMind(leMindRecord.getUserCurLeMind());
          sellerEndUser.setTotalLeMind(sellerEndUser.getTotalLeMind().add(mind));

          RebateRecord deductScore = new RebateRecord();
          deductScore.setRemark(Message.success("rebate.endUser.score.mind", divideMind.toString())
              .getContent());
          deductScore.setEndUser(sellerEndUser);
          deductScore.setRebateScore(mind.multiply(divideMind).negate());
          deductScore
              .setUserCurScore(sellerEndUser.getCurScore().add(deductScore.getRebateScore()));
          sellerEndUser.getRebateRecords().add(deductScore);
          sellerEndUser.setCurScore(deductScore.getUserCurScore());

        }
      }
    }
    endUserDao.merge(sellerEndUser);
    /**
     * 乐豆消费无推荐返利,无鼓励金
     */
    BigDecimal rebateAmount = order.getAmount().subtract(order.getSellerIncome());
    // if (!order.getIsBeanPay()) {

    // SystemConfig leScorePerConfig =
    // systemConfigDao.getConfigByKey(SystemConfigKey.LESCORE_PERCENTAGE);

    /**
     * 用户消费后推荐人返利乐分
     */
    UserRecommendRelation userRecommendRelation = userRecommendRelationDao.findByUser(endUser);
    SystemConfig directUserConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_DIRECT_USER);
    SystemConfig indirectUserConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_INDIRECT_USER);
    SystemConfig limitConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_LEVEL_LIMIT);
    if (directUserConfig != null && directUserConfig.getConfigValue() != null
        && indirectUserConfig != null && indirectUserConfig.getConfigValue() != null
        && limitConfig != null && limitConfig.getConfigValue() != null) {
      List<EndUser> records =
          userRecommendIncome(userRecommendRelation, directUserConfig, indirectUserConfig, null, 0,
              rebateAmount, limitConfig, orderId, endUser);
      if (!CollectionUtils.isEmpty(records)) {
        endUserDao.merge(records);
      }
    }

    /**
     * 商家消费收益后业务员返利乐分
     */
    SalesmanSellerRelation salesmanSellerRelation =
        salesmanSellerRelationDao.getRelationBySeller(seller);
    SystemConfig recommendSellerConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_SELLER);
    if (recommendSellerConfig != null && recommendSellerConfig.getConfigValue() != null
        && new BigDecimal(recommendSellerConfig.getConfigValue()).compareTo(new BigDecimal(0)) > 0
        && salesmanSellerRelation != null) {
      LeScoreRecord leScoreRecord = new LeScoreRecord();
      leScoreRecord.setOrderId(orderId);
      EndUser salesman = salesmanSellerRelation.getEndUser();
      leScoreRecord.setEndUser(salesman);
      leScoreRecord.setRecommender(seller.getName());
      leScoreRecord.setRecommenderPhoto(seller.getStorePictureUrl());
      leScoreRecord.setLeScoreType(LeScoreType.RECOMMEND_SELLER);
      BigDecimal income =
          rebateAmount.multiply(new BigDecimal(recommendSellerConfig.getConfigValue()));
      // leScoreRecord.setAmount(income.multiply(new
      // BigDecimal(leScorePerConfig.getConfigValue())));
      leScoreRecord.setAmount(income);
      leScoreRecord.setUserCurLeScore(salesman.getCurLeScore().add(leScoreRecord.getAmount()));

      // LeBeanRecord leBeanRecord = new LeBeanRecord();
      // leBeanRecord.setAmount(income.subtract(leScoreRecord.getAmount()));
      // leBeanRecord.setEndUser(sellerRecommender);
      // leBeanRecord.setType(LeBeanChangeType.RECOMMEND_SELLER);
      // leBeanRecord.setRecommender(sellerRecommendRelation.getEndUser().getNickName());
      // leBeanRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
      // leBeanRecord.setUserCurLeBean(sellerRecommender.getCurLeBean()
      // .add(leBeanRecord.getAmount()));
      // sellerRecommender.setCurLeBean(sellerRecommender.getCurLeBean().add(
      // leBeanRecord.getAmount()));
      // sellerRecommender.setTotalLeBean(sellerRecommender.getTotalLeBean().add(
      // leBeanRecord.getAmount()));
      // sellerRecommender.getLeBeanRecords().add(leBeanRecord);
      salesman.setCurLeScore(salesman.getCurLeScore().add(leScoreRecord.getAmount()));
      salesman.setTotalLeScore(salesman.getTotalLeScore().add(leScoreRecord.getAmount()));
      salesman.setIncomeLeScore(salesman.getIncomeLeScore().add(leScoreRecord.getAmount()));
      // salesman.setMotivateLeScore(salesman.getMotivateLeScore().add(leScoreRecord.getAmount()));
      salesman.getLeScoreRecords().add(leScoreRecord);
      endUserDao.merge(salesman);

      salesmanSellerRelation.setTotalRecommendLeScore(salesmanSellerRelation
          .getTotalRecommendLeScore().add(leScoreRecord.getAmount()));
      salesmanSellerRelationDao.merge(salesmanSellerRelation);
    }


    /**
     * 分销商提成
     */
    List<EndUser> agents = agentCommissionIncome(rebateAmount, seller.getArea());
    if (!CollectionUtils.isEmpty(agents)) {
      endUserDao.merge(agents);
    }

    orderDao.merge(order);
    LogUtil
        .debug(
            OrderServiceImpl.class,
            "updateOrderInfo",
            "update seller order info finished manually. orderId: %s,sn: %s,orderStatus: %s,payTime: %s",
            order.getId(), order.getSn(), order.getStatus().toString(), order.getPaymentTime());
    return order;
  }


  private List<EndUser> agentCommissionIncome(BigDecimal rebateAmount, Area area) {
    List<EndUser> records = new ArrayList<EndUser>();
    if (area != null) {
      EndUser agent = endUserDao.getAgentByArea(area);

      if (agent != null) {
        AgentCommissionConfig agentCommissionConfig =
            agentCommissionConfigDao.getConfigByArea(area);
        if (agentCommissionConfig != null && agentCommissionConfig.getCommissionRate() != null
            && agentCommissionConfig.getCommissionRate().compareTo(new BigDecimal(0)) > 0) {
          LeScoreRecord leScoreRecord = new LeScoreRecord();
          leScoreRecord.setEndUser(agent);
          leScoreRecord.setLeScoreType(LeScoreType.AGENT);
          BigDecimal agentAmount = rebateAmount.multiply(agentCommissionConfig.getCommissionRate());
          leScoreRecord.setAmount(agentAmount);
          leScoreRecord.setUserCurLeScore(agent.getCurLeScore().add(leScoreRecord.getAmount()));
          // agent.setIncomeLeScore(agent.getIncomeLeScore().add(agentAmount));
          agent.setAgentLeScore(agent.getAgentLeScore().add(agentAmount));
          agent.setCurLeScore(agent.getCurLeScore().add(agentAmount));
          agent.setTotalLeScore(agent.getTotalLeScore().add(agentAmount));
          agent.getLeScoreRecords().add(leScoreRecord);
          records.add(agent);
        }

      }

      records.addAll(agentCommissionIncome(rebateAmount, area.getParent()));
    }
    return records;

  }

  private List<EndUser> userRecommendIncome(UserRecommendRelation userRecommendRelation,
      SystemConfig directUser, SystemConfig indirectUser, SystemConfig leScorePer, int i,
      BigDecimal rebateAmount, SystemConfig limitConfig, Long orderId, EndUser consumeUser) {

    List<EndUser> records = new ArrayList<EndUser>();
    Integer limitLevel = Integer.parseInt(limitConfig.getConfigValue());
    if (i >= limitLevel) {
      return records;
    }
    if (userRecommendRelation.getParent() != null) {
      LeScoreRecord leScoreRecord = new LeScoreRecord();
      leScoreRecord.setOrderId(orderId);
      EndUser userRecommend = userRecommendRelation.getParent().getEndUser();
      leScoreRecord.setEndUser(userRecommend);
      leScoreRecord.setRecommender(consumeUser.getNickName());
      leScoreRecord.setRecommenderPhoto(consumeUser.getUserPhoto());
      leScoreRecord.setLeScoreType(LeScoreType.RECOMMEND_USER);
      BigDecimal income = new BigDecimal("0");
      if (i == 0) {
        income = rebateAmount.multiply(new BigDecimal(directUser.getConfigValue()));
      } else {
        income = rebateAmount.multiply(new BigDecimal(indirectUser.getConfigValue()));
      }
      i++;
      // leScoreRecord.setAmount(income.multiply(new BigDecimal(leScorePer.getConfigValue())));
      leScoreRecord.setAmount(income);
      leScoreRecord.setUserCurLeScore(userRecommend.getCurLeScore().add(leScoreRecord.getAmount()));

      // LeBeanRecord leBeanRecord = new LeBeanRecord();
      // leBeanRecord.setAmount(income.subtract(leScoreRecord.getAmount()));
      // leBeanRecord.setEndUser(userRecommend);
      // leBeanRecord.setType(LeBeanChangeType.RECOMMEND_USER);
      // leBeanRecord.setRecommender(userRecommendRelation.getEndUser().getNickName());
      // leBeanRecord.setRecommenderPhoto(userRecommendRelation.getEndUser().getUserPhoto());
      // leBeanRecord.setUserCurLeBean(userRecommend.getCurLeBean().add(leBeanRecord.getAmount()));
      // userRecommend.setCurLeBean(userRecommend.getCurLeBean().add(leBeanRecord.getAmount()));
      // userRecommend.setTotalLeBean(userRecommend.getTotalLeBean().add(leBeanRecord.getAmount()));
      // userRecommend.getLeBeanRecords().add(leBeanRecord);
      if (income.compareTo(new BigDecimal(0)) > 0) {
        userRecommend.setCurLeScore(userRecommend.getCurLeScore().add(leScoreRecord.getAmount()));
        userRecommend.setTotalLeScore(userRecommend.getTotalLeScore()
            .add(leScoreRecord.getAmount()));
        userRecommend.getLeScoreRecords().add(leScoreRecord);
        userRecommend.setMotivateLeScore(userRecommend.getMotivateLeScore().add(
            leScoreRecord.getAmount()));

        userRecommendRelation.setTotalRecommendLeScore(userRecommendRelation
            .getTotalRecommendLeScore().add(leScoreRecord.getAmount()));
        userRecommendRelationDao.merge(userRecommendRelation);
        records.add(userRecommend);
      }

      records.addAll(userRecommendIncome(userRecommendRelation.getParent(), directUser,
          indirectUser, leScorePer, i, rebateAmount, limitConfig, orderId, consumeUser));
    }
    return records;
  }
}
