package chap1;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		// 여러 클래스가 들어갈 컨테이너 클래스?
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");
	// xml에 저장한 빈클래스를 읽어옴
		
		Greeter g =  ctx.getBean("greeter", Greeter.class);
		//            ctx로 부터 greeter라는 클래스를 가져와
		System.out.println(g.greet("스프링"));  // greet 클래스의 greet메서드를 사용해
		
		Message m = ctx.getBean("message", Message.class);
		//m.sayHello("차승연");
		m.sayHello("chaseongyeon");
		
		Greeter g2 = ctx.getBean("greeter", Greeter.class); // 이름이 동일한 greeter을 가져오니까
		if(g == g2) { // 동일한 객체
			System.out.println("g 객체와 g2객체는 같은 객체다.");
		} // 컨테이너에 이미 만들어진 객체를 그냥 가져와서 씀
		
		
	}
}
