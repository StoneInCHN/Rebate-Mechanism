package org.rebate.dao;

import org.rebate.entity.ApkVersion;
import org.rebate.framework.dao.BaseDao;

public interface ApkVersionDao extends BaseDao<ApkVersion, Long> {

  Boolean versionExists(String versionName, Long id);

  /**
   * 获取当前最新版本
   * 
   * @return
   */
  ApkVersion getCurNewApkVersion();

}
