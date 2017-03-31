package org.rebate.service; 

import org.rebate.entity.MessageInfo;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.BaseService;

public interface MessageInfoService extends BaseService<MessageInfo,Long>{

  Page<MessageInfo> findMsgByUser(Long userId, Pageable pageable);

}