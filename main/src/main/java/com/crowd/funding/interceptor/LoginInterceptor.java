package com.crowd.funding.interceptor;

import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.User;
import com.crowd.funding.utils.ConstUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/*
*  登录拦截器
* */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    // 拦截的方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1.定义哪些路径不需要拦截(将这些路径称为白名单)
        Set<String> uri = new HashSet<>();
        uri.add("/register.do");   //注册功能
        uri.add("/registerUser.htm");  //注册页面
        uri.add("/login.do");    // 登录功能
        uri.add("/loginUser.htm");  // 登录页面
        uri.add("/loginOut.do");    // 注销登录功能
        uri.add("/index.htm");   // 主页面

        // 获取请求路径
        String servletPath = request.getServletPath();

        if(uri.contains(servletPath)) {
            return true;
        }

        // 2.判断用户是否登录,如果登录就放行
        HttpSession session = request.getSession();
        // 判断管理
        User user = (User)session.getAttribute(ConstUtils.USER_LOGIN);
        // 判断会员
        Member member = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);

        if(user!=null || member!=null) {
            return true;
        } else {
            response.sendRedirect(request.getContextPath()+"/loginUser.htm");

            return false;
        }


    }
}
