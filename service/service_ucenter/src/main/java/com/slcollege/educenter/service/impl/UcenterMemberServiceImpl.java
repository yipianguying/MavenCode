package com.slcollege.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.slcollege.commonutils.JwtUtils;
import com.slcollege.commonutils.MD5;
import com.slcollege.educenter.entity.UcenterMember;
import com.slcollege.educenter.entity.vo.RegisterVo;
import com.slcollege.educenter.mapper.UcenterMemberMapper;
import com.slcollege.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-22
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    // 登录的方法
    @Override
    public String userLogin(UcenterMember member) {
        // 获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        // 手机号和密码进行一个非空的判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new SlCollegeException(20001,"登录失败");
        }

        // 判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        // 判断查询对象是否为空,如果为空抛出异常
        if (mobileMember == null) {
            throw new SlCollegeException(20001,"手机号不存在,登录失败");
        }
        // 判断密码是否正确
        // 数据库中的密码是加密的
        // 把输入的密码进行加密,再和数据库中的密码进行比较
        // 密码加密 MD5(只能加密,不能解密)
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new SlCollegeException(20001,"密码错误,登录失败");
        }
        // 判断用户是否被禁用
        if(mobileMember.getIsDisabled()) {
            throw new SlCollegeException(20001,"用户被禁用,登录失败");
        }

        // 登录成功
        // 生成token字符串,使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册的方法
    @Override
    public void userRegister(RegisterVo registerVo) {
        // 获取注册的数据
        // 手机号
        String mobile = registerVo.getMobile();
        // 昵称
        String nickname = registerVo.getNickname();
        // 密码
        String password = registerVo.getPassword();

        // 非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
            throw new SlCollegeException(20001,"昵称、手机号、密码都不能为空,注册失败");
        }

        // 判断手机号是否重复,表里面存在相同手机号不进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {
            throw new SlCollegeException(20001,"手机号已经存在,注册失败");
        }

        // 数据添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);// 用户不被禁用
        // 用户头像
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        baseMapper.insert(member);


    }

    // 根据open_id判断
    @Override
    public UcenterMember getOpenIdMember(String open_id) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",open_id);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);

        return ucenterMember;
    }

    // 查询某一天的注册人数-用于生成统计图表
    @Override
    public Integer registerNumberDay(String day) {
        Integer number= baseMapper.registerNumberDay(day);

        return number;
    }
}
