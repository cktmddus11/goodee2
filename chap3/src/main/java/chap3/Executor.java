package chap3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 객체화됨. "executor"이름으로 컨테이너에 저장
public class Executor {
	@Autowired // 컨테이너 중 Worker 객체를 찾아서 주입.
	private Worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit); // 이미 Worker객체가 주입되어있기 때문에 null아니고 
		// work메서드 호출
	}
}
