package csy;

public class Project {
	   private ArticleDao articleDao;
	   private String dir;

	   public void setArticleDao(ArticleDao articleDao) {
		   this.articleDao = articleDao;
	   }
	   public void setDir(String dir) {
		   this.dir = dir;
	   }
	   
	   public void test() {
		   
		   
	      System.out.println("프로젝트 테스트 시작. 프로젝트 폴더:" + dir);
	      articleDao.insert(articleDao+ "insert 메서드 실행됨");
	      articleDao.update(articleDao+ "update 메서드 실행됨");
	      articleDao.delete(articleDao+ "delete 메서드 실행됨");

	   }

	}