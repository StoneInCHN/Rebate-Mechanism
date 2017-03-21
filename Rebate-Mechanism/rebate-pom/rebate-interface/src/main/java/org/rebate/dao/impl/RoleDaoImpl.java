package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Role;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.RoleDao;
@Repository("roleDaoImpl")
public class RoleDaoImpl extends  BaseDaoImpl<Role,Long> implements RoleDao {

}