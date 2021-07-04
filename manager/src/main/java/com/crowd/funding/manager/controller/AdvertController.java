package com.crowd.funding.manager.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.User;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.service.AdvertService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.ConstUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/advert")
public class AdvertController {

	@Autowired
	private AdvertService advertService;
	

	@RequestMapping("/index")
	public String index() {
		return "advert/index";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "advert/add";
	}
	
	@RequestMapping("/edit")
	public String edit( Integer id, Model model ) {
		
		// 根据主键查询广告信息
		Advert advert = advertService.queryById(id);
		model.addAttribute("advert", advert);
		
		return "advert/edit";
	}
	
	@ResponseBody
	@RequestMapping("/batchDelete")
	public Object batchDelete( Data ds ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			int count = advertService.deleteAdverts(ds);
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
			int count = advertService.deleteAdvert(id);
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
	public Object update( Advert advert ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			int count = advertService.updateAdvert(advert);
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
	 * 新增广告数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doAdd")
	public Object doAdd(HttpServletRequest request, Advert advert , HttpSession session) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
			
			MultipartFile mfile = mreq.getFile("advpic");
			
			String name = mfile.getOriginalFilename();//java.jpg
			String extname = name.substring(name.lastIndexOf(".")); // .jpg
			
			String iconpath = UUID.randomUUID().toString()+extname; //232243343.jpg
			
			ServletContext servletContext = session.getServletContext();
			String realpath = servletContext.getRealPath("/picture");
			
			String path =realpath+ "\\adv\\"+iconpath;
			
			mfile.transferTo(new File(path));
			
			User user = (User)session.getAttribute(ConstUtils.USER_LOGIN);
			advert.setUserid(user.getId());
			advert.setStatus("1");
			advert.setIconpath(iconpath);
			
			int count = advertService.insertAdvert(advert);
			result.setSuccess(count==1);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	
	//同步请求处理.
	/*@RequestMapping("/doAdd")
	public Object doAdd(HttpServletRequest request, Advert advert ,HttpSession session) {
	
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
			
			MultipartFile mfile = mreq.getFile("advpic");
			
			String name = mfile.getOriginalFilename();//java.jpg
			String extname = name.substring(name.lastIndexOf(".")); // .jpg
			
			String iconpath = UUID.randomUUID().toString()+extname; //  232243343.jpg
			
			ServletContext servletContext = session.getServletContext();
			String realpath = servletContext.getRealPath("/pic");
			
			String path =realpath+ "\\adv\\"+iconpath;
			
			mfile.transferTo(new File(path)); //文件上传.
			
			User user = (User)session.getAttribute(Const.LOGIN_USER);
			advert.setUserid(user.getId());
			advert.setStatus("1");
			advert.setIconpath(iconpath);
			
			int count = advertService.insertAdvert(advert);
			
		} catch ( Exception e ) {
			e.printStackTrace();
			
		}
		
		return "redirect:/advert/index.htm";
	}*/
	
	/**
	 * 分页查询广告数据
	 * @advert pageno
	 * @advert pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String pagetext, Integer pageno, Integer pagesize) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			// 查询广告数据
			Map<String, Object> advertMap = new HashMap<String, Object>();
			advertMap.put("pageno", pageno);
			advertMap.put("pagesize", pagesize);
			if ( StringUtils.isNotEmpty(pagetext) ) {
				pagetext = pagetext.replaceAll("%", "\\\\%");
			}
			advertMap.put("pagetext", pagetext);
			
			// 分页查询
			PageUtils<Advert> page = advertService.pageQuery(advertMap);
			result.setPages(page);
			result.setSuccess(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
}
