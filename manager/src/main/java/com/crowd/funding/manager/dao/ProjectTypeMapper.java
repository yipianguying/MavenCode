package com.crowd.funding.manager.dao;




import com.crowd.funding.bean.ProjectType;

import java.util.List;

public interface ProjectTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectType record);

    ProjectType selectByPrimaryKey(Integer id);

    List<ProjectType> selectAll();

    int updateByPrimaryKey(ProjectType record);
}