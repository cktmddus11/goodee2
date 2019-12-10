package chap2;


import java.util.Arrays;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		// 여러 클래스가 들어갈 컨테이너 클래스?
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml"); 
		//xml에 쓴것들을 다 객체로 만듦 - 객체를 만들때 객체값들을 
		// 다 주입시킴
		Project pro = ctx.getBean("project", Project.class);
		pro.build(); 
		//pro = new Project(); //빈객체를 생성한거기 때문에 
		//pro.build(); // 에러  nullpointer
		
		BuildRunner br = ctx.getBean("runner", BuildRunner.class);
		br.build(Arrays.asList("src/", "srcResource/"), "/bin2");
		br = new BuildRunner();
		br.build(Arrays.asList("src/", "srcResource/"), "/bin2");
		
		
		//
		WriteImp1 wi = ctx.getBean("write", WriteImp1.class);
		wi.write();
		
	}
}
