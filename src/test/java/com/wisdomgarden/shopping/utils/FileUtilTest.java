package com.wisdomgarden.shopping.utils;

import org.junit.Test;


/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class FileUtilTest {

    @Test
    public void bufferReadToString() {
        FileUtil.read("src/main/resources/input/case001");
    }

    @Test
    public void write() {
        FileUtil.write("src/main/resources/output/case001", FileUtil.read("src/main/resources/input/case001"));
    }

    @Test
    public void getCurrentPath() {
        FileUtil.getCurrentPath();
    }
}