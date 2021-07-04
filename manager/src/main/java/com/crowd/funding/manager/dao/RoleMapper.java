package com.crowd.funding.manager.dao;

import java.util.List;
import java.util.Map;


import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.RolePermission;
import com.crowd.funding.bean.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
public interface RoleMapper {

	List<Role> pageQuery(Map<String, Object> paramMap);

	int queryCount(Map<String, Object> paramMap);

	void insert(Role user);

	Role getRole(Integer id);

	int update(Role role);

	int delete(Integer uid); 

	int batchDelete(@Param("ids") Integer[] uid);

	int batchDeleteObj(Data datas);

	List<Role> queryAllRole();

	List<Integer> queryRoleidByUserid(Integer id);

	/*void saveUserRole(@Param("userid") Integer userid, @Param("roleids") Integer[] ids);

	void deleteUserRole(@Param("userid") Integer userid,@Param("roleids")  Integer[] ids);*/
	void saveUserRole(@Param("userid") Integer userid, @Param("roleids") List<Integer> ids);
	
	void deleteUserRole(@Param("userid") Integer userid, @Param("roleids") List<Integer> ids);
	// 插入新的许可分配
	int insertRolePermission(RolePermission rp);
	// 删除已有的许可分配
	void deleteRolePermissionRelationship(Integer roleid);

}
