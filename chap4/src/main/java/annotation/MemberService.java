package annotation;


import org.springframework.stereotype.Component;

import xml.Member;
import xml.UpdateInfo;

@Component// AOP 클래스로 사용하는게 아니라 그냥 호출해서 사용하는거고
public class MemberService {
	public void regist(Member member) {
		System.out.println("annotation.MemberService.regist() 메서드 실행");
	}
	public boolean update(String memberId,  UpdateInfo info) {
		System.out.println("annotation.MemberService.update() 메서드 실행");
		return true;
	}
	public boolean delete(String id, String str) {
		System.out.println("annotation.MemberService.delete() 메서드 실행");
		return false;
	}
}
