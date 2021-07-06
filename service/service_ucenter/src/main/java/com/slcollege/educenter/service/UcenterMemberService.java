package com.slcollege.educenter.service;

import com.slcollege.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slcollege.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-22
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    // 登录的方法
    String userLogin(UcenterMember member);
    // 用户注册的方法
    void userRegister(RegisterVo registerVo);

    // 根据open_id判断
    UcenterMember getOpenIdMember(String open_id);

    // 查询某一天的注册人数-用于生成统计图表
    Integer registerNumberDay(String day);
}
