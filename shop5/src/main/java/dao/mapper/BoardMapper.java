package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {
	String boardcolumn = "select num, name, pass, subject, "+
			"content, file1 fileurl, regdate, readcnt, grp, "+
			"grplevel, grpstep from board ";

	@Select({"<script>"
			, "select count(*) from board"
			,"<if test='searchtype != null'> where ${searchtype} like #{searchcontent}</if>"
			, "</script>"})
	int boardcount(Map<String, Object> param);
	
	   @Select({"<script>", boardcolumn,
	         "<if test='searchtype != null'> where ${searchtype} like #{searchcontent}</if>",
	         "<if test='num != null'> where num = #{num}</if>", 
	         "<if test='num == null'> order by grp desc, ",
	         "grpstep limit #{startrow}, #{limit} </if>",
	         "</script>"})

	List<Board> select(Map<String, Object> param); // selectOne이랑 검색 하고 조회합친 메서드
	
	@Select("select ifnull(max(num), 0) from board")
	int maxnum();
	
	@Insert("insert into board (num, name, pass, subject, content," + 
			"file1, regdate, readcnt, grp, grplevel, grpstep)" +
			" values (#{num}, #{name}, #{pass}, #{subject}, #{content}, " +
			"#{fileurl}, now(), 0, #{grp}, #{grplevel}, #{grpstep})")
	void insert(Board board);
	
	@Update("update board set readcnt = readcnt + 1" + 
					" where num = #{num}")
	void update(Map<String, Object> param);
	
	@Update("update board set grpstep = grpstep + 1 where grp = #{grp} and grpstep > #{grpstep}")
	void grpstep(Map<String, Object> param);
	
	// 그냥 selectOne메서드 써도됨
	@Select("select pass from board where num = #{num}")
	String getPass(Map<String, Object> param);

	
	@Update("update board set name = #{name}, subject=#{subject}," + 
			"content=#{content}, file1=#{fileurl} where num = #{num}")
	void boardupdate(Board board);

	@Delete("delete from board where num = #{num}")
	void boardDelete(Map<String, Object> param);
	
	
	
	
}
