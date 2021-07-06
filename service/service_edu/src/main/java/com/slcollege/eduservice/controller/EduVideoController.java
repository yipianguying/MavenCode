package com.slcollege.eduservice.controller;


import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.client.VideoClient;
import com.slcollege.eduservice.entity.EduVideo;
import com.slcollege.eduservice.service.EduVideoService;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    // 注入VideoClient
    @Autowired
    private VideoClient videoClient;

    // 添加小节的方法
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);

        return Result.ok();
    }
    // 删除小节,并删除阿里云中视频的方法
    @DeleteMapping("{passageId}")
    public Result deleteVideo(@PathVariable String passageId) {
        // 根据小节id得到视频id的方法
        EduVideo eduVideo = eduVideoService.getById(passageId);
        String videoSourceId = eduVideo.getVideoSourceId();

        // 判断小节里面是否有视频id,不为空则可以删除
        if (!StringUtils.isEmpty(videoSourceId)) {
            // 根据视频id,调用删除视频的方法(远程调用另一个模块中的方法,使用nacos技术)
            Result result = videoClient.removeAliyunVideo(videoSourceId);
            // 如果状态为false,则提示熔断器
            if(!result.getSuccess()) {
                throw new SlCollegeException(20001,"删除视频失败,调用熔断器");
            }
        }

        // 删除小节的方法
        eduVideoService.removeById(passageId);

        return Result.ok();
    }

    // 根据id查询小节的方法
    @GetMapping("getVideoInfo/{videoId}")
    public Result getVideoInfo(@PathVariable String videoId) {
        EduVideo video = eduVideoService.getById(videoId);

        return Result.ok().data("video",video);
    }
    // 修改小节的方法
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.updateById(eduVideo);

        return Result.ok();
    }
}

