package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository
public class AdminDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = 
			new BeanPropertyRowMapper<User>(User.class);
	// mapper : item형태로 리턴값을 보낼수있음, 자동으로 빈 클래스를 채워줌
	private Map<String, Object> param = new HashMap<String, Object>();
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}

	
	public List<User> userlist() {
		String sql = "select *from useraccount";
		return template.query(sql,mapper);
	}

}
