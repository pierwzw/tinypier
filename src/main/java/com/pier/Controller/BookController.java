package com.pier.Controller;

import com.alibaba.fastjson.JSON;
import com.pier.bean.Book;
import com.pier.bean.Order;
import com.pier.result.Result;
import com.pier.result.ResultUtil;
import com.pier.service.BookService;
import com.pier.service.OrderService;
import com.pier.utils.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongweiwu
 * @date 2019/10/24 17:58
 */
@Slf4j
@Controller
@RequestMapping("/book")
public class BookController {

    private static final String path = "E:\\PDF";
    private static final BigDecimal price = new BigDecimal(1.5);

    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;


    @GetMapping("/add")
    public String addBook(){
        return "book";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Book getBook(@PathVariable int id){
        return bookService.getBook(id);
    }

    @GetMapping("/title/{title}")
    @ResponseBody
    public List<Book> findBook(@PathVariable String title){
        return bookService.findBooks(title);
    }

    @GetMapping("/category/{category}")
    @ResponseBody
    public List<Book> findBooksByCategory(@PathVariable String category){
        return bookService.findBooksByCategory(category);
    }

    @PostMapping(value="/insert")
    public String insertBook(Book book){
        String passwd = RandomStringUtils.randomNumeric(6);
        book.setPasswd(passwd);
        int count = bookService.insertBook(book);
        if (count != 1) {
            return "error";
        }
        return "book";
    }

    @RequestMapping("/batch")
    public String batchInsert(){
        try{
            File file = new File(path);
            batchInsert(file);
        }catch (Exception e){
            log.error("batch insert book failed", e);
        }
        return "success";
    }

    private void batchInsert(File file){
        for (File toList:file.listFiles()){
            if (toList.isDirectory()){
                batchInsert(toList);
            }else if(toList.getName().endsWith(".pdf")){
                Book book = new Book();
                book.setTitle(toList.getName().substring(0, toList.getName().indexOf(".pdf")));
                book.setPrice(price);
                book.setPasswd(RandomStringUtils.randomNumeric(6));
                bookService.insertBook(book);
            }
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id){
        bookService.deleteBook(id);
        return "book_list";
    }

    @GetMapping("/fetch/{id}")
    public ModelAndView fetchBook(@PathVariable int id){
        Book book = bookService.getBook(id);
        // 创建订单
        Order order = new Order();
        String orderId = OrderUtil.generateOrderId();
        order.setOrderId(orderId);
        order.setBookId(book.getId());
        order.setPrice(book.getPrice());
        orderService.create(order);
        return new ModelAndView("index", "orderId", orderId);
    }

    @GetMapping("/list")
    public String listBook(){
        return "book_list";
    }

    @GetMapping("/layer")
    public String layer(){
        return "layer_test";
    }

    @PostMapping("/display")
    @ResponseBody
    public Object display(){
        List<Book> books = bookService.listBooks();
        return JSON.toJSON(books);
    }
}
