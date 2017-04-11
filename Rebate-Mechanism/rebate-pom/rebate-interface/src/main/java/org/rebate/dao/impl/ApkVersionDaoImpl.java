package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.ApkVersionDao;
import org.rebate.entity.ApkVersion;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("apkVersionDaoImpl")
public class ApkVersionDaoImpl extends BaseDaoImpl<ApkVersion, Long> implements ApkVersionDao {

  @Override
  public ApkVersion getNewVersion(Integer versionCode, AppPlatform appPlatform) {
    if (versionCode == null || appPlatform == null) {
      return null;
    }
    try {
      String jpql =
          "select version from ApkVersion version where version.appPlatform = :appPlatform and version.versionCode > :versionCode order by version.versionCode desc";
      return entityManager.createQuery(jpql, ApkVersion.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("appPlatform", appPlatform).setParameter("versionCode", versionCode)
          .setMaxResults(1).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
