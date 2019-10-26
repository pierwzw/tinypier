package com.pier.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhongweiwu
 * @date 2019/10/26 13:25
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 8026588058970706432L;

    private int id;
    private String orderId;
    private int bookId;
    private BigDecimal price;
    private int status;
    private Date createTime;
    private Date updateTime;
}
