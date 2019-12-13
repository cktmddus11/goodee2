package dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource; // Connection  객체

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	/* Item item = new Itme(); 
	 * item.setId(rs.getString("id"));
	 * item.setName(rs.getString("name")); 
	 */
	RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	private NamedParameterJdbcTemplate template; // spring jdbc 프레임워크
	// Connection 객체가 주입 => xml 설정
	// dataSource : DriverManagerDataSource 객체 주입
	public void setDataSource(DataSource datasource) {
		this.template = new NamedParameterJdbcTemplate(datasource);
		// template 는 db정보를 가지고 있고 이미 Connection 되어있는 상태
	}
	public List<Item> list() {
		return template.query("select *From item", mapper);
		// 조회된 행들을 mapper에 맞춰서 알아서 들어감
	}
	public Item selectOne(Integer id) {
		Map<String, Integer> param = new HashMap<>();
		param.put("id", id);
		return template.queryForObject("select *From item where id =:id", param,mapper);
	}
}