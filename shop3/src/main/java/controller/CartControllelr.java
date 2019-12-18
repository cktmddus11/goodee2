package controller;

import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;

@Controller
@RequestMapping("cart")
public class CartControllelr {
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(String id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id); // 선택한 상품 객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {
			cart = new Cart(); // session 에 등록된 CART가 비어있으면 
			session.setAttribute("CART", cart);
		}
		
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message", item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart", cart); // request에 저장되어있는 cart
		return mav;
	}
}
