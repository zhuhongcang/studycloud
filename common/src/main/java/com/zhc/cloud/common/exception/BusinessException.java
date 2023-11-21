package com.zhc.cloud.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends Exception {
    private String errorCode;

    private String errorMessage;
}
