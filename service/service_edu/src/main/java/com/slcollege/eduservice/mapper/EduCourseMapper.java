package com.slcollege.eduservice.mapper;

import com.slcollege.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slcollege.eduservice.entity.frontvo.CourseWebVo;
import com.slcollege.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    // 根据课程id查询课程确认信息
    public CoursePublishVo getPublishCourseInfo(String courseId);

    // 根据课程id, 编写sql语句查询课程详细信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
