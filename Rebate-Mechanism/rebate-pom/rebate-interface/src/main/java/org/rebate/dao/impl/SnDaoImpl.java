package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Sn;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SnDao;
@Repository("snDaoImpl")
public class SnDaoImpl extends  BaseDaoImpl<Sn,Long> implements SnDao {

}