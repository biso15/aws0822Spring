package com.myaws.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	private MemberService memberService;  // Interface에서 주입받아야 implements 한 class명이 변경되어도 수정하지 않아도 됨	
	
	@Autowired  // @Autowired를 각각 붙여줘야 함
	private BCryptPasswordEncoder bCryptPasswordEncoder;  //  비밀번호 암호화
	
	@RequestMapping(value="memberJoin.aws", method=RequestMethod.GET)  // ";" 없음 주의
	public String memberJoin() {
		
		logger.info("memberJoin들어옴");  // INFO : com.myaws.myapp.controller.MemberController - memberJoin들어옴
		
		// logger.info("tt값은 " + tt.test());
		
		return "WEB-INF/member/memberJoin";
	}
	
	@RequestMapping(value="memberJoinAction.aws", method=RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {  // <jsp:useBean id = "mv" class="Vo.MemberVo" scope = "page" /> 이것과 같다. form 태그 내부의 name과 class의 변수명이 같으면 getParameter 대체 가능

		logger.info("memberJoinAction들어옴");

		// 비밀번호 암호화
		String memberpwd_enc = bCryptPasswordEncoder.encode(mv.getMemberpwd());
		mv.setMemberpwd(memberpwd_enc);
		
		int value = memberService.memberInsert(mv);
		
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
	
	@RequestMapping(value="memberLoginAction.aws", method=RequestMethod.POST)
	public String memberLoginAction(
			@RequestParam("memberid") String memberId, 
			@RequestParam("memberpwd") String memberPwd,
			RedirectAttributes rttr,  // request.setAttribute("serverTime", formattedDate); 와 비슷
			HttpSession session
		) {

		logger.info("memberLoginAction들어옴");
		
		MemberVo mv = memberService.memberLoginCheck(memberId);
		
		String path = "";
		
		if(mv != null) {  // 객체값이 있으면
			
			// 저장된 비밀번호를 가져온다
			String reservedPwd = mv.getMemberpwd();
			
			if(bCryptPasswordEncoder.matches(memberPwd, reservedPwd)) {  // 비밀번호 일치
				logger.info("비밀번호 일치");
				rttr.addAttribute("midx", mv.getMidx());
				rttr.addAttribute("memberId", mv.getMemberid());
				rttr.addAttribute("memberName", mv.getMembername());
				
				if(session.getAttribute("saveUrl") != null) {
					path = "redirect:" + session.getAttribute("saveUrl").toString();
				} else {					
					path = "redirect:/";
				}
				
			} else {  // 비밀번호 불일치
				
				rttr.addFlashAttribute("msg", "아이디/비밀번호를 확인해주세요");  // addAttribute와 다르게 1회성임. 새로고침시 사라진다
				
				path = "redirect:/member/memberLogin.aws";
			}			
			
		} else {  // 객체값이 없으면

			rttr.addFlashAttribute("msg", "해당하는 아이디가 없습니다.");
			
			path = "redirect:/member/memberLogin.aws";
		}
		
		return path;
		
		// model.addAttribute("serverTime", formattedDate);
		
	}
	
	@ResponseBody
	@RequestMapping(value="memberIdCheck.aws", method=RequestMethod.POST)
	public JSONObject memberIdCheck(@RequestParam("memberId") String memberId) {  // 자바의 String memberId = request.getParameter("memberId"); 대체

		logger.info("memberIdCheck들어옴");
		
		// MemberDao md = new MemberDao();  // Spring에서도 Java처럼 POJO 방식도 지원하지만 service를 사용할것임
		// int cnt = md.memberIdCheck(memberId);
		
		int cnt = memberService.memberIdCheck(memberId);

		// PrintWriter out = response.getWriter();  // 라이브러리 사용해서 json 파일 쉽게 만들것임
		// out.println("{\"cnt\":\""+cnt+"\"}");
		
		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);
		
		return obj;	
		
	}
	
	@RequestMapping(value="memberList.aws", method=RequestMethod.GET)
	public String memberList(Model model) {  // model은 자동으로 생성되는 객체이다

		logger.info("memberList들어옴");
		
		ArrayList<MemberVo> alist = memberService.memberSelectAll();
		model.addAttribute("alist", alist);
		
		return "WEB-INF/member/memberList";
		
	}

	
	@RequestMapping(value="memberLogout.aws", method=RequestMethod.GET)
	public String memberLogout(HttpSession session) {  // model은 자동으로 생성되는 객체이다

		logger.info("memberLogout들어옴");
		
		session.removeAttribute("midx");
		session.removeAttribute("membername");
		session.removeAttribute("memberId");
		session.invalidate();
		
		return "redirect:/";
		
	}
	
	
}
