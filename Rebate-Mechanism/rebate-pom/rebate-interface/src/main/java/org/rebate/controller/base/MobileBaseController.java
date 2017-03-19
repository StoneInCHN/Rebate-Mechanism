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
   * @param email
   * @return
   */
  public boolean isEmail(String email) {
    Pattern regex = Pattern.compile(setting.getEmailPattern());    
    Matcher matcher = regex.matcher(email);    
    return matcher.matches(); 
  }

}
