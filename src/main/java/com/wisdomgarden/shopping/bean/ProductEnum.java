package com.wisdomgarden.shopping.bean;

/**
 * @Function 商品分类
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public enum ProductEnum {
    ELECTRONIC("电子"),
    FOOD("食品"),
    DAILY_NECESSITIES("日用品"),
    ALCOHOL("酒类");

    private final String name;

    ProductEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
