package com.pier.Controller;

import com.alibaba.fastjson.JSON;
import com.pier.bean.Book;
import com.pier.bean.Order;
import com.pier.result.Result;
import com.pier.result.ResultUtil;
import com.pier.service.BookService;
import com.pier.service.OrderService;
import com.pier.utils.OrderUtil;
import com.pier.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    private static final String passPath = "E:\\";

    private static final String txtFile  = "E:\\ziped\\txtfile\\密码链接说明";
    private static final String unzipPdf = "E:\\nozip";
    private static final String zipedPdf = "E:\\ziped\\zipedpdf\\";
    private static final String out = "E:\\ziped\\withtxt\\";
    private static final String moneyBookPath = "E:\\ziped\\技术宅赚钱秘籍.pdf";
    private static final String moneyGuidePath = "E:\\ziped\\赚钱指导说明书.zip";

    private static int n = 0;

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
    public ModelAndView batchInsert(){
        try{
            File file = new File(path);
            batchInsert(file);
        }catch (Exception e){
            log.error("batch insert book failed", e);
        }
        return new ModelAndView("success", "msg", n);
    }
    private void batchInsert(File file){
        for (File toList:file.listFiles()){
            if (toList.isDirectory()){
                batchInsert(toList);
            }else if(toList.getName().endsWith(".pdf")){
                Book book = new Book();
                book.setTitle(toList.getName().substring(0, toList.getName().lastIndexOf(".pdf")));
                book.setPrice(price);
                book.setPasswd(RandomStringUtils.randomNumeric(6));
                n += bookService.insertBook(book);
            }
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id){
        bookService.deleteBook(id);
        return "book_list";
    }

    /**
     * 打开密码说明文件时的链接入口
     * @param id
     * @return
     */
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

    /**
     * 获取指导说明书密码
     * @return
     */
    @GetMapping("/guide")
    public ModelAndView guide(){
        Book book = bookService.getBook(10);
        // 创建订单
        Order order = new Order();
        String orderId = OrderUtil.generateOrderId();
        order.setOrderId(orderId);
        order.setBookId(book.getId());
        order.setPrice(book.getPrice());
        orderService.create(order);
        return new ModelAndView("guide", "orderId", orderId);
    }

    /**
     * 压缩pdf文件和txt文件
     * @return
     * @throws IOException
     */
    @GetMapping("/batchpdf")
    @ResponseBody
    public String batchZip() throws IOException {
        File[] files = new File(unzipPdf).listFiles();
        List<Book> books = bookService.listBooks();
        int n=0;
        String dest1;
        String dest2;
        for(Book book:books){
            String title = book.getTitle() + ".pdf";
            for(File file:files){
                if ((title).equals(file.getName())){
                    dest1 = ZipUtil.zipPdf(file, zipedPdf, book.getPasswd());
                    dest2 = ZipUtil.zipSingle(new File(dest1), out, moneyBookPath,
                            moneyGuidePath, txtFile+"-"+book.getId() + ".txt");
                    log.info(++n + ". " + dest2);
                }
            }
        }
        return "success";
    }

    /**
     * 压缩已经压缩的pdf和txt文件
     * @return
     * @throws IOException
     */
    @GetMapping("/batchzip")
    @ResponseBody
    public String batchZipWithTxt() throws IOException {
        File[] files = new File(zipedPdf).listFiles();
        List<Book> books = bookService.listBooks();
        int n=0;
        String dest;
        for(Book book:books){
            String title = book.getTitle() + ".pdf";
            for(File file:files){
                if ((title).equals(file.getName())){
                    dest = ZipUtil.zipSingle(file, out, moneyBookPath,
                            moneyGuidePath, txtFile+"-"+book.getId() + ".txt");
                    log.info(++n + ". " + dest);
                }
            }
        }
        return "success";
    }

    /**
     * 创建密码链接说明
     * @return
     */
    @GetMapping("/txt")
    @ResponseBody
    public String makeTxt() throws IOException {
        List<Book> books = bookService.listBooks();
        int n=0;
        for (Book book:books){
            String content = "收集整理资料不易，请复制以下链接，在浏览器打开获取解压密码\n" +
                    "https://share.pierwzw.top/fetch/" + book.getId() + "\n\n";
            FileUtils.write(new File(txtFile+"-"+book.getId() + ".txt"), content, "utf-8");
            log.info(++n + ". " + txtFile+"-"+book.getId() + ".txt");
        }
       return "success";
    }

}
