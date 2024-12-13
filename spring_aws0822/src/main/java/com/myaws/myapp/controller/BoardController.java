package com.myaws.myapp.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;
import com.myaws.myapp.util.MediaUtils;
import com.myaws.myapp.util.UploadFileUtiles;
import com.myaws.myapp.util.UserIp;

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
	
	@Autowired(required=false)
	private UserIp userip;
	
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
			@RequestParam("attachfile") MultipartFile filename,  // input의 name 이름이 BoardVo에 있는 프로퍼티 이름과 동일하면 BoardVo로 값이 넘어가서 @RequestParam으로 받을 수 없으므로, input의 name을 filename이 아닌 attachfile으로 한다.
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
		String ip = userip.getUserIp(request);
		
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
		
		boardService.boardViewCntUpdate(bidx);  // 조회수 업데이트 하기
		BoardVo bv = boardService.boardSelectOne(bidx);  // 해당되는 bidx의 게시물 데이터 가져옴
		
		model.addAttribute("bv", bv);
		
		String path = "WEB-INF/board/boardContents";
		
		return path;			
	}
	
	// 파일을 보여줄 가상 경로에 파일 옮기기
	@RequestMapping(value="/displayFile.aws", method=RequestMethod.GET)  // 가상경로와 매핑이 되어야 함
	public ResponseEntity<byte[]> displayFile(
			@RequestParam("fileName") String fileName,
			@RequestParam(value="down", defaultValue="0") int down  // 다운 받을지, 화면에서 보여줄지 선택
			) {

		logger.info("displayFile들어옴");
		
		ResponseEntity<byte[]> entity = null;  // ResponseEntity : Collection처럼 객체를 담는다.
		InputStream in = null;
		
		try{
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);  // 파일의 확장자를 꺼냄
			MediaType mType = MediaUtils.getMediaType(formatName);  // MediaUtils에 확장자를 넣어서 파일의 타입을 알아냄
			
			HttpHeaders headers = new HttpHeaders();		
			 
			in = new FileInputStream(uploadPath+fileName);  // 파일 읽기
						
			if(mType != null){  // 파일의 타입이 JPG, GIF, PNG 중 하나일 경우
				
				if (down==1) {  // 다운을 받는다
					fileName = fileName.substring(fileName.indexOf("_")+1);
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.add("Content-Disposition", "attachment; filename=\""+
							new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");	
					
				}else {  // 다운받지 않고 타입을 저장
					headers.setContentType(mType);	
				}
				
			}else{  // 미리보기 하지 않고 다운을 받는다.
				
				fileName = fileName.substring(fileName.indexOf("_")+1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\""+
						new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");				
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);  // 생성자의 매개변수에 값을 받아서 생성
			
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
			
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
				
		return entity;
	}

	@ResponseBody
	@RequestMapping(value="boardRecom.aws")
	public JSONObject boardRecom(@RequestParam("bidx") int bidx) {
		
		logger.info("boardRecom들어옴");
		
		int recom = boardService.boardRecomUpdate(bidx);

		JSONObject js = new JSONObject();

		js.put("recom", recom);
		
		return js;
	}

	@RequestMapping(value="boardDelete.aws")
	public String boardDelete(@RequestParam("bidx") int bidx, Model model) {
		
		logger.info("boardDelete들어옴");
		
		model.addAttribute("bidx", bidx);

		return "WEB-INF/board/boardDelete";
		
	}
	
	@RequestMapping(value="boardDeleteAction.aws", method=RequestMethod.POST)
	public String boardDeleteAction(
			@RequestParam("bidx") int bidx,
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes rttr) {
		
		logger.info("boardDeleteAction들어옴");		
		
		int midx = Integer.parseInt(session.getAttribute("midx").toString());
		int value = boardService.boardDelete(bidx, midx, password);

		String path = "redirect:/board/boardList.aws";
		rttr.addFlashAttribute("msg", "글삭제 성공");
		if(value == 0) {
			path = "redirect:/board/boardDelete.aws?bidx=" + bidx;	
			rttr.addFlashAttribute("msg", "글삭제 실패");
		}
		
		return path;
	}
	
	@RequestMapping(value="boardModify.aws", method=RequestMethod.GET)
	public String boardModify(
			@RequestParam("bidx") int bidx,
			Model model) {
		
		logger.info("boardModify들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		return "WEB-INF/board/boardModify";
	}

	@RequestMapping(value="boardModifyAction.aws", method=RequestMethod.POST)
	public String boardModifyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,
			HttpServletRequest request,
			RedirectAttributes rttr,
			Model model,
			@RequestParam("isFileChange") String isFileChange
			) throws Exception {
		
		logger.info("boardModifyAction들어옴");
		
		BoardVo bvOrigin = boardService.boardSelectOne(bv.getBidx());

		String path = "";
		if(bvOrigin.getPassword().equals(bv.getPassword())) {
			int value = 0;

			String uploadedFileName = "";
			if(isFileChange.equals("true")) {
				// 파일첨부
				MultipartFile file = filename;
				
				if(!file.getOriginalFilename().equals("")) {			
					uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
				}
			} else {
				uploadedFileName = bvOrigin.getFilename();
			}
			
			String midx = request.getSession().getAttribute("midx").toString();
			int midx_int = Integer.parseInt(midx);
			String ip = userip.getUserIp(request);
			
			bv.setUploadedFilename(uploadedFileName);
			bv.setMidx(midx_int);
			bv.setIp(ip);
			
			// 파일 업로드하고 upadte를 하기 위한 service를 만든다
			value = boardService.boardUpdate(bv);
			
			if(value == 1) {
				rttr.addFlashAttribute("msg", "글수정 성공");
				path = "redirect:/board/boardContents.aws?bidx=" + bv.getBidx();
			} else {
				rttr.addFlashAttribute("msg", "입력이 잘못되었습니다.");
				path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
			}
			
		} else {
				
			// 비밀번호가 일치하지 않으면
			rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		}
			
		return path;
	}
	
	@RequestMapping(value="boardReply.aws", method=RequestMethod.GET)
	public String boardReply(
		@RequestParam("bidx") int bidx,
		Model model) {

		logger.info("boardReply들어옴");
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		
		model.addAttribute("bv", bv);
		
		return "WEB-INF/board/boardReply";
	
	}

	@RequestMapping(value="boardReplyAction.aws", method=RequestMethod.POST)
	public String boardReplyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile filename,  // input의 name 이름이 BoardVo에 있는 프로퍼티 이름과 동일하면 BoardVo로 값이 넘어가서 @RequestParam으로 받을 수 없으므로, input의 name을 filename이 아닌 attachfile으로 한다.
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		logger.info("boardReplyAction들어옴");
		
		// 파일첨부
		MultipartFile file = filename;
		String uploadedFileName = "";
		
		if(!file.getOriginalFilename().equals("")) {			
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
		}

		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx);
		String ip = userip.getUserIp(request);

		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);		
		
		int maxBidx = 0;
		maxBidx = boardService.boardReply(bv);

		String path = "";
		if (maxBidx != 0) {
			rttr.addFlashAttribute("msg", "답변 등록 성공");
			path = "redirect:/board/boardContents.aws?bidx=" + maxBidx;
			
		} else {
			rttr.addFlashAttribute("msg", "답변이 등록되지 않았습니다.");
			path ="redirect:/board/boardReply.aws?bidx=" + bv.getBidx();
			
		}
		
		return path;
	}
	
	
	// 여기부터는 리액트를 위한 코드
	@ResponseBody
	@PostMapping(value="boardWriteActionReact.aws")  // @RequestMapping(value="boardWriteActionReact.aws", method=RequestMethod.POST) 이것을 좀더 편리하게 @PostMapping으로 사용할 수 있다.
	public JSONObject boardWriteActionReact(@RequestBody BoardVo bv) {
		System.out.println("Midx: " + bv.getMidx());
		
		JSONObject js = new JSONObject();

		int value = boardService.boardInsert(bv);
		System.out.println("value: " + value);
				
		if(value == 2) {  // 성공
			js.put("result", "success");
		} else {
			js.put("result", "fail");
		}
		
		return js;
	}
}
