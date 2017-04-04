package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.MsgEndUser;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.MsgEndUserDao;
@Repository("msgEndUserDaoImpl")
public class MsgEndUserDaoImpl extends  BaseDaoImpl<MsgEndUser,Long> implements MsgEndUserDao {

}