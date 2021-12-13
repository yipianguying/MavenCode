package com.slcollege.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.slcollege.commonutils.Result;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import com.slcollege.vod.Utils.ConstantVideoUtils;
import com.slcollege.vod.Utils.InitVideoClient;
import com.slcollege.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;


    // 上传视频到阿里云的方法
    @PostMapping("uploadAliyunVideo")
    public Result uploadAliyunVideo(MultipartFile file) {
        String videoId = vodService.uploadVideoAliyun(file);
        return Result.ok().data("videoId",videoId);
    }
    // 根据视频id删除阿里云中的视频
    @DeleteMapping("removeAliyunVideo/{id}")
    public Result removeAliyunVideo(@PathVariable String id) {
        try {
            // 1.初始化对象
            DefaultAcsClient defaultClient = InitVideoClient.initVideoClient(ConstantVideoUtils.ACCESS_KEY_ID, ConstantVideoUtils.ACCESS_KEY_SECRET);
            // 2.创建视频删除对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            // 3.向request设置视频id
            request.setVideoIds(id);
            // 4.调用初始化对象的方法实现删除
            defaultClient.getAcsResponse(request);
            return Result.ok();

        } catch(Exception e) {
            e.printStackTrace();
            throw new SlCollegeException(20001,"删除视频失败");
        }
    }

    // 删除多个阿里云视频的方法
    @DeleteMapping("deleteSeveral")
    public Result deleteSeveral(@RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeSeveralAliyunVideo(videoIdList);

        return Result.ok();
    }

    // 根据视频id获取视频的播放凭证
    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(@PathVariable String videoId) {
        try {
            // 创建初始化对象
            DefaultAcsClient defaultAcsClient = InitVideoClient.initVideoClient(ConstantVideoUtils.ACCESS_KEY_ID, ConstantVideoUtils.ACCESS_KEY_SECRET);
            // 创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            // 在request中设置视频id
            request.setVideoId(videoId);
            // 调用方法得到凭证
            GetVideoPlayAuthResponse response = defaultAcsClient.getAcsResponse(request);
            String playAuth = response.getPlayAuth();

            return Result.ok().data("playAuth", playAuth);

        }catch(Exception e){
            throw new SlCollegeException(20001,"获取凭证失败");
        }

    }

}
