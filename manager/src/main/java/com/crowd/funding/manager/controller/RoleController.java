package com.crowd.funding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowd.funding.bean.Permission;
import com.crowd.funding.bean.Role;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.controller.BaseController;

import com.crowd.funding.manager.service.PermissionService;
import com.crowd.funding.manager.service.RoleService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@RequestMapping("/index")
	public String index() {
		return "role/index";
	}

	
	@RequestMapping("/add")
	public String add() {
		return "role/add";
	}

	
	
	
	@RequestMapping("/assignPermission")
	public String assignPermission() {
		return "role/assignPermission";
	}

	// 异步加载,加载许可树的数据并显示在页面上
	@ResponseBody
	@RequestMapping("/loadDataAsync")
	public Object loadDataAsync(Integer roleid){

		List<Permission> root = new ArrayList<Permission>();

		// 查询所有的数据
		List<Permission> childredPermissons = permissionService.queryAllPermission();
		// 根据角色id查询该角色分配前的许可
		List<Integer> permissionForRoleId = permissionService.queryPermissionByRoleId(roleid);

		Map<Integer, Permission> map = new HashMap<Integer, Permission>();//100

		for (Permission innerpermission : childredPermissons) {
			map.put(innerpermission.getId(), innerpermission);
			// 如果permissionForRoleId包含在innerpermission.getId()中,则说明许可已经分配,则页面上显示√
			if(permissionForRoleId.contains(innerpermission.getId())) {
				innerpermission.setChecked(true);
			}
		}

		for (Permission permission : childredPermissons) { //100
			//通过子查找父
			//子菜单
			Permission child = permission; //假设为子菜单
			if (child.getPid() == 0) {
				root.add(permission);
			} else {
				//父节点
				Permission parent = map.get(child.getPid());
				parent.getChildren().add(child);
			}
		}

		return root ;
	}
	// 分配许可的方法
	@ResponseBody
	@RequestMapping("/doAssignPermission")
	public Object doAssignPermission(Integer roleid, Data datas){
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			int count = roleService.saveRolePermissionRelationship(roleid,datas);
			
			result.setSuccess(count==datas.getIds().size());
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	
	//传递多个对象方式
	@ResponseBody
	@RequestMapping("/batchDelete")
	public Object batchDelete(Data datas){
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			int count = roleService.batchDeleteRole(datas);
			if(count==datas.getDatas().size()){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/* 传递多个id值方式
	@ResponseBody
	@RequestMapping("/batchDelete")
	public Object batchDelete(Integer[] ids){
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			int count = roleService.batchDeleteRole(ids);
			if(count==ids.length){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer uid){
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			int count = roleService.deleteRole(uid);
			if(count==1){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/doEdit")
	public Object doEdit(Role role) {
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			int count = roleService.updateRole(role);// id没传的情况下,更新并不会报错,但是并没有更新成功,所以,需要加以判断
			if(count==1){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Map<String,Object> map) {
		Role role = roleService.getRole(id);
		map.put("role", role);
		return "role/edit";
	}
	
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public Object doAdd(Role role) {
		
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			
			roleService.saveRole(role);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 异步分页查询
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String queryText,@RequestParam(required = false, defaultValue = "1") Integer pageno,
			@RequestParam(required = false, defaultValue = "2") Integer pagesize){
		AjaxResultUtils result = new AjaxResultUtils();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno); // 空指针异常
			paramMap.put("pagesize", pagesize);
			
			if(StringUtils.isNotEmpty(queryText)){
				queryText = queryText.replaceAll("%", "\\\\%"); //斜线本身需要转译
				System.out.println("--------------"+queryText);
			}
			
			paramMap.put("queryText", queryText);
			
			// 分页查询数据
			PageUtils<Role> rolePage = roleService.pageQuery(paramMap);

			result.setPages(rolePage);
			result.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			result.setSuccess(false);
		}
		return  result;
	}
}
