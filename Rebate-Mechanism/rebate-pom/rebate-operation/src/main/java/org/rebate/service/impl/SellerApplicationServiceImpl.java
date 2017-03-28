package org.rebate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.SellerApplicationDao;
import org.rebate.dao.SellerDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("sellerApplicationServiceImpl")
public class SellerApplicationServiceImpl extends BaseServiceImpl<SellerApplication, Long>
    implements SellerApplicationService {

  @Resource(name = "sellerApplicationDaoImpl")
  private SellerApplicationDao sellerApplicationDao;
  
  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;
  
  @Resource(name = "sellerApplicationDaoImpl")
  public void setBaseDao(SellerApplicationDao baseDao) {
    super.setBaseDao(baseDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Message applyUpdate(SellerApplication sellerApply) {
    SellerApplication apply = this.find(sellerApply.getId());
    try {
      apply.setApplyStatus(sellerApply.getApplyStatus());
      apply.setNotes(sellerApply.getNotes());
      if (ApplyStatus.AUDIT_PASSED == sellerApply.getApplyStatus()) {
        Seller seller =new Seller();
        SellerCategory sellerCategory = apply.getSellerCategory();
        EndUser endUser = apply.getEndUser();
        if(endUser!=null){
            seller.setEndUser(endUser);
        }
        List<SellerEnvImage> envImages=apply.getEnvImages();
        if(envImages!=null && envImages.size() >0){
          seller.setEnvImages(envImages);
        }
        if(sellerCategory!=null){
          seller.setSellerCategory(sellerCategory);
        }
        seller.setAddress(apply.getAddress());
        seller.setArea(apply.getArea());
        seller.setContactCellPhone(apply.getContactCellPhone());
        seller.setContactPerson(apply.getContactPerson());
        seller.setLatitude(apply.getLatitude());
        seller.setLicenseNum(apply.getLicenseNum());
        seller.setLicenseImgUrl(apply.getLicenseImgUrl());
        seller.setLongitude(apply.getLongitude());
        seller.setStorePhone(apply.getStorePhone());
        seller.setStorePictureUrl(apply.getStorePhoto());
        seller.setName(apply.getSellerName());
        seller.setAccountStatus(AccountStatus.ACTIVED);
        sellerDao.persist(seller);
      }
      this.update(apply);
      return Message.success("csh.message.success");
    } catch (Exception e) {
      return Message.error("csh.message.error");
    }
  }

}
