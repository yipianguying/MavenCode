package com.crowd.funding.manager.service;

import com.crowd.funding.bean.Advert;
import com.crowd.funding.utils.PageUtils;

import java.util.Map;

public interface AuthAdvService {


    // 分页查询的方法
    public PageUtils<Advert> pageQuery(Map<String, Object> advertMap);
    // 修改状态的方法
    int updateStatus(Integer id,String status);

    // 根据广告id查询广告
    Advert queryById(Integer id);
}
