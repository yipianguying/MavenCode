package com.slcollege.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.mapper.EduTeacherMapper;
import com.slcollege.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-15
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    // 1.分页查询教师的方法,page-分页,limit-记录数
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        // 把分页的数据封装到pageTeacher中
        baseMapper.selectPage(pageTeacher, wrapper);
        // 和分页功能相关的所有数据
        List<EduTeacher> records = pageTeacher.getRecords();
        long current = pageTeacher.getCurrent();
        long pages = pageTeacher.getPages();
        long size = pageTeacher.getSize();
        long total = pageTeacher.getTotal();
        // 下一页
        boolean hasNext = pageTeacher.hasNext();
        // 上一页
        boolean hasPrevious = pageTeacher.hasPrevious();

        // 把分页的数据获取,放到map集合中
        Map<String, Object> mapTeacher = new HashMap<>();
        mapTeacher.put("records",records);
        mapTeacher.put("current", current);
        mapTeacher.put("pages", pages);
        mapTeacher.put("size", size);
        mapTeacher.put("total", total);
        mapTeacher.put("hasNext", hasNext);
        mapTeacher.put("hasPrevious", hasPrevious);

        // 返回map集合
        return mapTeacher;
    }

    // 2.教师详情的功能

}
