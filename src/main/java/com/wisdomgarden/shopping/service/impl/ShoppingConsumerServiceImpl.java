package com.wisdomgarden.shopping.service.impl;

import com.wisdomgarden.shopping.bean.*;
import com.wisdomgarden.shopping.constant.Constant;
import com.wisdomgarden.shopping.service.ShoppingConsumerService;
import com.wisdomgarden.shopping.utils.FileUtil;
import com.wisdomgarden.shopping.utils.StringUtil;
import com.wisdomgarden.shopping.utils.ThreadUtil;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @Function 购物结算实现类
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
@Log4j2
public class ShoppingConsumerServiceImpl implements ShoppingConsumerService {
    private static final String OUTPUT_PATH = "\\output\\";

    private static final String SETTLEMENT_AMOUNT = "settlement amount: ";

    private static final String PRODUCT_PROPERTIES = "product.properties";

    private static final List<String> ELECTRONIC = new ArrayList<>(Arrays.asList("ipad", "iphone", "显示器", "笔记本电脑", "键盘"));

    private static final List<String> FOOD = new ArrayList<>(Arrays.asList("面包", "饼干", "蛋糕", "牛肉", "鱼", "蔬菜"));

    private static final List<String> DAILY_NECESSITIES = new ArrayList<>(Arrays.asList("餐巾纸", "收纳箱", "咖啡杯", "雨伞"));

    private static final List<String> ALCOHOL = new ArrayList<>(Arrays.asList("啤酒", "白酒", "伏特加"));

    private static final int SLEEP_TIME = 2 * 1000;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    private final LinkedBlockingDeque<ShoppingTaskInfo> shoppingTask;

    public ShoppingConsumerServiceImpl(LinkedBlockingDeque<ShoppingTaskInfo> shoppingTask) {
        this.shoppingTask = shoppingTask;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (shoppingTask.isEmpty()) {
                    log.info("shoppingTask is empty");
                    ThreadUtil.sleep(SLEEP_TIME);
                    continue;
                }
                ShoppingTaskInfo shoppingTaskInfo = shoppingTask.take();

                // 计算结算金额
                String count = getCount(shoppingTaskInfo.getInputInfo());

                // 输出到文件
                File file = new File(shoppingTaskInfo.getFilePath());
                String content = FileUtil.read(file.getPath()) + Constant.NEW_LINE_SIGN + SETTLEMENT_AMOUNT + count;
                String outputFilePath = FileUtil.getCurrentPath() + OUTPUT_PATH + file.getName();

                if (FileUtil.write(outputFilePath, content) && FileUtil.delete(file.getPath())) {
                    log.info("success to write file");
                }

            } catch (Exception e) {
                log.error("Failed to consume, exception occurred: " + e);
                break;
            }
        }
    }

    /**
     * 计算商品结算金额
     *
     * @param inputInfo 输入信息
     * @return 结算金额
     */
    private synchronized String getCount(InputInfo inputInfo) {
        log.info("start to get shopping count");
        String count;

        // 计算折扣后的商品金额
        double promotionCount = getPromotionCount(inputInfo.getPromotionInfoList(), inputInfo.getShoppingCartInfoList(), inputInfo.getSettlementDate());

        // 计算优惠后的金额
        List<CouponInfo> couponInfoList = inputInfo.getCouponInfoList().stream()
                .filter(couponInfo -> couponInfo.getExpiration().compareToIgnoreCase(inputInfo.getSettlementDate()) >= 0)
                .filter(couponInfo -> couponInfo.getLeastAmount() <= promotionCount)
                .sorted(Comparator.comparing(CouponInfo::getDiscountAmount, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        if (couponInfoList.isEmpty()) {
            log.info("no coupons to meet full discount");
            count = DECIMAL_FORMAT.format(promotionCount);
        } else {
            count = DECIMAL_FORMAT.format(promotionCount - couponInfoList.get(0).getDiscountAmount());
        }

        log.info("shopping count is: " + count);
        return count;
    }

    /**
     * 计算折扣后的商品金额
     *
     * @param promotionInfos    折扣信息
     * @param shoppingCartInfos 商品信息
     * @param settlementDate    结算日期
     * @return 折扣后的商品金额
     */
    private double getPromotionCount(List<PromotionInfo> promotionInfos, List<ShoppingCartInfo> shoppingCartInfos, String settlementDate) {
        log.info("start to get promotion count");
        double promotionCount = 0.00;

        // 筛选结算日期和折扣日期相同的数据
        List<PromotionInfo> infoList = promotionInfos.stream()
                .filter(promotionInfo -> StringUtil.equals(promotionInfo.getDate(), settlementDate)).collect(Collectors.toList());

        // 如果为空，则无有效的折扣信息
        if (infoList.isEmpty()) {
            for (ShoppingCartInfo shoppingCartInfo : shoppingCartInfos) {
                promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice();
            }
            return promotionCount;
        }

        // 将折扣信息按照折扣力度排序，然后根据产品类型分组
        Map<String, List<PromotionInfo>> promotionMap = promotionInfos.stream().
                sorted(Comparator.comparing(PromotionInfo::getDiscount, Comparator.reverseOrder())).
                collect(Collectors.groupingBy(PromotionInfo::getProductType));

        for (ShoppingCartInfo shoppingCartInfo : shoppingCartInfos) {
            // 电子类商品
            if (ELECTRONIC.contains(shoppingCartInfo.getProductName())) {
                List<PromotionInfo> promotionInfoList = promotionMap.get(ProductEnum.ELECTRONIC.getName());
                if (promotionMap.containsKey(ProductEnum.ELECTRONIC.getName()) && !promotionInfoList.isEmpty()) {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice() * promotionInfoList.get(0).getDiscount();
                } else {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice();
                }
                continue;
            }

            // 食品类商品
            if (FOOD.contains(shoppingCartInfo.getProductName())) {
                List<PromotionInfo> promotionInfoList = promotionMap.get(ProductEnum.FOOD.getName());
                if (promotionMap.containsKey(ProductEnum.FOOD.getName()) && !promotionInfoList.isEmpty()) {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice() * promotionInfoList.get(0).getDiscount();
                } else {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice();
                }
                continue;
            }

            // 日用品
            if (DAILY_NECESSITIES.contains(shoppingCartInfo.getProductName())) {
                List<PromotionInfo> promotionInfoList = promotionMap.get(ProductEnum.DAILY_NECESSITIES.getName());
                if (promotionMap.containsKey(ProductEnum.DAILY_NECESSITIES.getName()) && !promotionInfoList.isEmpty()) {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice() * promotionInfoList.get(0).getDiscount();
                } else {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice();
                }
                continue;
            }

            // 酒类
            if (ALCOHOL.contains(shoppingCartInfo.getProductName())) {
                List<PromotionInfo> promotionInfoList = promotionMap.get(ProductEnum.ALCOHOL.getName());
                if (promotionMap.containsKey(ProductEnum.ALCOHOL.getName()) && !promotionInfoList.isEmpty()) {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice() * promotionInfoList.get(0).getDiscount();
                } else {
                    promotionCount += shoppingCartInfo.getProductNum() * shoppingCartInfo.getProductPrice();
                }
            }
        }
        return promotionCount;
    }
}
