package com.zhc.cloud.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhuhongcang
 * @date 2023/12/27
 */
public class Test1 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
//        list = list.stream().filter(item -> "2".equals(item)).collect(Collectors.toList());
        list.stream().filter("2"::equals).collect(Collectors.toList());
        System.out.println(list);
        /*String[] array = new String[]{"4", "5", "6", "7"};
        List<String> strings = Arrays.asList(array);
        List<String> string1 = Arrays.asList("8", "9", "10");
        array = strings.toArray(array);*/
//        Pattern pattern = Pattern.compile("^\\d*[5]{1}[6]{1}[7]{1}$");
        Pattern pattern = Pattern.compile("^\\d*1448$");
//        Pattern pattern = Pattern.compile("^\\d*567$");
//        Pattern pattern = Pattern.compile("^\\d*$");
        String str1 = "1234567";
        String str2 = "1234568";
        System.out.println(pattern.matcher(str1).matches());
        System.out.println(pattern.matcher(str2).matches());
    }
}
