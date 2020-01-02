package controller;


import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

public class LoginController {
	private ShopService shopService;
	private Validator validator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	@GetMapping // 4.0 이후 버전에서 가능
	// @RequestMapping(method=RequestMethod.GET)
	public String loginForm(Model model) {
		model.addAttribute(new User()); // model : 뷰단으로 전달할 데이터 부분
		return "login";
	}
	@PostMapping
	public ModelAndView login //                              session 객체 전달 
		(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		validator.validate(user,  bresult);
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbuser = shopService.getUser(user.getUserid());
			if(user.getPassword().equals(dbuser.getPassword())) { // 비밀번호가 맞으면
				session.setAttribute("loginUser", dbuser); 
			}else { // 비밀번호가 틀리면 
				bresult.reject("error.login.password");
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
		}catch(EmptyResultDataAccessException e) { // 아이디가 없을때 
			// EmptyResultDataAccessException : db에서 조회된 레코드가 없는 경우
			bresult.reject("error.login.id"); // 해당 아이디 없음 
			mav.getModel().putAll(bresult.getModel()); 
			return mav;
		}
		mav.setViewName("loginSuccess");
		return mav;
		
	}
}
