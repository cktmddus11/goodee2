package controller;


import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartControllelr {
	@Autowired
	private ShopService service;

	@RequestMapping("cartAdd")
	public ModelAndView add(String id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id); // 선택한 상품 객체
		Cart cart = (Cart) session.getAttribute("CART");
		if (cart == null) {
			cart = new Cart(); // session 에 등록된 CART가 비어있으면
			session.setAttribute("CART", cart);
		}

		cart.push(new ItemSet(item, quantity));
		mav.addObject("message", item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart); // request에 저장되어있는 cart
		return mav;
	}

	/*
	 * index 파라미터값에 해당하는 값을 Cart의 ItemSetList 객체에서 제거. message에 ㅇㅇㅇ 상품을 장바구니에서 제거
	 * 메시지 view(cart.jsp)에 전달 1. session 에서 CART 객체를 조회 2. cart객체에서 itemSetList객체에서
	 * index에 해당하는 값을 제거 ItemSet itemSet = cart.getItemSetList().remove(index) 3.
	 * message, cart 를 view에 전달
	 */
	@RequestMapping("cartDelete") // url 파라미터 이름과 동일한 이름의 변수로 지정
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart) session.getAttribute("CART");
		ItemSet itemSet = null;
		try {
			itemSet = cart.getItemSetList().remove(index);
			// index를 Integer로 선언했을 시 래퍼클래스이므로 객체로 들어가게 된다.
			// Integer index => remove(Object obj) 인덱스에 있는 값을 지우는게
			// 아니라 그 객체를 지워라 라고 해서 boolean타입으로 리턴하게 됨
			mav.addObject("message", itemSet.getItem().getName() + " 상품을 장바구니에서 제거");
		} catch (Exception e) {
			mav.addObject("message", "장바구니 상품이 삭제되지 않았습니다.");
		}
		mav.addObject("cart", cart);
		return mav;
	}
	/* 장바구니에 상품이 없는 경우 CartEmptyException 
	 * "장바구니에 상품이 없습니다." 메시지를 
	 * alert 창으로 출력. /item/list.shop 페이지 이동 */
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart) session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size()==0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.", "../item/list.shop");
		}
		mav.addObject("cart", cart);
		return mav;
	}
	// 주문전 확인 
	// 반드시 로그인이 필요.
	// 장바구니에 상품이 존재해야함
	@RequestMapping("checkout")
	public String checkout(HttpSession session) { // 핵심로직
		return "cart/checkout";
	}
	/* 
	 * 주문 확정
	 * 1. 세션에서 CART, loginUser 정보 조회
	 * 2. sale, saleitem 테이블 데이터 추가
	 * 3. 장바구니에서 상품 제거
	 * 4. cart/end.jsp 페이지로 이동
	 * 
	 * */
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) { 
		// CartAspect AOP가 걸리는 핵심로직 대상
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart) session.getAttribute("CART");
		User loginUser = (User) session.getAttribute("loginUser");
		// sale : 주문 정보 내역
		Sale sale = service.checkend(loginUser, cart);
		
		long total = cart.getTotal(); // 주문상품의 총 금액 리턴
		session.removeAttribute("CART"); // 장바구니 내용 제거
		mav.addObject("sale", sale);
		mav.addObject("total", total);
		return mav;
	}
	

}
