package com.crowd.funding.manager.dao;


import com.crowd.funding.bean.Project;
import com.crowd.funding.bean.vo.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ProjectMapper {
    Project queryProject(Map<String, Object> projectMap);

    List<Project> pageQuery(Map<String, Object> projectMap);

    int queryCount(Map<String, Object> projectMap);
    // 插入项目数据的方法
    int insertProject(Project Project);

    Project queryById(Integer id);

    int updateProject(Project Project);

    int deleteProject(Integer id);

    int deleteProjects(Data ds);
}