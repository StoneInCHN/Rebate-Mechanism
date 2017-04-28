package org.rebate.dao.impl;

import javax.persistence.FlushModeType;

import org.rebate.dao.ApkVersionDao;
import org.rebate.entity.ApkVersion;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("apkVersionDaoImpl")
public class ApkVersionDaoImpl extends BaseDaoImpl<ApkVersion, Long> implements ApkVersionDao {

  @Override
  public Boolean versionExists(String versionName, Long id) {
    if (versionName == null) {
      return false;
    }
    if (id == null) {
      String jpql =
          "select count(*) from ApkVersion apkVersion where lower(apkVersion.versionName) = lower(:versionName) ";
      Long count =
          entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
              .setParameter("versionName", versionName).getSingleResult();
      return count > 0;
    } else {
      String jpql =
          "select count(*) from ApkVersion apkVersion where lower(apkVersion.versionName) = lower(:versionName) and apkVersion.id !=:id";
      Long count =
          entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
              .setParameter("versionName", versionName).setParameter("id", id).getSingleResult();
      return count > 0;
    }

  }

  @Override
  public ApkVersion getCurNewApkVersion() {
    try {
      String jpql =
          "select apkVersion from ApkVersion apkVersion order by apkVersion.versionCode desc";
      ApkVersion apkVersion =
          entityManager.createQuery(jpql, ApkVersion.class).setFlushMode(FlushModeType.COMMIT)
              .setMaxResults(1).getSingleResult();
      return apkVersion;
    } catch (Exception e) {
      return null;
    }



  }

}
