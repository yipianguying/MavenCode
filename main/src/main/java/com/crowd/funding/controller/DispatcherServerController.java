package com.crowd.funding.controller;


import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.Permission;
import com.crowd.funding.bean.User;
import com.crowd.funding.manager.service.UserService;

import com.crowd.funding.potal.service.MemberService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.ConstUtils;
import com.crowd.funding.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/*
*  既可以控制前台用户系统又可以控制后端服务系统,是一个公共的控制器类
* */
@Controller
public class DispatcherServerController {
    // 管理
    @Autowired
    private UserService userService;

    // 会员
    @Autowired
    private MemberService memberService;

    // 跳转到主页面的方法
    @RequestMapping("/index")
    public String index() {

        return "index";
    }
    // 从首页跳转到登录页面的方法
    @RequestMapping("/loginUser")
    public String loginUser(HttpSession session, HttpServletRequest request) {
        //判断是否需要自动登录
        //如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证

        boolean needLogin = true;
        String logintype = null ;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){ //如果客户端禁用了Cookie，那么无法获取Cookie信息

            for (Cookie cookie : cookies) {
                if("logincode".equals(cookie.getName())){
                    String loginCode = cookie.getValue();
                    System.out.println("获取到Cookie中的键值对"+cookie.getName()+"===== " + loginCode);
                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                    // 分解字符串
                    String[] split = loginCode.split("&");
                    if(split.length == 3){
                        // 得到Cookie中的登录信息
                        String loginacct = split[0].split("=")[1];
                        String userpwd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];
                        // 将Cookie中的登录信息放在Map集合中
                        Map<String,Object> paramMap = new HashMap<String,Object>();
                        paramMap.put("loginacct", loginacct);
                        paramMap.put("userpswd", userpwd);
                        paramMap.put("type", logintype);

                        if("user".equals(logintype)){

                            User dbLogin = userService.queryLogin(paramMap);

                            if(dbLogin!=null){
                                session.setAttribute(ConstUtils.USER_LOGIN, dbLogin);
                                needLogin = false ;
                            }


                            //加载当前登录用户的所拥有的许可权限.

                            //User user = (User)session.getAttribute(Const.LOGIN_USER);

                            List<Permission> myPermissions = userService.queryPermissionByUserId(dbLogin.getId()); //当前用户所拥有的许可权限

                            Permission permissionRoot = null;

                            Map<Integer,Permission> map = new HashMap<Integer,Permission>();

                            Set<String> myUris = new HashSet<String>(); //用于拦截器拦截许可权限

                            for (Permission innerpermission : myPermissions) {
                                map.put(innerpermission.getId(), innerpermission);

                                myUris.add("/"+innerpermission.getUrl());
                            }

                            session.setAttribute(ConstUtils.MY_URI, myUris);


                            for (Permission permission : myPermissions) {
                                //通过子查找父
                                //子菜单
                                Permission child = permission ; //假设为子菜单
                                if(child.getPid() == null ){
                                    permissionRoot = permission;
                                }else{
                                    //父节点
                                    Permission parent = map.get(child.getPid());
                                    parent.getChildren().add(child);
                                }
                            }

                            session.setAttribute("permissionRoot", permissionRoot);

                        }else if("member".equals(logintype)){

                            Member dbLogin = memberService.queryMemberLogin(paramMap);

                            if(dbLogin!=null){
                                session.setAttribute(ConstUtils.MEMBER_LOGIN, dbLogin);
                                needLogin = false ;
                            }
                        }

                    }
                }
            }
        }

        if(needLogin){
            return "loginUser";
        }else{
            if("user".equals(logintype)){
                return "redirect:/admin.htm";
            }else if("member".equals(logintype)){
                return "redirect:/member.htm";
            }
        }
        return "loginUser";
    }
    // 跳转到注册页面的方法
    @RequestMapping("/registerUser")
    public String  registerUser() {
        return "registerUser";
    }

    // 跳转到会员主页面的方法
    @RequestMapping("/member")
    public String member(){
        return "member/member";
    }
    // 跳转到管理员页面的方法
    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }
    // 注销登录的方法
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session,HttpServletRequest request) {
        // 清空session域或销毁session对象,invalidate()方法为销毁session的方法
        session.invalidate();

        return "redirect:/index.jsp";

        /*        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {

            }
        }*/
/*        Cookie cookie = new Cookie("logincode",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);*/
    }


    // 登录的方法,loginacct账号、userpswd密码、type类型-异步请求的方式
    // ResponseBody结合Jackson组件,将返回结果转换为字符串,将JSON以流的形式返回给客户端
    @ResponseBody
    @RequestMapping("/login")
    public Object login(String loginacct, String userpswd, String type, String rememberMe, HttpServletResponse response, HttpSession session) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            // 获得登录表单中的信息,将信息放到map集合中
            Map<String, Object> loginMap = new HashMap<>();
            loginMap.put("loginacct",loginacct);// 账号
            loginMap.put("userpswd", MD5Utils.digest(userpswd));// 密码,使用MD5Utils工具类进行加密
            loginMap.put("type",type);// 类型


            // 根据类型判断用户/会员登录
            if("member".equals(type)) {
                // 查询会员登录的方法
                Member member = memberService.queryMemberLogin(loginMap);
                // 在session对象中设置值,用于登录
                session.setAttribute(ConstUtils.MEMBER_LOGIN,member);
                // 记住我,flag值为1。为0则无法记住
                if("1".equals(rememberMe)) {
                    String loginCode = "\"loginacct="+member.getLoginacct()+"&userpwd="+member.getUserpswd()+"&logintype=member\"";
                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=1
                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+loginCode);
                    Cookie c = new Cookie("logincode",loginCode);

                    c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }


            } else if ("user".equals(type)) {
                // 查询登录的信息,并放到user对象中
                User user = userService.queryLogin(loginMap);
                // 将user对象中的信息放到ConstUtils工具类的USER_LOGIN中,USER_LOGIN是一个常量
                session.setAttribute(ConstUtils.USER_LOGIN,user);
                // 记住我,flag值为1。为0则无法记住
                if("1".equals(rememberMe)) {
                    String loginCode = "\"loginacct="+user.getLoginacct()+"&userpwd="+user.getUserpswd()+"&logintype=user\"";
                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=1
                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+loginCode);
                    Cookie c = new Cookie("logincode",loginCode);

                    c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }

                // 加载当前登录用户所拥有的权限
                // User user = (User) session.getAttribute(ConstUtils.USER_LOGIN);

                // 查询登录用户的权限
                List<Permission> permissions = userService.queryPermissionByUserId(user.getId());

                Permission permissionsRoot = null;

                Map<Integer,Permission> map = new HashMap<Integer,Permission>();
                // 用于拦截器拦截许可权限
                Set<String> myUri = new HashSet<String>();

                for (Permission innerpermission : permissions) {
                    map.put(innerpermission.getId(), innerpermission);
                    // 得到许可树的url,并添加到myUri中
                    myUri.add("/"+innerpermission.getUrl());
                }

                // 将myUri设置到session中
                session.setAttribute(ConstUtils.MY_URI,myUri);

                for (Permission permission : permissions) {
                    //通过子查找父
                    //子菜单
                    Permission child = permission ; //假设为子菜单
                    if(child.getPid() == 0 ){
                        permissionsRoot = permission;
                    }else{
                        //父节点
                        Permission parent = map.get(child.getPid());
                        parent.getChildren().add(child);
                    }
                }

                session.setAttribute("permissionsRoot", permissionsRoot);
            } else {

            }
            // 返回类型(会员或者是管理)
            result.setData(type);

            // 登录成功返回true,如果登录成功,返回的格式为{"success":true}
            result.setSuccess(true);

        } catch (Exception e) {
            //返回消息-登录失败
            result.setMessage("登录失败");
            e.printStackTrace();
            // 如果登录失败,返回的格式为{"success":false,"message":"登录失败"}
            result.setSuccess(false);
        }

        // 登录成功,重定向至main管理员页面,重定向防止重复登录
        return result;
    }

    // 注册的方法
    @ResponseBody
    @RequestMapping("/register")
    public Object register(String loginacct,String userpswd,String email,String type,HttpSession session) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            // 设置一个标志
            boolean flag = false;
            // 声明且实例化一个管理变量
            User user = new User();
            // 声明且实例化一个会员变量
            Member member = new Member();
            // 判断哪种类型的用户注册
            if(type.equals("user")) {
                // 管理注册的方法
                user.setLoginacct(loginacct);
                user.setUserpswd(MD5Utils.digest(userpswd));
                user.setEmail(email);
                user.setUsername(loginacct);
                flag = userService.insertUser(user);
            } else if(type.equals("member")) {
                // 会员(个人)注册的方法
                member.setLoginacct(loginacct);
                member.setUserpswd(MD5Utils.digest(userpswd));
                member.setEmail(email);
                member.setUsername(loginacct);
                member.setUsertype(ConstUtils.MEMBER_USER_TYPE);
                flag = memberService.insertMember(member);
            }  else if(type.equals("enterprise")) {
                // 会员(企业)注册的方法
                member.setLoginacct(loginacct);
                member.setUserpswd(MD5Utils.digest(userpswd));
                member.setEmail(email);
                member.setUsername(loginacct);
                member.setUsertype(ConstUtils.MEMBER_USER_TYPE_ENTERPRISE);
                flag = memberService.insertMember(member);
            } else {
                result.setSuccess(false);
            }

            result.setSuccess(flag);
            // {"success":true}
        } catch (Exception e) {
            result.setMessage("注册失败!");
            e.printStackTrace();
            result.setSuccess(false);
            // {"success":false,"message":"登录失败!"}
            //throw e ;
        }

        return result;
    }


    // 登录的方法,loginacct账号、userpswd密码、type类型-同步请求的方式
/*    @RequestMapping("/login")
    public String login(String loginacct, String userpswd, String type, HttpSession session) {
        // 获得登录表单中的信息,将信息放到map集合中
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("loginacct",loginacct);// 账号
        loginMap.put("userpswd",userpswd);// 密码
        loginMap.put("type",type);// 类型
        // 查询登录的信息,并放到user对象中
        User user = userService.queryLogin(loginMap);

        // 将user对象中的信息放到ConstUtils工具类的USER_LOGIN中,USER_LOGIN是一个常量
        session.setAttribute(ConstUtils.USER_LOGIN,user);

        // 登录成功,重定向至main管理员页面,重定向防止重复登录
        return "redirect:/admin.htm";
    }*/


}
