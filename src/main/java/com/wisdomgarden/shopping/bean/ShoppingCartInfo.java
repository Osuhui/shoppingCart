package com.wisdomgarden.shopping.bean;

/**
 * @Function 购物车详细信息
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class ShoppingCartInfo {
    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品单价
     */
    private double productPrice;

    /**
     * 商品数量
     */
    private int productNum;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    @Override
    public String toString() {
        return "ShoppingCartInfo{" + "productName='" + productName + '\'' + ", productPrice='" + productPrice + '\'' + ", productNum=" + productNum + '}';
    }
}
