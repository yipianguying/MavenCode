package com.crowd.funding.potal.dao;



import com.crowd.funding.bean.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

    // 查询会员登录的方法
    Member queryMebmerlogin(Map<String, Object> paramMap);

    // 更新审核类型的方法
	void updateAcctType(Member loginMember);

    // 更新账户类型的方法
	void updateBasicinfo(Member loginMember);

    void updateEmail(Member loginMember);

    void updateAuthstatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);

    // 会员注册的方法
    boolean insertMapper(Member member);
}