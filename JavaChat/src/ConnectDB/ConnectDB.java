package ConnectDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

	public static void main(String[] args) {
		
		Connection con = null;
		
		String url = "jdbc:mariadb://127.0.0.1:3306/chat";
		String user = "root";
		String pwd = "rlawhdms999";
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("데이터베이스 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
