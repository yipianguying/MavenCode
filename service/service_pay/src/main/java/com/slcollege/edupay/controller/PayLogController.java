package com.slcollege.edupay.controller;


import com.slcollege.commonutils.Result;
import com.slcollege.edupay.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
@RestController
@RequestMapping("/edupay/payLog")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    // 生成微信支付二维码的方法
    // 参数是订单号
    @GetMapping("nativeInit/{orderNumber}")
    public Result nativeInit(@PathVariable String orderNumber) {
        // 返回信息,包含二维码地址,还有其他和订单相关的信息
        Map map =  payLogService.nativeInit(orderNumber);
        System.out.println("*******返回二维码map集合:"+map);
        return Result.ok().data(map);
    }

    // 查询订单支付状态的方法,,参数为订单号,根据订单号查询订单状态
    @GetMapping("selectPayStatus/{orderNumber}")
    public Result selectPayStatus(@PathVariable String orderNumber) {
        Map<String, String> map = payLogService.selectPayStatus(orderNumber);
        System.out.println("=========查询订单状态map集合:"+map);

        if(map == null) {
            return Result.error().message("支付错误!");
        }

        // 如果返回map里面不为空,通过map获取订单状态,SUCCESS代表支付成功
        if(map.get("trade_state").equals("SUCCESS")) {
            // 添加记录到支付表,更新订单表订单状态
            payLogService.updateOrderStatus(map);

            return Result.ok().message("支付成功!");
        }
        // 25000：订单支付中，不做任何提示
        return Result.ok().code(25000).message("支付中......");
    }

}

