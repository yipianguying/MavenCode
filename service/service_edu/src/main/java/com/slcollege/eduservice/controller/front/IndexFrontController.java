package com.slcollege.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduCourse;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.service.EduCourseService;
import com.slcollege.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    // 查询前8条热门课程,查询前四条名师
    @GetMapping("index")
    public Result index() {
        // 查询前八条热门课程
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        wrapperCourse.orderByDesc("id");
        wrapperCourse.last("limit 8");
        List<EduCourse> eduListCourse = courseService.list(wrapperCourse);

        // 查询前四条名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> eduListTeacher = teacherService.list(wrapperTeacher);

        return Result.ok().data("eduListCourse",eduListCourse).data("eduListTeacher",eduListTeacher);
    }
}
