package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;

@Repository
public class BoardDao {
	@Autowired
	SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<String, Object>();

	public int count(String searchtype, String searchcontent) {
		param.clear();
		if(searchtype != null) {
			param.put("searchtype", searchtype);
			param.put("searchcontent", "%"+searchcontent+"%"); 	 // '%1%'
			// :searchtype -> ' '를 자동으로 붙여줌?
		}
		return sqlSession.getMapper(BoardMapper.class).boardcount(param);
	}

	public List<Board> list(Integer pageNum, int limit, String searchtype, String searchcontent) {
		param.clear();
		if(searchtype != null) {
			param.put("searchtype", searchtype);
			param.put("searchcontent", "%"+searchcontent+"%");
		}
		param.put("startrow", (pageNum - 1) * limit);
		param.put("limit", limit);
		return sqlSession.getMapper(BoardMapper.class).select(param);
	}

	public int maxnum() {
		return sqlSession.getMapper(BoardMapper.class).maxnum();
	}

	public void insert(Board board) {
		sqlSession.getMapper(BoardMapper.class).insert(board);
	}

	public void readcntadd(Integer num) {
		param.clear();
		param.put("num", num);
		sqlSession.getMapper(BoardMapper.class).update(param);
	}

	public Board selectOne(Integer num) {
		param.clear();
		param.put("num", num);
		return sqlSession.getMapper(BoardMapper.class).select(param).get(0);
	}

	public void grpstep(int grp, int grpstep) {
		param.clear();      
		param.put("grp", grp);
		param.put("grpstep", grpstep);
		sqlSession.getMapper(BoardMapper.class).grpstep(param);
	}

	public String getPass(int num) {
		param.clear();
		param.put("num",num);
		return sqlSession.getMapper(BoardMapper.class).getPass(param);
	}

	public void boardupdate(Board board) {
		sqlSession.getMapper(BoardMapper.class).boardupdate(board);
	}

	public void boardDelete(int num) {
		param.clear();
		param.put("num", num);
		sqlSession.getMapper(BoardMapper.class).boardDelete(param);
		
	}

}
