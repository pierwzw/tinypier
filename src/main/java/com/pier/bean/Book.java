package com.pier.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhongweiwu
 * @date 2019/10/24 17:09
 */
@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 3400019196656832455L;

    private int id;
    private String title;
    private BigDecimal price;
    private String passwd;
    private String category;
    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
