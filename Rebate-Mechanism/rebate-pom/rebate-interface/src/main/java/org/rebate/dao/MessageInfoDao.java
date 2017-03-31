package org.rebate.dao; 
import java.util.List;

import org.rebate.entity.MessageInfo;
import org.rebate.framework.dao.BaseDao;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;

public interface MessageInfoDao extends  BaseDao<MessageInfo,Long>{

  Page<MessageInfo> findMsgByUser(Long userId, Pageable pageable);

  List<MessageInfo> findMsgByUser(Long userId);

}