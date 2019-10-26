package com.pier.Enum;

/**
 * @author zhongweiwu
 * @date 2019/10/26 16:01
 */
public enum OrderStatusEnum {

    NOPAY(0), SUCCESS(1), FAIL(2), CLOSED(3);

    private int status;

    OrderStatusEnum(int status) {
        this.status = status;
    }

    public int value() {
        return status;
    }

}
