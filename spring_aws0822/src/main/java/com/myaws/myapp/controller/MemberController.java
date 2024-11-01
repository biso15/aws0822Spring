package com.myaws.myapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.service.MemberService;
import com.myaws.myapp.service.Test;

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/member/")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);  // ������ڵ�ó�� ����ϸ�, java�� System.out.println();�� ��������� ���ҽ��� �� �Һ��Ѵ�.
	
	// @Autowired  // ������ ��û
	// private Test tt;
	
	@Autowired
	MemberService memberService;  // Interface���� ���Թ޾ƾ� implements �� class���� ����Ǿ �������� �ʾƵ� ��
	
	@RequestMapping(value="memberJoin.aws", method=RequestMethod.GET)  // ";" ���� ����
	public String memberJoin() {
		
		logger.info("memberJoin����");  // INFO : com.myaws.myapp.controller.MemberController - memberJoin����
		
		
		
		
		
		// logger.info("tt���� " + tt.test());
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="memberJoinAction.aws", method=RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {  // <jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" /> �̰Ͱ� ����. form �±� ������ name�� class�� �������� ������ getParameter ��ü ����

		logger.info("memberJoinAction����");
		
		int value = memberService.memberInsert(mv);
		logger.info("value" + value);
		
		String path = "";
		if(value == 1) {
			path = "redirect:/";
		} else {
			path = "redirect:/member/memberJoin.aws";			
		}
		
		return path;
	}	
	
	
	@RequestMapping(value="memberLogin.aws", method=RequestMethod.GET)
	public String memberLogin() {
		
		return "WEB-INF/member/memberLogin";
	}
}
