package dao;

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

import logic.SaleItem;

@Repository
public class SaleItemDao {
	private NamedParameterJdbcTemplate template;
	private Map<String, Object> param = new HashMap<>();
	private RowMapper<SaleItem> mapper = new BeanPropertyRowMapper<SaleItem>(SaleItem.class);

	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public void insert(SaleItem si) {
		String sql = "insert into saleitem "
				+"(saleid, saleitemid, itemid, quantity) "
				+"values (:saleid, :saleitemid, :itemid, :quantity)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(si);
		template.update(sql, proparam);
		
	}
	
	
}
