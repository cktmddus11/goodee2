package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;

@Repository
public class BoardDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Board> mapper = 
			new BeanPropertyRowMapper<Board>(Board.class);
	// mapper : item형태로 리턴값을 보낼수있음, 자동으로 빈 클래스를 채워줌
	private Map<String, Object> param = new HashMap<String, Object>();
	private String boardcolumn = "select num, name, pass, subject, "
			+"content, file1 fileurl, regdate, readcnt, grp, "+
			"grplevel, grpstep from board";
	
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}

	public int count(String searchtype, String searchcontent) {
		String sql = "select count(*) from board";
		if(searchtype != null) {
			sql += " where "+searchtype+" like :searchcontent"; // :searchtype -> ' '를 자동으로 붙여줌?
		}
		param.clear();
		param.put("searchcontent", "%"+searchcontent+"%"); // '%1%'
		return template.queryForObject(sql, param, Integer.class);
	}

	public List<Board> list(Integer pageNum, int limit, String searchtype, String searchcontent) {
		param.clear();
		String sql =  boardcolumn;
		if(searchtype != null) {
			sql += " where "+searchtype+" like :searchcontent";
			param.put("searchcontent", "%"+searchcontent+"%");
		}
		sql += " order by grp desc, grpstep limit :startrow, :limit";
		
		param.put("startrow", (pageNum - 1) * limit);
		param.put("limit", limit);
		return template.query(sql, param, mapper);
	}

	public int maxnum() {
		String sql = "select ifnull(max(num), 0) from board";
		param.clear();
		return template.queryForObject(sql, param, Integer.class);
	}

	public void insert(Board board) {
		String sql = "insert into board (num, name, pass, subject, content, "
				+ "file1, regdate, readcnt, grp, grplevel, grpstep)"
				+ " values (:num, :name, :pass, :subject, :content, " + 
				":fileurl, now(), 0, :grp, :grplevel, :grpstep)";
		SqlParameterSource proparam =	 new BeanPropertySqlParameterSource(board);
		template.update(sql, proparam);
	}

	public void readcntadd(Integer num) {
		String sql = "update board set readcnt = readcnt + 1 "
				+" where num = :num";
		param.clear();
		param.put("num", num);
		template.update(sql, param);
	}

	public Board selectOne(Integer num) {
		String sql = boardcolumn+" where num = :num";
		param.clear();
		param.put("num", num);
		return template.queryForObject(sql, param, mapper);
	}

	public void grpstep(int grp, int grpstep) {
		String sql = "update board set grpstep = grpstep + 1 where grp = :grp and grpstep > :grpstep";
		param.clear();  // 1  > 0 and 44 = 44 -> 1 + 1 = 2          
		param.put("grp", grp);
		param.put("grpstep", grpstep);
		template.update(sql, param);
	}

	public String getPass(int num) {
		String sql = "select pass from board where num = :num";
		param.clear();
		param.put("num",num);
		return template.queryForObject(sql, param, String.class);
	}

	public void boardupdate(Board board) {
		String sql = "update board set name = :name, subject=:subject," + 
				"content=:content, file1=:fileurl where num = :num";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(board);
		template.update(sql, proparam);
	}

	public void boardDelete(int num) {
		String sql = "delete from board where num = :num";
		param.clear();
		param.put("num", num);
		template.update(sql, param);
		
	}

}
