package util;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import logic.User;

public class UserValidator implements Validator {
	@Override
	public boolean supports(Class<?> cls) {
		//  유효성 검증 대상이 되는 객체 여부 확인 
		return User.class.isAssignableFrom(cls);
	}
	@Override
	public void validate(Object obj, Errors errors) {
		//                              user, bindResult 객체 들어옴
		User user = (User)obj;
		String group = "error.required";
		if(user.getUserid() == null || user.getUserid().length()==0) {
			errors.rejectValue("userid", group);
			//      컬럼 하나하나 오류를 알려줌?
		}
		if(user.getPassword() == null || user.getPassword().length()==0) {
			errors.rejectValue("password", group);
		}
		if(user.getUsername() == null || user.getUsername().length()==0) {
			errors.rejectValue("username", group);
		}
		if(user.getPhoneno() == null || user.getPhoneno().length()==0) {
			errors.rejectValue("phoneno", group);
		}
		if(user.getAddress() == null || user.getAddress().length()==0) {
			errors.rejectValue("address", group);
		}
		if(user.getEmail() == null || user.getEmail().length()==0) {
			errors.rejectValue("email", group);
		}

		if(errors.hasErrors()) { // 한개라도 오류가 발생하면 
			errors.reject("error.input.user"); // 글로벌 오류 
		}
		//  errors 객체에 에러가 있는 부분 저장
	}

}
