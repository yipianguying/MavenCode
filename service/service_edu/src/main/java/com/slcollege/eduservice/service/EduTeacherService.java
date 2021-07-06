package com.slcollege.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-15
 */
public interface EduTeacherService extends IService<EduTeacher> {

    // 1.分页查询教师的方法,page-分页,limit-记录数
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
