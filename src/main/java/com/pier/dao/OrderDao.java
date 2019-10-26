package com.pier.dao;

import com.pier.bean.Order;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhongweiwu
 * @date 2019/10/26 14:30
 */
public interface OrderDao {

    void create(@Param("order") Order order);

    Order getOrderByOrderId(String orderId);

    void deleteOrder(String orderId);

    void updateOrder(@Param("order") Order order);

    void updateStatus(@Param("orderId") String orderId, @Param("status") int status);

}
