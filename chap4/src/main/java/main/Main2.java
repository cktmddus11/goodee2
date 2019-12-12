package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xml.Article;
import xml.Member;
import xml.MemberService;
import xml.ReadArticleService;
import xml.UpdateInfo;

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
		System.out.println("\n UpdateMemberInfoTrace Aspect 연습");
		MemberService msvc = ctx.getBean("memberService", MemberService.class);
		msvc.regist(new Member());
		msvc.update("hong",  new UpdateInfo());
		msvc.delete("hong2", "test");
		
		System.out.println("\n main.MemberService 메서드 호출");
		main.MemberService msvc2 = new main.MemberService();
				//ctx.getBean("memberService2", main.MemberService.class);
		msvc2.regist(new Member());
		msvc2.update("hong",  new UpdateInfo());
		msvc2.delete("hong2",  "test");
	}
}
