package com.crowd.funding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowd.funding.bean.Permission;
import com.crowd.funding.controller.BaseController;

import com.crowd.funding.manager.service.PermissionService;
import com.crowd.funding.utils.AjaxResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	// 跳转到许可维护页面的方法
	@RequestMapping("/permitIndex")
	public String permitIndex(){
		return "permission/index";
	}

	// 跳转到添加子节点的页面的方法
	@RequestMapping("/permissionAdd")
	public String permissionAdd() {

		return "permission/add";
	}

	// 跳转到修改子节点的页面的方法
	@RequestMapping("/permissionUpdate")
	public String permissionUpdate(Integer id, Map map) {
		// 查询id的方法
		Permission permission =  permissionService.getPermissionById(id);
		map.put("permission",permission);

		return "permission/update";
	}

	// 修改子节点的方法
	@ResponseBody
	@RequestMapping("/doPermissionUpdate")
	public Object doPermissionUpdate(Permission permission){
		AjaxResultUtils result = new AjaxResultUtils();

		try {
			// 调用保存子节点的数据的方法
			int num = permissionService.updatePermission(permission);

			result.setSuccess(num == 1);

		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("修改许可树子节点数据失败!");
		}

		return result ;
	}

	// 删除子节点的方法
	@ResponseBody
	@RequestMapping("/doPermissionDelete")
	public Object doPermissionDelete(Integer id) {
		// 调用BaseController类中的start()方法
		start();

		try {
			// 调用保存子节点的数据的方法
			int num = permissionService.deletePermission(id);
			// 调用BaseController类中的success()方法
			success(num==1);
		} catch (Exception e) {
			success(false);
			e.printStackTrace();
			error("删除许可树子节点数据失败!");
		}

		return end();
	}
/*	// 删除子节点的方法
	@ResponseBody
	@RequestMapping("/doPermissionDelete")
	public Object doPermissionDelete(Integer id){
		AjaxResultUtils result = new AjaxResultUtils();

		try {
			// 调用保存子节点的数据的方法
			int num = permissionService.deletePermission(id);

			result.setSuccess(num == 1);

		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("删除许可树子节点数据失败!");
		}

		return result ;
	}*/
	// 添加子节点的方法
	@ResponseBody
	@RequestMapping("/doPermissionAdd")
	public Object doPermissionAdd(Permission permission){
		AjaxResultUtils result = new AjaxResultUtils();

		try {
			// 调用保存子节点的数据的方法
			int num = permissionService.addPermission(permission);

			result.setSuccess(num == 1);

		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("添加许可树子节点数据失败!");
		}

		return result ;
	}

	// 用Map集合来查找父,来组合父子关系.减少循环的次数 ,提高性能.
	@ResponseBody
	@RequestMapping("/loadTreeData")
	public Object loadTreeData(){
		AjaxResultUtils result = new AjaxResultUtils();

		try {

			List<Permission> root = new ArrayList<Permission>();


			List<Permission> childredPermissons =  permissionService.queryAllPermission();


			Map<Integer,Permission> map = new HashMap<Integer,Permission>();//100

			for (Permission innerpermission : childredPermissons) {
				map.put(innerpermission.getId(), innerpermission);
			}


			for (Permission permission : childredPermissons) { //100
				//通过子查找父
				//子菜单
				Permission child = permission ; //假设为子菜单
				if(child.getPid() == 0 ){
					root.add(permission);
				}else{
					//父节点
					Permission parent = map.get(child.getPid());
					parent.getChildren().add(child);
				}
			}

			result.setSuccess(true);
			result.setData(root);

		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
			result.setMessage("加载许可树数据失败!");
		}


		return result ;
	}

	/**
	 * 递归使用条件:
	 * 	1.调用自身方法
	 *  2.不断调用自身方法时,操作范围一定要缩小.
	 *  3.一定要存在跳出条件.
	 * @param permission
	 */
	// 使用递归的方法查询出所有的子菜单
	private void queryChildPermissions(Permission permission){
		List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());
		//组合父子关系
		permission.setChildren(children);

		for (Permission innerChildren : children) {
			queryChildPermissions(innerChildren);
		}
	}



	//Demo4 - 采用一次性加载所有permission数据;减少与数据的交互次数.
/*	@ResponseBody
	@RequestMapping("/loadTreeData")
	public Object loadTreeData(){
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			//Demo 4 一次查询所有数据
			List<Permission> root = new ArrayList<Permission>();

			
			List<Permission> childredPermissons =  permissionService.queryAllPermission();
			for (Permission permission : childredPermissons) {
				//通过子查找父
				//子菜单
				Permission child = permission ; //假设为子菜单
				if(child.getPid() == null ){
					root.add(permission);
				}else{
					//父节点
					for (Permission innerpermission : childredPermissons) {
						if(child.getPid() == innerpermission.getId()){
							Permission parent = innerpermission;							
							parent.getChildren().add(child);
							break ; //跳出内层循环,如果跳出外层循环,需要使用标签跳出
						}
					}
				}
			}

			
			
			result.setSuccess(true);
			result.setData(root);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		

		return result ;
	}*/

	//Demo3 - 采用递归调用来解决,许可树多个层次的问题.
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();
			
			//父
			Permission permission = permissionService.getRootPermission();
			
			root.add(permission);
			
			
			queryChildPermissions(permission);

			result.setSuccess(true);
			result.setData(root);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		

		return result ;
	}*/
	

	
	
	
	//Demo2-从数据表t_permission查询数据,显示许可树.
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();
			
			//父
			Permission permission = permissionService.getRootPermission();
			
			root.add(permission);
			
			//子
			List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());
			
			
			//设置父子关系
			permission.setChildren(children);
			
			
			for (Permission child : children) {
				child.setOpen(true);
				
				//根据父查找子
				List<Permission> innerChildren = permissionService.getChildrenPermissionByPid(child.getId());
				
				//设置父子关系
				child.setChildren(innerChildren);
			}
			
			
			
			result.setSuccess(true);
			result.setData(root);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		

		return result ;
	}*/
	
	
	//Demo1 - 模拟数据生成树
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();
			
			//父
			Permission permission = new Permission();
			permission.setName("系统权限菜单");
			permission.setOpen(true);
			
			root.add(permission);
			
			//子
			List<Permission> children = new ArrayList<Permission>();

			Permission permission1 = new Permission();
			permission1.setName("控制面板");
			
			Permission permission2 = new Permission();
			permission2.setName("权限管理");
			
			children.add(permission1);
			children.add(permission2);
			
			
			//设置父子关系
			permission.setChildren(children);
			
			result.setSuccess(true);
			result.setData(root);
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		
		
		 * {"success":true,"message":null,"page":null,
		 * "data":[{"id":null,"pid":null,"name":"系统权限菜单","icon":null,"url":null,"open":true,
		 * "children":[{"id":null,"pid":null,"name":"控制面板","icon":null,"url":null,"open":false,"children":null},
		 * {"id":null,"pid":null,"name":"权限管理","icon":null,"url":null,"open":false,"children":null}]}]}
		 
		return result ;
	}*/
	
	
	
}
