package dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import logic.User;

public class UserDao {
	private NamedParameterJdbcTemplate template; // spring jdbc 프레임워크
	// Connection 객체가 주입 => xml 설정
	// dataSource : DriverManagerDataSource 객체 주입
	public void setDataSource(DataSource datasource) {
		this.template = new NamedParameterJdbcTemplate(datasource);
		// template 는 db정보를 가지고 있고 이미 Connection 되어있는 상태
	}
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount "
				+"(userid, username, password, birthday, phoneno, "
				+" postcode, address, email ) values "
				+" (:userid, :username, :password, :birthday, :phoneno, "
				+ ":postcode, :address, :email)";
		template.update(sql, param);
		
	}
}
