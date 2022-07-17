package com.wisdomgarden.shopping.utils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/17
 **/
@Log4j2
public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    private PropertiesUtil() {
    }

    /**
     * 获取properties值
     *
     * @param inputStream properties
     * @param key key
     * @return String 值
     */
    public static String getProp(InputStream inputStream, String key) {
        try {
            PROPERTIES.load(inputStream);
            return PROPERTIES.getProperty(key);
        } catch (IOException e) {
            log.error("Failed to load properties");
        }
        return "";
    }
}
