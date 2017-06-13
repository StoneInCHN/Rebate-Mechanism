package org.rebate.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.beans.SMSVerificationCode;
import org.rebate.dao.AdminDao;
import org.rebate.entity.Admin;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao - 管理员
 * 
 */
@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

  public boolean usernameExists(String username) {
    if (username == null) {
      return false;
    }
    String jpql = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
    Long count =
        entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
            .setParameter("username", username).getSingleResult();
    return count > 0;
  }

  public boolean emailExists(String email) {
    if (email == null) {
      return false;
    }
    String jpql = "select count(*) from Admin admin where lower(admin.email) = lower(:email)";
    Long count =
        entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
            .setParameter("email", email).getSingleResult();
    return count > 0;
  }

  @Transactional
  public Admin findByUsername(String username) {
    if (username == null) {
      return null;
    }
    try {
      String jpql = "select admin from Admin admin where lower(admin.username) = lower(:username)";
      return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("username", username).getSingleResult();
    } catch (NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Admin> findByName(String name) {
    if (name == null) {
      return null;
    }
    try {
      String jpql = "select admin from Admin admin where lower(admin.name) like lower(:name)";
      return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("name", "%" + name + "%").getResultList();

    } catch (NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Transactional
  public Admin findByNameAccurate(String name) {
    String jpql = "select admin from Admin admin where lower(admin.name)= lower(:name)";
    try {
      return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("name", name).getSingleResult();
    } catch (NoResultException e) {
      e.printStackTrace();
      return null;
    }

  }
  @Override
  @CachePut(value = "admin", key = "'admin.smsCode='+#cellPhoneNum")
  public SMSVerificationCode createSmsCode(String cellPhoneNum, SMSVerificationCode smsCode) {
    return smsCode;
  }

  @Override
  @Cacheable(value = "admin", key = "'admin.smsCode='+#cellPhoneNum")
  public SMSVerificationCode getSmsCode(String cellPhoneNum) {
    return null;
  }

  @Override
  @CacheEvict(value = "admin", key = "'admin.smsCode='+#cellPhoneNum")
  public void deleteSmsCode(String cellPhoneNum) {

  }
}
