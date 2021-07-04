package com.crowd.funding.manager.service.impl;

import java.util.List;


import com.crowd.funding.bean.Permission;
import com.crowd.funding.manager.dao.PermissionMapper;
import com.crowd.funding.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public Permission getRootPermission() {
		return permissionMapper.getRootPermission();
	}

	@Override
	public List<Permission> getChildrenPermissionByPid(Integer id) {
		return permissionMapper.getChildrenPermissionByPid(id);
	}

	@Override
	public List<Permission> queryAllPermission() {
		return permissionMapper.queryAllPermission();
	}

	// 保存子节点的数据的方法
	@Override
	public int addPermission(Permission permission) {
		return permissionMapper.insert(permission);
	}
	// 查询id的方法
	@Override
	public Permission getPermissionById(Integer id) {
		return permissionMapper.selectByPrimaryKey(id);
	}

	// 修改子节点的方法
	@Override
	public int updatePermission(Permission permission) {
		return permissionMapper.updateByPrimaryKey(permission);
	}

	// 删除子节点的方法
	@Override
	public int deletePermission(Integer id) {
		return permissionMapper.deleteByPrimaryKey(id);
	}

	// 根据角色id查询该角色分配前的许可
	@Override
	public List<Integer> queryPermissionByRoleId(Integer roleId) {
		return permissionMapper.queryPermissionIdsByRoleId(roleId);
	}

}
