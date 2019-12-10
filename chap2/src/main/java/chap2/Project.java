package chap2;

import java.util.List;

public class Project {
	private List<String> srcdirs;
	private String bindir;
	private BuildRunner b; // 이미 여기에 BuildRunner 같은거 다 주입되어있는거임 
	private String classpath; // xml을 실행한 순간부터 
	
	public void build() {
		b.build(srcdirs, bindir);
	}
	public List<String> getSrcdirs() {
		return srcdirs;
	}
	public void setSrcdirs(List<String> srcdirs) {
		this.srcdirs = srcdirs;
	}
	public String getBindir() {
		return bindir;
	}
	public void setBindir(String bindir) {
		this.bindir = bindir;
	}
	
	public BuildRunner getB() {
		return b;
	}
	public void setB(BuildRunner b) {
		this.b = b;
	}
	public String getClasspath() {
		return classpath;
	}
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	
}
