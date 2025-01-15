package com.zhc.cloud.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 下载文件Util
 *
 * @author zhuhongcang
 * @date 2021/4/26
 */
public class DownloadUtil {

    private static Logger log = LoggerFactory.getLogger(DownloadUtil.class);

    /**
     * 将文本内容写入到文件并下载
     *
     * @param response 响应
     * @param fileName 文件名
     * @param content  文本内容
     */
    public static void downloadString(HttpServletResponse response, String fileName, String content) {
        downloadString(response, fileName, content, "UTF-8", "GBK");
    }

    /**
     * 将文本内容写入到文件并下载
     *
     * @param response           响应
     * @param fileName           文件名
     * @param content            文本内容
     * @param respCharsetName    响应给浏览器的编码
     * @param contentCharsetName 文本内容写入时编码格式
     */
    public static void downloadString(HttpServletResponse response, String fileName, String content, String respCharsetName, String contentCharsetName) {
        OutputStream os = null;
        try {
            response.setContentType(getContentType(fileName));
            response.setCharacterEncoding(respCharsetName);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, respCharsetName));
            os = response.getOutputStream();
            os.write(content.getBytes(contentCharsetName));
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param response http响应
     * @param filePath 文件路径
     * @param fileName 下载文件的名称
     */
    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            File file = new File(filePath);
            //将文件导出到页面
            response.setContentType(getContentType(fileName));
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            in = new BufferedInputStream(new FileInputStream(file));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件名获取  ContentType
     *
     * @param fileName 文件名
     * @return ContentType
     */
    private static String getContentType(String fileName) {
        String contentType = null;
        //利用nio提供的类判断文件ContentType
        Path path = Paths.get(fileName);
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //若失败则调用另一个方法进行判断
        if (contentType == null) {
            contentType = new MimetypesFileTypeMap().getContentType(new File(fileName));
        }
        return contentType;
    }
}
