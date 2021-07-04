package com.crowd.funding.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowd.funding.bean.Cert;
import com.crowd.funding.manager.service.CertService;
import com.crowd.funding.manager.service.CerttypeService;
import com.crowd.funding.utils.AjaxResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/certtype")
public class CerttypeController {

	@Autowired
	private CerttypeService certtypeService;
	
	@Autowired
	private CertService certService ;

	// 跳转到资质类型主页的方法
	@RequestMapping("/index")
	public String index(Map<String, Object> map){
		//查询所有资质
		List<Cert> queryAllCert = certService.queryAllCert();
		map.put("allCert", queryAllCert);
		
		
		//查询资质与账户类型之间关系(表示之前给账户类型分配过资质)
		List<Map<String,Object>> certAccttypeList =  certtypeService.queryCertAccttype();
		map.put("certAccttypeList", certAccttypeList);
		
		return "certtype/index";
	}
	
	// 增加资质的方法
	@ResponseBody
	@RequestMapping("/insertAcctTypeCert")
	public Object insertAcctTypeCert( Integer certid, String accttype ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("certid", certid);
			paramMap.put("accttype", accttype);
			
			int count = certtypeService.insertAcctTypeCert(paramMap);
			result.setSuccess(count==1);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}

	// 删除资质的方法
	@ResponseBody
	@RequestMapping("/deleteAcctTypeCert")
	public Object deleteAcctTypeCert( Integer certid, String accttype ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("certid", certid);
			paramMap.put("accttype", accttype);
			
			int count = certtypeService.deleteAcctTypeCert(paramMap);
			result.setSuccess(count==1);
		} catch ( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}

}
