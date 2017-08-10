package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.dao.AreaDao;
import org.rebate.dao.ClearingOrderRelationDao;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SettingConfigDao;
import org.rebate.dao.SnDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.dao.UserRegReportDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.LeMindRecord;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.Sn.Type;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.UserRegReport;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.RebateType;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
import org.rebate.utils.TimeUtils;
import org.rebate.utils.ToolsUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("endUserServiceImpl")
public class EndUserServiceImpl extends BaseServiceImpl<EndUser, Long> implements EndUserService {

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "userRecommendRelationDaoImpl")
  private UserRecommendRelationDao userRecommendRelationDao;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "userRegReportDaoImpl")
  private UserRegReportDao userRegReportDao;

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "areaDaoImpl")
  private AreaDao areaDao;

  @Resource(name = "settingConfigDaoImpl")
  private SettingConfigDao settingConfigDao;

  @Resource(name = "snDaoImpl")
  private SnDao snDao;

  @Resource(name = "orderDaoImpl")
  private OrderDao orderDao;

  @Resource(name = "leScoreRecordDaoImpl")
  private LeScoreRecordDao leScoreRecordDao;

  @Resource(name = "clearingOrderRelationDaoImpl")
  private ClearingOrderRelationDao clearingOrderRelationDao;

  @Resource(name = "salesmanSellerRelationDaoImpl")
  private SalesmanSellerRelationDao salesmanSellerRelationDao;

  @Resource(name = "endUserDaoImpl")
  public void setBaseDao(EndUserDao endUserDao) {
    super.setBaseDao(endUserDao);
  }

  @Override
  public EndUser findByUserName(String userName) {
    return endUserDao.findByUserName(userName);
  }

  @Override
  public EndUser findByUserMobile(String mobileNo) {
    return endUserDao.findByUserMobile(mobileNo);
  }

  @Override
  public String getEndUserToken(Long id) {
    return endUserDao.getEndUserToken(id);
  }

  @Override
  public String createEndUserToken(String token, Long id) {
    return endUserDao.createEndUserToken(token, id);
  }

  @Override
  public AppPlatform getEndUserAppPlatform(Long id) {
    return endUserDao.getEndUserAppPlatform(id);
  }

  @Override
  public AppPlatform createEndUserAppPlatform(AppPlatform appPlatform, Long id) {
    return endUserDao.createEndUserAppPlatform(appPlatform, id);
  }

  @Override
  public void deleteEndUserToken(Long id) {
    endUserDao.deleteEndUserToken(id);
  }

  @Override
  public Boolean isRecommendLimited(String recommenderMobile) {
    SystemConfig recommendLimitConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.RECOMMEND_LEVEL_LIMIT);
    if (recommendLimitConfig == null || recommendLimitConfig.getConfigValue() == null) {
      return false;
    }
    Integer maxLevel = Integer.parseInt(recommendLimitConfig.getConfigValue());
    EndUser recommend = endUserDao.findByUserMobile(recommenderMobile);
    if (recommend == null) {
      return false;
    }
    List<UserRecommendRelation> relations = getRecommendRelations(recommend);
    if (CollectionUtils.isEmpty(relations)) {
      return false;
    }
    if (maxLevel <= relations.size()) {
      return true;
    }
    return false;
  }

  public List<UserRecommendRelation> getRecommendRelations(EndUser endUser) {
    List<UserRecommendRelation> relations = new ArrayList<UserRecommendRelation>();
    UserRecommendRelation relation = userRecommendRelationDao.findByUser(endUser);
    relations.add(relation);
    if (relation.getParent() != null) {
      relations.addAll(getRecommendRelations(relation.getParent().getEndUser()));
    }
    return relations;

  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser userReg(String cellPhoneNum, String password, String recommenderMobile) {
    EndUser regUser = new EndUser();
    regUser.setUserName(ToolsUtils.createUserName());
    regUser.setCellPhoneNum(cellPhoneNum);
    if (password != null) {
      regUser.setLoginPwd(DigestUtils.md5Hex(password));
    }
    regUser.setAccountStatus(AccountStatus.ACTIVED);
    regUser.setNickName("享个购" + ToolsUtils.createNickName());

    UserRecommendRelation relation = new UserRecommendRelation();
    relation.setEndUser(regUser);
    // relation.setStatus(CommonStatus.ACITVE);
    if (recommenderMobile != null) {
      EndUser recommend = endUserDao.findByUserMobile(recommenderMobile);
      if (recommend != null) {// 存在该推荐人
        regUser.setRecommender(recommend.getNickName());
        regUser.setRecommenderId(recommend.getId());
        regUser.setRecommenderMobile(recommend.getCellPhoneNum());

        UserRecommendRelation parent = userRecommendRelationDao.findByUser(recommend);
        relation.setParent(parent);
      } else {// 无推荐人

      }
    }

    endUserDao.persist(regUser);
    userRecommendRelationDao.persist(relation);

    Date cur = TimeUtils.formatDate2Day0(new Date());
    UserRegReport report = userRegReportDao.getRegReportByDate(cur);
    if (report != null) {
      report.setRegNum(report.getRegNum() + 1);
    } else {
      report = new UserRegReport();
      report.setStatisticsDate(cur);
      report.setRegNum(1);
    }
    userRegReportDao.merge(report);
    return regUser;
  }

  @Override
  public SMSVerificationCode createSmsCode(String cellPhoneNum, SMSVerificationCode smsCode) {
    return endUserDao.createSmsCode(cellPhoneNum, smsCode);
  }

  @Override
  public SMSVerificationCode getSmsCode(String cellPhone) {
    return endUserDao.getSmsCode(cellPhone);
  }

  @Override
  public void deleteSmsCode(String cellPhone) {
    endUserDao.deleteSmsCode(cellPhone);
  }

  @Override
  public Map<String, Object> isUserHasSeller(EndUser endUser) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (!CollectionUtils.isEmpty(endUser.getSellers())) {
      map.put("sellerStatus", "YES");
    } else {
      if (!CollectionUtils.isEmpty(endUser.getSellerApplications())) {
        for (SellerApplication application : endUser.getSellerApplications()) {
          if (ApplyStatus.AUDIT_WAITING.equals(application.getApplyStatus())) {
            map.put("sellerStatus", "AUDIT_WAITING"); // 待审核
          } else if (ApplyStatus.AUDIT_FAILED.equals(application.getApplyStatus())) {
            map.put("sellerStatus", "AUDIT_FAILED"); // 审核失败
            map.put("applyId", application.getId());
            map.put("failReason", application.getNotes());
            map.put("salesmanCellphone",
                salesmanSellerRelationDao.getRelationBySellerApplication(application).getEndUser()
                    .getCellPhoneNum());
          }
          break;
        }
      } else {
        map.put("sellerStatus", "NO");
      }
    }
    return map;
  }

  @Override
  public EndUser getAgentByArea(Area area) {
    return endUserDao.getAgentByArea(area);
  }


  public Map<String, String> getAvlRule(EndUser endUser) {
    Map<String, String> map = new HashMap<String, String>();
    // map.put("agentRule", null);
    // map.put("incomeRule", null);
    SettingConfig endUserRule =
        settingConfigDao.getConfigsByKey(SettingConfigKey.WITHDRAW_RULE_ENDUSER);
    map.put("motivateRule", endUserRule != null ? endUserRule.getConfigValue() : null);
    // if (!CollectionUtils.isEmpty(endUser.getSellers())) {
    // SettingConfig sellerRule =
    // settingConfigDao.getConfigsByKey(SettingConfigKey.WITHDRAW_RULE_SELLER);
    // map.put("incomeRule", sellerRule != null ? sellerRule.getConfigValue() : null);
    // }
    // if (endUser.getAgent() != null) {
    // SettingConfig agentRule =
    // settingConfigDao.getConfigsByKey(SettingConfigKey.WITHDRAW_RULE_AGENT);
    // map.put("agentRule", agentRule != null ? agentRule.getConfigValue() : null);
    // }
    return map;
  }

  @Override
  public Map<String, BigDecimal> getAvlLeScore(EndUser endUser) {
    SystemConfig minLimit = systemConfigDao.getConfigByKey(SystemConfigKey.WITHDRAW_MINIMUM_LIMIT);

    BigDecimal incomeScore = endUser.getIncomeLeScore();
    BigDecimal motivateScore = endUser.getMotivateLeScore();
    BigDecimal agentScore = endUser.getAgentLeScore();
    /**
     * 业务员直接收益乐分不足1乐分无法提取
     */
    incomeScore = incomeScore.setScale(0, BigDecimal.ROUND_DOWN);
    /**
     * 代理商提成乐分不足1乐分无法提取
     */
    agentScore = agentScore.setScale(0, BigDecimal.ROUND_DOWN);
    /**
     * 激励乐分满config才能提取
     */
    motivateScore =
        ((motivateScore.divide(new BigDecimal(minLimit.getConfigValue()))).setScale(0,
            BigDecimal.ROUND_DOWN)).multiply(new BigDecimal(minLimit.getConfigValue()));

    BigDecimal avlLeScore = incomeScore.add(motivateScore).add(agentScore);
    // BigDecimal avlLeScore = motivateScore.add(agentScore);
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    map.put("avlLeScore", avlLeScore);
    map.put("agentLeScore", agentScore);
    map.put("incomeLeScore", incomeScore);
    map.put("motivateLeScore", motivateScore);
    return map;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser userWithdraw(EndUser endUser, BigDecimal withdrawAmount, Long bankCardId,
      String remark) {
    BigDecimal agentAmount = endUser.getAgentLeScore();
    BigDecimal salesmanAmount = endUser.getIncomeLeScore();
    // BigDecimal userAmount = endUser.getMotivateLeScore();

    LeScoreRecord leScoreRecord = new LeScoreRecord();
    leScoreRecord.setRemark(remark);
    leScoreRecord.setEndUser(endUser);
    leScoreRecord.setLeScoreType(LeScoreType.WITHDRAW);
    leScoreRecord.setWithdrawStatus(ApplyStatus.AUDIT_WAITING);
    leScoreRecord.setAmount(withdrawAmount.negate());
    // 乐分提现顺序：代理商提成>业务员推荐商家>会员分红或推荐好友
    if (withdrawAmount.compareTo(agentAmount) > 0) {
      BigDecimal remainAmount = withdrawAmount.subtract(agentAmount);
      leScoreRecord.setAgentLeScore(agentAmount);
      if (remainAmount.compareTo(salesmanAmount) > 0) {
        leScoreRecord.setIncomeLeScore(salesmanAmount);
        leScoreRecord.setMotivateLeScore(remainAmount.subtract(salesmanAmount));
      } else {
        leScoreRecord.setIncomeLeScore(remainAmount);
      }
    } else {
      leScoreRecord.setAgentLeScore(withdrawAmount);
    }

    leScoreRecord.setWithDrawSn(snDao.generate(Type.WITHDRAW));
    leScoreRecord.setWithDrawType(bankCardId);
    leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
    // leScoreRecordDao.persist(leScoreRecord);

    endUser.getLeScoreRecords().add(leScoreRecord);
    endUser.setAgentLeScore(endUser.getAgentLeScore().subtract(leScoreRecord.getAgentLeScore()));
    if (leScoreRecord.getIncomeLeScore() != null) {
      endUser.setIncomeLeScore(endUser.getIncomeLeScore()
          .subtract(leScoreRecord.getIncomeLeScore()));
    }
    if (leScoreRecord.getMotivateLeScore() != null) {
      endUser.setMotivateLeScore(endUser.getMotivateLeScore().subtract(
          leScoreRecord.getMotivateLeScore()));
    }

    endUser.setCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
    endUserDao.merge(endUser);

    // Seller seller = endUser.getSeller();
    // List<Filter> filters = new ArrayList<Filter>();
    // filters.add(Filter.eq("seller", seller));
    // filters.add(Filter.eq("isClearing", false));
    // List<Order> orders = orderDao.findList(null, null, filters, null);
    //
    // List<ClearingOrderRelation> clearingOrderRelations = new ArrayList<ClearingOrderRelation>();
    // for (Order order : orders) {
    // ClearingOrderRelation clearingOrderRelation = new ClearingOrderRelation();
    // clearingOrderRelation.setClearingRecId(leScoreRecord.getId());
    // clearingOrderRelation.setOrder(order);
    // clearingOrderRelations.add(clearingOrderRelation);
    // }
    //
    // if (!CollectionUtils.isEmpty(clearingOrderRelations)) {
    // clearingOrderRelationDao.persist(clearingOrderRelations);
    // }
    // if (!CollectionUtils.isEmpty(endUser.getSellers())) {
    // for (Seller seller : endUser.getSellers()) {
    // seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(map.get("incomeLeScore")));
    // sellerDao.merge(seller);
    // break;
    // }
    // }
    return endUser;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser userWithdraw(Long userId, Long bankCardId, String remark) {
    EndUser endUser = endUserDao.find(userId);
    Map<String, BigDecimal> map = getAvlLeScore(endUser);
    if (map.get("avlLeScore").compareTo(new BigDecimal(0)) <= 0) {
      return null;
    }

    LeScoreRecord leScoreRecord = new LeScoreRecord();
    leScoreRecord.setRemark(remark);
    leScoreRecord.setEndUser(endUser);
    leScoreRecord.setLeScoreType(LeScoreType.WITHDRAW);
    leScoreRecord.setWithdrawStatus(ApplyStatus.AUDIT_WAITING);
    leScoreRecord.setAmount(map.get("avlLeScore").negate());
    leScoreRecord.setMotivateLeScore(map.get("motivateLeScore"));
    leScoreRecord.setIncomeLeScore(map.get("incomeLeScore"));
    leScoreRecord.setAgentLeScore(map.get("agentLeScore"));
    leScoreRecord.setWithDrawSn(snDao.generate(Type.WITHDRAW));
    leScoreRecord.setWithDrawType(bankCardId);
    leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
    // leScoreRecordDao.persist(leScoreRecord);

    endUser.getLeScoreRecords().add(leScoreRecord);
    endUser.setAgentLeScore(endUser.getAgentLeScore().subtract(map.get("agentLeScore")));
    endUser.setIncomeLeScore(endUser.getIncomeLeScore().subtract(map.get("incomeLeScore")));
    endUser.setMotivateLeScore(endUser.getMotivateLeScore().subtract(map.get("motivateLeScore")));
    endUser.setCurLeScore(endUser.getCurLeScore().subtract(map.get("avlLeScore")));
    endUserDao.merge(endUser);

    // Seller seller = endUser.getSeller();
    // List<Filter> filters = new ArrayList<Filter>();
    // filters.add(Filter.eq("seller", seller));
    // filters.add(Filter.eq("isClearing", false));
    // List<Order> orders = orderDao.findList(null, null, filters, null);
    //
    // List<ClearingOrderRelation> clearingOrderRelations = new ArrayList<ClearingOrderRelation>();
    // for (Order order : orders) {
    // ClearingOrderRelation clearingOrderRelation = new ClearingOrderRelation();
    // clearingOrderRelation.setClearingRecId(leScoreRecord.getId());
    // clearingOrderRelation.setOrder(order);
    // clearingOrderRelations.add(clearingOrderRelation);
    // }
    //
    // if (!CollectionUtils.isEmpty(clearingOrderRelations)) {
    // clearingOrderRelationDao.persist(clearingOrderRelations);
    // }
    // if (!CollectionUtils.isEmpty(endUser.getSellers())) {
    // for (Seller seller : endUser.getSellers()) {
    // seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(map.get("incomeLeScore")));
    // sellerDao.merge(seller);
    // break;
    // }
    // }
    return endUser;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser editInfo(Long userId, Long areaId, String nickName) {
    EndUser endUser = endUserDao.find(userId);
    List<EndUser> endUsers = new ArrayList<EndUser>();
    if (nickName != null) {// 修改昵称
      endUser.setNickName(nickName);
      List<Filter> filters = new ArrayList<Filter>();
      Filter recommendFilter = new Filter("recommenderId", Operator.eq, userId);
      filters.add(recommendFilter);
      List<EndUser> recommendUsers = endUserDao.findList(null, null, filters, null);
      for (EndUser user : recommendUsers) {
        user.setRecommender(nickName);
      }
      endUsers.addAll(recommendUsers);
    }
    if (areaId != null) {// 修改所在地区
      Area area = areaDao.find(areaId);
      if (!CollectionUtils.isEmpty(area.getChildren())) {
        return null;
      }
      endUser.setArea(area);
      Area cityArea = area.getParent();
      if (cityArea != null) {
        Area provinceArea = cityArea.getParent();
        if (provinceArea != null) {// 三级行政单位 省市区
          endUser.setCity(cityArea.getId());
          endUser.setProvince(provinceArea.getId());
        } else {// 二级行政单位 省市
          endUser.setCity(areaId);
          endUser.setProvince(cityArea.getId());
        }
      }
    }
    endUsers.add(endUser);
    endUserDao.merge(endUsers);
    return endUser;
  }


  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser changeUserMobile(EndUser endUser) {
    Seller seller = endUser.getSeller();
    if (seller != null) {
      seller.setContactCellPhone(endUser.getCellPhoneNum());
      sellerDao.merge(seller);
    }
    endUserDao.merge(endUser);
    return endUser;
  }

  @Override
  public void transferRebate(EndUser transUser, EndUser receiver, RebateType transType,
      BigDecimal amount) {
    if (RebateType.LE_MIND.equals(transType)) {
      LeMindRecord transRecord = new LeMindRecord();
      transRecord.setEndUser(transUser);
      transRecord.setAmount(amount.negate());
      transRecord.setUserCurLeMind(transUser.getCurLeMind().add(transRecord.getAmount()));
      transRecord.setRemark("您向好友转账:" + receiver.getCellPhoneNum());
      transUser.getLeMindRecords().add(transRecord);
      transUser.setCurLeMind(transUser.getCurLeMind().subtract(amount));

      LeMindRecord receiverRecord = new LeMindRecord();
      receiverRecord.setEndUser(receiver);
      receiverRecord.setAmount(amount);
      receiverRecord.setUserCurLeMind(receiver.getCurLeMind().add(receiverRecord.getAmount()));
      receiverRecord.setRemark("好友向您转账:" + transUser.getCellPhoneNum());
      receiver.getLeMindRecords().add(receiverRecord);
      receiver.setCurLeMind(receiver.getCurLeMind().add(amount));
    }
    if (RebateType.LE_BEAN.equals(transType)) {
      LeBeanRecord transLeBeanRecord = new LeBeanRecord();
      transLeBeanRecord.setAmount(amount.negate());
      transLeBeanRecord.setEndUser(transUser);
      transLeBeanRecord.setRemark("您向好友转账");
      transLeBeanRecord.setType(LeBeanChangeType.TRANSFER);
      transLeBeanRecord.setRecommender(receiver.getNickName());
      transLeBeanRecord.setRecommenderPhoto(receiver.getUserPhoto());
      transLeBeanRecord.setUserCurLeBean(transUser.getCurLeBean()
          .add(transLeBeanRecord.getAmount()));
      transUser.getLeBeanRecords().add(transLeBeanRecord);
      transUser.setCurLeBean(transLeBeanRecord.getUserCurLeBean());


      LeBeanRecord receiverLeBeanRecord = new LeBeanRecord();
      receiverLeBeanRecord.setAmount(amount);
      receiverLeBeanRecord.setEndUser(receiver);
      receiverLeBeanRecord.setRemark("好友向您转账");
      receiverLeBeanRecord.setType(LeBeanChangeType.TRANSFER);
      receiverLeBeanRecord.setRecommender(transUser.getNickName());
      receiverLeBeanRecord.setRecommenderPhoto(transUser.getUserPhoto());
      receiverLeBeanRecord.setUserCurLeBean(receiver.getCurLeBean().add(
          receiverLeBeanRecord.getAmount()));
      receiver.getLeBeanRecords().add(receiverLeBeanRecord);
      receiver.setCurLeBean(receiverLeBeanRecord.getUserCurLeBean());

    }
    if (RebateType.LE_SCORE.equals(transType)) {
      LeScoreRecord transLeScoreRecord = new LeScoreRecord();
      transLeScoreRecord.setEndUser(transUser);
      transLeScoreRecord.setRemark("您向好友转账");
      transLeScoreRecord.setLeScoreType(LeScoreType.TRANSFER);
      transLeScoreRecord.setAmount(amount.negate());
      transLeScoreRecord.setRecommender(receiver.getNickName());
      transLeScoreRecord.setRecommenderPhoto(receiver.getUserPhoto());
      transLeScoreRecord.setUserCurLeScore(transUser.getCurLeScore().add(
          transLeScoreRecord.getAmount()));

      if (transUser.getMotivateLeScore().compareTo(amount) >= 0) {// 乐分支付优先消耗激励乐分
        transUser.setMotivateLeScore(transUser.getMotivateLeScore().subtract(amount));
        transLeScoreRecord.setMotivateLeScore(amount);
      } else {
        BigDecimal diff = amount.subtract(transUser.getMotivateLeScore());
        transLeScoreRecord.setMotivateLeScore(transUser.getMotivateLeScore());
        transUser.setMotivateLeScore(new BigDecimal(0));
        if (transUser.getIncomeLeScore().compareTo(diff) >= 0) {// 然后消耗业务员乐分
          transLeScoreRecord.setIncomeLeScore(diff);
          transUser.setIncomeLeScore(transUser.getIncomeLeScore().subtract(diff));
        } else {// 最后消耗代理商乐分
          BigDecimal remain = diff.subtract(transUser.getIncomeLeScore());
          transLeScoreRecord.setIncomeLeScore(transUser.getIncomeLeScore());
          transLeScoreRecord.setAgentLeScore(remain);
          transUser.setIncomeLeScore(new BigDecimal(0));
          transUser.setAgentLeScore(transUser.getAgentLeScore().subtract(remain));
        }
      }
      transUser.getLeScoreRecords().add(transLeScoreRecord);
      transUser.setCurLeScore(transUser.getCurLeScore().add(transLeScoreRecord.getAmount()));


      LeScoreRecord receiverLeScoreRecord = new LeScoreRecord();
      receiverLeScoreRecord.setEndUser(receiver);
      receiverLeScoreRecord.setRemark("好友向您转账");
      receiverLeScoreRecord.setLeScoreType(LeScoreType.TRANSFER);
      receiverLeScoreRecord.setAmount(amount);
      receiverLeScoreRecord.setRecommender(transUser.getNickName());
      receiverLeScoreRecord.setRecommenderPhoto(transUser.getUserPhoto());
      receiverLeScoreRecord.setUserCurLeScore(receiver.getCurLeScore().add(
          receiverLeScoreRecord.getAmount()));


      if (transLeScoreRecord.getMotivateLeScore() != null) {
        receiverLeScoreRecord.setMotivateLeScore(transLeScoreRecord.getMotivateLeScore());
        receiver.setMotivateLeScore(receiver.getMotivateLeScore().add(
            transLeScoreRecord.getMotivateLeScore()));
      }
      if (transLeScoreRecord.getIncomeLeScore() != null) {
        receiverLeScoreRecord.setIncomeLeScore(transLeScoreRecord.getIncomeLeScore());
        receiver.setIncomeLeScore(receiver.getIncomeLeScore().add(
            transLeScoreRecord.getIncomeLeScore()));
      }
      if (transLeScoreRecord.getAgentLeScore() != null) {
        receiverLeScoreRecord.setAgentLeScore(transLeScoreRecord.getAgentLeScore());
        receiver.setAgentLeScore(receiver.getAgentLeScore().add(
            transLeScoreRecord.getAgentLeScore()));
      }
      receiver.getLeScoreRecords().add(receiverLeScoreRecord);
      receiver.setCurLeScore(receiver.getCurLeScore().add(receiverLeScoreRecord.getAmount()));
    }

    endUserDao.merge(transUser);
    endUserDao.merge(receiver);
  }

}
