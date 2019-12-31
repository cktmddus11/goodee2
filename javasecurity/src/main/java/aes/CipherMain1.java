package aes;

public class CipherMain1 {
	public static void main(String [] args) {
		String plain1 = "안녕하세요. 홍길동입니다.";
		String cipher1 = CipherUtil.encrypt(plain1);
		System.out.println("암호문 : "+cipher1);
		
		String plain2 = CipherUtil.decrypt(cipher1);
		System.out.println("복호문 : "+plain2);
		
		// 비빌번호 찾기 했을 떄 알려주는거는
		// 초기화를 안해서 그럼
		// 초기화 해서 임시 비밀번호 주고 그러는거
		
		// 실행할 때마다 결과가 다름
		// 키를 랜덤으로 해서 다른건가?
		
	}
}
