package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.EndUserDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
import org.springframework.stereotype.Service;

@Service("endUserServiceImpl")
public class EndUserServiceImpl extends BaseServiceImpl<EndUser, Long> implements EndUserService {

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;


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
  public EndUser userReg(String userName, String password) {
    return null;
  }
}
