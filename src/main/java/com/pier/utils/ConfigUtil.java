package com.pier.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @auther zhongweiwu
 * @date 2018/10/18 15:28
 */
public class ConfigUtil {
	private static Log log = LogFactory.getLog(ConfigUtil.class);
	private static Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

	/**
	 * 加载class目录下的配置文件
	 */
	public static Properties loadProperties(String fileName) {
		if (propertiesMap.containsKey(fileName)) {
			return propertiesMap.get(fileName);
		}

		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(ConfigUtil.class.getResourceAsStream(fileName));
			propertiesMap.put(fileName, properties);
		}
		catch (Exception e) {
			log.error("load property" + fileName + "exception", e);
		}
		return properties;
	}

}
