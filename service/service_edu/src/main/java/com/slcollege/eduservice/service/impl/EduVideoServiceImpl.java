package com.slcollege.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.client.VideoClient;
import com.slcollege.eduservice.entity.EduVideo;
import com.slcollege.eduservice.mapper.EduVideoMapper;
import com.slcollege.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    // 注入VideoClient
    @Autowired
    private VideoClient videoClient;
    // 1.根据课程id删除课程小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 1.根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> video= new QueryWrapper<>();
        video.eq("course_id",courseId);
        video.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(video);

        // 将EduVideo对象转换成String对象
        // 遍历集合
        List<String> eduVideoIds = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            // 遍历出每个小节的视频的id
            String videoSourceId = eduVideo.getVideoSourceId();

            // 如果小节中的视频id不为空
            if(!StringUtils.isEmpty(videoSourceId)) {
                // 放到eduVideoIds中
                eduVideoIds.add(videoSourceId);
            }
        }
        // 远程调用删除每个视频的方法
        if(eduVideoIds.size() > 0) {
            Result result = videoClient.deleteSeveral(eduVideoIds);
            // 如果状态为false,则提示熔断器
            if(!result.getSuccess()) {
                throw new SlCollegeException(20001,"删除视频失败,调用熔断器");
            }
        }

        // 删除小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(videoQueryWrapper);
    }
}
