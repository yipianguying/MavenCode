package com.crowd.funding.listener;


import com.crowd.funding.bean.Permission;
import com.crowd.funding.manager.service.PermissionService;
import com.crowd.funding.utils.ConstUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
*  配置web.xml文件中的监听器
* */
public class StarSystemListener implements ServletContextListener {
    // 在服务器启动时,创建application对象时需要执行的方法
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 1.将项目的上下文路径（request.getContextPath()）放置到application域中
        // 得到application域
        ServletContext application = servletContextEvent.getServletContext();
        // 从application域中得到上下文路径
        String contextPath = application.getContextPath();
        // 将得到的上下文路径放置到jsp页面的参数里面
        application.setAttribute("APP_PATH",contextPath);

        // System.out.println("APP_PATH........");

        // 2.加载所有许可路径,存放到application域中
        // 得到ioc容器对象
        ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        // 从ioc容器中获取PermissionService类
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        // 查询出所有的许可树数据
        List<Permission> permissions= permissionService.queryAllPermission();

        Set<String> allURI = new HashSet<String>();

        for(Permission permission : permissions) {
            allURI.add("/"+permission.getUrl());
        }

        // 将上下文路径和得到的allURI放到application对象中
        application.setAttribute(ConstUtils.ALL_PERMISSION_URI,allURI);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
