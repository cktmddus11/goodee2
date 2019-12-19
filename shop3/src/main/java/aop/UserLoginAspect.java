package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
@Order(1)
public class UserLoginAspect {
	//                         접근제어자  패키지.User로 시작하는 모든 클래스.check로 시작하는 메서드 
	//                         매개변수 상관없고 그리고 앞 매개변수 상관없고 뒤 메개변수 session 있어야함
	@Around("execution(* controller.User*.check*(..)) && args(..,session)") 
	// 핵심 로직에서 중간에 끼어들어가는 시점 지정
	// advice : 핵심로직 전, 후
	// pointcut : 핵심로직 설정
	// 로그인 검증 처리시 check 메서드, session 객체를 넣어서 메서드를 만들어 주면 됨
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			throw new LoginException("로그인 후 거래하세요", "login.shop");
			// 로그아웃 상태일때 main을 접근하면 강제로 에러를 발생시킴 
			//joinPoint부분으로 갈수 없음 
		}
		Object ret = joinPoint.proceed(); 
		return ret;
		
	}
	/* around advice는 joinPoint.proceed() 전 후로 before, AfterThrowing 
	 * 로 나뉨 */
}
