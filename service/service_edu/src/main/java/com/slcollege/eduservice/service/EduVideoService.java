package com.slcollege.eduservice.service;

import com.slcollege.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
public interface EduVideoService extends IService<EduVideo> {
    // 1.根据课程id删除课程小节
    void removeVideoByCourseId(String courseId);
}
