package com.crowd.funding.manager.dao;


import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface AuthAdvMapper {

	Advert queryAdvert(Map<String, Object> advertMap);

	List<Advert> pageQuery(Map<String, Object> advertMap);

	int queryCount(Map<String, Object> advertMap);

	int insertAdvert(Advert advert);

	Advert queryById(Integer id);

	int updateAdvert(Advert advert);

	int deleteAdvert(Integer id);

	int deleteAdverts(Data ds);
	// 修改状态的方法
    int updateStatus(@Param(value = "id") Integer id,@Param(value = "status") String status);
}
