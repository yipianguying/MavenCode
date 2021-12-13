package com.slcollege.eduservice.client;

import com.slcollege.commonutils.Result;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoFileDegradeFeignClient implements VideoClient {
    // 删除视频出错提示的方法
    @Override
    public Result removeAliyunVideo(String videoId) {
        return Result.error().message("删除视频出错!");
    }

    // 删除多个视频出错提示的方法
    @Override
    public Result deleteSeveral(List<String> videoIdList) {
        return Result.error().message("删除多个视频出错!");
    }
}
