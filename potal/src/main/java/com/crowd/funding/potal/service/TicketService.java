package com.crowd.funding.potal.service;


import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.Ticket;

public interface TicketService {
	// 获取需要保存的会员id的方法
	Ticket getTicketByMemberId(Integer id);
	// 保存流程单的方法
	void saveTicket(Ticket ticket);
	// 更新流程的方法
	void updatePstep(Ticket ticket);
	// 更新流程步骤
	void updatePiidAndPstep(Ticket ticket);

	Member getMemberByPiid(String processInstanceId);

	void updateStatus(Member member);



}
