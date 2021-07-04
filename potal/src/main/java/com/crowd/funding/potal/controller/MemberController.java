package com.crowd.funding.potal.controller;

import javax.servlet.http.HttpSession;


import com.crowd.funding.bean.Cert;
import com.crowd.funding.bean.Member;
import com.crowd.funding.bean.MemberCert;
import com.crowd.funding.bean.Ticket;
import com.crowd.funding.bean.vo.Data;
import com.crowd.funding.manager.service.CertService;
import com.crowd.funding.potal.listener.PassListener;
import com.crowd.funding.potal.listener.RefuseListener;
import com.crowd.funding.potal.service.MemberService;
import com.crowd.funding.potal.service.TicketService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.ConstUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


/*
*  会员信息的
* */
@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService ;

	@Autowired
	private TicketService ticketService ;

	@Autowired
	private CertService certService ;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService ;

	// 跳转到选择选择账户类型的页面的方法
	@RequestMapping("/accttype")
	public String accttype(){		
		return "member/accttype";
	}
	// 跳转到更新审核信息页面的方法
	@RequestMapping("/basicinfo")
	public String basicinfo(){		
		return "member/basicinfo";
	}
	// 跳转到上传资质文件的页面
	@RequestMapping("/uploadCert")
	public String uploadCert() {
		return "member/uploadCert";
	}
	// 跳转到邮箱页面的方法
	@RequestMapping("/checkemail")
	public String checkemail(){
		return "member/checkemail";
	}
	// 跳转到最终确认的方法
	@RequestMapping("/checkauthcode")
	public String checkauthcode(){
		return "member/checkauthcode";
	}
	// 跳转到我的众筹页面的方法
	@RequestMapping("/mineCrowdFunding")
	public String mineCrowdFunding() {
		return "member/mineCrowdFunding";
	}

	// 跳转到添加审核页面的方法并添加审核的方法
	@RequestMapping("/apply")
	public String apply(HttpSession session){

		Member member = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);

		Ticket ticket = ticketService.getTicketByMemberId(member.getId()) ;

		if(ticket == null ){
			ticket  = new Ticket(); //封装数据
			ticket.setMemberid(member.getId());
			ticket.setPstep("apply");
			ticket.setStatus("0");

			ticketService.saveTicket(ticket);

		}else{
			String pstep = ticket.getPstep();

			if("accttype".equals(pstep)){

				return "redirect:/member/basicinfo.htm";
			} else if("basicinfo".equals(pstep)){

				//根据当前用户查询账户类型,然后根据账户类型查找需要上传的资质
				List<Cert> queryCertByAccttype = certService.queryCertByAccttype(member.getAccttype());

				session.setAttribute("queryCertByAccttype", queryCertByAccttype);

				return "redirect:/member/uploadCert.htm";
			}else if("uploadcert".equals(pstep)){
				// 发送邮件的页面
				return "redirect:/member/checkemail.htm";
			}else if("checkemail".equals(pstep)){

				return "redirect:/member/checkauthcode.htm";
			}


		}

		return "member/accttype";
	}

	// 申请完成的方法
	@ResponseBody
	@RequestMapping("/finishApply")
	public Object finishApply( HttpSession session, String authcode) {
		AjaxResultUtils result = new AjaxResultUtils();

		try {

			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);

			//让当前系统用户完成:验证码审核任务.
			Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
			if(ticket.getAuthcode().equals(authcode)){
				//完成审核验证码任务
				Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
				taskService.complete(task.getId());

				//更新用户申请状态
				loginMember.setAuthstatus("1");
				memberService.updateAuthstatus(loginMember);

				//记录流程步骤:
				ticket.setPstep("finishapply");
				// 更新流程的方法
				ticketService.updatePstep(ticket);
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
				result.setMessage("验证码不正确,请重新输入!");
			}
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;

	}


	@ResponseBody
	@RequestMapping("/doUploadCert")
	public Object doUploadCert( HttpSession session, Data ds) {
		AjaxResultUtils result = new AjaxResultUtils();

		try {

			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);

			String realPath = session.getServletContext().getRealPath("/picture");

			List<MemberCert> certimgs = ds.getCertimgs();
			for (MemberCert memberCert : certimgs) {
				// 文件上传
				MultipartFile fileImg = memberCert.getFileImg();
				String extName = fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));
				String tmpName = UUID.randomUUID().toString() +  extName;
				String filename = realPath + "/cert" +"/" + tmpName;

				fileImg.transferTo(new File(filename));	//资质文件上传.

				//准备数据
				memberCert.setIconpath(tmpName); //封装数据,保存数据库
				memberCert.setMemberid(loginMember.getId());
			}

			// 保存会员与资质关系数据.
			certService.saveMemberCert(certimgs);

			//记录流程步骤:
			Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
			ticket.setPstep("uploadcert");
			ticketService.updatePstep(ticket);

			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;

	}

	// 更新账户基本信息的方法
	@ResponseBody
	@RequestMapping("/updateBasicinfo")
	public Object updateBasicinfo( HttpSession session, Member member) {
		AjaxResultUtils result = new AjaxResultUtils();

		try {

			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);
			loginMember.setRealname(member.getRealname());
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());

			// 更新账户类型
			memberService.updateBasicinfo(loginMember);

			//记录流程步骤:
			Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
			ticket.setPstep("basicinfo");
			ticketService.updatePstep(ticket);

			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;

	}
	
	/**
	 * 更新账户类型
	 */
	@ResponseBody
	@RequestMapping("/updateAcctType")
	public Object updateAcctType( HttpSession session, String accttype ) {
		AjaxResultUtils result = new AjaxResultUtils();
		
		try {
			
			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);
			loginMember.setAccttype(accttype);
			// 更新审核类型的方法
			memberService.updateAcctType(loginMember);
			
			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;

	}

	// 发送邮件的方法
	@ResponseBody
	@RequestMapping("/startProcess")
	public Object startProcess( HttpSession session, String email) {
		AjaxResultUtils result = new AjaxResultUtils();

			try {

			// 获取登录会员信息
			Member loginMember = (Member)session.getAttribute(ConstUtils.MEMBER_LOGIN);

			// 如果用户输入新的邮箱,将旧的邮箱地址替换
			if(!loginMember.getEmail().equals(email)){
				loginMember.setEmail(email);
				memberService.updateEmail(loginMember);
			}

			//启动实名认证流程 - 系统自动发送邮件,生成验证码.验证邮箱地址是否合法(模拟:银行卡是否邮箱).
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();

			//toEmail 邮箱
			//authCode 实名认证验证码,随即得到
			//loginacct 登录账号
			//flag  审核实名认证时提供
			//passListener 监听器:通过流程执行
			//refuseListener 监听器:没有通过流程执行
			StringBuilder authCode = new StringBuilder();
			for (int i = 1; i <= 4; i++) {
				authCode.append(new Random().nextInt(10));
			}
			// 得到流程实例需要的变量
			Map<String,Object> variables= new HashMap<String,Object>();
			variables.put("toEmail", email);
			variables.put("authcode", authCode);
			variables.put("loginacct", loginMember.getLoginacct());
			variables.put("passListener", new PassListener());
			variables.put("refuseListener", new RefuseListener());


			//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth");
			// 得到流程实例
			ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);


			//记录流程步骤:
			Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
			ticket.setPstep("checkemail");
			ticket.setPiid(processInstance.getId());
			ticket.setAuthcode(authCode.toString());
			// 更新流程步骤(携带验证码)
			ticketService.updatePiidAndPstep(ticket);

			result.setSuccess(true);
		} catch( Exception e ) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;

	}
}
