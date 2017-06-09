package org.rebate.service;

import org.rebate.entity.UserAuth;
import org.rebate.framework.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

public interface UserAuthService extends BaseService<UserAuth, Long> {


  /**
   * 根据用户ID获取用户实名认证信息
   * 
   * @param userId
   * @return
   */
  UserAuth getUserAuth(Long userId, Boolean isAuth);

  /**
   * 根据身份证号码获取用户实名认证信息
   * 
   * @param userId
   * @return
   */
  UserAuth getUserAuthByIdCard(String cardNo, Boolean isAuth);

  /**
   * 用户实名认证
   * 
   * @return
   */
  UserAuth doAuth(Long userId, String realName, String cardNo, MultipartFile cardFrontPic,
      MultipartFile cardBackPic);



}
