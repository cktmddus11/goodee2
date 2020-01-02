package controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserEntryController {
	private ShopService shopService;
	private UserValidator uservalidator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setUserValidator(UserValidator uservalidator) {
		this.uservalidator = uservalidator;
	}
	@ModelAttribute //유저객체에 값을 집어넣어서 뷰단에 보여지게하는 부분
	public User getUser() {
		//User user = new User();
		//user.setUsername("홍길동");
		//return user;
		return new User();
	}
	@RequestMapping(method=RequestMethod.GET)
	public String userEntryForm() {
		return "userEntry"; // view만 설정
		// return null;
	}
	// 등록버튼을 누르기전 그냥 userEntry.jsp로드되면 위에꺼 
	// 버튼 누르면 아래꺼
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView userEntry(User user, BindingResult bindResult) { // 프로퍼티에 맞게 알아서 파라미터값을
		// user : 파라미터값(입력된 값)을 저장하고 있는 객체
		ModelAndView mav = new ModelAndView();
		uservalidator.validate(user, bindResult);
		if(bindResult.hasErrors()) { // 에러가 있으면
			mav.getModel().putAll(bindResult.getModel());
			return mav; // userEntry 로 가서 
		}
		try {
			shopService.insertUser(user);
			mav.addObject("user", user); // 입력된 파라미터값 저장한 객체
		}catch(DataIntegrityViolationException e) { // 키가 중복되었을 때 발생하는 에러 처리
			// spring jsbc에서 발생되는 예외
			e.printStackTrace();
			bindResult.reject("error.duplicate.user"); // 글로벌 에러
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess"); // 등록이되면 이쪽 view로 이동
		return mav;
	}
	@InitBinder // 파라미터 값 형변환
	// User 객체에서 date타입 객체들어오면 ??
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/* 
		 * Date.class : 형변환 대상이 되는 자료형
		 * format : 형식 지정
		 * true/false : 비입력 허용/비입력불허
		 * => false : 필수입력, true : 선택입력
		 * */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, false));
	}
	

	
}
