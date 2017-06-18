package org.rebate.controller.base;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
import org.rebate.utils.DateUtils;
import org.rebate.utils.ExportExcel;




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
	
	@Resource(name = "taskExecutor")
	private Executor threadPoolExecutor;

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
    /**
     * 导出数据到Excel
     * 
     * @param response
     * @param baseEntityList 源集合
     * @param headers 需要导出的字段
     * @param headersName 字段对应列的列名
     */
    protected void exportListToExcel(HttpServletResponse response,
        List<Map<String, String>> eventRecordMapList, String title, String[] headers,
        String[] headersName) {
      if (StringUtils.isBlank(title)) {
        title = "XGG_DATA";
      }
      if (headers != null && headersName != null && headers.length == headersName.length) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < headersName.length; i++) {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("headerName", headersName[i]);
          jsonObject.put("header", headers[i]);
          jsonArray.put(jsonObject);
        }

        try {
          response.setContentType("octets/stream");
          // 导出文件名
          String filename = title + "_" + DateUtils.getDateFormatString(DateUtils.filePostfixFormat, new Date());
          response.addHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
          OutputStream out = response.getOutputStream();// 获得输出流

          Object locker = new Object();//当前主线程的一把锁

          ExportExcel ex = new ExportExcel(title, jsonArray, eventRecordMapList, out,locker);// 开启一个导出数据的线程

          synchronized (locker) {
            threadPoolExecutor.execute(ex); // 加入到线程池中执行
            locker.wait();//释放对象锁的控制，主线程等待，当且仅当其他线程notify该对象锁，主线程才可以继续执行下去
          }

          out.flush();
          out.close();

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }
}
