package com.slcollege.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduCourse;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.service.EduCourseService;
import com.slcollege.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
*  和用户相关的的教师数据的接口
* */
@RestController
@RequestMapping("/eduservice/teacherFront")
@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    // 1.分页查询教师的方法,page-分页,limit-记录数
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public Result getTeacherFrontList(@PathVariable long page, @PathVariable long limit) {
        Page<EduTeacher> pageTeacher = new Page<>(page, limit);
        Map<String, Object> teacherMap = eduTeacherService.getTeacherFrontList(pageTeacher);

        // 返回分页中的所有数据
        return Result.ok().data(teacherMap);
    }

    // 2.教师详情的功能
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public Result getTeacherFrontInfo(@PathVariable String teacherId) {
        // 根据教师id查询教师基本信息
        EduTeacher teacher = eduTeacherService.getById(teacherId);

        // 根据教师id查询所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = eduCourseService.list(wrapper);

        // 返回查询到的信息
        return Result.ok().data("teacher",teacher).data("courseList",courseList);
    }

}
