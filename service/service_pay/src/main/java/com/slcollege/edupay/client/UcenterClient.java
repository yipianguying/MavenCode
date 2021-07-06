package com.slcollege.edupay.client;

import com.slcollege.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    // 根据用户id获取用户信息
    @PostMapping("/educenter/member/getUserOrderInfo/{userId}")
    public UcenterMemberOrder getUserOrderInfo(@PathVariable("userId") String userId);
}
