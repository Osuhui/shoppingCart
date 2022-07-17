package com.wisdomgarden.shopping.adapter;

import com.wisdomgarden.shopping.bean.ShoppingCartInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Function
 * @Author huiwl
 * @Date 2022/7/17
 **/
public class ShoppingTaskAdapterTest {

    @Test
    public void autoAddTask() {
        ShoppingTaskAdapter.autoAddTask();
    }

    @Test
    public void test() {
        ShoppingCartInfo shoppingCartInfo = new ShoppingCartInfo();
        shoppingCartInfo.setProductName("ipad");
        shoppingCartInfo.setProductNum(1);
        shoppingCartInfo.setProductPrice(2399.00);

        ShoppingCartInfo shoppingCartInfo1 = new ShoppingCartInfo();
        shoppingCartInfo1.setProductName("显示器");
        shoppingCartInfo1.setProductNum(1);
        shoppingCartInfo1.setProductPrice(1799.00);

        ShoppingCartInfo shoppingCartInfo2 = new ShoppingCartInfo();
        shoppingCartInfo2.setProductName("啤酒");
        shoppingCartInfo2.setProductNum(12);
        shoppingCartInfo2.setProductPrice(25.00);

        ShoppingCartInfo shoppingCartInfo3 = new ShoppingCartInfo();
        shoppingCartInfo3.setProductName("面包");
        shoppingCartInfo3.setProductNum(5);
        shoppingCartInfo3.setProductPrice(90.00);

        ArrayList<ShoppingCartInfo> shoppingCartInfos = new ArrayList<>();
        shoppingCartInfos.add(shoppingCartInfo2);
        shoppingCartInfos.add(shoppingCartInfo1);
        shoppingCartInfos.add(shoppingCartInfo);
        shoppingCartInfos.add(shoppingCartInfo3);

        List<ShoppingCartInfo> infos = shoppingCartInfos.stream().sorted(Comparator.comparing(ShoppingCartInfo::getProductPrice, Comparator.reverseOrder())).collect(Collectors.toList());
        System.out.println(infos);
    }
}