package com.crowd.funding.manager.controller;

import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.Project;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.service.ProjectService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.ConstUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/project")
public class ProjectController {
    // 注入ProjectService，调用其中方法
    @Autowired
    private ProjectService projectService;
    // 跳转到项目管理页面的方法
    @RequestMapping("/index")
    public String index() {
        return "project/index";
    }

    //  跳转到增加项目页面的方法
    @RequestMapping("/add")
    public String add() {
        return "project/add";
    }
    // 跳转到修改项目页面的方法
    @RequestMapping("/edit")
    public String edit( Integer id, Model model ) {

        // 根据主键查询资质信息
        Project project = projectService.queryById(id);
        model.addAttribute("project", project);

        return "project/edit";
    }

    // 分页查询项目数据
    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(String pagetext, Integer pageno, Integer pagesize) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            // 查询项目数据
            Map<String, Object> projectMap = new HashMap<String, Object>();
            projectMap.put("pageno", pageno);
            projectMap.put("pagesize", pagesize);
            if ( StringUtils.isNotEmpty(pagetext) ) {
                pagetext = pagetext.replaceAll("%", "\\\\%");
            }
            projectMap.put("pagetext", pagetext);

            // 分页查询
            PageUtils<Project> page = projectService.pageQuery(projectMap);
            result.setPages(page);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }
    // 增加项目数据的方法
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(HttpServletRequest request, Project project , HttpSession session) {
        AjaxResultUtils result = new AjaxResultUtils();


        try {
            project.setStatus("0");
            int count = projectService.insertProject(project);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }
    // 修改项目数据的方法
    @ResponseBody
    @RequestMapping("/update")
    public Object update( Project project ) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            int count = projectService.updateProject(project);
            if ( count == 1 ) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }
    // 删除一条项目数据的方法
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(Integer id) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            int count = projectService.deleteProject(id);
            if ( count == 1 ) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    // 批量删除项目的数据
    @ResponseBody
    @RequestMapping("/batchDelete")
    public Object batchDelete( Data ds ) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            int count = projectService.deleteProjects(ds);
            if ( count == ds.getDatas().size() ) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }


}
