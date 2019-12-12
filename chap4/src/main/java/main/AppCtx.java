package main;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // 설정 java소스. xml 설정 대체 하는 소스
@ComponentScan(basePackages = {"annotation", "main"}) // 패키지에있는거 객체화 
@EnableAspectJAutoProxy // AOP설정
public class AppCtx {
	/*
	 * @Bean // <bean id='memberService2' class="main.MemberServie" /> public
	 * MemberService memberService2() { // 컨터이너에 commponent 처리하는거와 같음 return new
	 * MemberService(); }
	 */
}
