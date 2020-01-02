package hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
/* 
 * 화면에서 아이디와 비밀번호를 입력받아서 
 * 해당아이디가 userbackup 테이블에 없으면 "아이디 확인"출력
 * 해당아이디의 비밀번호를 비교해서 맞으면 "반갑습니다. 아이디님" 출력
 * 해당아이디의 비밀번호를 비교해서 틀리면 "비밀번호 확인" 출력
 * 
 * */
public class DigestMain3 {
	public static void main(String args[]) throws Exception{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb", "scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, password from userbackup where userid=?");
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("아이디를 입력해주세요 : ");
		String id = br.readLine();
		System.out.print("비밀번호를 입력해주세요 : ");
		String pw = br.readLine();
		
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		 
		if(rs.next()) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pw.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			if(hashpass.equals(rs.getString(2))) {
				System.out.println(rs.getString(1)+"님 반갑습니다");
			}else {
				System.out.println("비밀번호 확인");
			}
		}else {
			System.out.println("아이디 없음");
		}
		
	}
}
/* 
 * 
		PreparedStatement pstmt = conn.prepareStatement("select userid, password from userbackup");
		ResultSet rs = pstmt.executeQuery();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("아이디를 입력해주세요 : ");
		String id = br.readLine();
		System.out.print("비밀번호를 입력해주세요 : ");
		byte[] plain = br.readLine().getBytes();
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(plain);
		String hashpass = "";
		for(byte b : hash) {
			hashpass += String.format("%02X", b);
		}
		
		while(rs.next()) {
			if(rs.getString(1).equals(id)) {
				System.out.println("아이디 확인");
				if(rs.getString(2).equals(hashpass)) {
					System.out.println("반갑습니다. "+id+"님");
					break;
				}else {
					System.out.println("비밀번호를 확인하세요");
					break;
				}
			}
		}
		
		System.out.println("종료");
*/
