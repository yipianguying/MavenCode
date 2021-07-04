package com.crowd.funding.potal.service.impl;


import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.Ticket;
import com.crowd.funding.potal.dao.TicketMapper;
import com.crowd.funding.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketMapper ticketMapper ;
	// 获取需要保存的会员id的方法
	@Override
	public Ticket getTicketByMemberId(Integer id) {
		return ticketMapper.getTicketByMemberId(id);
	}
	// 保存流程单的方法
	@Override
	public void saveTicket(Ticket ticket) {
		ticketMapper.saveTicket(ticket);
	}
	// 更新流程的方法
	@Override
	public void updatePstep(Ticket ticket) {
		ticketMapper.updatePstep(ticket);
	}
	// 更新流程步骤
	@Override
	public void updatePiidAndPstep(Ticket ticket) {
		ticketMapper.updatePiidAndPstep(ticket);
	}

	@Override
	public Member getMemberByPiid(String processInstanceId) {
		return ticketMapper.getMemberByPiid(processInstanceId);
	}

	@Override
	public void updateStatus(Member member) {
		ticketMapper.updateStatus(member);
	}


	
}
