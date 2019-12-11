package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xml.Article;
import xml.ReadArticleService;

public class Main2 {
	public static void main(String []args) {
		String[] config = {"di.xml", "aop2.xml"}; 
		ApplicationContext ctx = new ClassPathXmlApplicationContext(config); 
		// ctx : xml.ReadArticleServiceImpl 객체 저장
		ReadArticleService service = ctx.getBean("readArticleService", ReadArticleService.class);
		// ctx 는 ReadAricleService를 구현한 구현클래스이므로 형변환 가능
		try {  // 핵심로직 .get ~ 
			Article a1 = service.getArticleAndReadCnt(1);
		// 	System.out.println(a1); 로직 어드바이스 먼저 호출한후 a1에 동일한 리턴 값을 저장하게됨
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1 == a2 : "+(a1 == a2)); // 새로운 객체를 리턴하니까 다르다고 나옴
			service.getArticleAndReadCnt(0);
		}catch(Exception e) {
			System.out.println("[main] "+e.getMessage());
		}
	}
}
