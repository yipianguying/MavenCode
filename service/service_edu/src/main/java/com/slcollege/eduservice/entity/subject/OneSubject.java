package com.slcollege.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 课程分类:一级分类
@Data
public class OneSubject {
    // 定义字段
    private String id;

    private String title;

    // 一个一级分类有多个二级分类
    private List<TwoSubject> children = new ArrayList<>();

}
