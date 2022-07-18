package com.wisdomgarden.shopping.adapter;

import com.wisdomgarden.shopping.bean.*;
import com.wisdomgarden.shopping.constant.Constant;
import com.wisdomgarden.shopping.service.impl.ShoppingConsumerServiceImpl;
import com.wisdomgarden.shopping.utils.FileUtil;
import com.wisdomgarden.shopping.utils.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Function 购物任务
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
@Log4j2
public class ShoppingTaskAdapter {
    private static final String INPUT_PATH = "\\input\\";

    private static final LinkedBlockingDeque<ShoppingTaskInfo> LINKED_DEQUE = new LinkedBlockingDeque<>();

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(2, 10, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static final ShoppingConsumerServiceImpl SHOPPING_CONSUMER_SERVICE = new ShoppingConsumerServiceImpl(LINKED_DEQUE);

    private ShoppingTaskAdapter() {
    }

    static {
        THREAD_POOL_EXECUTOR.execute(SHOPPING_CONSUMER_SERVICE);
    }

    public static void autoAddTask() {
        log.info("start to add shoppingTask");
        String inputPath = FileUtil.getCurrentPath() + INPUT_PATH;
        File fileDir = new File(inputPath);
        File[] files = fileDir.listFiles();
        if (files == null || files.length == 0) {
            log.info("not shopping input");
            return;
        }

        for (File file : files) {
            ShoppingTaskInfo shoppingTaskInfo = new ShoppingTaskInfo();
            shoppingTaskInfo.setFilePath(file.getPath());
            shoppingTaskInfo.setInputInfo(getInputInfo(file.getPath()));

            // 根据文件名判断任务是否已经存在
            if (LINKED_DEQUE.stream().anyMatch(taskInfo -> StringUtil.equals(taskInfo.getFilePath(), file.getPath()))) {
                log.info("the task is already exist, filePath: " + file.getPath());
                continue;
            }

            // 无购物，无需添加到任务列表
            if (shoppingTaskInfo.getInputInfo().getShoppingCartInfoList().isEmpty()) {
                log.info("no shopping info, need not to add task");
                continue;
            }
            LINKED_DEQUE.add(shoppingTaskInfo);
            log.info("success to add shopping task, filePath: " + file.getPath());
        }
    }

    /**
     * 获取输入的信息
     *
     * @param filePath 路径
     * @return InputInfo
     */
    private static InputInfo getInputInfo(String filePath) {
        log.info("start to get input info, filePath: " + filePath);
        InputInfo inputInfo = new InputInfo();
        String content = FileUtil.read(filePath);
        if (content.isEmpty()) {
            log.error("incorrect input file");
            return inputInfo;
        }

        // 根据换行符分割为数组
        String[] contents = content.split(Constant.NEW_LINE_SIGN);

        try {
            // 促销信息结束下标
            int promotionEndIndex = 0;
            for (int index = 0; index < contents.length; index++) {
                if (contents[index].isEmpty()) {
                    promotionEndIndex = index;
                    break;
                }
            }

            // 获取促销信息
            List<PromotionInfo> promotionInfos = getPromotionInfo(contents, promotionEndIndex);
            inputInfo.setPromotionInfoList(promotionInfos);

            // 购物信息结束下标
            int shoppingEndIndex = promotionEndIndex;
            for (int index = promotionEndIndex + 1; index < contents.length; index++) {
                if (contents[index].isEmpty()) {
                    shoppingEndIndex = index;
                    break;
                }
            }

            // 获取购物信息
            List<ShoppingCartInfo> shoppingCartInfos = getShoppingInfo(contents, promotionEndIndex, shoppingEndIndex);
            inputInfo.setShoppingCartInfoList(shoppingCartInfos);

            // 结算日期
            if (shoppingEndIndex == contents.length) {
                log.error("Failed to get settlementDate, incorrect input file");
                return inputInfo;
            }
            inputInfo.setSettlementDate(contents[shoppingEndIndex + 1]);

            // 获取优惠券信息
            List<CouponInfo> couponInfos = getCouponInfo(contents, shoppingEndIndex + 2);
            inputInfo.setCouponInfoList(couponInfos);
        } catch (RuntimeException e) {
            log.error("Failed to get input info, exception occurred: " + e);
            e.printStackTrace();

            // 删除无效文件
            FileUtil.delete(filePath);
            return inputInfo;
        }

        log.info(inputInfo.toString());
        return inputInfo;
    }

    /**
     * 获取促销信息
     *
     * @param contents 内容
     * @param endIndex 结束下标
     * @return List<PromotionInfo>
     */
    private static List<PromotionInfo> getPromotionInfo(String[] contents, int endIndex) throws NumberFormatException {
        log.info("start to get promotion info, endIndex: " + endIndex);
        ArrayList<PromotionInfo> promotionInfos = new ArrayList<>();

        // 如果第一行为空行，则无促销信息
        if (endIndex <= 0) {
            log.info("no promotions");
            return promotionInfos;
        }

        for (int index = endIndex - 1; index >= 0; index--) {
            String tmp = contents[index];
            String[] info = tmp.split(Constant.VERTICAL_SIGN);

            PromotionInfo promotionInfo = new PromotionInfo();
            promotionInfo.setDate(info[0].trim());
            promotionInfo.setDiscount(Double.parseDouble(info[1].trim()));
            promotionInfo.setProductType(info[2].trim());

            promotionInfos.add(promotionInfo);
        }

        log.info(promotionInfos.toString());
        return promotionInfos;
    }

    /**
     * 获取购物信息
     *
     * @param contents   内容
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @return List<ShoppingCartInfo>
     */
    private static List<ShoppingCartInfo> getShoppingInfo(String[] contents, int startIndex, int endIndex) throws NumberFormatException {
        log.info("start to get shopping info, startIndex:{}, endIndex:{} ", startIndex, endIndex);
        ArrayList<ShoppingCartInfo> shoppingCartInfos = new ArrayList<>();

        // 如果结束下标小于等于开始下标，则无购物信息
        if (endIndex <= startIndex) {
            log.error("no shopping");
            return shoppingCartInfos;
        }

        for (int index = endIndex - 1; index > startIndex; index--) {
            String tmp = contents[index];
            int starIndex = tmp.indexOf(Constant.STAR_SIGN);
            int colonIndex = tmp.indexOf(Constant.COLON_SIGN);
            if (starIndex == Constant.INVALID_INDEX || colonIndex == Constant.INVALID_INDEX) {
                continue;
            }

            ShoppingCartInfo shoppingCartInfo = new ShoppingCartInfo();
            shoppingCartInfo.setProductNum(Integer.parseInt(tmp.substring(0, starIndex - 1)));
            shoppingCartInfo.setProductName(tmp.substring(starIndex + 2, colonIndex - 1));
            shoppingCartInfo.setProductPrice(Double.parseDouble(tmp.substring(colonIndex + 1)));
            shoppingCartInfos.add(shoppingCartInfo);
        }

        log.info(shoppingCartInfos.toString());
        return shoppingCartInfos;
    }

    /**
     * 获取优惠券信息
     *
     * @param contents   内容
     * @param startIndex 开始下标
     * @return List<CouponInfo>
     */
    private static List<CouponInfo> getCouponInfo(String[] contents, int startIndex) throws NumberFormatException {
        log.info("start to get coupon info, startIndex: " + startIndex);
        ArrayList<CouponInfo> couponInfos = new ArrayList<>();

        for (int index = startIndex; index < contents.length; index++) {
            String tmp = contents[index];
            String[] info = tmp.split(Constant.SPACE_SIGN);

            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setExpiration(info[0]);
            couponInfo.setLeastAmount(Integer.parseInt(info[1]));
            couponInfo.setDiscountAmount(Integer.parseInt(info[2]));
            couponInfos.add(couponInfo);
        }

        log.info(couponInfos.toString());
        return couponInfos;
    }
}
