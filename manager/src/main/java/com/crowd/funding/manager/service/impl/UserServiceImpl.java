package com.crowd.funding.manager.service.impl;

import com.crowd.funding.bean.Permission;
import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.exception.UserLoginException;

import com.crowd.funding.manager.dao.UserMapper;
import com.crowd.funding.manager.service.UserService;
import com.crowd.funding.utils.ConstUtils;
import com.crowd.funding.utils.MD5Utils;
import com.crowd.funding.utils.PageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    // 查询用户登录的方法
    @Override
    public User queryLogin(Map<String, Object> loginMap) {
        User user = userMapper.queryLogin(loginMap);

        if(loginMap.get("loginacct").equals("")) {
            // 登录失败,抛出异常
            throw new UserLoginException("用户账号不能为空!");
        }
        if(loginMap.get("userpswd").equals("")) {
            // 登录失败,抛出异常
            throw new UserLoginException("用户密码不能为空!");
        }
        if(user == null) {
            // 登录失败,抛出异常
            throw new UserLoginException("用户账号或密码不正确!");
        }

        return user;
    }

/*    @Override
    public PageUtils queryPages(Integer pageNumber, Integer pageSize) {
        // 创建PageUtils对象
        PageUtils pages = new PageUtils(pageNumber,pageSize);

        // 设置开始索引
        Integer startIndex = pages.getStartIndex();

        // 获取当前页的数据
        List<User> datas =  userMapper.queryList(startIndex,pageSize);

        // 设置当前页的数据
        pages.setData(datas);

        // 查询并设置设置总记录数
        Integer totalSize = userMapper.queryCount();

        // 设置总记录数
        pages.setTotalSize(totalSize);

        return pages;
    }*/
    // 保存user对象的方法
    @Override
    public int saveUser(User user) {
        // 格式化时间的类
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间类
        Date date = new Date();
        // 创建时间-为当前时间
        String createTime = simpleDateFormat.format(date);
        // 设置当前时间
        user.setCreatetime(createTime);
        //设置密码
        user.setUserpswd(MD5Utils.digest(ConstUtils.PASSWORD));

        return userMapper.insert(user);
    }

    // 分页查询的方法
    @Override
    public PageUtils queryPages(Map<String, Object> map) {
        // 创建PageUtils对象
        PageUtils pages = new PageUtils((Integer) map.get("pageNumber"),(Integer) map.get("pageSize"));

        // 设置开始索引
        Integer startIndex = pages.getStartIndex();
        map.put("startIndex", startIndex);

        // 获取当前页的数据
        List<User> datas =  userMapper.queryList(map);

        // 设置当前页的数据
        pages.setData(datas);

        // 查询并设置设置总记录数
        Integer totalSize = userMapper.queryCount(map);

        // 设置总记录数
        pages.setTotalSize(totalSize);

        return pages;
    }

    // 修改用户数据时获取用户id的方法
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
    // 修改用户数据的方法
    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    // 删除用户数据的方法
    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    // 批量删除用户数据的方法
    @Override
    public int userDeleteBatch(Integer[] ids) {
        // 定义一个用于计数的变量,计算总共要删除几条id
        int userIdCount = 0;
        // 将数组中的id全部取出来
        for (Integer userId : ids) {
            int count = userMapper.deleteByPrimaryKey(userId);
            // 将需要删除的id数目加起来
            userIdCount += count;
        }
        // 如果加起来的需要删除的id数与传过来的id数目不一致,则抛出异常
        if(userIdCount != ids.length) {
            throw new RuntimeException("批量删除失败");
        }

        return userIdCount;
    }

    // 批量删除用户数据的方法-前端页面为JSON格式
    @Override
    public int deleteBatchUserByVO(Data data) {
        return userMapper.deleteBatchUserByVO(data.getDatas());
    }

    // 得到已经分配给用户的角色id
    @Override
    public List<Integer> queryRoleByUserId(Integer id) {
        return userMapper.queryRoleByUserId(id);

    }

    // 查询所有的角色
    @Override
    public List<Role> queryRole() {
        return userMapper.queryRole();
    }

    // 调用给用户分配角色的方法
    @Override
    public int saveUserRoleRelationship(Integer userId, Data data) {
        return userMapper.saveUserRoleRelationship(userId, data);
    }

    // 调用取消分配角色的方法
    @Override
    public int deleteUserRoleRelationship(Integer userId, Data data) {
        return userMapper.deleteUserRoleRelationship(userId, data);
    }
    // 查询登录用户的权限
    @Override
    public List<Permission> queryPermissionByUserId(Integer id) {
        return userMapper.queryPermissionByUserId(id);
    }
    // 用户注册的方法
    @Override
    public boolean insertUser(User user) {
        // 格式化时间的类
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间类
        Date date = new Date();
        // 创建时间-为当前时间
        String createTime = simpleDateFormat.format(date);
        // 设置当前时间
        user.setCreatetime(createTime);

        if(user.getLoginacct().equals("")) {
            // 注册失败,抛出异常
            throw new UserLoginException("用户账号不能为空!");
        }
        if(user.getUserpswd().equals("")) {
            // 注册失败,抛出异常
            throw new UserLoginException("用户密码不能为空!");
        }
        if(user.getEmail().equals("")) {
            // 注册失败,抛出异常
            throw new UserLoginException("用户邮箱不能为空!");
        }

        return userMapper.insertUser(user);
    }

/*    // 批量删除用户数据的方法-前端页面为JSON格式
    @Override
    public int deleteBatchUserByVO(Data data) {
        return userMapper.deleteBatchUserByVO(data);
    }*/




}
