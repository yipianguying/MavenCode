package com.slcollege.msmservice.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.slcollege.commonutils.Result;
import com.slcollege.msmservice.service.MsmService;
import com.slcollege.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 发送短信的方法
    @GetMapping("sendMsm/{phone}")
    public Result sendMsm(@PathVariable String phone) {
        // 1.从Redis里面获得验证码,能获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return Result.ok();
        }

        // 2.如果redis获取不到,则进行阿里云发送
        // 生成随机值,传递至阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> map = new HashMap<>();

        map.put("code",code);

        // 调用service发送短信的方法
        boolean isSendMsm =  msmService.sendMsm(map,phone);

        if (isSendMsm) {
            // 发送成功,把发送成功的验证码放到redis里面
            // 设置有效时间,设置有效值的时间为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return Result.ok();
        } else {
            return Result.error().message("短信发送失败");
        }

    }

}
