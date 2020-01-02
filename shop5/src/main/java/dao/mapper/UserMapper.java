package dao.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.User;

public interface UserMapper {// 여기서는 오버로딩 불가능?해서 동일한 이름의 메서드 사용 불가
	@Insert("insert into userbackup" + 
			"(userid, password, username, phoneno, " + 
			"postcode, address, email, birthday) values " + 
			" (#{userid}, #{password}, #{username}, #{phoneno}, #{postcode}" + 
			", #{address}, #{email}, #{birthday})")
	void insert(User user);

	
	@Select({
		"<script>",
		"select *from userbackup ",
		"<if test='userid != null'>where userid  = #{userid}</if>",
		"<if test='userids != null'>where userid  in",
		"<foreach collection='userids' item='id' separator=',' index = 'i' open='(' close=')'>",
		"#{id}",
		"</foreach>",
		"</if>",
		"</script>"
			/*
			 * "<script>", "select *from userbackup ",
			 * "<if test='userid != null'>where userid  = #{userid}</if>",
			 * "<if test='userids != null'>where userid in (${userids})</if>", "</script>"
			 */
		
	})
	List<User>select(Map<String, Object> param);

	@Update("update userbackup set " + 
			"username = #{username}, phoneno=#{phoneno}, " + 
			"postcode = #{postcode}, email=#{email}, " + 
			"birthday = #{birthday}, address=#{address} where userid = #{userid}")
	void update(User user);

	@Delete("delete from userbackup where userid = #{userid}")
	void delete(Map<String, Object> param);


	@Update("update userbackup set password = #{chgpass} where userid = #{userid}")
	void passwordupdate(Map<String, Object> param);

	



}
