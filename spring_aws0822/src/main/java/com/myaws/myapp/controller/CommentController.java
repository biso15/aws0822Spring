package com.myaws.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myaws.myapp.aop.SampleAdvice;
import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.service.CommentService;
import com.myaws.myapp.util.UserIp;

@RestController  // @ResponseBody가 포함되어 있는 Controller
@RequestMapping(value="/comment")
public class CommentController {

	@Autowired(required=false)
	CommentService commentService;

	@Autowired(required=false)
	private UserIp userip;
	
	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	@RequestMapping(value="/{bidx}/commentList.aws")  // RestFul 방식
	public JSONObject commentList(@PathVariable("bidx") int bidx) {

		logger.info("commentList 들어옴");
		
		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx);
		
		JSONObject js = new JSONObject();

		js.put("clist", clist);
		
		return js;
	}
	
	@RequestMapping(value="/commentWriteAction.aws", method=RequestMethod.POST)
	public JSONObject commentWriteAction(
			CommentVo cv,
			HttpServletRequest request
			) throws Exception {

		logger.info("commentWriteAction 들어옴");
		
		cv.setCip(userip.getUserIp(request));
		
		int value = commentService.commentInsert(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
	@RequestMapping(value="/{cidx}/commentDeleteAction.aws")
	public JSONObject commentDeleteAction(
			@PathVariable("cidx") int cidx,
			HttpServletRequest request,
			CommentVo cv) throws Exception {

		logger.info("commentDeleteAction 들어옴");		
		
		int midx = Integer.parseInt(request.getAttribute("midx").toString());
		cv.setMidx(midx);
		cv.setCidx(cidx);
		cv.setCip(userip.getUserIp(request));
		
		int value = commentService.commentDelete(cv);
		
		JSONObject js = new JSONObject();
		js.put("value", value);
		
		return js;
	}
	
}
