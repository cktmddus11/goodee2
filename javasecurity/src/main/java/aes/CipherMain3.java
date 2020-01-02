package aes;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/* 
 * useraccount 테이블의 email 값을 읽어서
 * userbackup 테이블의 email 을 암호화하기
 * key 는 userid의 해쉬값의 문자열 앞 16자리로 정한다. 
 * 
 * */
public class CipherMain3 {
	public static void main(String [] args) throws Exception{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb", "scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, email from useraccount");
		ResultSet rs = pstmt.executeQuery();

		while(rs.next()) {
//			String cipher1 = CipherUtil.encrypt2(rs.getString(2), rs.getString(1));
//			System.out.println(cipher1);
//			pstmt.close();
//			pstmt = conn.prepareStatement("update userbackup set  email = ? where userid=?");
//			pstmt.setString(1, cipher1); // 암호화한 이메일을 db에 업데이트 시킴
//			pstmt.setString(2, rs.getString(1));
//			System.out.println(rs.getString(1)+" 암호화 이메일 : "+cipher1+"\n"+
//			"복호화 이메일 : "+CipherUtil.decrypt2(cipher1, rs.getString(1)));		 
//			pstmt.execute();
			String userid = rs.getString(1);
			String key = CipherUtil.makehash(userid).substring(0, 16);
			String email = rs.getString(2);
			String newEmail = CipherUtil.encrypt(email, key);
			pstmt = conn.prepareStatement("update userbackup set email = ? where userid = ?");
			pstmt.setString(1, newEmail);
			pstmt.setString(2, userid);
			pstmt.executeUpdate();
			
		}
	
	}
}
