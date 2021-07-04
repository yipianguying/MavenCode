package com.crowd.funding.manager.service;



import com.crowd.funding.bean.Permission;

import java.util.List;



public interface PermissionService {

	Permission getRootPermission();

	List<Permission> getChildrenPermissionByPid(Integer id);

	List<Permission> queryAllPermission();
	// 保存子节点的数据的方法
    int addPermission(Permission permission);

	// 查询id的方法
	Permission getPermissionById(Integer id);

	// 修改子节点的方法
	int updatePermission(Permission permission);

	// 删除子节点的方法
	int deletePermission(Integer id);

	// 根据角色id查询该角色分配前的许可
	List<Integer> queryPermissionByRoleId(Integer roleId);
}
