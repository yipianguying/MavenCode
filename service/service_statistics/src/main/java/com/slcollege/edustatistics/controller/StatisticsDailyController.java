package com.slcollege.edustatistics.controller;


import com.slcollege.commonutils.Result;
import com.slcollege.edustatistics.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-10
 */
@RestController
@CrossOrigin
@RequestMapping("/edustatistics/statistics")
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsService;

    // 统计某一天的注册人数,用于生成图表统计数据
    @PostMapping("registerNumber/{day}")
    public Result registerNumber(@PathVariable String day) {

       statisticsService.registerNumber(day);

        return Result.ok();
    }

    // 图表显示,返回两部分数据,日期JSON数组,数量JSON数组
    @GetMapping("getShowData/{type}/{beginTime}/{endTime}")
    public Result getShowData(@PathVariable String type,@PathVariable String beginTime,
                           @PathVariable String endTime) {
        Map<String, Object> dataMap = statisticsService.getShowData(type, beginTime, endTime);

        return Result.ok().data(dataMap);
    }
}

