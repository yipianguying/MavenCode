package com.crowd.funding.manager.service.impl;

import java.util.List;
import java.util.Map;


import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.dao.AdvertMapper;
import com.crowd.funding.manager.service.AdvertService;
import com.crowd.funding.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AdvertServiceImpl implements AdvertService {

	@Autowired
	private AdvertMapper advertDao;

	public Advert queryAdvert(Map<String, Object> advertMap) {
		return advertDao.queryAdvert(advertMap);
	}

	public PageUtils<Advert> pageQuery(Map<String, Object> paramMap) {
		PageUtils<Advert> advertPage = new PageUtils<Advert>((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
		paramMap.put("startIndex", advertPage.getStartIndex());
		List<Advert> advertList= advertDao.pageQuery(paramMap);
		// 获取数据的总条数
		int count = advertDao.queryCount(paramMap);
		
		advertPage.setData(advertList);
		advertPage.setTotalSize(count);
		return advertPage;
	}

	public int queryCount(Map<String, Object> advertMap) {
		return advertDao.queryCount(advertMap);
	}

	public int insertAdvert(Advert advert) {
		return advertDao.insertAdvert(advert);
	}

	public Advert queryById(Integer id) {
		return advertDao.queryById(id);
	}

	public int updateAdvert(Advert advert) {
		return advertDao.updateAdvert(advert);
	}

	public int deleteAdvert(Integer id) {
		return advertDao.deleteAdvert(id);
	}

	public int deleteAdverts(Data ds) {
		return advertDao.deleteAdverts(ds);
	}

}
