package com.slcollege.eduservice.client;

import com.slcollege.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// 根据服务名远程调用, name代表服务名, fallback代表出错后调用的类。
@FeignClient(name = "service-vod",fallback = VideoFileDegradeFeignClient.class)
@Component
public interface VideoClient {
    // 定义调用的方法的路径
    // 根据视频id删除阿里云视频
    @DeleteMapping("/eduvod/video/removeAliyunVideo/{videoId}")
    public Result removeAliyunVideo(@PathVariable("videoId") String videoId);

    // 删除多个阿里云视频的方法
    @DeleteMapping("/eduvod/video/deleteSeveral")
    public Result deleteSeveral(@RequestParam("videoIdList") List<String> videoIdList);
}
