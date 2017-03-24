package org.rebate.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
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
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public EndUser userReg(String cellPhoneNum, String password, String recommenderMobile) {
    EndUser regUser = new EndUser();
    regUser.setUserName(ToolsUtils.createUserName());
    regUser.setCellPhoneNum(cellPhoneNum);
    regUser.setLoginPwd(DigestUtils.md5Hex(password));
    regUser.setAccountStatus(AccountStatus.ACTIVED);
    regUser.setNickName(cellPhoneNum.substring(3, 7));
    UserRecommendRelation relation = new UserRecommendRelation();
    if (recommenderMobile != null) {
      EndUser recommend = endUserDao.findByUserMobile(recommenderMobile);
      if (recommend != null) {// 存在该推荐人
        regUser.setRecommender(recommend.getNickName());
        regUser.setRecommenderId(recommend.getId());

        UserRecommendRelation parent = userRecommendRelationDao.findByUserMobile(recommenderMobile);
        relation.setCellPhoneNum(cellPhoneNum);
        relation.setNickName(regUser.getNickName());
        relation.setParent(parent);
      } else {// 无推荐人
        relation.setCellPhoneNum(cellPhoneNum);
        relation.setNickName(regUser.getNickName());
      }
    }
    userRecommendRelationDao.persist(relation);
    endUserDao.persist(regUser);
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
    return null;
  }
}
