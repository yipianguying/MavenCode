package com.crowd.funding.manager.dao;



import com.crowd.funding.bean.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> queryAllPermission();

    int updateByPrimaryKey(Permission record);

	Permission getRootPermission();

	List<Permission> getChildrenPermissionByPid(Integer id);

    // 根据角色id查询该角色分配前的许可
    List<Integer> queryPermissionIdsByRoleId(Integer roleId);
}