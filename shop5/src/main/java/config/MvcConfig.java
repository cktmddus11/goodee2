package config;

import java.util.Properties;
import java.util.ResourceBundle;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration // 환경설정 클래스. xml 대체하는 클래스
// @Component, streotype(@Service, @Repository, @Controller)  어노테이션이 
// 부여된 Class를 자동을 Scan하여 Bean으로 등록해줌
@ComponentScan(basePackages= {"controller", "logic", "dao", "aop", "websockiet"}) 
@EnableAspectJAutoProxy // AOP설정
@EnableWebMvc // 유효성 검증
public class MvcConfig implements WebMvcConfigurer {
	 // @Bean : <bean id="handlerMapping" class="..HandlerMapping" />
	// 			=> HandlerMapping 클래스의 객체를 handlerMapping 이름으로 
	// 			 	컨테이너에 저장
	// 웹 요청이 들어올 경우 DispatcherServlet(web.xml에 설정) 가 요청을 어떤 
	// 컨트롤러에 위임할 것인지 결정해야 하는 데 그 요청과 해당 요청을 처리하는 
	// 컨트롤러의 맵핑을 담당하는 인터페이스 
	/*@Controller
		class HomeController {
    
    	@RequestMapping(value = "/test")
    	public String test() {
        return "test";
    	}
	} 
	 * 
	 * */
	@Bean // 요청 정보와 Controller를 mapping
	public HandlerMapping handlerMapping() {
		RequestMappingHandlerMapping hm = new RequestMappingHandlerMapping();
		hm.setOrder(0);
		return hm;
	}
	// view의 위치 지정
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/view/");
		vr.setSuffix(".jsp");
		return vr;
	}
	// 에러코드 찾아깔때 씀
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("messages");
		return ms;
	}
	
	
	//파일 업로드 객체 : enctype='multipart/form-data' 형식인 경우 실행
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		mr.setMaxInMemorySize(10485760);
		mr.setMaxUploadSize(10485760); // 최대 업로드 크기 지정
		return mr;
	}
	//예외 처리 객체
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		pr.put("exception.CartEmptyException", "exception");
		pr.put("exception.LoginException", "exception");
		pr.put("exception.BoardException", "exception");
		ser.setExceptionMappings(pr);
		return ser;
	}
	
	
}
