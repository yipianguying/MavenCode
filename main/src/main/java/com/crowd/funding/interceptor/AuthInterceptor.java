package com.crowd.funding.interceptor;


import com.crowd.funding.manager.service.PermissionService;
import com.crowd.funding.utils.ConstUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
*  登录之后的权限拦截器,未分配权限的用户不能看到不属于自己权限的模块
* */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.查询所有许可
/*        List<Permission> permissions= permissionService.queryAllPermission();

        Set<String> allURI = new HashSet<String>();

        for(Permission permission : permissions) {
            allURI.add("/"+permission.getUrl());
        }*/

        // 改进效率:在服务器启动时加载所有许可路径,并存放到application域中
        Set<String> allURI = (Set<String>) request.getSession().getServletContext().getAttribute(ConstUtils.ALL_PERMISSION_URI);

        // 2.判断请求路径是否在所有许可的范围内
        String servletPath = request.getServletPath();
        if(allURI.contains(servletPath)) {
            // 3.判断请求路径是否在用户所拥有的权限内
            Set<String> myURI = (Set<String>) request.getSession().getAttribute(ConstUtils.MY_URI);
            // 如果包含则返回true
            if(myURI.contains(servletPath)) {
                return true;
            } else {
                // 不包含则返回false,并且返回到登录界面
                response.sendRedirect(request.getContextPath()+"/loginUser.htm");
                return  false;
            }

        } else { // 不在拦截范围内,放行
            return true;
        }

    }
}
