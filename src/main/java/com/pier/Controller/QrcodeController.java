package com.pier.Controller;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.pier.service.AlipayService;
import com.pier.bean.Book;
import com.pier.bean.Order;
import com.pier.dao.BookDao;
import com.pier.dao.OrderDao;
import com.pier.service.BookService;
import com.pier.service.OrderService;
import com.pier.utils.MatrixToImageWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhongweiwu
 * @date 2019/10/25 11:02
 */
@Slf4j
@Controller
@RequestMapping("/qrcode")
public class QrcodeController {

    private static final int HEIGHT = 100;
    private static final int WIDTH = 100;

    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AlipayService alipayService;

    public String generate(Order order, String title){
        AlipayTradePrecreateResponse response = alipayService.precreateOrder(order, title);
        if (response == null || !response.isSuccess()){
            return null;
        }
        return response.getQrCode();
    }

    @RequestMapping("/{orderId}")
    public void getQrCodeImg(@PathVariable String orderId,
                            HttpServletResponse resp) throws Exception {
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null){
            throw new RuntimeException("order is null, orderId:" + orderId);
        }
        Book book = bookService.getBook(order.getBookId());
        String qrCodeId = generate(order, book.getTitle());
        if (StringUtils.isBlank(qrCodeId)){
            log.info("get qrcodeid failed, orderid=" + orderId);
            throw new Exception("get qrcode failed");
        }

        try {
            MatrixToImageWriter.WriteImageStream(qrCodeId, HEIGHT, WIDTH, resp);
        }
        catch (Exception e) {
            log.error("create exception:url=" + qrCodeId, e);
        }
    }
}
