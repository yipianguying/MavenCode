package com.crowd.funding.manager.dao;


import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.vo.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface AdvertMapper {

	Advert queryAdvert(Map<String, Object> advertMap);

	List<Advert> pageQuery(Map<String, Object> advertMap);

	int queryCount(Map<String, Object> advertMap);

	int insertAdvert(Advert advert);

	Advert queryById(Integer id);

	int updateAdvert(Advert advert);

	int deleteAdvert(Integer id);

	int deleteAdverts(Data ds);
}
