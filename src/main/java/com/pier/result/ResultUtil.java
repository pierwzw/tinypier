package com.pier.result;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ResultUtil {
	private static final Log log = LogFactory.getLog(ResultUtil.class);

	/**
	 * 返回api格式结果
	 *
	 * @param obj
	 * @return
	 */
	public static String commonRender(Object obj) {
		return JSONObject.toJSONString(obj);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface JsonLibIgnore {
	}
}
