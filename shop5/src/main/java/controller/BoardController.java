package controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardExcepetion;
import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	// requestMapping하면 get, post요청 다받는듯?
	@RequestMapping("list") // view : /WEB-INF/view/board/list.jsp
	public ModelAndView list(Integer pageNum, String searchtype, String searchcontent) {
		ModelAndView mav = new ModelAndView();
		//  검색조건
		if (pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if (searchtype != null && searchtype.trim().equals("")) { // 입력내용 띄어쓰기?
			searchtype = null;
		}
		if (searchcontent != null && searchcontent.trim().equals("")) {
			searchcontent = null;
		}
		if (searchtype == null || searchcontent == null) { // 두개다 있어야지 검색되게
			searchtype = null;
			searchcontent = null;
		}
		int limit = 10; // 페이지당 게시물 건수
		int listcount = service.boardcount(searchtype, searchcontent); // 전체 등록된 게시물 건수
		List<Board> boardlist = service.boardlist(pageNum, limit, searchtype, searchcontent);
		// 최대 페이지
		int maxpage = (int) ((double) listcount / limit + 0.95);
		// 보여지는 첫번째 페이지차승연
		int startpage = (int) ((pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		// 보여지는 마지막 페이지
		int endpage = startpage + 9;
		if (endpage > maxpage)
			endpage = maxpage;
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
	public ModelAndView write(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardWrite(board, request);
			mav.setViewName("redirect:list.shop");
		} catch (Exception e) {
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
	 * @GetMapping("write") // 이거를 아래껄로 public ModelAndView getBoard(){ ModelAndView
	 * mav = new ModelAndView(); mav.addObject("board", new Board()); return mav; }
	 */
	@GetMapping("*")
	public ModelAndView getBoard(Integer num, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(); // request : url에 따라 조회수같은거 하려고??
		Board board = null;
		// 파라미터가 없을때는 빈객체를 전달
		if (num == null) { // num이 int로 하니까 안되네 객체끼리 비교여서 그런듯 null은
			board = new Board();
		} else { // 파라미터가 있을 때는 게시물 한개 조회해오는 걸로
			board = service.getBoard(num, request);
		}
		mav.addObject("board", board);
		return mav;
	}

	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request, Model model) { // ckedit.jsp에
																														// 파라미터값
																														// 넘겨줄라고
		// upload : 업로드된 이미지 정보 저장. 이미지 파일
		String path = request.getServletContext().getRealPath("/") + "board/imgfile/";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		if (!upload.isEmpty()) {
			// file : 업로드 될 파일을 저장할 File 객체
			File file = new File(path, upload.getOriginalFilename());
			try {
				upload.transferTo(file); // 업로드 파일 생성
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String fileName = "/shop5/board/imgfile/" + upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);
		return "ckedit";
	}

	/*
	 * 1. 파라미터 값 Board 객체 저장. 유효성 검증 2. 답변글 데이터 추가 - grp에 해당하는 레코드 grpstep 값보다 큰
	 * grpstep의 값을 grpstep + 1 수정 - maxnum + 1 값으로 num값을 저장 - grplevel + 1 값으로
	 * grplevel값을 저장 - grpstep + 1 값으로 grpstep값을 저장 - 파라미터값으로 board 테이블에 insert하기 3.
	 * list.shop 페이지를 요청 redirect
	 * 
	 * 
	 */
	@PostMapping("reply")
	public ModelAndView replay(@Valid Board board, BindingResult bresult) throws BoardExcepetion {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) { // 유효성 검사에 걸리면 제목을 다시 디비에서 읽어서 뿌려줌
			Board dbBoard = service.getBoard(board.getNum());
			Map<String, Object> map = bresult.getModel();
			Board b = (Board) map.get("board");
			b.setSubject(dbBoard.getSubject()); // 알아서 제목부분에 뿌려주나봄..
			// mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list.shop");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoardExcepetion("답글 등록에 실패했습니다.", "reply.shop");
		}

		return mav;
	}

	/*
	 * 1. 파라미터 값 Board 객체 저장. 유효성 검증 2. 입력된 비밀번호와 db의 비밀번호를 비교. 비밀번호가 맞는 경우 3. 틀린경우
	 * '비밀번호가 틀립니다.', update.shop Get방식으로 호출 3. 수정 정보를 db에 변경 - 첨부파일 변경 : 첨부파일 업로드,
	 * fileurl 정보 수정 4. list.shop 페이지 요청
	 * 
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult, HttpServletRequest request)
			throws BoardExcepetion {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		String dbpass = service.getPass(board.getNum()); // selectOne메서드로 해도됨
		if (!board.getPass().equals(dbpass)) {
			throw new BoardException("비밀번호가 틀립니다.", "update.shop?num=" + board.getNum());
		}
		try {
			service.boardUpdate(board, request);
			mav.setViewName("redirect:list.shop");
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			throw new BoardExcepetion("게시글 수정 실패", "list.shop");
		}
		return mav;
	}

	/*
	 * 1. num, pass 파라미터 저장 2. db의 비밀번호와 입력된 비밀번호가 틀리면 error.login.password 코드 입력 3.
	 * db에서 해당 게시물 삭제 실패 삭제 실패 : 게시글 삭제 실패. delete.shop페이지로 이동 삭제 성공 : list.shop 페이지
	 * 이동
	 * 
	 */
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		String dbpass = service.getPass(board.getNum());
		try {
			if (!board.getPass().equals(dbpass)) {
				bresult.reject("error.login.password");
				return mav;
			}
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list.shop");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoardException("게시글 삭제 실패", "delete.shop?num=" + board.getNum());
		}
		return mav;
	}

}
