package com.pier.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.pier.service.impl.WebSocketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

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
    @Autowired
    private WebSocketServiceImpl webSocketService;

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

    /**
     * 支付宝异步回调
     * @param request
     * @param response
     */
    @PostMapping("/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String,String> params = new TreeMap<>();
        //将异步通知中收到的待验证所有参数都存放到map中, 必须以字典排序
        for (String key : requestParams.keySet()) {
            params.put(key, requestParams.get(key)[0]);
        }
        log.info("verify sign param:" + params);
        String orderId = params.get("out_trade_no");
        webSocketService.setId(orderId);
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null){
            log.info("order is null, orderId:" + orderId);
            webSocketService.sendInfo("1,order is null", orderId);
        }
        if(order.getStatus() != 0){
            log.info("order status error, orderId:" + orderId);
            webSocketService.sendInfo("1,order status error", orderId);
        }
        if (!order.getPrice().toString().equals(params.get("total_amount"))) {
            log.info("price can not match, orderid:" + orderId);
            webSocketService.sendInfo("1,price can not match", orderId);
        }
        boolean verifySignResult = alipayService.verifySign(params);
        if (verifySignResult) {
            log.info("update order status->1, orderid:" + orderId);
            orderService.updateStatus(orderId, OrderStatusEnum.SUCCESS.value());
            Book book = bookService.getBook(order.getBookId());
            String passwd = book.getPasswd();
            webSocketService.sendInfo("0," + passwd, orderId);
        } else {
            log.info("pay falied, orderid:" + orderId);
            webSocketService.sendInfo("1,pay failed", orderId);
        }
        log.info("return back success");
        PrintWriter writer = response.getWriter();
        writer.println("success");
    }

    @GetMapping("/status/{orderId}")
    @ResponseBody
    public String orderStatus(@PathVariable String orderId){
        Result result = new Result();
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null){
            result.setCode("1");
            result.setMessage("order is null");
            return ResultUtil.commonRender(result);
        }
        String status = ""+orderService.getOrderByOrderId(orderId).getStatus();
        result.put("status", status);
        return ResultUtil.commonRender(result);
    }
}
