package com.crowd.funding.manager.service;


import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.utils.PageUtils;

import java.util.Map;



public interface AdvertService {
	public Advert queryAdvert(Map<String, Object> advertMap);

	public PageUtils<Advert> pageQuery(Map<String, Object> advertMap);

	public int queryCount(Map<String, Object> advertMap);

	public int insertAdvert(Advert advert);

	public Advert queryById(Integer id);

	public int updateAdvert(Advert advert);

	public int deleteAdvert(Integer id);

	public int deleteAdverts(Data ds);
}
