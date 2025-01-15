//package com.zhc.cloud.service.impl;
//
//import com.alibaba.fastjson.JSON;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Pattern;
//
///**
// * @author zhuhongcang
// * @date 2023/12/27
// */
//public class Test {
//    public static void main(String[] args) {
//        String detailStr = "[{\"order_no\":\"20051800000390\",\"sku\":\"2236191440\",\"num\":2}," +
//                "{\"order_no\":\"20051800000390\",\"sku\":\"2236191448\",\"num\":2}," +
//                "{\"order_no\":\"20051800000451\",\"sku\":\"2236191441\",\"num\":2}," +
//                "{\"order_no\":\"20051800000451\",\"sku\":\"SKU003\",\"num\":2}," +
//                "{\"order_no\":\"20051800000549\",\"sku\":\"2236191447\",\"num\":1}," +
//                "{\"order_no\":\"20051800000549\",\"sku\":\"SKU002\",\"num\":1}," +
//                "{\"order_no\":\"20051800000576\",\"sku\":\"2236191440\",\"num\":1}]";
//        List<HashMap> detailList = JSON.parseArray(detailStr, HashMap.class);
//        List<HashMap> matchRule1 = JSON.parseArray("[{\"attr\":\"sku\",\"rule\":\"^\\\\d*1448$\",\"sign\":\"and\"}]", HashMap.class);
//        List<HashMap> matchRule2 = JSON.parseArray("[{\"attr\":\"num\",\"rule\":\"^1$\",\"sign\":\"and\"}]", HashMap.class);
//        List<HashMap> matchRule3 = JSON.parseArray("[{\"attr\":\"num\",\"rule\":\"^1$\",\"sign\":\"and\",},{\"attr\":\"order_no\",\"rule\":\"^\\\\d*576$\",\"sign\":\"and\",}]", HashMap.class);
//
//        // 这里定义3个规则条件 matchRule1、matchRule2、matchRule3
//
//        System.out.println("找出订单明细sku尾数是1448的数据");
//        System.out.println(findDetails(detailList, matchRule1));
//        System.out.println("找出订单明细num为1的数据");
//        System.out.println(findDetails(detailList, matchRule2));
//        System.out.println("找出订单明细num为1并且单号尾数是576的数据");
//        System.out.println(findDetails(detailList, matchRule3));
//    }
//
//    public static List<HashMap> findDetails(List<HashMap> detailList, List<HashMap> matchRule) {
//        // 这里是具体的实现代码
//        List<HashMap> list = new ArrayList<>();
//        for (HashMap detail : detailList) {
//            Boolean isNeedAdd = null;
//            for (HashMap ruleItem : matchRule) {
//                String attr = ruleItem.get("attr").toString();
//                String rule = ruleItem.get("rule").toString();
//                String sign = ruleItem.get("sign").toString();
//                String value = detail.get(attr).toString();
//                int signVal = "and".equals(sign) ? 1 : 0;
//                Pattern pattern = Pattern.compile(rule);
//                boolean isMatch = pattern.matcher(value).matches();
//                isNeedAdd = isNeedAdd == null ? isMatch : signVal == 1 ? isNeedAdd && isMatch : isNeedAdd || isMatch;
//            }
//            if (isNeedAdd != null && isNeedAdd) {
//                list.add(detail);
//            }
//        }
//        return list;
//    }
//
//}
