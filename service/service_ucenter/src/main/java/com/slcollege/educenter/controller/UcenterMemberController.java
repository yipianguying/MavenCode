package com.slcollege.educenter.controller;


import com.slcollege.commonutils.JwtUtils;
import com.slcollege.commonutils.Result;
import com.slcollege.commonutils.ordervo.UcenterMemberOrder;
import com.slcollege.educenter.entity.UcenterMember;
import com.slcollege.educenter.entity.vo.RegisterVo;
import com.slcollege.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-22
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    // 登录的方法
    @PostMapping("userLogin")
    public Result userLogin(@RequestBody UcenterMember member) {
        // 调用service方法实现登录
        // 返回token值,使用Jwt生成
        String token = memberService.userLogin(member);
        return Result.ok().data("token",token);
    }

    // 注册的方法
    @PostMapping("userRegister")
    public Result userRegister(@RequestBody RegisterVo registerVo) {
        memberService.userRegister(registerVo);

        return Result.ok();
    }

    // 根据token获取用户信息
    @GetMapping("getMemberInfo")
    public Result getMemberInfo (HttpServletRequest request) {
        // 调用jwt工具类中的方法,根据request对象获取头信息,返回用户id
        String memberId= JwtUtils.getMemberIdByJwtToken(request);
        // 查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);

        return Result.ok().data("userInfo",member);
    }

    // 根据用户id获取用户信息
    @PostMapping("getUserOrderInfo/{userId}")
    public UcenterMemberOrder getUserOrderInfo(@PathVariable String userId) {
        UcenterMember member = memberService.getById(userId);
        // 把member对象里面的值赋值给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);

        return ucenterMemberOrder;
    }

    // 查询某一天的注册人数-用于生成统计图表
    @GetMapping("registerCount/{day}")
    public Result registerCount(@PathVariable String day) {
        Integer number = memberService.registerNumberDay(day);

        return Result.ok().data("registerNumber",number);
    }

}

