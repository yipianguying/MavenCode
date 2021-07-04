package com.crowd.funding.manager.service.impl;

import java.util.List;
import java.util.Map;


import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.RolePermission;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.dao.RoleMapper;
import com.crowd.funding.manager.service.RoleService;
import com.crowd.funding.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleDao;


	public PageUtils<Role> pageQuery(Map<String, Object> paramMap) {
		PageUtils<Role> rolePage = new PageUtils<Role>((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
		
		paramMap.put("startIndex", rolePage.getStartIndex());
		
		List<Role> roles = roleDao.pageQuery(paramMap);
		
		// 获取数据的总条数
		int count = roleDao.queryCount(paramMap);
		rolePage.setData(roles);
		rolePage.setTotalSize(count);
		return rolePage;
	}

	public int queryCount(Map<String, Object> paramMap) {
		return roleDao.queryCount(paramMap);
	}

	public void saveRole(Role user) {
		roleDao.insert(user);
	}

	public Role getRole(Integer id) {
		return roleDao.getRole(id);
	}

	public int updateRole(Role user) {
		return roleDao.update(user);
	}

	public int deleteRole(Integer uid) {
		return roleDao.delete(uid);
	}

	public int batchDeleteRole(Integer[] uid) {
		return roleDao.batchDelete(uid);
	}

	public int batchDeleteRole(Data datas) {
		return roleDao.batchDeleteObj(datas);
	}

	public List<Role> queryAllRole() {
		return roleDao.queryAllRole();
	}

	@Override
	public List<Integer> queryRoleidByUserid(Integer id) {
		return roleDao.queryRoleidByUserid(id);
	}

/*	@Override
	public void doAssignRoleByUserid(Integer userid, Integer[] ids) {
		roleDao.saveUserRole(userid,ids);
	}

	@Override
	public void doUnAssignRoleByUserid(Integer userid, Integer[] ids) {
		roleDao.deleteUserRole(userid,ids);
	}*/

	@Override
	public void doAssignRoleByUserid(Integer userid, List<Integer> ids) {
		roleDao.saveUserRole(userid,ids);
	}

	@Override
	public void doUnAssignRoleByUserid(Integer userid, List<Integer> ids) {
		roleDao.deleteUserRole(userid,ids);
	}

	// 分配许可的方法
	@Override
	public int saveRolePermissionRelationship(Integer roleid, Data datas) {
		// 删除已有的许可分配
		roleDao.deleteRolePermissionRelationship(roleid);
		
		int totalCount = 0 ;
		List<Integer> ids = datas.getIds();
		for (Integer permissionid : ids) {
			RolePermission rp = new RolePermission();
			rp.setRoleid(roleid);
			rp.setPermissionid(permissionid);
			// 插入新的许可分配
			int count = roleDao.insertRolePermission(rp);
			totalCount += count ;
		}
		
		return totalCount;
	}

}
