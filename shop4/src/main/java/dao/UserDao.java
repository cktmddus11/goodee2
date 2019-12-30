package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.mapper.ItemMapper;
import dao.mapper.UserMapper;
import exception.LoginException;
import logic.User;
@Repository 
public class UserDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<String, Object>();

	
	public void userinsert(User user) {
		sqlSession.getMapper(UserMapper.class).insert(user);
	}

	public User selectOne(String userid) {
		param.clear();
		param.put("userid",  userid);
		List<User> list = sqlSession.getMapper(UserMapper.class).select(param);
		if(list == null || list.isEmpty()) {
			throw new LoginException("해당 아이디가 없습니다.", "");
		}else {
			return list.get(0);
		}
	}

	public void update(User user) {
		sqlSession.getMapper(UserMapper.class).update(user);
	}

	public void delete(String userid) {
		param.clear();
		param.put("userid", userid); 
		sqlSession.getMapper(UserMapper.class).delete(param);
	}

	public List<User> list(String[] idchks) {
		List<String> ids = Arrays.asList(idchks);
//		List<String> ids = new ArrayList<String>();
//		for(int i = 0;i<idchks.length;i++) {
//			ids.add(idchks[i]);
//		}
		
		param.clear();
		param.put("userids",  ids); // 리스트 말고 idchks 바로 넣어줘도 됨
		// param.put("userids", idchks);
		/*
		 * String sql = "select *from useraccount where userid in (" +ids+")";
		 */
//		System.out.println("****ids:" + ids);
//		param.clear();
//		param.put("userids", ids);
		
		
		
		return sqlSession.getMapper(UserMapper.class).select(param);
	}
	

	
}
