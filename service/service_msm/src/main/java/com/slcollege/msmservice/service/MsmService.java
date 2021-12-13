package com.slcollege.msmservice.service;

import java.util.Map;

public interface MsmService {
    // 发送短信的方法
    boolean sendMsm(Map<String, Object> map, String phone);
}

