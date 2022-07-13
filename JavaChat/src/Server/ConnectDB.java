package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

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
}
