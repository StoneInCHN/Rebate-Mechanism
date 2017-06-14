package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Admin;
import org.rebate.entity.BankCard;
import org.rebate.entity.Role;
import org.rebate.entity.base.BaseEntity.Save;
import org.rebate.entity.commonenum.CommonEnum.AdminStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.beans.VerifyBankcardBean;
import org.rebate.json.beans.VerifyBankcardResult;
import org.rebate.service.AdminService;
import org.rebate.service.BankCardService;
import org.rebate.service.RoleService;
import org.rebate.utils.ApiUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.TimeUtils;
import org.rebate.utils.TokenGenerator;
import org.rebate.utils.ToolsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller - 管理员
 * 
 */
@Controller("adminController")
@RequestMapping("/console/admin")
public class AdminController extends BaseController {

  @Resource(name = "adminServiceImpl")
  private AdminService adminService;
  @Resource(name = "roleServiceImpl")
  private RoleService roleService;
  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;


  /**
   * 检查用户名是否存在
   */
  @RequestMapping(value = "/check_username", method = RequestMethod.GET)
  public @ResponseBody boolean checkUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return false;
    }
    if (adminService.usernameExists(username)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.ne("", ""));
    model.addAttribute("roles", roleService.findList(null, filters, null));
    model.addAttribute("adminStatusTypes", AdminStatus.values());
    return "/admin/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Admin admin, Long[] roleIds) {
    admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
    if (!isValid(admin, Save.class)) {
      return ERROR_VIEW;
    }
    if (adminService.usernameExists(admin.getUsername())) {
      return ERROR_VIEW;
    }
    admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    admin.setIsSystem(false);
    adminService.save(admin);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("roles", roleService.findAll());
    model.addAttribute("admin", adminService.find(id));
    model.addAttribute("adminStatusTypes", AdminStatus.values());
    return "/admin/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
    admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
    if (!isValid(admin)) {
      return ERROR_VIEW;
    }
    Admin pAdmin = adminService.find(admin.getId());
    if (pAdmin == null) {
      return ERROR_VIEW;
    }
    if (StringUtils.isNotEmpty(admin.getPassword())) {
      admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    } else {
      admin.setPassword(pAdmin.getPassword());
    }
    adminService.update(admin, "username", "loginDate", "loginIp", "isSystem");

    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, String username, String name, String email, Date loginDate,
      AdminStatus adminStatus, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (username != null) {
      filters.add(Filter.like("username", username));
      model.addAttribute("username", username);
    }
    if (name != null) {
      filters.add(Filter.like("name", name));
      model.addAttribute("name", name);
    }
    if (email != null) {
      filters.add(Filter.like("email", email));
      model.addAttribute("email", email);
    }
    if (loginDate != null) {
      Date formDate = TimeUtils.formatDate2Day(loginDate);
      Date toDate = TimeUtils.addDays(1, loginDate);
      filters.add(Filter.ge("loginDate", formDate));
      filters.add(Filter.le("loginDate", toDate));
      model.addAttribute("loginDate", loginDate);
    }
    if (adminStatus != null) {
      filters.add(Filter.eq("adminStatus", adminStatus));
      model.addAttribute("adminStatus", adminStatus);
    }
    filters.add(Filter.ne("username", "superadmin"));
    pageable.setFilters(filters);
    model.addAttribute("page", adminService.findPage(pageable));
    return "/admin/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= adminService.count()) {
      return Message.error("admin.common.deleteAllNotAllowed");
    }
    adminService.delete(ids);
    return SUCCESS_MESSAGE;
  }
  /**
   * 添加验证
   */
  @RequestMapping(value = "/validation", method = RequestMethod.GET)
  public String validationPage(ModelMap model) {
    Admin admin = adminService.getCurrent();
    if (!adminService.isSystemAdmin(admin)) {//非内置账户admin
        return "redirect:list.jhtml";
    }
    model.addAttribute("admin",admin);
    return "/admin/validation";
  }
  /**
   * 请求验证码
   */
  @RequestMapping(value = "/reqeustSmsCode", method = RequestMethod.POST)
  public @ResponseBody Message reqeustSmsCode() {
    Admin admin = adminService.getCurrent();
    if (!adminService.isSystemAdmin(admin)) {//非内置账户admin
        LogUtil.debug(this.getClass(), "reqeustSmsCode", "非系统内置账户(非admin管理员)");
        return Message.error("非系统内置账户(非admin管理员)");
    }else if (admin.getCellPhoneNum() == null) {
        LogUtil.debug(this.getClass(), "reqeustSmsCode", "admin管理员未配置预留手机号");
        return Message.error("admin管理员未配置预留手机号");
    }else if (isMobileNumber(admin.getCellPhoneNum())) {
        String cellPhoneNum = admin.getCellPhoneNum();
        SMSVerificationCode smsVerificationCode = adminService.getSmsCode(cellPhoneNum);
        if (smsVerificationCode != null) {
              adminService.deleteSmsCode(cellPhoneNum);
        }
        Integer smsCode = (int) ((Math.random() * 9 + 1) * 1000);
        ToolsUtils.sendSmsMsg(cellPhoneNum, message("rebate.admin.smsCodeContent", smsCode.toString()));// 发送短信验证码
        SMSVerificationCode newSmsCode = new SMSVerificationCode();
        newSmsCode.setCellPhoneNum(cellPhoneNum);
        newSmsCode.setSmsCode(smsCode.toString());
        newSmsCode.setTimeoutToken(new Long(new Date().getTime()).toString());
        try {
          adminService.createSmsCode(cellPhoneNum, newSmsCode);
          LogUtil.debug(this.getClass(), "reqeustSmsCode", "Send SmsCode for cellPhone number: %s, smsCode: %s", cellPhoneNum, smsCode.toString());
          return SUCCESS_MESSAGE;
        } catch (Exception e) {
          e.printStackTrace();
          LogUtil.debug(this.getClass(), "reqeustSmsCode", "Catch Exception:", e.getMessage());
          return Message.error(e.getMessage());
        }
    }
    return ERROR_MESSAGE;
  } 
  /**
   * 验证admin密码和手机验证码
   */
  @RequestMapping(value = "/editAdmin", method = {RequestMethod.POST, RequestMethod.GET})
  public String editAdmin(String password, String smsCode, ModelMap model) {
    Admin admin = adminService.getCurrent();
    if (password != null && smsCode != null && DigestUtils.md5Hex(password).equals(admin.getPassword())) {//密码有效
        SMSVerificationCode smsVerficationCode = adminService.getSmsCode(admin.getCellPhoneNum());
        if (smsVerficationCode != null) {
            String code = smsVerficationCode.getSmsCode();
            String timeoutToken = smsVerficationCode.getTimeoutToken();
            if (timeoutToken != null && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
                if (smsCode.equals(code)) {//验证码有效
                    adminService.deleteSmsCode(admin.getCellPhoneNum());
                    model.addAttribute("admin", admin);
                    List<BankCard> cardList = bankCardService.getAllCardList(admin);
                    if (cardList != null && cardList.size() > 0) {
                        model.addAttribute("cardList", cardList);
                    }
                    return "/admin/editAdmin";
                }
            }
        }
    }
    return "redirect:validation.jhtml";
  }
  /**
   * 更新admin预留手机号
   */
  @RequestMapping(value = "/updateCellPhone", method = RequestMethod.POST)
  public @ResponseBody Message singlePay(String cellPhone) {
     if(!isMobileNumber(cellPhone)){
       return Message.error("手机号码无效");
     }
     Admin admin = adminService.getCurrent();
     if (!adminService.isSystemAdmin(admin)) {//非内置账户admin
       return Message.error("不是内置账户admin,不能编辑预留手机号");
     } 
     admin.setCellPhoneNum(cellPhone);
     adminService.update(admin);
     return Message.success("Admin预留手机号修改成功");
  }
  /**
   * 添加银行卡
   */
  @RequestMapping(value = "/addBankCard", method = RequestMethod.GET)
  public String addBankCard(ModelMap model) {

    return "/admin/addBankCard";
  }
  /**
   * 银行卡四元素校验 四元素:姓名、身份证、银行卡、手机号码
   */
  @RequestMapping(value = "/verifyBankCard", method = RequestMethod.POST)
  public @ResponseBody Message verifyBankCard(BankCard bankCard, ModelMap model) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("key", setting.getJuheKey());
    params.put("bankcard", bankCard.getCardNum());
    params.put("realname", bankCard.getOwnerName());
    params.put("idcard", bankCard.getIdCard());
    params.put("mobile", bankCard.getReservedMobile());
    Admin admin = adminService.getCurrent();
    //如果存在，表示以前已经验证过了，不用再验证
    boolean exist = bankCardService.exists(Filter.eq("cardNum", bankCard.getCardNum()),
            Filter.eq("ownerName", bankCard.getOwnerName()),
            Filter.eq("idCard", bankCard.getIdCard()),
            Filter.eq("reservedMobile", bankCard.getReservedMobile()),
            Filter.eq("delStatus", false),
            Filter.eq("admin", admin));
    if (exist) {
      return Message.error("银行卡已存在，不能重复添加");
    }

    try {
      String result = ApiUtils.post(setting.getJuheVerifyBankcard4(), params);

      LogUtil.debug(this.getClass(), "verifyBankCard", "request params: %s, result: %s",
          params.toString(), result);

      ObjectMapper objectMapper = new ObjectMapper();
      VerifyBankcardBean verifyBankcardBean =
          objectMapper.readValue(result, VerifyBankcardBean.class);
      if (verifyBankcardBean == null) {
        LogUtil.debug(this.getClass(), "verifyBankCard",
            "Cannot convert from result to VerifyBankcardBean!!");
        return ERROR_MESSAGE;
      }

      if (verifyBankcardBean.getError_code() == 0 && verifyBankcardBean.getResult() != null) {
        VerifyBankcardResult verifyBankcardResult = verifyBankcardBean.getResult();
        if ("1".equals(verifyBankcardResult.getRes())) {// 验证成功
          return Message.success(verifyBankcardResult.getMessage());
        } else if ("2".equals(verifyBankcardResult.getRes())) {// 验证不匹配
          return Message.error(verifyBankcardResult.getMessage());
        }
      } else {
        return Message.error(verifyBankcardBean.getReason());
      }
    } catch (Exception e) {
      LogUtil.debug(this.getClass(), "verifyBankCard","Catch Exception:"+e.getMessage());
      return Message.error("系统错误");
    }
    //return "redirect:list.jhtml";
    return ERROR_MESSAGE;
  } 
  /**
   * 设置默认银行卡
   */
  @RequestMapping(value = "/updateCardDefault", method = RequestMethod.POST)
  public @ResponseBody Message updateCardDefault(Long bankCardId) {
    if (bankCardId == null) {
      return ERROR_MESSAGE;
    }
    BankCard bankCard = bankCardService.find(bankCardId);
    if (bankCard == null) {
      return ERROR_MESSAGE;
    }
    Admin admin = adminService.getCurrent();
    try {
      bankCardService.updateCardDefault(bankCard, admin);
    } catch (Exception e) {
      LogUtil.debug(this.getClass(), "updateCardDefault","Catch Exception:"+e.getMessage());
      return ERROR_MESSAGE;
    }
    return SUCCESS_MESSAGE;
  }
  /**
   * 删除
   */
  @RequestMapping(value = "/deleteCard", method = RequestMethod.POST)
  public @ResponseBody Message deleteCard(Long[] ids) {
    Admin admin = adminService.getCurrent();
    long adminAllCards = bankCardService.countAllCardList(admin);
    if (ids.length >= adminAllCards) {
      return Message.error("不允许全部删除");
    }
    List<BankCard> mergeCards = new ArrayList<BankCard>();
    for (int i = 0; i < ids.length; i++) {
      BankCard card = bankCardService.find(ids[i]);
      card.setDelStatus(true);
      mergeCards.add(card);
    }
    bankCardService.update(mergeCards);
    return SUCCESS_MESSAGE;
  }
  /**
   * 保存银行卡
   */
  @RequestMapping(value = "/saveBankCard", method = RequestMethod.POST)
  public String saveBankCard(BankCard bankCard) {
    Admin admin = adminService.getCurrent();
    bankCardService.saveBankCard(bankCard, admin);
    return "redirect:validation.jhtml";
  }
}
