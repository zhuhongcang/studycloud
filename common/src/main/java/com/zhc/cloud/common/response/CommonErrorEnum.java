package com.zhc.cloud.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通用异常码
 *
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorEnum implements ErrorCode {
    /**
     * 成功
     */
    SYSTEM_SUCCESS("200", "success"),
    /**
     * 失败
     */
    SYSTEM_ERROR("500", "success");

    /**
     * code
     */
    private final String code;
    /**
     * msg
     */
    private final String msg;
}
