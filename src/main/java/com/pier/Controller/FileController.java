package com.pier.Controller;

import com.pier.bean.Book;
import com.pier.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @author zhongweiwu
 * @date 2019/9/23 10:36
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    // 此处之前就已经报错
    //@Value("${spring.servlet.multipart.location}")
    private static final String filepath = "D:/tmp/image";

    static String path1 = "C:\\Users\\pier\\Desktop\\direcory.txt";
    static String path2 = "C:\\Users\\pier\\Desktop\\books.txt";
    static String path3 = "C:\\Users\\pier\\Desktop\\out.txt";

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

    public static void diff() {
        InputStreamReader reader1 = null;
        InputStreamReader reader2 = null;
        BufferedReader br1 = null;//还可以使用scanner来读取不过要慢十倍：
        BufferedReader br2 = null;
        //Scanner sc = new Scanner(new File(""));
//读入数字只能用scanner,BufferedWriter没有读入数字的方法
        File fo = new File(path3);
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            InputStream is1 = new FileInputStream(path1);
            InputStream is2 = new FileInputStream(path2);
            reader1 = new InputStreamReader(is1, "utf-8");
            reader2 = new InputStreamReader(is2, "utf-8");
            br1 = new BufferedReader(reader1);
            br2 = new BufferedReader(reader2);

            OutputStream os = new FileOutputStream(fo, false);
            writer = new OutputStreamWriter(os, "utf-8");
            bw = new BufferedWriter(writer);

            String line1 = null;
            String line2 = null;
            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();

            while((line1 = br1.readLine()) != null){
                list1.add(line1);
            }
            while((line2 = br2.readLine()) != null){
                list2.add(line2);
            }
            list1.removeAll(list2);
            for(String str:list1){
                bw.write(str+"\n");
                bw.flush();//非常重要,close的时候会强制flush
            }

            bw.write("\n");//可以使用bw.newLine();代替
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                br1.close();
                br2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static int n=0;

    /**
     * 复制文件到另一个文件夹
     * @param file
     * @param out
     * @throws IOException
     */
    public static void copyAll(File file, String out) throws IOException {
        for (File toList:file.listFiles()){
            if (toList.isDirectory()){
                copyAll(toList, out);
            }else if(toList.getName().endsWith(".pdf")){
                System.out.println(++n + ". " + toList.getName());
                FileUtils.copyFile(toList, new File(out + "\\" + toList.getName()));
            }
        }

    }

    public static void main(String[] args) throws IOException {
        /*String in = "E:\\PDF";
        String out = "E:\\all";
        copyAll(new File(in), out);*/
        diff();
    }
}