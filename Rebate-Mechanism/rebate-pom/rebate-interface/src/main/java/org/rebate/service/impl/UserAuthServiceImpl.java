package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.UserAuthDao;
import org.rebate.entity.UserAuth;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.FileService;
import org.rebate.service.UserAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("userAuthServiceImpl")
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuth, Long> implements UserAuthService {

  @Resource(name = "userAuthDaoImpl")
  private UserAuthDao userAuthDao;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  @Resource(name = "userAuthDaoImpl")
  public void setBaseDao(UserAuthDao userAuthDao) {
    super.setBaseDao(userAuthDao);
  }

  @Override
  public UserAuth getUserAuth(Long userId, Boolean isAuth) {
    return userAuthDao.getUserAuth(userId, isAuth);
  }

  @Override
  public UserAuth getUserAuthByIdCard(String cardNo, Boolean isAuth) {
    return userAuthDao.getUserAuthByIdCard(cardNo, isAuth);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public UserAuth doAuth(Long userId, String realName, String cardNo, MultipartFile cardFrontPic,
      MultipartFile cardBackPic) {

    UserAuth userAuth = getUserAuth(userId, false);
    if (userAuth == null) {
      userAuth = new UserAuth();
    }
    userAuth.setUserId(userId);
    userAuth.setIsAuth(false);
    userAuth.setIdCardNo(cardNo);
    userAuth.setRealName(realName);
    userAuth.setIdCardFrontPic(fileService.saveImage(cardFrontPic, ImageType.AUTH_IDCARD));
    userAuth.setIdCardBackPic(fileService.saveImage(cardBackPic, ImageType.AUTH_IDCARD));
    userAuthDao.merge(userAuth);
    return userAuth;
  }
}
