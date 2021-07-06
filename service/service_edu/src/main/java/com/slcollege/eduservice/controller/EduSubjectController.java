package com.slcollege.eduservice.controller;


import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduSubject;
import com.slcollege.eduservice.entity.subject.OneSubject;
import com.slcollege.eduservice.entity.subject.TwoSubject;
import com.slcollege.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-23
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;

    // 添加课程分类
    // 获取到上传过来的文件, 把文件内容读取出来
    @PostMapping("addSubject")
    public Result addSubject(MultipartFile file) {
        // 得到上传的excel文件

        subjectService.saveSubject(file,subjectService);
        return Result.ok();
    }

    // 课程分类列表 (树形结构)
    @GetMapping("getAllSubject")
    public Result getAllSubject() {
        // list集合中的泛型时一级分类,因为一级分类本身包含二级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return Result.ok().data("list",list);
    }

}

