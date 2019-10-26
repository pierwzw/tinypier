package com.pier.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author zhongweiwu
 * @date 2019/10/25 20:09
 */
public class OrderUtil {

    public static String generateOrderId(){
        StringBuilder sb = new StringBuilder(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        for (int i = 0; i < 6; i++) {
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }
}
