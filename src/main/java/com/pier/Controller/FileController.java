package com.pier.Controller;

import org.apache.catalina.core.ApplicationPart;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongweiwu
 * @date 2019/9/23 10:36
 */
@Controller
@RequestMapping("/file")
public class FileController {

    // 此处之前就已经报错
    //@Value("${spring.servlet.multipart.location}")
    private static final String filepath = "D:/tmp/image";

    /**
     * 打开文件上传请求页面
     * @return 指向JSP的字符串
     */
    @GetMapping("/upload/page")
    public String uploadPage() {
        return "/file/upload";
    }
    
    // 使用HttpServletRequest作为参数
    @PostMapping("/upload/request")
    @ResponseBody
    public Map<String, Object> uploadRequest(HttpServletRequest request) {
        boolean flag = false;
        MultipartHttpServletRequest mreq = null;
        // 强制转换为MultipartHttpServletRequest接口对象
        if (request instanceof MultipartHttpServletRequest) {
            mreq = (MultipartHttpServletRequest) request;
        } else {
            return dealResultMap(false, "上传失败");
        }
        // 获取MultipartFile文件信息
        MultipartFile mf = mreq.getFile("file");
        // 获取源文件名称
        String fileName = mf.getOriginalFilename();
        File file = new File(fileName);
        try {
            // 保存文件
            mf.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
            return dealResultMap(false, "上传失败");
        } 
        return dealResultMap(true, "上传成功");
    }
    
    // 使用Spring MVC的MultipartFile类作为参数,目录可以不存在
    @PostMapping("/upload/multipart")
    @ResponseBody
    public Map<String, Object> uploadMultipartFile(MultipartFile file) {
        File file1 = new File(filepath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(filepath + "/" + fileName);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            return dealResultMap(false, "上传失败");
        } 
        return dealResultMap(true, "上传成功");
    }

    // 目录一定要存在
    @PostMapping("/upload/part")
    @ResponseBody
    public Map<String, Object> uploadPart(ApplicationPart file) {
        /*File file1 = new File(filepath);
        if (!file1.exists()) {
            file1.mkdir();
        }*/
        // 获取提交文件名称
        String fileName = file.getSubmittedFileName();
        try {
            // 写入文件
            file.write(/*filepath + "/" + */fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return dealResultMap(false, "上传失败");
        } 
        return dealResultMap(true, "上传成功");
    }

    /**
     * 下载文件
     * @param request
     * @param filename
     * @return
     * @throws Exception
     */
    @RequestMapping("/fileDownload")
    public ResponseEntity<byte[]> fileDownLoad(HttpServletRequest request, @RequestParam String filename) throws Exception {

        //ServletContext servletContext = request.getServletContext();

        byte[] body = null;
        // 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
        //filename = new String(filename.getBytes("ISO8859-1"), "utf-8");//防止中文乱码
        InputStream in = new FileInputStream(new File(filepath+"/"+filename));//将该文件加入到输入流之中
        body = new byte[in.available()];
        in.read(body);//读入到输入流里面
        HttpHeaders headers = new HttpHeaders();//设置响应头
        // Content-Disposition用于下载
        headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }
    
    // 处理上传文件结果
    private Map<String, Object> dealResultMap(boolean success, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", success);
        result.put("msg", msg);
        return result;
    }
}