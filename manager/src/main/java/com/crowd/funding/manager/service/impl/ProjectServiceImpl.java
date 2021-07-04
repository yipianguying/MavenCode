package com.crowd.funding.manager.service.impl;

import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.Project;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.dao.AdvertMapper;
import com.crowd.funding.manager.dao.ProjectMapper;
import com.crowd.funding.manager.service.ProjectService;
import com.crowd.funding.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Project queryProject(Map<String, Object> projectMap) {
        return projectMapper.queryProject(projectMap);
    }

    @Override
    public PageUtils<Project> pageQuery(Map<String, Object> paramMap) {
        PageUtils<Project> projectPage = new PageUtils<Project>((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
        paramMap.put("startIndex", projectPage.getStartIndex());
        List<Project> projectList= projectMapper.pageQuery(paramMap);
        // 获取数据的总条数
        int count = projectMapper.queryCount(paramMap);

        projectPage.setData(projectList);
        projectPage.setTotalSize(count);
        return projectPage;
    }

    @Override
    public int queryCount(Map<String, Object> projectMap) {
        return projectMapper.queryCount(projectMap);
    }
    // 插入项目数据的方法
    @Override
    public int insertProject(Project project) {
        return projectMapper.insertProject(project);
    }

    @Override
    public Project queryById(Integer id) {
        return projectMapper.queryById(id);
    }

    @Override
    public int updateProject(Project project) {
        return projectMapper.updateProject(project);
    }

    @Override
    public int deleteProject(Integer id) {
        return projectMapper.deleteProject(id);
    }

    @Override
    public int deleteProjects(Data ds) {
        return projectMapper.deleteProjects(ds);
    }
}
