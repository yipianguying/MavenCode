package com.slcollege.eduservice.service;

import com.slcollege.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slcollege.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    // 添加课程分类
   public  void saveSubject(MultipartFile file,EduSubjectService subjectService);

   // 课程分类列表,树型结构
    List<OneSubject> getAllOneTwoSubject();
}
