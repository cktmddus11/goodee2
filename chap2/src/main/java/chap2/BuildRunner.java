package chap2;

import java.util.List;

public class BuildRunner {
	private String path; // c:/setup/
	public void setPath(String path) {
		this.path = path;
	}
	public void build(List<String> srcdirs, String bindir) { // src/, srcResource/ + bin/
		// 2 : src/, srcResource/ + /bin2
		String info = "프로젝트 경로 : "+path+"\n"; // c:/setup/
		for(String dir : srcdirs) {
			info += "소스 파일 경로 : "+dir+"\n"; // src/, srcResource/
		}
		info += "바이트 코드 경로 : "+bindir +"\n"; // bin/
		System.out.println(info);
	}
	
}
