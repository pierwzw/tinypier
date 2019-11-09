package com.pier.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhongweiwu
 * @date 2019/11/8 11:13
 */
public class HttpUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
}
