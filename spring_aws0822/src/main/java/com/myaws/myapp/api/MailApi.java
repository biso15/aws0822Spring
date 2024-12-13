package com.myaws.myapp.api;

import java.util.HashMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.myaws.myapp.util.MailHandler;


@Service
public class MailApi {
	
	@Autowired(required=false)
	private JavaMailSender mailSender;
	
	public void sendEmail(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> hm) {

		String subject = hm.get("subject").toString();
		String contents = hm.get("contents").toString();
		String senderEmail = hm.get("senderEmail").toString();
		String receiverEmail = hm.get("receiverEmail").toString();
		
		try {
			MailHandler mailHandler = new MailHandler(mailSender);
			mailHandler.setSubject(subject);
			mailHandler.setText(contents, true);
			mailHandler.setFrom(senderEmail);
			mailHandler.setTo(receiverEmail);
			mailHandler.send();
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}
