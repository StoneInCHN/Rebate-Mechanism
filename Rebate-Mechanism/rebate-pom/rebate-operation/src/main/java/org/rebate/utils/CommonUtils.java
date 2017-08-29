package org.rebate.utils;

import org.rebate.beans.Setting;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;

/**
 * Utils - 公共
 * 
 */
public final class CommonUtils {
	
	private static Setting setting = SettingUtils.get();
	
	/**
	 * 不可实例化
	 */
	private CommonUtils() {}

	/**
	 * 获取支付渠道(默认 通联支付)
	 * @return
	 */
	public static PaymentChannel getPaymentChannel(){
		PaymentChannel channel = PaymentChannel.ALLINPAY;//默认 通联支付
		if (setting.getPaymentChannel() == null) {
			LogUtil.debug(CommonUtils.class, "getPaymentChannel", "系统未配置支付渠道, 返回默认 通联支付");
			return channel;
		}
		try {
			channel = PaymentChannel.valueOf(setting.getPaymentChannel().toUpperCase());
		} catch (Exception e) {
			channel = PaymentChannel.ALLINPAY;//默认 通联支付
			LogUtil.debug(CommonUtils.class, "getPaymentChannel", "系统配置错误的支付渠道, 返回默认 通联支付");
		}
		LogUtil.debug(CommonUtils.class, "getPaymentChannel", "系统配置支付渠道:" + channel.toString());
		return channel;
	}

}