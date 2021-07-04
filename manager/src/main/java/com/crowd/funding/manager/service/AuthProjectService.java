package com.crowd.funding.manager.service;

import com.crowd.funding.bean.Project;
import com.crowd.funding.utils.PageUtils;

import java.util.Map;

public interface AuthProjectService {

    // 分页查询的方法
    public PageUtils<Project> pageQuery(Map<String, Object> projectMap);
    // 修改状态的方法
    int updateStatus(Integer id,String status);

    // 根据广告id查询项目
    Project queryById(Integer id);

}
