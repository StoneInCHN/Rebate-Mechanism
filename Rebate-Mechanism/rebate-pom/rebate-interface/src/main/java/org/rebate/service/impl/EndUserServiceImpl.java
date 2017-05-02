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
import org.rebate.dao.EndUserDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.dao.UserRegReportDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.UserRegReport;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
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
    regUser.setNickName(cellPhoneNum.substring(3, 7));
    UserRecommendRelation relation = new UserRecommendRelation();
    relation.setEndUser(regUser);
    // relation.setStatus(CommonStatus.ACITVE);
    if (recommenderMobile != null) {
      EndUser recommend = endUserDao.findByUserMobile(recommenderMobile);
      if (recommend != null) {// 存在该推荐人
        regUser.setRecommender(recommend.getNickName());
        regUser.setRecommenderId(recommend.getId());

        UserRecommendRelation parent = userRecommendRelationDao.findByUser(recommend);
        relation.setParent(parent);
      } else {// 无推荐人

      }
    }

    endUserDao.persist(regUser);
    userRecommendRelationDao.persist(relation);

    Date cur = TimeUtils.formatDate2Day(new Date());
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

  @Override
  public Map<String, BigDecimal> getAvlLeScore(EndUser endUser) {
    SystemConfig minLimit = systemConfigDao.getConfigByKey(SystemConfigKey.WITHDRAW_MINIMUM_LIMIT);
    BigDecimal incomeScore = endUser.getIncomeLeScore();
    BigDecimal motivateScore = endUser.getMotivateLeScore();
    BigDecimal agentScore = endUser.getAgentLeScore();
    /**
     * 商家直接收益乐分不足1乐分无法提取
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
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    map.put("avlLeScore", avlLeScore);
    map.put("agentLeScore", agentScore);
    map.put("incomeLeScore", incomeScore);
    map.put("motivateLeScore", motivateScore);
    return map;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser userWithdraw(Long userId, String remark) {
    EndUser endUser = endUserDao.find(userId);
    Map<String, BigDecimal> map = getAvlLeScore(endUser);

    LeScoreRecord leScoreRecord = new LeScoreRecord();
    leScoreRecord.setRemark(remark);
    leScoreRecord.setEndUser(endUser);
    leScoreRecord.setLeScoreType(LeScoreType.WITHDRAW);
    leScoreRecord.setWithdrawStatus(ApplyStatus.AUDIT_WAITING);
    leScoreRecord.setAmount(map.get("avlLeScore").negate());
    leScoreRecord.setMotivateLeScore(map.get("motivateLeScore"));
    leScoreRecord.setIncomeLeScore(map.get("incomeLeScore"));
    leScoreRecord.setAgentLeScore(map.get("agentLeScore"));
    leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
    endUser.getLeScoreRecords().add(leScoreRecord);

    endUser.setAgentLeScore(endUser.getAgentLeScore().subtract(map.get("agentLeScore")));
    endUser.setIncomeLeScore(endUser.getIncomeLeScore().subtract(map.get("incomeLeScore")));
    endUser.setMotivateLeScore(endUser.getMotivateLeScore().subtract(map.get("motivateLeScore")));
    endUser.setCurLeScore(endUser.getCurLeScore().subtract(map.get("avlLeScore")));
    endUserDao.merge(endUser);

    if (!CollectionUtils.isEmpty(endUser.getSellers())) {
      for (Seller seller : endUser.getSellers()) {
        seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(map.get("incomeLeScore")));
        sellerDao.merge(seller);
        break;
      }
    }
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
      endUser.setArea(area);
    }
    endUsers.add(endUser);
    endUserDao.merge(endUsers);
    return endUser;
  }

}
