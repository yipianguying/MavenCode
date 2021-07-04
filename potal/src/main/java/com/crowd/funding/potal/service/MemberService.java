package com.crowd.funding.potal.service;



import com.crowd.funding.bean.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    // 查询会员登录的方法
    Member queryMemberLogin(Map<String, Object> loginMap);
    // 更新审核类型的方法
    void updateAcctType(Member loginMember);
    // 更新账户类型的方法
    void updateBasicinfo(Member loginMember);
    // 更新邮箱的方法
    void updateEmail(Member loginMember);
    // 更新
    void updateAuthstatus(Member loginMember);

    Member getMembersById(Integer memberid);

    List<Map<String, Object>> queryCertByMembersId(Integer memberid);

    // 会员注册的方法
    boolean insertMember(Member member);
}
