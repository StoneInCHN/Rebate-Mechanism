package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.ApkVersionDao;
import org.rebate.entity.ApkVersion;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.ApkVersionService;
import org.springframework.stereotype.Service;

@Service("apkVersionServiceImpl")
public class ApkVersionServiceImpl extends BaseServiceImpl<ApkVersion, Long> implements
    ApkVersionService {

  @Resource(name = "apkVersionDaoImpl")
  private ApkVersionDao apkVersionDao;

  @Resource(name = "apkVersionDaoImpl")
  public void setBaseDao(ApkVersionDao apkVersionDao) {
    super.setBaseDao(apkVersionDao);
  }

  @Override
  public ApkVersion getNewVersion(Integer versionCode, AppPlatform appPlatform) {
    return apkVersionDao.getNewVersion(versionCode, appPlatform);
  }
}
