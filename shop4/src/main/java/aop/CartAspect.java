package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;

@Component
@Aspect
@Order(2)
public class CartAspect {
	@Around("execution(* controller.Cart*.check*(..))")
	public Object cartCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		HttpSession session = (HttpSession) joinPoint.getArgs()[0];
		//  AOP가 적용된 메서드(핵심로직)에 전달된 인자를 배열로 얻어냄 
		Cart cart = (Cart) session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 주문 상품이 없습니다.", "../item/list.shop");
		}
		if(session.getAttribute("loginUser") == null) {
			throw new LoginException("로그인 한 고객만 상품 주문이 가능합니다.", "../user/login.shop");
		}
		Object ret = joinPoint.proceed(); //핵심로직으로 이동 
		return ret;
	}
}
