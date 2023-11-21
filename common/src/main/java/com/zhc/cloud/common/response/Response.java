package com.zhc.cloud.common.response;

import com.zhc.cloud.common.constant.ResponseConstants;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Getter
@Setter
public class Response<T> implements Serializable {
    /**
     * 状态码
     */
    private String code;

    /**
     * 返回的数据信息
     */
    private String msg;

    /**
     * 返回的数据实体
     */
    private T data;

    public Response() {
    }

    public Response(int String, T data) {
        this.code = code;
        this.data = data;
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> error(ErrorCode error) {
        return new Response<T>(error.getCode(), error.getMsg());
    }

    public static <T> Response<T> error() {
        return new Response<T>(ResponseConstants.ERROR_CODE, ResponseConstants.ERROR_MSG);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<T>(ResponseConstants.ERROR_CODE, msg);
    }

    public static <T> Response<T> error(T data) {
        return new Response<T>(ResponseConstants.ERROR_CODE, ResponseConstants.ERROR_MSG, data);
    }

    public static <T> Response<T> error(String code, String msg) {
        return new Response<T>(code, msg);
    }

    public static <T> Response<T> success(T data) {
        return new Response<T>(ResponseConstants.SUCCESS_CODE, ResponseConstants.SUCCESS_MSG, data);
    }

}
