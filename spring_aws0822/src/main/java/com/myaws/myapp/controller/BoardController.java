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

@Controller  // Controller ��ü�� �������
@RequestMapping(value="/board/")  // �ߺ��� �ּҴ� ���ʿ��� �ѹ��� ó��
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)  // @Autowired : Ÿ���� ���� ��ü�� ã�Ƽ� ����. required=false : ���� ���� ������ ���, null�� ����
	private BoardService boardService;
	
	@Autowired(required=false)
	private PageMaker pm;  // @Component ������̼� ��� ���� ���, private PageMaker pm = new PageMaker(); �̷��� ����ϸ� �Ǵµ�
	
	@Resource(name = "uploadPath")  // @Resource : �̸��� uploadpath�� ��ü�� ã�Ƽ� ����. Bean�� �ۼ��� id�� ã�´�.
	private String uploadPath;
	
	@RequestMapping(value="boardList.aws")
	public String boardList(
			SearchCriteria scri,  // SearchCriteria�� �ڵ����� ��û �Ķ���Ϳ��� ���ε���
			Model model
			) {
		
		logger.info("boardList����");		
		
		pm.setScri(scri);  // <-- PageMaker�� SearhCriteria ��Ƽ� ������ �ٴѴ�
		
		// ����¡ ó���ϱ� ���� ��ü ������ ���� ��������
		int cnt = boardService.boardTotalCount(scri);
		
		pm.setTotalCount(cnt);  // <-- PageMaker�� ��ü�Խù����� ��Ƽ� ������ ���
		
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri);
		model.addAttribute("blist", blist);	 // ȭ����� ������ �������� model ��ü�� ��´�(redirect ��� ���ϹǷ� Modele�� ���)
		model.addAttribute("pm", pm);  // forward ������� �ѱ�� ������ ������ �����ϴ�
		
		return "WEB-INF/board/boardList";
	}
	
	@RequestMapping(value="boardWrite.aws")
	public String boardWrite() {
		
		logger.info("boardWrite����");
		
		return "WEB-INF/board/boardWrite";
	}

	@RequestMapping(value="boardWriteAction.aws", method=RequestMethod.POST)
	public String boardWriteAction(
			BoardVo bv,
			@RequestParam("filename") MultipartFile filename,  // filename�� RequestParam���� �޾ƾ��ؼ� BoardVo�� �ִ� filename�� ����
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		logger.info("boardWriteAction����");
		
		// ����÷��
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {			
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());  // getOriginalFilename ��.�ҹ��� ����
		}
		
		String midx = request.getSession().getAttribute("midx").toString();  // HttpSession�� HttpServletRequest �ȿ� ����
		int midx_int = Integer.parseInt(midx);  // session.getAttribute()�� String Ÿ���� ���ɼ��� �� �����Ƿ� (int)session.getAttribute("midx")�� �ٷ� ����ȯ ���� �ʰ� String���� ��ȯ �� int�� ��ȯ�Ѵ�.
		String ip = getUserIp(request);
		
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);
		
		String path = "";
		if(value == 2) {
			rttr.addFlashAttribute("msg", "�۾��� ����");
			path = "redirect:/board/boardList.aws";  // redirect�� ���ο� �ּҷ� ������ ������ .aws�� �ٿ���� ��. �ּ� �տ� request.getContextPath()�� �ٿ��� ������ �������� �������ֹǷ� ����
		} else {
			rttr.addFlashAttribute("msg", "�Է��� �߸��Ǿ����ϴ�.");
			path = "redirect:/board/boardWrite.aws";
		}
		return path;
	}
	
	@RequestMapping(value="boardContents.aws")
	public String boardContents(@RequestParam("bidx") int bidx,	Model model) {
		
		logger.info("boardContents����");
				
		BoardVo bv = boardService.boardSelectOne(bidx);  // �ش�Ǵ� bidx�� �Խù� ������ ������
		int value = boardService.boardViewCntUpdate(bv);  // ��ȸ�� +1 ������Ʈ �ϱ�
		
		String path = "";
		if (value == 1) {  // ��ȸ�� ������Ʈ ����			
			model.addAttribute("bv", boardService.boardSelectOne(bidx));
			path ="WEB-INF/board/boardContents";
			
		} else {  // ��ȸ�� ������Ʈ ����
			path ="WEB-INF/board/boardList.jsp";
		}
		
		return path;			
	}

	// ip�ּ� ����
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
