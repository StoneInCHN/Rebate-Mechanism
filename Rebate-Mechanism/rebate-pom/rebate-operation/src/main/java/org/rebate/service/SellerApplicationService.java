package org.rebate.service;

import org.rebate.beans.Message;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.service.BaseService;

public interface SellerApplicationService extends BaseService<SellerApplication, Long> {

   Message applyUpdate(SellerApplication sellerApply);
}
