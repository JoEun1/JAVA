package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {

	public static void main(String[] args) {
		Connection con = ConnectDB.makeConnection();
		PreparedStatement pstmt = null; 
		
		String url = "jdbc:mariadb://127.0.0.1:3306/chat";
		String user = "root";
		String pwd = "rlawhdms999";
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("데이터베이스 연결 성공");
			String sql = "insert into chat_log(message, date) values" + "(" + "'" + "gg" + "'" + "," + "now()" + ");";
			pstmt = con.prepareStatement(sql);
			
			
			if (pstmt.executeUpdate(sql)==1)
				System.out.println("Add to Record complete");
			else
				System.out.println("Add to Record Fail"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}	

	}

}
