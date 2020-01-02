package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import logic.User;
@Repository 
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = 
			new BeanPropertyRowMapper<User>(User.class);
	// mapper : item형태로 리턴값을 보낼수있음, 자동으로 빈 클래스를 채워줌
	private Map<String, Object> param = new HashMap<String, Object>();
	
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

	public void update(User user) {
		String sql = "update useraccount set "
				+ "username = :username, phoneno=:phoneno, "
				+ "postcode = :postcode, email=:email, "
				+ "birthday = :birthday, address=:address"
				+ " where userid = :userid";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		template.update(sql,  proparam);
	}

	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		template.update("delete from useraccount where "
				+ "userid = :userid", param);
	}

	public List<User> list(String[] idchks) {
		/*String sql = "select *from useraccunt where userid in (";
		 * for(String id : idchks) { sql += "'"+id+"'"+((id","; } sql += ")";
		 */

		String ids = "";
		for(int i = 0;i<idchks.length;i++) {
			ids += "'"+idchks[i]+((i == idchks.length -1)?"'":"',");
		}
		String sql = "select *from useraccount where userid in ("
				+ids+")";
		//System.out.println(sql);
		return template.query(sql, mapper);
	}
	

	
}
