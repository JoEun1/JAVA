package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleServer {
	
	public static void main(String[] args) throws IOException, SQLException{
		
		Connection con = ConnectDB.makeConnection();
		PreparedStatement pstmt = null; 
		
		BufferedReader br_out;
		BufferedWriter bw;
		PrintWriter pw = null;
		OutputStream os;//수신 스레드로 보냄
		ServerSocket serverSocket;
		Socket s1 = null;
		String outMessage = null;

		try {
			serverSocket = new ServerSocket(5434);
			System.out.println("채팅이 시작되었습니다.");
			s1 = serverSocket.accept();
			os = s1.getOutputStream();

			ReThread_Server rThread = new ReThread_Server(s1);
			rThread.start();

			br_out = new BufferedReader(new InputStreamReader(System.in));//콘솔 읽기
			bw = new BufferedWriter(new OutputStreamWriter(os));
			pw = new PrintWriter(bw, true);

			pw.println("채팅이 시작되었습니다.");//클라이언트 콘솔에 띄움

			while (true) {
				outMessage = br_out.readLine();
				String sql = "insert into chat_log(message, date) values" + "(" + "'" + outMessage + "'" + "," + "now()" + ");";
				pstmt = con.prepareStatement(sql);
				
				pstmt.executeUpdate(sql);
				
				/*if ( ==1)
					System.out.println("Add to Record complete");
				else
					System.out.println("Add to Record Fail"); 
				*/
				if (outMessage.equals("exit"))
					break;

				pw.println("서버: " + outMessage);//client에 띄워주는 메시지


			}
			pw.close();
			s1.close();

			if (rThread.isAlive()) {
				rThread.interrupt();
				rThread = null;
			}
		} catch (SocketException e) {
			System.out.println("클라이언트로 부터 연결이 끊어졌습니다. 종료합니다.");
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}
