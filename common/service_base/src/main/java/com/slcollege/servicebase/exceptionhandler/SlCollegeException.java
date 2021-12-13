package com.slcollege.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
*  自定义异常类
* */
@Data//生成get和set方法
@AllArgsConstructor//生成有参数的构造方法
@NoArgsConstructor// 生成无参数的构造方法
public class SlCollegeException extends RuntimeException{

    @ApiModelProperty(value = "状态码")
    private Integer code;// 状态码

    private String msg;// 异常信息



}
