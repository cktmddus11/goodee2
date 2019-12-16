package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;

import java.util.Map;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
@Repository // @Component + dao  기능 
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Item> mapper = 
			new BeanPropertyRowMapper<Item>(Item.class);
	private Map<String, Object> param = new HashMap<>();
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Item> list() {
		return template.query("select *From item order by id", mapper); // Item 행태의 리스트로 리턴
		
	}

	public void insert(Item item) {
		param.clear();
		// id ; 등록된 id의 최대값
		int id=template.queryForObject("select ifnull(max(id), 0) from item", param, Integer.class);
		item.setId(++id+"");
		String sql = "insert into item (id, name, price, description, pictureUrl) values "
				+"(:id, :name, :price, :description, :pictureUrl)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql,  proparam);
		
	}
}
