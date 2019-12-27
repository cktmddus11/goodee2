package dao.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.User;

public interface UserMapper {
	@Insert("insert into useraccount" + 
			"(userid, password, username, phoneno, " + 
			"postcode, address, email, birthday) values " + 
			" (#{userid}, #{password}, #{username}, #{phoneno}, #{postcode}" + 
			", #{address}, #{email}, #{birthday})")
	void insert(User user);

	
	@Select({
		"<script>",
		"select *from useraccount ",
		"<if test='userid != null'>where userid  = #{userid}</if>",
		"<if test='userids != null'>where userid  in",
		"<foreach collection='userids' item='id' separator=',' index = 'i' open='(' close=')'>",
		"#{id}",
		"</foreach>",
		"</if>",
		"</script>"
			/*
			 * "<script>", "select *from useraccount ",
			 * "<if test='userid != null'>where userid  = #{userid}</if>",
			 * "<if test='userids != null'>where userid in (${userids})</if>", "</script>"
			 */
		
	})
	List<User>select(Map<String, Object> param);

	@Update("update useraccount set " + 
			"username = #{username}, phoneno=#{phoneno}, " + 
			"postcode = #{postcode}, email=#{email}, " + 
			"birthday = #{birthday}, address=#{address} where userid = #{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid = #{userid}")
	void delete(Map<String, Object> param);

	



}
