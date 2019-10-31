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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping
    public ModelAndView notify(HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String,String> params = new TreeMap<>();
        //将异步通知中收到的待验证所有参数都存放到map中, 必须以字典排序
        for (String key : requestParams.keySet()) {
            params.put(key, requestParams.get(key)[0]);
        }
        log.info("verrify sign param:" + params);
        String orderId = params.get("out_trade_no");
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null){
            log.info("order is null, orderId:" + orderId);
            return new ModelAndView("paynotify", "msg", "order is null");
        }
        if(order.getStatus() != 0){
            log.info("order status error, orderId:" + orderId);
            return new ModelAndView("paynotify", "msg", "order status error");
        }
        if (!order.getPrice().toString().equals(params.get("total_amount"))) {
            log.info("price can not match, orderid:" + orderId);
            return new ModelAndView("paynotify", "msg", "price can not match");
        }
        boolean verifySignResult = alipayService.verifySign(params);
        if (verifySignResult) {
            Book book = bookService.getBook(order.getBookId());
            String passwd = book.getPasswd();
            return new ModelAndView("passwd", "passwd", passwd);
        } else {
            log.info("pay falied, orderid:" + orderId);
            return new ModelAndView("paynotify", "msg", "pay failed");
        }
    }
}
