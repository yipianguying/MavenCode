package com.slcollege.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduCourse;
import com.slcollege.eduservice.entity.EduCourseDescription;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.entity.frontvo.CourseFrontVo;
import com.slcollege.eduservice.entity.frontvo.CourseWebVo;
import com.slcollege.eduservice.entity.vo.CourseInfoVo;
import com.slcollege.eduservice.entity.vo.CoursePublishVo;
import com.slcollege.eduservice.mapper.EduCourseMapper;
import com.slcollege.eduservice.service.EduChapterService;
import com.slcollege.eduservice.service.EduCourseDescriptionService;
import com.slcollege.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.eduservice.service.EduVideoService;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */

@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    // 注入课程描述
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    // 注入章节和小节
    @Autowired
    private EduVideoService courseVideoService;
    @Autowired
    private EduChapterService courseChapterService;

    // 添加课程信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 向课程表里面添加课程基本信息
        // CourseInfoVo对象转化为eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int num = baseMapper.insert(eduCourse);

        if(num == 0) {
            // 添加失败
            throw new SlCollegeException(20001,"添加课程信息失败");
        }
        // 获取添加之后的课程id
        String courseId = eduCourse.getId();

        // 向课程简介表里面添加数据
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        // 设置课程简介的id就是课程id
        courseDescription.setId(courseId);
        courseDescriptionService.save(courseDescription);

        return courseId;
    }

    // 根据课程id查询课程基本信息的方法
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        // 1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        // 2.查询课程简介表
        EduCourseDescription  courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    // 根据id修改课程信息的方法
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        // 1.修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);

        if(update == 0) {
            throw new SlCollegeException(20001,"修改课程信息失败");
        }

        // 2.修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    // 根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {
        // 调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);

        return publishCourseInfo;
    }

    // 删除课程
    @Override
    public void removeCourse(String courseId) {
        // 1.根据课程id删除课程小节
        courseVideoService.removeVideoByCourseId(courseId);

        // 2.根据课程id删除章节
        courseChapterService.removeChapterByCourseId(courseId);

        // 3.根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        // 4.根据课程id删除课程
        int num = baseMapper.deleteById(courseId);
        // 如果结果为0,则删除失败
        if(num == 0) {
            throw new SlCollegeException(20001,"删除失败");
        }
    }

    // 条件查询带分页查询课程(前台部分)
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {
        // 根据教师id查询课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 判断条件值是否为空,不为空拼接
        // 判断一级分类是否存在
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        // 判断二级分类是否存在
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        // 判断销量
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }
        // 判断价格
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse, wrapper);

        // 和分页功能相关的所有数据
        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        // 下一页
        boolean hasNext = pageCourse.hasNext();
        // 上一页
        boolean hasPrevious = pageCourse.hasPrevious();

        // 把分页的数据获取,放到map集合中
        Map<String, Object> mapCourse = new HashMap<>();
        mapCourse.put("records",records);
        mapCourse.put("current", current);
        mapCourse.put("pages", pages);
        mapCourse.put("size", size);
        mapCourse.put("total", total);
        mapCourse.put("hasNext", hasNext);
        mapCourse.put("hasPrevious", hasPrevious);

        return mapCourse;
    }

    // 根据课程id, 编写sql语句查询课程详细信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }


}
