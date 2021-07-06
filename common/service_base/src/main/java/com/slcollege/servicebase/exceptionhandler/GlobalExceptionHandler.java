package com.slcollege.servicebase.exceptionhandler;


import com.slcollege.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //全局异常处理
    @ExceptionHandler(Exception.class)// 指定异常
    @ResponseBody //为了能够返回数据
    public Result errorException(Exception e) {
        e.printStackTrace();
        return Result.error().message("执行了全局异常处理");
    }

    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//为了能够返回数据
    public Result errorException(ArithmeticException e){
        e.printStackTrace();
        return Result.error().message("执行了ArithmeticException异常处理...");
    }

    // 自定义异常处理
    @ExceptionHandler(SlCollegeException.class)
    @ResponseBody//为了能够返回数据
    public Result errorException(SlCollegeException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }
}
