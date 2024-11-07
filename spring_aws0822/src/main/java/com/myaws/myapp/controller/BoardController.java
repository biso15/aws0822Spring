package com.myaws.myapp.controller;

import java.net.InetAddress;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;
import com.myaws.myapp.util.UploadFileUtiles;

@Controller  // Controller 객체를 만들어줘
@RequestMapping(value="/board/")  // 중복된 주소는 위쪽에서 한번에 처리
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)  // @Autowired : 타입이 같은 객체를 찾아서 주입. required=false : 만약 주입 못받을 경우, null로 지정
	private BoardService boardService;
	
	@Autowired(required=false)
	private PageMaker pm;  // @Component 어노테이션 사용 안할 경우, private PageMaker pm = new PageMaker(); 이렇게 사용하면 되는듯
	
	@Resource(name = "uploadPath")  // @Resource : 이름이 uploadpath인 객체를 찾아서 주입. Bean에 작성한 id를 찾는다.
	private String uploadPath;
	
	@RequestMapping(value="boardList.aws")
	public String boardList(
			SearchCriteria scri,  // SearchCriteria는 자동으로 요청 파라미터에서 바인딩됨
			Model model
			) {
		
		logger.info("boardList들어옴");		
		
		pm.setScri(scri);  // <-- PageMaker에 SearhCriteria 담아서 가지고 다닌다
		
		// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
		int cnt = boardService.boardTotalCount(scri);
		
		pm.setTotalCount(cnt);  // <-- PageMaker에 전체게시물수를 담아서 페이지 계산
		
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri);
		model.addAttribute("blist", blist);	 // 화면까지 가지고 가기위해 model 객체에 담는다(redirect 사용 안하므로 Modele을 사용)
		model.addAttribute("pm", pm);  // forward 방식으로 넘기기 때문에 공유가 가능하다
		
		return "WEB-INF/board/boardList";
	}
	
	@RequestMapping(value="boardWrite.aws")
	public String boardWrite() {
		
		logger.info("boardWrite들어옴");
		
		return "WEB-INF/board/boardWrite";
	}

	@RequestMapping(value="boardWriteAction.aws", method=RequestMethod.POST)
	public String boardWriteAction(
			BoardVo bv,
			@RequestParam("filename") MultipartFile filename,  // filename은 RequestParam으로 받아야해서 BoardVo에 있는 filename을 삭제
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		logger.info("boardWriteAction들어옴");
		
		// 파일첨부
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {			
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());  // getOriginalFilename 대.소문자 주의
		}
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession은 HttpServletRequest 안에 있음
		int midx_int = Integer.parseInt(midx);  // session.getAttribute()가 String 타입일 가능성이 더 높으므로 (int)session.getAttribute("midx")로 바로 형변환 하지 않고 String으로 변환 후 int로 변환한다.
		String ip = getUserIp(request);
		
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);
		
		String path = "";
		if(value == 2) {
			rttr.addFlashAttribute("msg", "글쓰기 성공");
			path = "redirect:/board/boardList.aws";  // redirect는 새로운 주소로 보내기 때문에 .aws도 붙여줘야 함. 주소 앞에 request.getContextPath()도 붙여야 하지만 서버에서 지원해주므로 생략
		} else {
			rttr.addFlashAttribute("msg", "입력이 잘못되었습니다.");
			path = "redirect:/board/boardWrite.aws";
		}
		return path;
	}
	
	@RequestMapping(value="boardContents.aws")
	public String boardContents(@RequestParam("bidx") int bidx,	Model model) {
		
		logger.info("boardContents들어옴");
				
		BoardVo bv = boardService.boardSelectOne(bidx);  // 해당되는 bidx의 게시물 데이터 가져옴
		int value = boardService.boardViewCntUpdate(bv);  // 조회수 +1 업데이트 하기
		
		String path = "";
		if (value == 1) {  // 조회수 업데이트 성공			
			model.addAttribute("bv", boardService.boardSelectOne(bidx));
			path ="WEB-INF/board/boardContents";
			
		} else {  // 조회수 업데이트 실패
			path ="WEB-INF/board/boardList.jsp";
		}
		
		return path;			
	}

	// ip주소 추출
	public String getUserIp(HttpServletRequest request) throws Exception {
		
        String ip = null;
        // HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) { 
        	InetAddress address = InetAddress.getLocalHost();
        	ip = address.getHostAddress();
        }
		
		return ip;
	}
}
