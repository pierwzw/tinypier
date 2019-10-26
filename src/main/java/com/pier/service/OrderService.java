package com.pier.service;

import com.pier.bean.Order;
import com.pier.dao.OrderDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhongweiwu
 * @date 2019/10/26 17:03
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void create(Order order){
        orderDao.create(order);
    }

    public Order getOrderByOrderId(String orderId){
        return orderDao.getOrderByOrderId(orderId);
    }

    public void deleteOrder(String orderId){
        orderDao.deleteOrder(orderId);
    }

    public void updateOrder(Order order){
        orderDao.updateOrder(order);
    }

    public void updateStatus(String orderId, int status){
        orderDao.updateStatus(orderId, status);
    }
}
