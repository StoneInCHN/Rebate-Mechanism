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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EndUser findByUserMobile(String mobileNo) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getEndUserToken(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String createEndUserToken(String token, Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AppPlatform getEndUserAppPlatform(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AppPlatform createEndUserAppPlatform(AppPlatform appPlatform, Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteEndUserToken(Long id) {
    // TODO Auto-generated method stub

  }

  @Override
  public EndUser userReg(String userName, String password) {
    // TODO Auto-generated method stub
    return null;
  }
}
