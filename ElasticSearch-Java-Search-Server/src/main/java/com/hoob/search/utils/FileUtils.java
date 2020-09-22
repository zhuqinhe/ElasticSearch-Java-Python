package com.hoob.search.utils;

import java.io.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static Properties getResource(File file) throws Exception {
        Properties p = new Properties();
        InputStream in = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            in = new BufferedInputStream(fin);
            p.load(in);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                fin = null;
                in = null;
            }
        }
        return p;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @return boolean
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void deleteDirectory(File file) {
        if (file.isFile()) {// 表示该文件不是文件夹
            file.delete();
            LOGGER.debug("delete file success,name is :{}", file.getAbsolutePath());
        } else {
            // 首先得到当前的路径
        	if(file.list() != null){
        		String[] childFilePaths = file.list();
        		if (ObjectUtils.isNotEmpty(childFilePaths)){
        			for (String childFilePath : childFilePaths) {
        				File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
        				deleteDirectory(childFile);
        			}
        		}
        	}
            file.delete();
            LOGGER.debug("delete file menu success,path is :{}", file.getAbsolutePath());
        }
    }

    /**
     * 保存文件
     *
     * @param msg      文件内容
     * @param path     路径
     * @param fileName 文件名字
     */
    public static void saveFile(String msg, String path, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        //
        String filePath = path + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            out.write(msg.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(filePath + "is not find,", e);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        File file = new File(filePath);
        return readFile(file);
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        try (ByteArrayOutputStream rst = new ByteArrayOutputStream(); InputStream in = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                rst.write(buffer, 0, length);
            }
            return rst.toString("UTF-8");
        } catch (Exception e) {
            LOGGER.error("read file[" + file.getAbsolutePath() + "] error:", e);
        }
        return null;
    }

}
