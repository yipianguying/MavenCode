package com.crowd.funding.manager.service.impl;


import com.crowd.funding.bean.Project;
import com.crowd.funding.manager.dao.AuthProjectMapper;
import com.crowd.funding.manager.service.AuthProjectService;
import com.crowd.funding.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthProjectServiceImpl implements AuthProjectService {
    // 注入AuthProjectMapper
    @Autowired
    private AuthProjectMapper authProjectMapper;

    // 分页查询的方法
    @Override
    public PageUtils<Project> pageQuery(Map<String, Object> projectMap) {
        PageUtils<Project> projectPage = new PageUtils<Project>((Integer)projectMap.get("pageno"),(Integer)projectMap.get("pagesize"));
        projectMap.put("startIndex", projectPage.getStartIndex());
        List<Project> projectList= authProjectMapper.pageQuery(projectMap);
        // 获取数据的总条数
        int count = authProjectMapper.queryCount(projectMap);

        projectPage.setData(projectList);
        projectPage.setTotalSize(count);
        return projectPage;
    }
    // 修改状态的方法
    @Override
    public int updateStatus(Integer id, String status) {
        return authProjectMapper.updateStatus(id,status);
    }
    // 根据项目id查询项目的方法
    @Override
    public Project queryById(Integer id) {
        return authProjectMapper.queryById(id);
    }
}
