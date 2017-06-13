package org.rebate.controller.base;

import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.rebate.beans.DateEditor;
import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.SpringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;




public class BaseController{
	/** 错误视图 */
	protected static final String ERROR_VIEW = "/common/error";
	
	protected static final String VIEW_404 = "/common/404";

	/** 错误消息 */
	protected static final Message ERROR_MESSAGE = Message.error("rebate.message.error");

	/** 成功消息 */
	protected static final Message SUCCESS_MESSAGE = Message.success("rebate.message.success");

	/** "验证结果"参数名称 */
	private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";
	
	public Setting setting = SettingUtils.get();
	
	@Resource(name = "validator")
	private Validator validator;

	/**
	 * 数据绑定
	 * 
	 * @param binder
	 *            WebDataBinder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}

	/**
	 * 数据验证
	 * 
	 * @param target
	 *            验证对象
	 * @param groups
	 *            验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}

	/**
	 * 数据验证
	 * 
	 * @param type
	 *            类型
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param groups
	 *            验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Class<?> type, String property, Object value, Class<?>... groups) {
		Set<?> constraintViolations = validator.validateValue(type, property, value, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}


	/**
	 * 获取国际化消息
	 * 
	 * @param code
	 *            代码
	 * @param args
	 *            参数
	 * @return 国际化消息
	 */
	protected String message(String code, Object... args) {
		return SpringUtils.getMessage(code, args);
	}

	/**
	 * 添加瞬时消息
	 * 
	 * @param redirectAttributes
	 *            RedirectAttributes
	 * @param message
	 *            消息
	 */
	/*protected void addFlashMessage(RedirectAttributes redirectAttributes, Message message) {
		if (redirectAttributes != null && message != null) {
			redirectAttributes.addFlashAttribute(FlashMessageDirective.FLASH_MESSAGE_ATTRIBUTE_NAME, message);
		}
	}*/

	/**
	 * 添加日志
	 * 
	 * @param content
	 *            内容
	 */
	protected void addLog(String content) {
		/*if (content != null) {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME, content, RequestAttributes.SCOPE_REQUEST);
		}*/
	}
	/**
	 * 验证是否合法手机号
	 * @param mobile
	 * @return
	 */
	public boolean isMobileNumber(String mobile) {
		  Pattern p = Pattern.compile(setting.getMobilePattern());
		  Matcher m = p.matcher(mobile);
		  return m.matches();
	}
}
