package com.crowd.funding.potal.dao;



import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    Ticket selectByPrimaryKey(Integer id);

    List<Ticket> selectAll();

	Ticket getTicketByMemberId(Integer memberid);

	void saveTicket(Ticket ticket);

	void updatePstep(Ticket ticket);
	// 更新流程步骤
	void updatePiidAndPstep(Ticket ticket);

	Member getMemberByPiid(String processInstanceId);

	void updateStatus(Member member);



}