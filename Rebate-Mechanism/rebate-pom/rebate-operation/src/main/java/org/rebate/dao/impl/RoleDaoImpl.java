package org.rebate.dao.impl;

import javax.persistence.FlushModeType;

import org.rebate.dao.RoleDao;
import org.rebate.entity.Role;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 角色
 * 
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

  @Override
  public boolean nameExists(String name, Long id) {
    if (name == null) {
      return false;
    }
    if (id == null) {
      String jpql = "select count(*) from Role role where lower(role.name) = lower(:name)";
      Long count =
          entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
              .setParameter("name", name).getSingleResult();
      return count > 0;
    } else {
      String jpql = "select count(*) from Role role where lower(role.name) = lower(:name) and role.id !=:id";
      Long count =
          entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT)
              .setParameter("name", name).setParameter("id", id).getSingleResult();
      return count > 0;
    }
  }

  @Override
  public boolean hasContainAdmin(Role role) {
    
    return false;
  }

}
