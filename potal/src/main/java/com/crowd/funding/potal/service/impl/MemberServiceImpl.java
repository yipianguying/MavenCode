package com.crowd.funding.potal.service.impl;


import com.crowd.funding.bean.Member;
import com.crowd.funding.potal.dao.MemberMapper;
import com.crowd.funding.potal.service.MemberService;
import com.crowd.funding.utils.ConstUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member queryMemberLogin(Map<String, Object> loginMap) {
        // 查询会员登录的方法
        return memberMapper.queryMebmerlogin(loginMap);
    }

    // 更新审核类型的方法
    @Override
    public void updateAcctType(Member loginMember) {
        memberMapper.updateAcctType(loginMember);
    }

    // 更新账户类型的方法
    @Override
    public void updateBasicinfo(Member loginMember) {
        memberMapper.updateBasicinfo(loginMember);
    }


    @Override
    public void updateEmail(Member loginMember) {
        memberMapper.updateEmail(loginMember);
    }

    @Override
    public void updateAuthstatus(Member loginMember) {
        memberMapper.updateAuthstatus(loginMember);
    }

    @Override
    public Member getMembersById(Integer memberid) {
        return memberMapper.getMemberById(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMembersId(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }

    // 会员注册的方法
    @Override
    public boolean insertMember(Member member) {
        // 设置实名认证状态(第一次注册都是未实名认证的状态)
        member.setAuthstatus(ConstUtils.MEMBER_AUTH_STATUS);

        return memberMapper.insertMapper(member);
    }
}
