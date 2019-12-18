package csy;

public class OracleArticleDao implements ArticleDao {
	
	public void insert(String string) {
		System.out.println(string);
		
	}

	public void update(String string) {
		System.out.println(string);
	}

	public void delete(String string) {
		System.out.println(string);
		
		
	}
	@Override
	public String toString() {
		return "OracleArticleDao";
	}

	
}
