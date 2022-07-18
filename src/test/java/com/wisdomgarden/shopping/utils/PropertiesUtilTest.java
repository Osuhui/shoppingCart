package com.wisdomgarden.shopping.utils;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/17
 **/
public class PropertiesUtilTest {

    @Test
    public void getProp() {
        InputStream resourceAsStream = PropertiesUtilTest.class.getClassLoader().getResourceAsStream("product.properties");
        String electronic = PropertiesUtil.getProp(resourceAsStream, "electronic");
        String[] split = electronic.replace(" ", "").split(",");
        List<String> strings = Arrays.asList(split);
        System.out.println(strings);
    }
}