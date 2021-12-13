package com.slcollege.edupay.client;

import com.slcollege.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface CourseClient {
    // 根据课程id查询课程信息
    @PostMapping("/eduservice/courseFront/getCourseOrderInfo/{courseId}")
    public CourseWebVoOrder getCourseOrderInfo(@PathVariable("courseId") String courseId);
}
