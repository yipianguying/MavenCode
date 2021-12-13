package com.slcollege.edustatistics.client;

import com.slcollege.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    // 查询某一天的注册人数-用于生成统计图表
    @GetMapping("/educenter/member/registerCount/{day}")
    public Result registerCount(@PathVariable("day") String day);
}
