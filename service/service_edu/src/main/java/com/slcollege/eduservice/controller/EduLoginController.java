package com.slcollege.eduservice.controller;

import com.slcollege.commonutils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin  //解决跨域问题
public class EduLoginController {

    //login-登录
    @PostMapping("login")
    public Result login() {

        return Result.ok().data("token","admin");
    }
    //info-个人信息
    @GetMapping("info")
    public Result info() {

        return Result.ok().data("roles","[admin]").data("name","admin").data("avatar","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2367655072,408948354&fm=26&gp=0.jpg");
    }
}
