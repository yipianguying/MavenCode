package com.slcollege.edustatistics.scheduled;

import com.slcollege.edustatistics.service.StatisticsDailyService;
import com.slcollege.edustatistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

/*    @Scheduled(cron = "0/5 * * * * ?") //每隔5s去执行一次这个方法
    public void taskOne() {
        System.out.println("************taskOne执行了");
    }*/
    // @Scheduled注解执行定时任务
    // 在每天凌晨1点,执行方法,将前一天的数据查询进行添加
    @Scheduled(cron = "0 0 1 * * ?") //在每天凌晨1点,执行方法
    public void taskTwo() {
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));

        statisticsDailyService.registerNumber(day);
    }
}
