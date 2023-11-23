package com.zhc.cloud.common;

import com.zhc.cloud.common.exception.BusinessException;
import com.zhc.cloud.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.info("处理 Exception");
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return Response.error(businessException.getErrorCode(), businessException.getErrorMessage());
        }
        return Response.error();
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Response handleException(BindException e) {
        log.info("处理 Exception");
        return Response.error(e.getBindingResult().getAllErrors().get(0));
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleException(MethodArgumentNotValidException e) {
        log.info("处理 Exception");
        return Response.error(e.getBindingResult().getAllErrors().get(0));
    }

    /*@ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Response handleBussinessException(BusinessException e) {
        log.info("处理 BusinessException");
        return Response.error(e.getErrorCode(), e.getErrorMessage());
    }*/
}
