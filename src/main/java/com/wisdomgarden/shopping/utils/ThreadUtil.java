package com.wisdomgarden.shopping.utils;

import lombok.extern.log4j.Log4j2;

/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/17
 **/
@Log4j2
public class ThreadUtil {
    private ThreadUtil() {
    }

    /**
     * 线程休眠
     *
     * @param time 休眠时间
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Failed to Thread sleep, exception occurred: " + e);
        }
    }
}
