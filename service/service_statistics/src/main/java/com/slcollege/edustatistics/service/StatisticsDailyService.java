package com.slcollege.edustatistics.service;

import com.slcollege.edustatistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-10
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {
    // 统计某一天的注册人数,用于生成图表统计数据
    void registerNumber(String day);

    // 图表显示,返回两部分数据,日期JSON数组,数量JSON数组
    Map<String, Object> getShowData(String type, String beginTime, String endTime);
}
