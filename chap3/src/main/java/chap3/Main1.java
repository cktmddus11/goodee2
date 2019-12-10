package chap3;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		// 여러 클래스가 들어갈 컨테이너 클래스?
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:annotation.xml"); 
		Executor exec = ctx.getBean("executor", Executor.class);
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
		// exec 객체부분이 달라지는 것이 중요
		
	}
}
