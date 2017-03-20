package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.rebate.beans.Principal;
import org.rebate.dao.AdminDao;
import org.rebate.entity.Admin;
import org.rebate.entity.Role;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.AdminService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 管理员
 * 
 */
@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

  @Resource(name = "adminDaoImpl")
  private AdminDao adminDao;

  @Resource(name = "adminDaoImpl")
  public void setBaseDao(AdminDao adminDao) {
    super.setBaseDao(adminDao);
  }

  @Transactional(readOnly = true)
  public boolean usernameExists(String username) {
    return adminDao.usernameExists(username);
  }

  @Transactional(readOnly = true)
  public boolean emailExists(String email) {
    return adminDao.emailExists(email);
  }

  @Transactional(readOnly = true)
  public Admin findByUsername(String username) {
    return adminDao.findByUsername(username);
  }

  @Transactional(readOnly = true)
  public List<Admin> findByName(String name) {
    return adminDao.findByName(name);
  }

  @Override
  public Admin findByNameAccurate(String name) {
    return adminDao.findByNameAccurate(name);
  }


  @Transactional(readOnly = true)
  public List<String> findAuthorities(Long id) {
    List<String> authorities = new ArrayList<String>();
    Admin admin = adminDao.find(id);
    if (admin != null) {
      for (Role role : admin.getRoles()) {
        authorities.addAll(role.getAuthorities());
      }
    }
    return authorities;
  }

  @Transactional(readOnly = true)
  public boolean isAuthenticated() {
    Subject subject = SecurityUtils.getSubject();
    if (subject != null) {
      return subject.isAuthenticated();
    }
    return false;
  }

  @Transactional(readOnly = true)
  public Admin getCurrent() {
    Subject subject = SecurityUtils.getSubject();
    if (subject != null) {
      Principal principal = (Principal) subject.getPrincipal();
      if (principal != null) {
        return adminDao.find(principal.getId());
      }
    }
    return null;
  }

  @Transactional(readOnly = true)
  public String getCurrentUsername() {
    Subject subject = SecurityUtils.getSubject();
    if (subject != null) {
      Principal principal = (Principal) subject.getPrincipal();
      if (principal != null) {
        return principal.getUsername();
      }
    }
    return null;
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public void save(Admin admin) {
    super.save(admin);
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public Admin update(Admin admin) {
    return super.update(admin);
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public Admin update(Admin admin, String... ignoreProperties) {
    return super.update(admin, ignoreProperties);
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public void delete(Long id) {
    super.delete(id);
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public void delete(Long... ids) {
    super.delete(ids);
  }

  @Override
  @Transactional
  @CacheEvict(value = "authorization", allEntries = true)
  public void delete(Admin admin) {
    super.delete(admin);
  }

  @Transactional(readOnly = true)
  public boolean isSystemAdmin() {
    Admin admin = getCurrent();
    Set<Role> roles = admin.getRoles();
    Iterator<Role> it = roles.iterator();
    while (it.hasNext()) {
      Role role = it.next();
      if (role.getIsSystem()) {
        return true;
      }
    }
    return false;
  }

}
