package com.slcollege.educms.service;

import com.slcollege.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-07
 */
public interface CrmBannerService extends IService<CrmBanner> {
    // 查询所有banner的方法
    List<CrmBanner> selectAllBanner();
}
