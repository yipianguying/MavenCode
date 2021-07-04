package com.crowd.funding.manager.controller;


import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.service.UserService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 跳转到用户维护的方法
    @RequestMapping("toUserList")
    public String toUserList() {
        return "user/index";
    }


    // 跳转到添加用户页面的方法
    @RequestMapping("toUserAdd")
    public String toUserAdd() {
        return "user/add";
    }
    // 添加用户数据的方法
    @ResponseBody
    @RequestMapping("/userAdd")
    public Object userAdd(User user) {

        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用添加用户数据的方法
            int num = userService.saveUser(user);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==1);


        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("添加数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }
    // 删除用户数据的方法
    @ResponseBody
    @RequestMapping("/userDelete")
    public Object userDelete(Integer id) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用修改用户数据的方法
            int num = userService.deleteUser(id);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==1);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("删除数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回

        return resultUtils;
    }

    // 批量删除用户数据的方法-前端页面为Json格式
    @ResponseBody
    @RequestMapping("/userDeleteBatch")
    public Object doDeleteBatch(Data data){
        AjaxResultUtils result = new AjaxResultUtils();
        try {

            int count = userService.deleteBatchUserByVO(data);
            result.setSuccess(count==data.getDatas().size());
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败!");
        }

        return result;
    }

    // 批量删除用户数据的方法
/*    @ResponseBody
    @RequestMapping("/userDeleteBatch")
    public Object userDeleteBatch(Integer[] id) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用修改用户数据的方法
            int num = userService.userDeleteBatch(id);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==id.length);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("删除数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回

        return resultUtils;
    }*/

    // 修改用户数据时查询用户id的方法
    @RequestMapping("toUserUpdate")
    public String toUserUpdate(Integer id, Map map) {

        User user = userService.getUserById(id);
        map.put("user",user);

        return "user/update";
    }
    // 修改用户数据的方法
    @ResponseBody
    @RequestMapping("/userUpdate")
    public Object userUpdate(User user) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用修改用户数据的方法
            int num = userService.updateUser(user);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==1);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("修改数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }


    // 条件查询的方法
    @ResponseBody
    @RequestMapping("/userList")
    public Object userList(@RequestParam(value = "pageNumber",required = false,defaultValue = "1") Integer pageNumber,// 如果没有传递参数则设置默认参数
                           @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                           String queryText) {

        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 将分页的数据和查询框的数据放入到集合中
            Map map = new HashMap();
            map.put("pageNumber", pageNumber);
            map.put("pageSize", pageSize);


            // 判断传入的查询框的数据是否为空
            if(StringUtils.isNotEmpty(queryText)) {
                // 如果传递的值当中有%,则将%替换为\%
                if(queryText.contains("%")) {
                    queryText =  queryText.replaceAll("%","\\\\%");
                }

                // 将查询框的数据放入到map集合中
                map.put("queryText", queryText);
            }
            // 调用查询分页的方法
            PageUtils pages = userService.queryPages(map);

            // 如果成功则返回true值
            resultUtils.setSuccess(true);

            // 设置分页数
            resultUtils.setPages(pages);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("查询数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }

    // 跳转到用户分配角色的页面的方法
    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map map) {
        // 查询所有的角色
        List<Role> roleList = userService.queryRole();
        // 查询已经分配给用户的角色id
        List<Integer> roleId = userService.queryRoleByUserId(id);
        // 得到没有分配的角色
        List<Role> leftRoleList = new ArrayList<>();
        // 得到已经分配的角色
        List<Role> rightRoleList = new ArrayList<>();

        for (Role role : roleList) {
            // 已经分配给用户的角色id,包不包含当前的角色
            if(roleId.contains(role.getId())) {
                // 如果包含则说明已经分配,放入已经分配的角色的集合里面
                rightRoleList.add(role);
            } else {
                // 如果不包含则没有分配,放入为分配角色的集合里面
                leftRoleList.add(role);
            }
        }
        // 集合中放入没有分配的角色和已经分配的角色
        map.put("leftRoleList",leftRoleList);
        map.put("rightRoleList",rightRoleList);

        return "user/assignrole";
    }

    // 给用户分配角色的方法
    @ResponseBody
    @RequestMapping("/userAssignRole")
    public Object userAssignRole(Integer userId, Data data) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用给用户分配角色的方法
            userService.saveUserRoleRelationship(userId, data);

            // 如果成功则返回true值
            resultUtils.setSuccess(true);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("分配角色失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }

    // 取消分配角色的方法
    @ResponseBody
    @RequestMapping("/unUserAssignRole")
    public Object unUserAssignRole(Integer userId, Data data) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {
            // 调用取消分配角色的方法
            userService.deleteUserRoleRelationship(userId, data);

            // 如果成功则返回true值
            resultUtils.setSuccess(true);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("取消分配角色失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }


    // 同步请求开发方式
/*    @RequestMapping("/userList")
    public String userList(@RequestParam(value = "pageNumber",required = false,defaultValue = "1") Integer pageNumber,// 如果没有传递参数则设置默认参数
                           @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                           Map map) {

        PageUtils pages =  userService.queryPages(pageNumber, pageSize);

        map.put("page",pages);

        return "user/index";
    }*/

}
