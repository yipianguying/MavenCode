package com.crowd.funding.manager.service.impl;

import com.crowd.funding.bean.Advert;
import com.crowd.funding.manager.dao.AuthAdvMapper;
import com.crowd.funding.manager.service.AuthAdvService;
import com.crowd.funding.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class AuthAdvServiceImpl implements AuthAdvService {
    @Autowired
    private AuthAdvMapper authAdvMapper;

    // 分页查询的方法
    @Override
    public PageUtils<Advert> pageQuery(Map<String, Object> advertMap) {
        PageUtils<Advert> advertPage = new PageUtils<Advert>((Integer)advertMap.get("pageno"),(Integer)advertMap.get("pagesize"));
        advertMap.put("startIndex", advertPage.getStartIndex());
        List<Advert> advertList= authAdvMapper.pageQuery(advertMap);
        // 获取数据的总条数
        int count = authAdvMapper.queryCount(advertMap);

        advertPage.setData(advertList);
        advertPage.setTotalSize(count);
        return advertPage;
    }
    // 修改状态的方法
    @Override
    public int updateStatus(Integer id,String status) {
        return authAdvMapper.updateStatus(id,status);
    }
    // 根据广告id查询广告
    @Override
    public Advert queryById(Integer id) {
        return authAdvMapper.queryById(id);
    }
}
