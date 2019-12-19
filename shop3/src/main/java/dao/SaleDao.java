package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Sale;

@Repository
public class SaleDao {
	private NamedParameterJdbcTemplate template;
	private Map<String, Object> param = new HashMap<>();
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public int getMaxSaleId() {
		String sql = "select ifnull(max(saleid), 0) from sale";
		// sale 테이블에 저장된 saleid값의 최대값
		Integer max = template.queryForObject(sql, param, Integer.class);
		return max+1;
	}
	public void insert(Sale sale) {
		String sql = "insert into sale (saleid, userid, updatetime) "
				+"values (:saleid, :userid, :updatetime)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(sale);
		template.update(sql, proparam);
	}
}
