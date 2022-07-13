package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
//import java.sql.Connection;

//import Server.ConnectDB;

public class SimpleClient {
	public static void main(String[] args) {
		//Connection con = ConnectDB.makeConnection();
		//PreparedStatement pstmt = null; 
		
		OutputStream os;//수신 스레드로 보냄
		BufferedReader br_in;
		BufferedWriter bw = null;

		PrintWriter pw = null;
		String outMessage = null;

		try {
			Socket s1 = new Socket("127.0.0.1", 5434);
			os = s1.getOutputStream();
			ReThread_Client rThread = new ReThread_Client(s1);
			rThread.start();

			br_in = new BufferedReader(new InputStreamReader(System.in));
			bw = new BufferedWriter(new OutputStreamWriter(os));
			pw = new PrintWriter(bw, true);

			while (true) {
				outMessage = br_in.readLine();
				//String sql = "INSERT INTO chat_log(message, date) VALUES" + "(" + "'" + outMessage + "'" + "," + "NOW()" + ");";
				//pstmt = con.prepareStatement(sql);
				if (outMessage.equals("exit"))
					break;

				pw.println("userName: " + outMessage);
			}
			pw.close();
			s1.close();

			if (rThread.isAlive()) {
				rThread.interrupt();
				rThread = null;
			}
		} catch (SocketException e) {
			System.out.println("서버로 부터 연결이 끊어졌습니다. 종료합니다.");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
