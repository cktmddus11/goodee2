package csy;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {

	public static void main(String[] args) {

		String config = "classpath:applicationContext.xml";
		AbstractApplicationContext ctx =new GenericXmlApplicationContext(config);
		Project proj = ctx.getBean("project", Project.class);
		proj.test();

	}

}