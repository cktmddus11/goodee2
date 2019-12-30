package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleMapper;
import logic.Sale;

@Repository
public class SaleDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<String, Object>();
	
	public int getMaxSaleId() {
		// sale 테이블에 저장된 saleid값의 최대값
		Integer max = sqlSession.getMapper(SaleMapper.class).maxsaleid();
		return max+1;
	}
	public void insert(Sale sale) {
		sqlSession.getMapper(SaleMapper.class).insert(sale);
	}
	public List<Sale> list(String id) {
		param.clear();
		param.put("userid", id);
		return sqlSession.getMapper(SaleMapper.class).select(param);
 
	}
}
