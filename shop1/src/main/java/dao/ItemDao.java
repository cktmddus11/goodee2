package dao;

import java.util.List;

import javax.sql.DataSource;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	private NamedParameterJdbcTemplate template;
	public void setDataSource(DataSource datasource) {
		this.template = new NamedParameterJdbcTemplate(datasource);
	}
	public List<Item> list() {
		RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
		return template.query("select *From item", mapper);
	}
}