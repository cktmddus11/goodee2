package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

public class DetailController {
	private ShopService shopService;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@RequestMapping      // detail.shop?id=1
	public ModelAndView detail(Integer id) { // 매개변수이름이 파라미터 이름과 동일해야함
		                                 // 파라미터값 매개변수로만 하면 됨 request.getPa이런거 필요없
		Item item = shopService.getItemById(id);
		ModelAndView mav = new ModelAndView(); // "detail" 기본 설정
		mav.addObject("item", item);
		return mav;
	}
	
	
}
