package com.pier.config;

import com.pier.utils.ConfigUtil;

import java.util.Properties;

/**
 * @auther zhongweiwu
 * @date 2018/10/18 15:25
 */
public class Constant {
	private static final Properties PROPERTIES = ConfigUtil.loadProperties("/constant.properties");

	public static final String APP_ID = PROPERTIES.getProperty("APP_ID");

	public static final String APP_PRIVATE_KEY = PROPERTIES.getProperty("APP_PRIVATE_KEY");

	public static final String ALIPAY_PUBLIC_KEY = PROPERTIES.getProperty("ALIPAY_PUBLIC_KEY");

	public static final String ALIPAY_URL = PROPERTIES.getProperty("ALIPAY_URL");

	public static final String NOTIFY_URL = PROPERTIES.getProperty("NOTIFY_URL");
}
