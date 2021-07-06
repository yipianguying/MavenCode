package com.slcollege.edustatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.commonutils.Result;
import com.slcollege.edustatistics.client.UcenterClient;
import com.slcollege.edustatistics.entity.StatisticsDaily;
import com.slcollege.edustatistics.mapper.StatisticsDailyMapper;
import com.slcollege.edustatistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-03-10
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;

    // 统计某一天的注册人数,用于生成图表统计数据
    @Override
    public void registerNumber(String day) {

        // 添加记录之前删除表相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        // 远程调用得到某一天的注册人数
        Result registerResult = ucenterClient.registerCount(day);
        // 得到某一天的注册人数
        Integer registerNumber = (Integer)registerResult.getData().get("registerNumber");

        // 将数据添加到数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        // 设置注册人数
        statisticsDaily.setRegisterNum(registerNumber);
        // 设置统计日期
        statisticsDaily.setDateCalculated(day);
        // 设置每日播放视频数、登录人数、每日新增课程数(使用随机数生成)
        // int num = RandomUtils.nextInt(100, 200);
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(20, 375));
        statisticsDaily.setLoginNum(RandomUtils.nextInt(10, 400));
        statisticsDaily.setCourseNum(RandomUtils.nextInt(30, 360));
        // 插入数据
        baseMapper.insert(statisticsDaily);
    }

    // 图表显示,返回两部分数据,日期JSON数组,数量JSON数组
    @Override
    public Map<String, Object> getShowData(String type, String beginTime, String endTime) {
        // 根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        // 查询某个范围内的数据
        wrapper.between("date_calculated",beginTime,endTime);
        // 查询对应类型的数据
        wrapper.select("date_calculated",type);

        List<StatisticsDaily> statisticsList = baseMapper.selectList(wrapper);

        // 返回值需要两部分数据:(1).日期 (2).日期对应数量
        // 前端要求JSON数组数据,对应Java代码是List集合
        // 创建两个List集合,一个日期List, 一个数量List
        List<String> dateCalculatedList = new ArrayList<>();//日期
        List<Integer> countList = new ArrayList<>();//数量

        // 遍历查询所有数据list集合,进行封装
        for(StatisticsDaily daily : statisticsList) {
            // 封装日期的List集合
            dateCalculatedList.add(daily.getDateCalculated());
            // 封装对应数量
            switch (type) {
                case "login_num":
                    countList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    countList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    countList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    countList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dateCalculatedList",dateCalculatedList);
        map.put("countList",countList);

        return map;
    }
}
