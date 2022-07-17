package com.wisdomgarden.shopping.bean;

/**
 * @Function 优惠券信息
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class CouponInfo {
    /**
     * 过期时间
     */
    private String expiration;

    /**
     * 至少消费金额
     */
    private int leastAmount;

    /**
     * 优惠金额
     */
    private int discountAmount;

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getLeastAmount() {
        return leastAmount;
    }

    public void setLeastAmount(int leastAmount) {
        this.leastAmount = leastAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "CouponInfo{" + "expiration='" + expiration + '\'' + ", leastAmount=" + leastAmount + ", discountAmount=" + discountAmount + '}';
    }
}
