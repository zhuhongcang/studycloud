package com.zhc.cloud.controller.test;

/**
 * @author zhuhongcang
 * @date 2024/2/20
 */
public class ClimbThread extends Thread {
    private Integer time;
    private Integer num;

    public ClimbThread() {
    }
    
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
