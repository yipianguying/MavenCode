package com.slcollege.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-pay")
public interface PayClient {

    // 根据课程id和用户id查询订单表中订单状态
    @GetMapping("/edupay/order/isBuyCourse/{courseId}/{userId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId);
}
