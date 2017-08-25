package org.rebate.controller.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;

public class MobileBaseController extends BaseController {

  public Setting setting = SettingUtils.get();

  public boolean isMobileNumber(String mobile) {
    Pattern p = Pattern.compile(setting.getMobilePattern());
    Matcher m = p.matcher(mobile);
    return m.matches();
  }

  /**
   * 邮箱格式验证
   * 
   * @param email
   * @return
   */
  public boolean isEmail(String email) {
    Pattern regex = Pattern.compile(setting.getEmailPattern());
    Matcher matcher = regex.matcher(email);
    return matcher.matches();
  }

  /**
   * 正整数格式验证
   * 
   * @param email
   * @return
   */
  public boolean isInteger(String req) {
    Pattern regex = Pattern.compile("^[1-9]+\\d*$");
    Matcher matcher = regex.matcher(req);
    return matcher.matches();
  }

  /**
   * 小数格式验证(大于0且最多四位小数)
   * 
   * @param email
   * @return
   */
  public boolean isNumber(String req) {
    Pattern regex = Pattern.compile("^[1-9]\\d*(\\.\\d{1,4})?|(0\\.\\d{1,4})$");
    Matcher matcher = regex.matcher(req);
    return matcher.matches();
  }

  /**
   * 支付金额(大于0且最多2位小数)
   * 
   * @param email
   * @return
   */
  public boolean isCNYAmount(String req) {
    Pattern regex = Pattern.compile("^[1-9]\\d*(\\.\\d{1,2})?|(0\\.\\d{1,2})$");
    Matcher matcher = regex.matcher(req);
    return matcher.matches();
  }

}
