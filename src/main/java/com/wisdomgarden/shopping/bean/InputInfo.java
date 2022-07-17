package com.wisdomgarden.shopping.bean;

import java.util.List;

/**
 * @Function 输入信息类
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class InputInfo {
    /**
     * 促销信息
     */
    private List<PromotionInfo> promotionInfoList;

    /**
     * 购物信息
     */
    private List<ShoppingCartInfo> shoppingCartInfoList;

    /**
     * 结算日期
     */
    private String settlementDate;

    /**
     * 优惠券信息
     */
    private List<CouponInfo> couponInfoList;

    public List<PromotionInfo> getPromotionInfoList() {
        return promotionInfoList;
    }

    public void setPromotionInfoList(List<PromotionInfo> promotionInfoList) {
        this.promotionInfoList = promotionInfoList;
    }

    public List<ShoppingCartInfo> getShoppingCartInfoList() {
        return shoppingCartInfoList;
    }

    public void setShoppingCartInfoList(List<ShoppingCartInfo> shoppingCartInfoList) {
        this.shoppingCartInfoList = shoppingCartInfoList;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public List<CouponInfo> getCouponInfoList() {
        return couponInfoList;
    }

    public void setCouponInfoList(List<CouponInfo> couponInfoList) {
        this.couponInfoList = couponInfoList;
    }

    @Override
    public String toString() {
        return "InputInfo{" + "promotionInfoList=" + promotionInfoList + ", shoppingCartInfoList=" + shoppingCartInfoList + ", settlementDate='" + settlementDate + '\'' + ", couponInfoList=" + couponInfoList + '}';
    }
}
