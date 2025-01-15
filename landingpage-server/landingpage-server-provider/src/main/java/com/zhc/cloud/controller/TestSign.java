package com.zhc.cloud.controller;

import com.zhc.cloud.entity.ChannelCode;

/**
 * @author zhuhongcang
 * @date 2024/3/4
 */
public class TestSign {
    public static void main(String[] args) {
        int a = 1;
        int b=0;
        ChannelCode code = (ChannelCode) null;
        System.out.println(code);
        System.out.println(1^1);
        System.out.println(1^0);
        System.out.println(0^1);
        System.out.println(0^0);
    }
}
