package com.zhc.cloud.common.response;

/**
 * 异常码接口
 *
 * @author zhuhongcang
 * @date 2023/4/19
 */
public interface ErrorCode {
    /**
     * 获取异常码
     *
     * @return 异常码
     */
    String getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMsg();
}
