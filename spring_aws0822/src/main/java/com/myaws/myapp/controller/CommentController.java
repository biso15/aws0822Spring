package com.myaws.myapp.controller;

import java.util.ArrayList;


import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myaws.myapp.aop.SampleAdvice;
import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.service.CommentService;

@RestController  // @ResponseBody가 포함되어 있는 Controller
@RequestMapping(value="/comment")
public class CommentController {

	@Autowired(required=false)
	CommentService commentService;
	
	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	@RequestMapping(value="/{bidx}/commentList.aws")  // RestFul 방식
	public JSONObject commentList(@PathVariable("bidx") int bidx) {

		logger.info("commentList 들어옴");
		
		ArrayList<CommentVo> clist = commentService.commentSelectAll(bidx);
		
		JSONObject js = new JSONObject();

		js.put("clist", clist);
		
		return js;
	}
	
//	@RequestMapping(value="/commentWriteAction.aws")
//	public JSONObject commentWriteAction(
//			CommentVo cv,
//			HttpServletRequest request
//			) throws Exception {
//
//		logger.info("commentWriteAction 들어옴");
//		
//		// ip주소 추출
//		String ip = getUserIp(request);
//		cv.setCip(ip);
//		
//		// 2. DB 처리한다.
//		int value = commentService.commentInsert(cv);
//				
//		JSONObject js = new JSONObject();
//
//		js.put("value", value);
//		
//		return js;
//	}
	
	// ip주소 추출
//	public String getUserIp(HttpServletRequest request) throws Exception {
//		
//        String ip = null;
//        // HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        ip = request.getHeader("X-Forwarded-For");
//        
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("Proxy-Client-IP"); 
//        } 
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("WL-Proxy-Client-IP"); 
//        } 
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("HTTP_CLIENT_IP"); 
//        } 
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("X-Real-IP"); 
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("X-RealIP"); 
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getHeader("REMOTE_ADDR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
//            ip = request.getRemoteAddr(); 
//        }
//        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) { 
//        	InetAddress address = InetAddress.getLocalHost();
//        	ip = address.getHostAddress();
//        }
//		
//		return ip;
//	}
	
}
