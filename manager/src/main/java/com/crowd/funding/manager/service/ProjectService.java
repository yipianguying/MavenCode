package com.crowd.funding.manager.service;

import com.crowd.funding.bean.Project;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.utils.PageUtils;

import java.util.Map;

public interface ProjectService {
    // 查询项目数据的方法
    public Project queryProject(Map<String, Object> projectMap);
    // 分页查询项目的方法
    public PageUtils<Project> pageQuery(Map<String, Object> projectMap);
    // 查询项目数量的方法
    public int queryCount(Map<String, Object> projectMap);
    // 插入项目数据的方法
    public int insertProject(Project project);
    // 查询项目id的方法
    public Project queryById(Integer id);
    // 修改项目的方法
    public int updateProject(Project project);
    // 删除项目数据的方法
    public int deleteProject(Integer id);
    // 批量删除项目数据的方法
    public int deleteProjects(Data ds);
}
