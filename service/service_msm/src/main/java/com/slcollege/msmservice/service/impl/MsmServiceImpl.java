package com.slcollege.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.slcollege.msmservice.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    // 发送短信的方法
    @Override
    public boolean sendMsm(Map<String, Object> map, String phone) {
        if(StringUtils.isEmpty(phone))
            return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4FzaV5A1D1kmK65xVB27", "RwcEKtJz7x9RUPF3yK7L09p5HzytVP");
        IAcsClient client = new DefaultAcsClient(profile);

        // 设置参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        // 设置发送相关的参数
        request.putQueryParameter("PhoneNumbers",phone);// 手机号
        request.putQueryParameter("SignName","我的胜利学院在线教育网站");
        request.putQueryParameter("TemplateCode", "");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));

        // 最终发送
        try {
            // 最终发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
