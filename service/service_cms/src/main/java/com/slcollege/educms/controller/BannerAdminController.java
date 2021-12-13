package com.slcollege.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.Result;
import com.slcollege.educms.entity.CrmBanner;
import com.slcollege.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  后台banner管理接口
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-07
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;

    // 1.分页查询banner
    @GetMapping("pageBanner/{page}/{limit}")
    public Result pageBanner(@PathVariable long page,@PathVariable long limit) {
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        crmBannerService.page(pageBanner,null);

        return Result.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

    // 2.添加banner的方法
    @PostMapping("addBanner")
    public Result addBanner(@RequestBody CrmBanner crmBanner) {
        crmBannerService.save(crmBanner);

        return Result.ok();
    }
    // 3.根据id获取Banner的方法
    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public Result get(@PathVariable String id) {
        CrmBanner banner = crmBannerService.getById(id);
        return Result.ok().data("item", banner);
    }

    // 4.修改Banner的方法
    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public Result updateById(@RequestBody CrmBanner crmBanner) {
        crmBannerService.updateById(crmBanner);
        return Result.ok();
    }

    // 5.删除Banner的方法
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        crmBannerService.removeById(id);
        return Result.ok();
    }


}

