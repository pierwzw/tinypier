package com.pier.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author zhongweiwu
 * @date 2019/10/27 22:45
 */
public class FileUtil {

    public void bufferedReaderAndWrite(String in, String out) {
        InputStreamReader reader = null;
        BufferedReader br = null;
        File fi = new File(in);
        File fo = new File(out);
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            InputStream is = new FileInputStream(fi);
            reader = new InputStreamReader(is);
            br = new BufferedReader(reader);

            OutputStream os = new FileOutputStream(fo, true);
            writer = new OutputStreamWriter(os);
            bw = new BufferedWriter(writer);

            String line = null;
            while((line = br.readLine()) != null){
                bw.write(line);
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
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeFile(String fileName, String content) throws IOException {
        FileUtils.write(new File(fileName), content, "UTF-8");
    }
}
