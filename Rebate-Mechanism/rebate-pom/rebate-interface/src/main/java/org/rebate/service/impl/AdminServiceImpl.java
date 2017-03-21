package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Admin;
import org.rebate.dao.AdminDao;
import org.rebate.service.AdminService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin,Long> implements AdminService {

      @Resource(name="adminDaoImpl")
      public void setBaseDao(AdminDao adminDao) {
         super.setBaseDao(adminDao);
  }
}