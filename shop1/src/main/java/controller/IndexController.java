package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

public class IndexController {
	private ShopService shopService;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	@RequestMapping // 요청시 호출되는 메서드
	public ModelAndView itemList() {
		List<Item> itemList = shopService.getItemList();
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("itemList", itemList);
		return mav;
	}
}
