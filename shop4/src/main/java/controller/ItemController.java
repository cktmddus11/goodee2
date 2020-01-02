package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller // @Component + Controller기능 => action 클래스 같은거? 
@RequestMapping("item")  // item/xxx.shop 
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") // item/list.shop요청
	public ModelAndView list() {
		// itemList : item 테이블의 모든 레코드와 모든 컬럼정보를 저장
		List<Item> itemList = service.getItemList(); 
		ModelAndView mav = new ModelAndView(); // 뷰 : item/list 
		mav.addObject("itemList", itemList);
		return mav; // /WEB-INF/view/item/list.jsp
	}
	
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item()); // add.jsp에 modelAttribute= "item" 때문에 해주는거
		return mav;
	}
	@RequestMapping("register") // @Valid 유효성 검사하라고 붙이는거
	// 저거 붙이면 알아서 유효성 하는거(bindResult)에 넣는과정 이 생략됨 ??
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request ) { 
		//                                        입력한 내용    유효성 검사때문에   파일 업로드 정보도 request객체에 있음 
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item, request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	@PostMapping("update") //    @Valid :  Item에 들어온 값들의 유효성 검사를 함 
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item, request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	@GetMapping("delete")
	public ModelAndView delete(int id) {
		ModelAndView mav = new ModelAndView();
		service.deleteItem(id);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
	
	//@RequestMapping("detail")
	// 아래 메서드 이름은 바꿔도 됨. 
	// RequestMapping으로 헀으니까
	// dispactureServlet? 가 *.shop 을 알아서 호출
	@GetMapping("*") // 그 외 요청 정보 
	// get 방식 요청 처리
	public ModelAndView detail(String  id) { // 파라미터 자동으로 받아옴
		ModelAndView mav = new ModelAndView(); //  뷰에 이름이 null이면 url로 들어가게됨
		Item item = service.detailView(id);
		mav.addObject("item", item);
		return mav;
	}
	
	
	
	
}
