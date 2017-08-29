package org.rebate.utils;

import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;

public class Test {

	public static void main(String[] args) {
		PaymentChannel aChannel = PaymentChannel.ALLINPAY;
		PaymentChannel bChannel = PaymentChannel.valueOf("ALLINPAY");
		if (aChannel == bChannel) {
			System.out.println("aChannel == bChannel");
		}

	}

}
