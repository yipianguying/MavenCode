package com.slcollege.eduservice.controller.front;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.JwtUtils;
import com.slcollege.commonutils.Result;
import com.slcollege.commonutils.ordervo.CourseWebVoOrder;
import com.slcollege.eduservice.client.PayClient;
import com.slcollege.eduservice.entity.EduCourse;
import com.slcollege.eduservice.entity.chapter.ChapterVo;
import com.slcollege.eduservice.entity.frontvo.CourseFrontVo;
import com.slcollege.eduservice.entity.frontvo.CourseWebVo;
import com.slcollege.eduservice.service.EduChapterService;
import com.slcollege.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/courseFront")
@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private PayClient payClient;

    // 1.条件查询带分页查询课程,required = false不加的话 必须要有值
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public Result getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                     @RequestBody(required = false) CourseFrontVo courseFrontVo) {

        Page<EduCourse> pageCourse = new Page<>(page, limit);
        Map<String, Object> courseMap = eduCourseService.getCourseFrontList(pageCourse, courseFrontVo);

        return Result.ok().data(courseMap);
    }

    // 2.查询课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public Result getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        // 根据课程id, 编写sql语句查询课程信息
        CourseWebVo courseWeb = eduCourseService.getBaseCourseInfo(courseId);

        // 根据课程id查询章节和小节部分
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);

        // 根据课程id和用户id查询当前课程是否已经支付过了
        String userId = JwtUtils.getMemberIdByJwtToken(request);//得到用户id
        boolean flag;
        // 如果用户id不为空
        if(!StringUtils.isEmpty(userId)) {
            flag = payClient.isBuyCourse(courseId, userId);
        } else {
            flag = false;
        }
        // boolean flag = payClient.isBuyCourse(courseId, userId);

        return Result.ok().data("courseWeb",courseWeb).data("chapterVideoList",chapterVideoList).data("flag",flag);
    }

    // 根据课程id查询课程信息
    @PostMapping("getCourseOrderInfo/{courseId}")
    public CourseWebVoOrder getCourseOrderInfo(@PathVariable String courseId) {
        CourseWebVo baseCourseInfo = eduCourseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(baseCourseInfo,courseWebVoOrder);

        return courseWebVoOrder;
    }

}
