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

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/member/")  // 중복된 주소는 위쪽에서 한번에 처리
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);  // 디버깅코드처럼 사용하며, java의 System.out.println();과 비슷하지만 리소스를 덜 소비한다.
	
	// @Autowired  // 서버에 요청
	// private Test tt;
	
	@Autowired
	MemberService memberService;  // Interface에서 주입받아야 implements 한 class명이 변경되어도 수정하지 않아도 됨
	
	@RequestMapping(value="memberJoin.aws", method=RequestMethod.GET)  // ";" 없음 주의
	public String memberJoin() {
		
		logger.info("memberJoin들어옴");  // INFO : com.myaws.myapp.controller.MemberController - memberJoin들어옴
		
		
		
		
		
		// logger.info("tt값은 " + tt.test());
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="memberJoinAction.aws", method=RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {  // <jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" /> 이것과 같다. form 태그 내부의 name과 class의 변수명이 같으면 getParameter 대체 가능

		logger.info("memberJoinAction들어옴");
		
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
