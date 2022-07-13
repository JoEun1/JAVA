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
		OutputStream os;//���� ������� ����
		ServerSocket serverSocket;
		Socket s1 = null;
		String outMessage = null;

		try {
			serverSocket = new ServerSocket(5434);
			System.out.println("ä���� ���۵Ǿ����ϴ�.");
			s1 = serverSocket.accept();
			os = s1.getOutputStream();

			ReThread_Server rThread = new ReThread_Server(s1);
			rThread.start();

			br_out = new BufferedReader(new InputStreamReader(System.in));//�ܼ� �б�
			bw = new BufferedWriter(new OutputStreamWriter(os));
			pw = new PrintWriter(bw, true);

			pw.println("ä���� ���۵Ǿ����ϴ�.");//Ŭ���̾�Ʈ �ֿܼ� ���

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

				pw.println("����: " + outMessage);//client�� ����ִ� �޽���


			}
			pw.close();
			s1.close();

			if (rThread.isAlive()) {
				rThread.interrupt();
				rThread = null;
			}
		} catch (SocketException e) {
			System.out.println("Ŭ���̾�Ʈ�� ���� ������ ���������ϴ�. �����մϴ�.");
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}
