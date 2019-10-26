package com.pier.Controller;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.pier.Enum.OrderStatusEnum;
import com.pier.service.AlipayService;
import com.pier.bean.Book;
import com.pier.bean.Order;
import com.pier.dao.BookDao;
import com.pier.dao.OrderDao;
import com.pier.result.Result;
import com.pier.result.ResultUtil;
import com.pier.service.BookService;
import com.pier.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhongweiwu
 * @date 2019/10/26 11:46
 */
@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AlipayService alipayService;

    @RequestMapping("/check/{orderId}")
    @ResponseBody
    public String checkOrder(@PathVariable String orderId){
        Result result = new Result();
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null){
            result.setCode("1");
            result.setMessage("order is null");
            return ResultUtil.commonRender(result);
        }
        AlipayTradeQueryResponse response = alipayService.queryOrder(orderId);
        if (response.isSuccess()) {
            Book book = bookService.getBook(order.getBookId());
            orderService.updateStatus(orderId, OrderStatusEnum.SUCCESS.value());
            result.put("passwd", book.getPasswd());
            return ResultUtil.commonRender(result);
        } else {
            log.info("order check failed,sub_code:" + response.getSubCode() + ", sub_msg:" + response.getSubMsg());
            orderService.updateStatus(orderId, OrderStatusEnum.FAIL.value());
            result.setCode("1");
            result.setMessage("pay failed");
            return ResultUtil.commonRender(result);
        }
    }
}
