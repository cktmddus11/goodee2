package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;
// index.shop 요청시 호출되는 클래스
public class IndexController {
	private ShopService shopService;
	//  itemDao 객체를 저장하고 있는 ShopService 객체 주입
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	@RequestMapping // index.shop 요청시 호출되는 메서드
	public ModelAndView itemList() { // 알아서 스프링이 호출해줌
		// ModelAndView : Model : view에 전달될 데이터 저장하는 객체
		//                         View : view 설정 객체 
		
		List<Item> itemList = shopService.getItemList(); //  List<Item> 리턴 받음
		// itemList : item테이블의 모든 컬럼, 모든 레코드 정보를 
		//               Item 객체의 List  객체로 저장 
		ModelAndView mav = new ModelAndView("index"); // view 설정
		mav.addObject("itemList", itemList); // 데이터 설정
		return mav; // view 의 이름 전달될 데이터를 모두 가지고 리턴시킴
		// 리턴값은  dispatcherServlet 가 가지고 있음 
	}
}
