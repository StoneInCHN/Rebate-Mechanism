package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.ApkVersion;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.ApkVersionDao;
@Repository("apkVersionDaoImpl")
public class ApkVersionDaoImpl extends  BaseDaoImpl<ApkVersion,Long> implements ApkVersionDao {

}