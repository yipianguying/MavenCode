package com.crowd.funding.manager.dao;




import com.crowd.funding.bean.Permission;
import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {
    // 删除用户数据的方法
    int deleteByPrimaryKey(Integer id);
    // 添加用户数据的方法
    int insert(User record);
    // 修改用户数据时,查询用户id的方法
    User selectByPrimaryKey(Integer id);
    // 查询所有用户的方法
    List<User> selectAll();
    // 修改用户数据的方法
    int updateByPrimaryKey(User record);
    // 查询登录的方法
	User queryLogin(Map<String, Object> paramMap);

    /*
    Integer queryCount();

    List<User> queryList(@Param("startIndex") Integer startIndex,
                         @Param("pageSize") Integer pageSize);
    */
    // 获取当前页的数据的方法
    List<User> queryList(Map<String, Object> map);

    // 获取总记录数的方法
    Integer queryCount(Map<String, Object> map);

    // 批量删除用户数据的方法-前端页面为JSON格式
    int deleteBatchUserByVO(@Param("userList") List<User> datas);

    // 得到已经分配给用户的角色id
    List<Integer> queryRoleByUserId(Integer id);

    // 查询所有的角色
    List<Role> queryRole();

    // 调用给用户分配角色的方法
    int saveUserRoleRelationship(@Param("userId") Integer userId, @Param("data") Data data);

    // 调用取消分配角色的方法
    int deleteUserRoleRelationship(@Param("userId")  Integer userId, @Param("data") Data data);

    // 查询登录用户的权限
    List<Permission> queryPermissionByUserId(Integer id);
    // 用户注册的方法
    boolean insertUser(User user);

    // 批量删除用户数据的方法-前端页面为JSON格式
    // int deleteBatchUserByVO(Data data);


}