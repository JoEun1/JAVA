package chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public static Connection makeConnection() {
		
		Connection con = null;
		
		String url = "jdbc:mariadb://127.0.0.1:3306/chat";
		String user = "tomato";
		String pwd = "0987";
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("데이터베이스 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return con;
	}
	
	public void sql(String s) throws SQLException {
		Connection con = makeConnection();
		pstmt = con.prepareStatement("insert into chat_log(message, date) values ('" + s + "',now());");
		// pstmt.executeUpdate(sql);
		
		if (pstmt.executeUpdate() == 1)
			System.out.println("Add to Record complete");
		else
			System.out.println("Add to Record Fail");
	}
	
	public void join (String id, String pw) { //회원가입 쿼리문 실행 메소드
		
		Connection con = makeConnection();
		
		try {
			/*pstmt = con.prepareStatement("SELECT id FROM account_tbl WHERE id="+id);
			rs = pstmt.executeQuery();
			if(rs != null) {
			}//아이디 중복검사*/
			
			pstmt = con.prepareStatement("insert into account_tbl(ID,PW) values (?,?)");
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void login(String id, String pw) {
		Connection con = makeConnection();
	}
	
	
}
