package com.myaws.myapp.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myaws.myapp.api.MailApi;

@Controller
@RequestMapping(value="/email")
public class ApiController {
	
	@Autowired(required=false)
	private MailApi mailApi;
	
	@RequestMapping(value="/emailWrite.aws", method=RequestMethod.GET)
	public String emailWrite() {
		
		String path = "WEB-INF/email/emailWrite";
		return path;
	}

	@RequestMapping(value="/emailWriteAction.aws", method=RequestMethod.POST)
	public String emailWriteAction(
			@RequestParam("subject") String subject,
			@RequestParam("contents") String contents,
			@RequestParam("senderEmail") String senderEmail,
			@RequestParam("receiverEmail") String receiverEmail,
			HttpServletRequest request,
			HttpServletResponse response
			) {
		
		System.out.println("emailWriteAction µé¾î¿È");
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("subject", subject);
		hm.put("contents", contents);
		hm.put("senderEmail", senderEmail);
		hm.put("receiverEmail", receiverEmail);
		
		mailApi.sendEmail(request, response, hm);
		
		String path = "redirect:/";
		return path;
	}
}
