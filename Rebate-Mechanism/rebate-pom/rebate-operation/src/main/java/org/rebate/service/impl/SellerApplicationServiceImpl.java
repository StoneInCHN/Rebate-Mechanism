package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.dao.SellerApplicationDao;
import org.rebate.dao.SellerDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.SellerCommitmentImage;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerApplicationService;
import org.rebate.utils.SpringUtils;
import org.rebate.utils.ToolsUtils;
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

  @Resource(name = "salesmanSellerRelationDaoImpl")
  private SalesmanSellerRelationDao salesmanSellerRelationDao;

  @Resource(name = "sellerApplicationDaoImpl")
  public void setBaseDao(SellerApplicationDao baseDao) {
    super.setBaseDao(baseDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public synchronized Message applyUpdate(SellerApplication sellerApply) {
    String message = "";
    // 业务员
    EndUser salesMan = new EndUser();
    SellerApplication apply = this.find(sellerApply.getId());
    // 商家所属用户
    EndUser endUser = apply.getEndUser();

    if (endUser != null && endUser.getSeller() != null) {
      return Message.error("rebate.message.error");
    }

    try {
      apply.setApplyStatus(sellerApply.getApplyStatus());
      apply.setNotes(sellerApply.getNotes());
      apply.setLimitAmountByDay(sellerApply.getLimitAmountByDay());
      if (ApplyStatus.AUDIT_PASSED == sellerApply.getApplyStatus()) {
        Seller seller = new Seller();
        SellerCategory sellerCategory = apply.getSellerCategory();
        if (endUser != null) {
          seller.setEndUser(endUser);
        }
        List<SellerEnvImage> envImages = apply.getEnvImages();
        List<SellerEnvImage> sellerEnvImages = new ArrayList<SellerEnvImage>();

        if (envImages != null && envImages.size() > 0) {
          for (SellerEnvImage image : envImages) {
            SellerEnvImage sellerEnvImage = image;
            sellerEnvImages.add(sellerEnvImage);
          }
        }
        seller.setEnvImages(sellerEnvImages);

        List<SellerCommitmentImage> commintmentImages = apply.getCommitmentImages();
        List<SellerCommitmentImage> sellerCommintmentImages =
            new ArrayList<SellerCommitmentImage>();

        if (commintmentImages != null && commintmentImages.size() > 0) {
          for (SellerCommitmentImage image : commintmentImages) {
            SellerCommitmentImage sellerCommintmentImage = image;
            sellerCommintmentImages.add(sellerCommintmentImage);
          }
        }
        seller.setCommitmentImages(sellerCommintmentImages);

        if (sellerCategory != null) {
          seller.setSellerCategory(sellerCategory);
        }
        seller.setAddress(apply.getAddress());

        if (apply.getArea() != null) {
          Area area = apply.getArea();
          // if (!CollectionUtils.isEmpty(area.getChildren())) {
          // return null;
          // }
          seller.setArea(area);
          Area cityArea = area.getParent();
          if (cityArea != null) {
            Area provinceArea = cityArea.getParent();
            if (provinceArea != null) {// 三级行政单位 省市区
              seller.setCity(cityArea.getId());
              seller.setProvince(provinceArea.getId());
            } else {// 二级行政单位 省市
              seller.setCity(area.getId());
              seller.setProvince(cityArea.getId());
            }
          }
        }

        seller.setContactCellPhone(apply.getContactCellPhone());
        seller.setContactPerson(apply.getContactPerson());
        seller.setLatitude(apply.getLatitude());
        seller.setLicenseNum(apply.getLicenseNum());
        seller.setLicenseImgUrl(apply.getLicenseImgUrl());
        seller.setLongitude(apply.getLongitude());
        seller.setStorePhone(apply.getStorePhone());
        seller.setStorePictureUrl(apply.getStorePhoto());
        seller.setName(apply.getSellerName());
        seller.setLimitAmountByDay(apply.getLimitAmountByDay());
        seller.setDiscount(apply.getDiscount());
        seller.setDescription(apply.getDescription());
        seller.setAccountStatus(AccountStatus.ACTIVED);
        sellerDao.persist(seller);

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("sellerApplication", sellerApply.getId()));
        List<SalesmanSellerRelation> salesmanSellerRelations =
            salesmanSellerRelationDao.findList(null, null, filters, null);
        if (salesmanSellerRelations != null && salesmanSellerRelations.size() == 1) {
          SalesmanSellerRelation salesmanSellerRelation = salesmanSellerRelations.get(0);
          salesmanSellerRelation.setSeller(seller);
          salesmanSellerRelation.setApplyStatus(true);
          salesmanSellerRelationDao.merge(salesmanSellerRelation);
          salesMan = salesmanSellerRelation.getEndUser();
        }
        message = SpringUtils.getMessage("rebate.sellerApplication.audit.passed", seller.getName());
      } else {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("sellerApplication", sellerApply.getId()));
        List<SalesmanSellerRelation> salesmanSellerRelations =
            salesmanSellerRelationDao.findList(null, null, filters, null);
        if (salesmanSellerRelations != null && salesmanSellerRelations.size() == 1) {
          SalesmanSellerRelation salesmanSellerRelation = salesmanSellerRelations.get(0);
          salesMan = salesmanSellerRelation.getEndUser();
        }
        message =
            SpringUtils.getMessage("rebate.sellerApplication.audit.failed", apply.getSellerName());
      }

      this.update(apply);
      // 给业务员发消息
      if (salesMan.getCellPhoneNum() != null) {
        ToolsUtils.sendSmsMsg(salesMan.getCellPhoneNum(), message);
      }
      // 给商家发消息
      if (apply.getContactCellPhone() != null) {
        ToolsUtils.sendSmsMsg(apply.getContactCellPhone(), message);
      }
      return Message.success("rebate.message.success");
    } catch (Exception e) {
      return Message.error("rebate.message.error");
    }
  }

}
