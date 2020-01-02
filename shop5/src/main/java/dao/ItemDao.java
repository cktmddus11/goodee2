package dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;
import logic.User;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
@Repository // @Component + dao  기능 
public class ItemDao {
	@Autowired // spring-db.xml에 객체 주입 설정됨
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<String, Object>();
	
	public List<Item> list() {
		return sqlSession.getMapper(ItemMapper.class).select(null);
	}

	public void insert(Item item) {
		int id = sqlSession.getMapper(ItemMapper.class).maxid();
		item.setId(++id+"");
		sqlSession.getMapper(ItemMapper.class).insert(item);
	}
 // selectOne메서드
	public Item detailView(String id) {
		param.clear();                                     // 리턴값이 리스트여서 
		param.put("id",  id);                                // 한건의 데이터만 가져오라고 .get(0)
		return sqlSession.getMapper(ItemMapper.class).select(param).get(0);
	}

	public void update(Item item) {
		sqlSession.getMapper(ItemMapper.class).update(item);		
	}

	public void delete(int id) {
		param.clear();
		param.put("id",  id);
		sqlSession.getMapper(ItemMapper.class).delete(param);
		
	}

	
}
