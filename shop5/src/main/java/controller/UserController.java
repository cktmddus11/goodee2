package controller;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;
import util.CipherUtil;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User()); // model : 뷰단으로 전달할 데이터 부분
		return null; // null : url로  보고 이동?
	}
	// 1. 비밀번호 : 해쉬화 db 에 저장
	// 2. email : id 해쉬값에서 키 결정. 
	// 				암호화 db에 저장
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.input.user"); // 글로벌 에러 
			return mav;
		}
		// useracount 테이블에 내용 추가. login.jsp로 이동
		try {
			// 암호화 
			String userid = CipherUtil.makehash(user.getUserid());
			user.setPassword(CipherUtil.makehash(user.getPassword()));
			user.setEmail(CipherUtil.encrypt(user.getEmail(), userid.substring(0, 16)));
			service.userInsert(user);
			//mav.setViewName("user/login");
			//mav.addObject("user",user); // 회원가입하고 아이디 안넘기려면
			// mav.addObject("user", new User()); 
			mav.setViewName("redirect:login.shop");
			// 또는 mav.setViewName("redirect:login.shop")으로 처리
			// rediredect로 하면 url바뀌면서 이동되는거 
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbUser = service.getUser(user.getUserid());
			// 사용자가 입력한 비밀번호를 hash값으로 변환해서 db에 있는 해쉬 암호
			// 값이랑 비교
			String pass = CipherUtil.makehash(user.getPassword());
			if(!dbUser.getPassword().equals(pass)){
				bresult.reject("error.login.password");
				return mav;
			}else {
				String userid = CipherUtil.makehash(dbUser.getUserid());
				String email = CipherUtil.decrypt(dbUser.getEmail(), userid.substring(0, 16)); // 복호화
				dbUser.setEmail(email);
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main.shop");
			}
		}catch(LoginException e) { // 
			// 아이디 없는 거 입력했을 때 에러 처리가 안됨	
			// EmptyResult 어쩌구 에러는 spring jdbc에서만 발생해서?
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return mav;
	}
	//@GetMapping("logout")
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	@RequestMapping("main") // UserLoginAspect 클래스에 해당하는 핵심로직
	public String checkmain(HttpSession session) { // 핵심 로직
		// UserLoginAspect 클래스 적용한 포인트 컷에 args가 session 객체를 
		// 가지고 있어야 핵심로직을 지정함 
		return "user/main";
	}
	// 로그인 했는지 확인하는 절차 필요
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id); // 사용자 정보 
		/* 
		 * 사용자가 주문한 모든 주문 내역 조회
		 * admin인 경우 모든 주문내역 조회
		 * */
		List<Sale> salelist = service.salelist(id);
		for(Sale sa : salelist) { 
			// 주문 아이디로 saleitem테이블에서 조회해서 주문한 물품들을 리스트로 만듦
			// 주문번호에 해당하는 주문상품 내역 조회
			List<SaleItem> saleitemlist = service.saleItemList(sa.getSaleid()); 
			for(SaleItem si : saleitemlist) { // 주문한 물품들 리스트에서 한개씩 뽑음
				// 주문 상품 한개에 해당하는 Item 조회 
				// db에 외래키 필요함
				Item item = service.getItem(si.getItemid()); // 아이템번호로 아이템정보 가져옴
				si.setItem(item); 
			}
			try {
				User saleUser = service.getUser(sa.getUserid());
				sa.setUser(saleUser);
			}
			catch(LoginException e) {}
			sa.setItemList(saleitemlist); // 구매한 물품들의 정보객체들을 리스트로 만듦
		}
		String userid;
		try {
			userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.decrypt(user.getEmail(), userid.substring(0, 16)); // 복호화
			user.setEmail(email);
		}catch(Exception e) {
			e.printStackTrace();
		}
		mav.addObject(user);
		mav.addObject("salelist", salelist);
		return mav;
	}
	//  여러곳에서 아래 메서드가 호출되지만 
	// 매개변수가 다를 때도 있으니까 
	// * 는 모든 요청을 받게 되니까
	// 여러개를 지정해줌
	@GetMapping(value= {"update", "delete"})
	public ModelAndView checkview(String id, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		String userid = CipherUtil.makehash(user.getUserid());
		String email = CipherUtil.decrypt(user.getEmail(), userid.substring(0, 16)); // 복호화
		user.setEmail(email);
		mav.addObject("user", user);
		return mav;
	}
	@PostMapping("update")
	public ModelAndView checkupdate(@Valid User user, BindingResult bresult, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
	
		// 비밀번호 검증 : 입력된 비밀번호와 db에 저장된 비밀번호를 비교
		// 일치 : update 
		// 불일치 : 메시지를 유효성 출력으로 화면에 출력
		// error.login.password
		User loginUser = (User) session.getAttribute("loginUser");
		String pass = CipherUtil.makehash(user.getPassword());
		if(!loginUser.getPassword().equals(pass)){
			// 입력한 비밀번호와     세션에 저장되어있는 로그인
			//                             정보에 비밀번호(db에 저장된 비밀번호)       	
			bresult.reject("error.login.password");
			return mav;
		}
		try {
			String userid = CipherUtil.makehash(user.getUserid());// 키
			String email = CipherUtil.encrypt(user.getEmail(), userid); // 암호화
			user.setEmail(email);
			service.userUpdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			if(!loginUser.getUserid().equals("admin")) {
				// 로그인한 사람이 admin이 아니면 = 사용자이면
				// 수정된 내용이 들어있는 User객체를 
				// 세션에 다시 올려줌
				session.setAttribute("loginUser", user);
	
			}
		}catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		
		return mav;
	}
												// hidden값 userid, 입력한 비밀번호
	 //                                            알아서 User객체로 들어가게
	// 해도 되고 그냥 변수 두개로 해도됨
	@PostMapping("delete") 
	public ModelAndView checkdelete(String userid, String password, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User) session.getAttribute("loginUser");
		String pass = CipherUtil.makehash(password);
		if(!loginUser.getPassword().equals(pass)) {
			throw new LoginException("회원 탈퇴 비밀번호가 틀립니다.", 
					"delete.shop?id="+userid);
		}
		try {
			service.userDelete(userid);
			if(loginUser.getUserid().equals("admin")) { 
				// 관리자이면 탈퇴 후 다시 리스트 페이지로 
				mav.setViewName("redirect:/admin/list.shop");
			}else { // 일반 사용자면 탈퇴후 로그인 페이지로 
				session.invalidate();
				mav.addObject("msg", userid+"님 회원 탈퇴되었습니다.");
				mav.addObject("url", "login.shop");
				mav.setViewName("alert");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LoginException("회원 탈퇴중 오류 발생 "
					+ "전산부 연락 요망", "delete.shop?id="+userid);
		}
		return mav;
	}
	// produces="text/plain ~ : 순수 문자? -> 그냥 텍스트로 인식해서
	// <script>그대로 나옴
	// text/html로 하면 html parse를 해서 html태그로 인식하게 됨
	@PostMapping(value="password", produces="text/html;charset=UTF-8")
	@ResponseBody //  리턴값을 string으로 하는데바로 뷰단에 할거를 쓸수 있음
	// return 데이터 자체를 view의 내용으로 전달
	public String checkpassword(@RequestParam HashMap<String, String> param, HttpSession session) {
		// RequestParam으로 자동으로 파라미터 값 받음 
		User login = (User)session.getAttribute("loginUser"); 
		String hashpass = null;
		try {
			// param.get("pass") : request.getParameter("pass")
			hashpass = CipherUtil.makehash(param.get("pass"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(!login.getPassword().equals(hashpass)) {
			throw new LoginException("비밀번호 오류", "password.shop");
		}
		String chgpass = null;
		try {
			chgpass = CipherUtil.makehash(param.get("chgpass"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		String pass;
		try {
			pass = CipherUtil.makehash(chgpass);
			service.userPasswordUpdate(login.getUserid(), pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		login.setPassword(chgpass);
		return "<script>alert('비밀번호가 변경되었습니다.')\n"
				+"self.close()\n"
				+"</script>"; // @ResponseBody
		// view jsp를 만들면 상단에 utf-8로 인코딩을 해주는데
		// 얘는 문자열로 그냥 해버리니까 Pproduces="text/html~ 이런거 해준거임
	}
}
