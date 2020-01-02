package spring;

public class WriteImpl {
	private ArticleDao dao; // xml에서 주입. MariadbArticleDao 객체 저장
	public WriteImpl (ArticleDao dao) {
		this.dao = dao;
	}
	public void write() { // 핵심 로직, pointcut 에 대상이 되는 메서드
		// 이 메서드가 실행되기 전에 loggingAspect클래스의 logging메서드가 먼저 실행됨
		// 중간에 로깅이라는 메서드가 끼어든거임
		System.out.println("writeImpl.write 메서드 호출");
		dao.insert();
	}
}
