package exception;

public class BoardExcepetion extends Exception {
	private String url;
	
	public BoardExcepetion(String msg, String url) {
		super(msg);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	

}
