package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

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
		}
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbUser = service.getUser(user.getUserid());
			if(!dbUser.getPassword().equals(user.getPassword())){
				bresult.reject("error.login.password");
				return mav;
			}else {
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main.shop");
			}
		}catch(EmptyResultDataAccessException e) {
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
	@RequestMapping("main")
	public String checkmain(HttpSession session) { // 핵심 로직
		return "user/main";
	}
	
}
