package com.crowd.funding.manager.dao;


import com.crowd.funding.bean.Project;
import com.crowd.funding.bean.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AuthProjectMapper {
    Project queryProject(Map<String, Object> projectMap);

    List<Project> pageQuery(Map<String, Object> projectMap);

    int queryCount(Map<String, Object> projectMap);
    // 插入项目数据的方法
    int insertProject(Project Project);
    // 根据项目id查询项目信息的方法
    Project queryById(Integer id);

    int updateProject(Project Project);

    int deleteProject(Integer id);

    int deleteProjects(Data ds);
    // 修改状态的方法
    int updateStatus(@Param(value = "id") Integer id, @Param(value = "status") String status);

}