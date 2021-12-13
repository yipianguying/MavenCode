package com.slcollege.edupay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.commonutils.JwtUtils;
import com.slcollege.commonutils.Result;
import com.slcollege.edupay.entity.Order;
import com.slcollege.edupay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
@RestController
@RequestMapping("/edupay/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 生成订单的方法
    @PostMapping("orderInit/{courseId}")
    public Result orderInit(@PathVariable String courseId, HttpServletRequest request) {
        // 得到token字符串中的用户id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        // 创建订单,返回订单号
        String orderNumber = orderService.saveOrders(courseId,userId);

        return Result.ok().data("orderNumber",orderNumber);
    }

    // 根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public Result getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        // 根据订单id查询订单信息
        wrapper.eq("order_no",orderId);
        Order oneOrder = orderService.getOne(wrapper);

        return Result.ok().data("oneOrder",oneOrder);
    }

    // 根据课程id和用户id查询订单表中订单状态
    @GetMapping("isBuyCourse/{courseId}/{userId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",userId);
        // 查询支付状态,1代表已经支付
        wrapper.eq("status",1);
        // 是否能查出记录
        int count = orderService.count(wrapper);
        // count > 0代表已经支付
        if (count > 0) {
            return  true;
        } else {
            return false;
        }
    }

}

