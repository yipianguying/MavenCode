package com.slcollege.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.educms.entity.CrmBanner;
import com.slcollege.educms.mapper.CrmBannerMapper;
import com.slcollege.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-07
 */
/*
* Redis不同的是它会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，实现数据的持久化。
* 首页数据变化不是很频繁，而且首页访问量相对较大，
*   所以我们有必要把首页接口数据缓存到redis缓存中，减少数据库压力和提高访问速度。
* */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    // 查询所有banner的方法
    @Cacheable(value = "banner",key = "'selectIndexList'") //添加redis缓存
    @Override
    public List<CrmBanner> selectAllBanner() {
        // 根据id进行降序排列,显示排列后的两条数据
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        // last方法,拼接sql语句
        wrapper.last("limit 2");

        List<CrmBanner> list = baseMapper.selectList(null);
        return list;
    }
}
