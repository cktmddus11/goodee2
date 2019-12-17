package logic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
@Repository 
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = 
			new BeanPropertyRowMapper<User>(User.class);
	// mapper : item형태로 리턴값을 보낼수있음, 자동으로 빈 클래스를 채워줌
	private Map<String, Object> param = new HashMap<>();
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void userinsert(User user) {
		String sql = "insert into useraccount "
				+"(userid, password, username, phoneno, "
				+ "postcode, address, email, birthday) values "
				+ " (:userid, :password, :username, :phoneno, :postcode"
				+ ", :address, :email, :birthday)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		template.update(sql,  proparam);
		
	}

	public User selectOne(String userid) {
		String sql = "select *From useraccount where userid = :userid";
		param.clear();
		param.put("userid",  userid);
		return template.queryForObject(sql, param, mapper);
	}
}
