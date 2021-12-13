package com.slcollege.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduCourse;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.entity.vo.CourseInfoVo;
import com.slcollege.eduservice.entity.vo.CoursePublishVo;
import com.slcollege.eduservice.entity.vo.CourseQuery;
import com.slcollege.eduservice.entity.vo.TeacherQuery;
import com.slcollege.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    // 课程列表
    @GetMapping
    public Result getCourseList() {
        List<EduCourse> list = courseService.list(null);
        return Result.ok().data("list",list);
    }

    // 添加课程基本信息的方法
    // @RequestBody:主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)
    @PostMapping("addCourseInfo")
    public Result  addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        // 返回添加之后的课程id,为了第二步的添加大纲
        String courseId = courseService.saveCourseInfo(courseInfoVo);

        return Result.ok().data("courseId",courseId);
    }

    // 根据课程id查询课程基本信息的方法
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return Result.ok().data("courseInfoVo",courseInfoVo);
    }

    // 根据id修改课程信息
    @PostMapping("updateCourseInfo")
    public Result updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return Result.ok();
    }

    // 根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{courseId}")
    public Result getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(courseId);

        return Result.ok().data("publishCourse",coursePublishVo);
    }

    // 课程最终发布
    // 修改课程状态
    @PostMapping("publishCourseUpdate/{courseId}")
    public Result publishCourseUpdate(@PathVariable String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        // 设置课程发布状态
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);

        return Result.ok();
    }

    // 删除课程
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);

        return Result.ok();
    }

    // 条件查询带分页
    @ApiOperation(value = "条件分页课程带查询")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public Result pageCourseCondition(@PathVariable long current, @PathVariable long limit,
                                       @RequestBody(required = false) CourseQuery courseQuery) {//@RequestBody:使用json形式传递数据,
        // 把json数据封装到对应对象里面,需要使用post提交方式
        // 创建一个page对象
        Page<EduCourse> coursePage = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis-动态sql
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        // 判断条件值是否为空，如果不为空拼接条件'
        if(!StringUtils.isEmpty(title)) {
            // 构建条件
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)) {
            wrapper.eq("status",status);
        }

        // 根据创建时间排序-降序排序
        wrapper.orderByDesc("gmt_create");

        // 调用方法实现条件分页查询
        courseService.page(coursePage,wrapper);

        // 总记录数
        long total = coursePage.getTotal();
        // 数据list集合
        List<EduCourse> records = coursePage.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }
}

