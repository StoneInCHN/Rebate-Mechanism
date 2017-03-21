package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Role;
import org.rebate.dao.RoleDao;
import org.rebate.service.RoleService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role,Long> implements RoleService {

      @Resource(name="roleDaoImpl")
      public void setBaseDao(RoleDao roleDao) {
         super.setBaseDao(roleDao);
  }
}