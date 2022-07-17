package com.wisdomgarden.shopping.utils;

import com.wisdomgarden.shopping.constant.Constant;
import lombok.extern.log4j.Log4j2;

import java.io.*;

/**
 * @Function 文件工具类
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
@Log4j2
public class FileUtil {
    /**
     * 构造方法私有化
     */
    private FileUtil() {
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return String
     */
    public static String read(String filePath) {
        log.info("start to read file: " + filePath);
        StringBuilder result = new StringBuilder();
        if (!isFileExist(filePath)) {
            log.error("Failed to read file, file is not exist!");
            return result.toString();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append(Constant.NEW_LINE_SIGN);
            }
        } catch (IOException e) {
            log.error("Failed to read file, exception occurred: " + e.getMessage());
            return result.toString();
        }
        log.info("success to read file, result: " + result);
        return result.toString();
    }

    /**
     * 写入文件
     *
     * @param filePath 文件路径
     * @param content 内容
     * @return boolean
     */
    public static boolean write(String filePath, String content) {
        log.info("start to write file, filePath:" + filePath);
        log.debug("content:" + content);
        File file = new File(filePath);

        // 如果路径不存在，则创建路径
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            log.error("file is not exist and failed to create!");
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(content);
        } catch (IOException e) {
            log.error("Failed to write file, exception occurred: " + e.getMessage());
            return false;
        }
        log.info("success to write file");
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean delete(String filePath) {
        if (!isFileExist(filePath)) {
            log.info("file is not exist, need not to delete");
            return true;
        }
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * 文件是否存在
     *
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 获取当前jar包路径
     *
     * @return 当前路径
     */
    public static String getCurrentPath() {
        try {
            File file = new File("");
            return file.getCanonicalPath();
        } catch (IOException e) {
            log.error("Failed to get current path, exception occurred: " + e.getMessage());
            return "";
        }
    }

}
