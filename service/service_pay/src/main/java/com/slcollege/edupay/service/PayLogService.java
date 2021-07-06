package com.slcollege.edupay.service;

import com.slcollege.edupay.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
public interface PayLogService extends IService<PayLog> {
    // 生成微信支付二维码的方法
    Map nativeInit(String orderNumber);

    // 根据订单号查询订单状态的方法
    Map<String, String> selectPayStatus(String orderNumber);

    // 添加记录到支付表,更新订单表订单状态
    void updateOrderStatus(Map<String, String> map);
}
