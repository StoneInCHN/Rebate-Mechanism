package org.rebate.dao;

import org.rebate.entity.Role;
import org.rebate.framework.dao.BaseDao;

/**
 * Dao - 角色
 * 
 */
public interface RoleDao extends BaseDao<Role, Long> {
  boolean nameExists(String name, Long id);
  
  boolean hasContainAdmin(Role role);
}
