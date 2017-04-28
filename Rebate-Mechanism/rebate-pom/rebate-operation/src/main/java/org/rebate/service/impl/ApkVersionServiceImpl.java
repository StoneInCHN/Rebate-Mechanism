package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.ApkVersionDao;
import org.rebate.entity.ApkVersion;
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
  public Boolean versionExists(String versionName, Long id) {
    return apkVersionDao.versionExists(versionName, id);
  }

  @Override
  public ApkVersion getCurNewApkVersion() {
    return apkVersionDao.getCurNewApkVersion();
  }
}
