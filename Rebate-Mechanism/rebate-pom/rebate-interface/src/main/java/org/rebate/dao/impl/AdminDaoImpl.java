package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Admin;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.AdminDao;
@Repository("adminDaoImpl")
public class AdminDaoImpl extends  BaseDaoImpl<Admin,Long> implements AdminDao {

}