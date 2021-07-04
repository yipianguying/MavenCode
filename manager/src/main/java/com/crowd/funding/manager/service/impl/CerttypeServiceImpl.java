package com.crowd.funding.manager.service.impl;

import java.util.List;
import java.util.Map;

import com.crowd.funding.manager.dao.AccountTypeCertMapper;
import com.crowd.funding.manager.service.CerttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CerttypeServiceImpl implements CerttypeService {

	@Autowired
	private AccountTypeCertMapper accountTypeCertMapper ;

	@Override
	public List<Map<String, Object>> queryCertAccttype() {
		return accountTypeCertMapper.queryCertAccttype();
	}

	@Override
	public int deleteAcctTypeCert(Map<String, Object> paramMap) {
		return accountTypeCertMapper.deleteAcctTypeCert(paramMap);
	}

	@Override
	public int insertAcctTypeCert(Map<String, Object> paramMap) {
		return accountTypeCertMapper.insertAcctTypeCert(paramMap);
	}
	
	
}
