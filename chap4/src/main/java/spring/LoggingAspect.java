package spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StopWatch;

public class LoggingAspect {
	public Object logging(ProceedingJoinPoint joinPoint) throws 
		Throwable{
		System.out.println("[LA]로그 시작");
		StopWatch sw = new StopWatch();
		sw.start();
		Object ret = joinPoint.proceed(); // 내 다음 메서드를 알고있는 애 => write()메서드를 알고있음
		sw.stop();
		System.out.println("[LA]메서드 실행 시간 : "+sw.getTotalTimeMillis()+"밀리초");
		return ret;
	}
}
