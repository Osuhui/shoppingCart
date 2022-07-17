package com.wisdomgarden.shopping.bean;

import java.util.Comparator;

/**
 * @Function 促销信息
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class PromotionInfo {
    /**
     * 日期
     */
    private String date;

    /**
     * 折扣
     */
    private double discount;

    /**
     * 产品品类
     */
    private String productType;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "PromotionInfo{" + "date='" + date + '\'' + ", discount=" + discount + ", productType='" + productType + '\'' + '}';
    }
}
