package chap2;

public class WriteImp1 {
	private ArticleDao dao;
	public WriteImp1(ArticleDao dao) { // 생성자 : 클래스와 생성자 이름 동일
		this.dao = dao;                          // default 기본 생성자 
	} // 객체 생성시 생성자에 매개변수가 있기 때문에 
	// 매개변수 타입에 맞는 인자를 지정해주어야함 
	public void write() {
		System.out.println("WriteImp1.write 메서드 호출");
		dao.insert();
	}
}
