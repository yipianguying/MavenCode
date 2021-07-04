package com.crowd.funding.manager.controller;

import java.util.HashMap;
import java.util.Map;


import com.crowd.funding.bean.Cert;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.service.CertService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/cert")
public class CertController {

	@Autowired
	private CertService certService;
	

	@RequestMapping("/index")
	public String index() {
		return "cert/index";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "cert/add";
	}
	
	@RequestMapping("/edit")
	public String edit( Integer id, Model model ) {
		
		// 根据主键查询资质信息
		Cert cert = certService.queryById(id);
		model.addAttribute("cert", cert);
		
		return "cert/edit";
	}
	
	@ResponseBody
	@RequestMapping("/deletes")
	public Object deletes( Data ds ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			int count = certService.deleteCerts(ds);
			if ( count == ds.getDatas().size() ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete( Integer id ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			int count = certService.deleteCert(id);
			if ( count == 1 ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update( Cert cert ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			int count = certService.updateCert(cert);
			if ( count == 1 ) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 新增资质数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert( Cert cert ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			certService.insertCert(cert);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 分页查询资质数据
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String pagetext, Integer pageno, Integer pagesize) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			// 查询资质数据
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			if ( StringUtils.isNotEmpty(pagetext) ) {
				//pagetext = pagetext.replaceAll("%", "\\\\%");
			}
			paramMap.put("pagetext", pagetext);
			
			// 分页查询数据
			PageUtils<Cert> page = certService.pageQuery(paramMap);

			result.setPages(page);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
}
