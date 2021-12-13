package com.slcollege.edupay.service;

import com.slcollege.edupay.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-07
 */
public interface OrderService extends IService<Order> {
    // 生成订单的方法
    String saveOrders(String courseId, String userId);
}
