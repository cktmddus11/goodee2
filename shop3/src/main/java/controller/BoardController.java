package controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") // view : /WEB-INF/view/board/list.jsp
	public ModelAndView list(Integer pageNum) {
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		int limit = 10; // 페이지당 게시물 건수
		int listcount = service.boardcount(); // 전체 등록된 게시물 건수
		List<Board> boardlist = service.boardlist(pageNum, limit);
		// 최대 페이지 
		int maxpage = (int)((double) listcount / limit + 0.95);
		// 보여지는 첫번째 페이지
		int startpage = (int) ((pageNum/10.0 + 0.9) - 1) * 10 +1;
		// 보여지는 마지막 페이지
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage;
		// 화면에 출력되는 게시물 순서
		int boardno = listcount - (pageNum - 1) * limit;
		
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		return mav; 
	}

	@PostMapping("write")
	public ModelAndView write(@Valid Board board, BindingResult bresult, HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardWrite(board, request);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 등록에 실패했습니다.", "write.shop");
		}
		return mav;
	}

	/*
	 * // detail, update, delete, replay 할 때 다 사용
	 * 
	 * @GetMapping("*") public ModelAndView detail(int num) { // db에서 num에 해당하는 게시물
	 * 읽어서 Board객체에 저장 ModelAndView mav = new ModelAndView(); Board board =
	 * service.getboard(num); mav.addObject("board", board); return mav; }
	 * 
	 * @GetMapping("write") // 이거를 아래껄로 
	public ModelAndView getBoard(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("board", new Board());
		return mav;
	}
	 */
	@GetMapping("*")
	public ModelAndView getBoard(Integer num, HttpServletRequest request){
		ModelAndView mav = new ModelAndView(); // request : url에 따라 조회수같은거 하려고?? 
		Board board = null;
		// 파라미터가 없을때는 빈객체를 전달
		if(num == null) { // num이 int로 하니까 안되네 객체끼리 비교여서 그런듯 null은
			board = new Board();
		}else { // 파라미터가 있을 때는 게시물 한개 조회해오는 걸로
			board = service.getBoard(num, request); 
		}
		mav.addObject("board", board);
		return mav;
	}
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request,
			Model model) {                                   // ckedit.jsp에 파라미터값 넘겨줄라고
		// upload : 업로드된 이미지 정보 저장. 이미지 파일 
		String path = request.getServletContext().getRealPath("/")+"board/imgfile/";
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		if(!upload.isEmpty()) {
			// file : 업로드 될 파일을 저장할 File 객체
			File file = new File(path, upload.getOriginalFilename());
			try {
				upload.transferTo(file); // 업로드 파일 생성
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		String fileName = "/shop3/board/imgfile/"+upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);
		return "ckedit";
	}
}
