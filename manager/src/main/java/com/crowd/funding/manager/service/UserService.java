package com.crowd.funding.manager.service;



import com.crowd.funding.bean.Permission;
import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.utils.PageUtils;


import java.util.List;
import java.util.Map;

public interface UserService {
    User queryLogin(Map<String, Object> loginMap);

    // @Deprecated // 方法已经过时的注解
    // PageUtils queryPages(Integer pageNumber, Integer pageSize);
    // 保存用户数据的方法
    int saveUser(User user);
    // 分页查询的方法
    PageUtils queryPages(Map<String,Object> map);
    // 修改用户数据时查询用户id的方法
    User getUserById(Integer id);
    // 修改用户数据的方法
    int updateUser(User user);
    // 删除用户数据的方法
    int deleteUser(Integer id);
    // 批量删除用户数据的方法
    int userDeleteBatch(Integer[] ids);
    // 批量删除用户数据的方法-前端页面为JSON格式
    int deleteBatchUserByVO(Data data);
    // 得到已经分配给用户的角色id
    List<Integer> queryRoleByUserId(Integer id);
    // 查询所有的角色
    List<Role> queryRole();
    // 调用给用户分配角色的方法
    int saveUserRoleRelationship(Integer userId, Data data);
    // 调用取消分配角色的方法
    int deleteUserRoleRelationship(Integer userId, Data data);
    // 查询登录用户的权限
    List<Permission> queryPermissionByUserId(Integer id);
    // 管理注册的方法
    boolean insertUser(User user);
}
