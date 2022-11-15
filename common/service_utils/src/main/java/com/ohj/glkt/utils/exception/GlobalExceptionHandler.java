package com.ohj.glkt.utils.exception;

import com.ohj.glkt.utils.result.Result;
import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    public Result exception(Exception exception){
        exception.printStackTrace();
        return Result.fail().message("执行全局异常处理");
    }
}
