package com.slcollege.educms.controller;


import com.slcollege.commonutils.Result;
import com.slcollege.educms.entity.CrmBanner;
import com.slcollege.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-07
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    // 查询所有banner的方法
    @GetMapping("getAllBanner")
    public Result getAllResult() {
        List<CrmBanner> bannerList =  crmBannerService.selectAllBanner();
        return Result.ok().data("bannerList",bannerList);
    }

}

