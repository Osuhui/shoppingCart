package com.wisdomgarden.shopping;

import com.wisdomgarden.shopping.adapter.ShoppingTaskAdapter;
import com.wisdomgarden.shopping.utils.ThreadUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @Function 主方法
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
@Log4j2
public class ShoppingMain {
    public static void main(String[] args) {
        while (true) {
            try {
                ShoppingTaskAdapter.autoAddTask();
                ThreadUtil.sleep(10 * 1000);
            } catch (Exception e) {
                log.error("Failed to shopping, exception occurred: " + e);
                break;
            }
        }
    }
}
