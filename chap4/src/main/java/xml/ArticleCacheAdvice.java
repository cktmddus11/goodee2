package xml;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

public class ArticleCacheAdvice {
	private Map<Integer, Article> cache = new HashMap<Integer, Article>();
	public Object cache(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("[ACA] cache before 실행");
		Integer id = (Integer)joinPoint.getArgs()[0]; // 핵심로직의 매개변수값을 가져옴. 값이 한개라 [0]
		Article article = cache.get(id);
		if(article != null) {
			System.out.println("[ACA] cache 에서 Article["+id+"] 가져옴");
			return article;
		}// 예외 발생하면 아래 과정 안하고 걍 끝내버림?
		Object ret = joinPoint.proceed();
		System.out.println("[ACA] cache after 실행");
		if(ret != null && ret instanceof Article) {
			cache.put(id,  (Article) ret);
			System.out.println("[ACA] cache에 Article["+id+"] 추가함");
		}
		return ret;
	}
}
