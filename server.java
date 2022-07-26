package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class server extends Thread {
	static BufferedReader br = null;
	static BufferedWriter bw = null;

	static ConnectDB DB = new ConnectDB();
	// ConnectDB클래스 불러오기 , 새 클라이언트 들어올 때마다 데이터베이스 연결 클래스를 씀
	PreparedStatement pstmt = null;
	// 쿼리문에 넣어줄 문장 읽기

	static ArrayList<Socket> users = new ArrayList<Socket>();//클라이언트소켓저장
	Socket socket;
	static String nick = "";

	public server(Socket socket) {
		this.socket = socket;
		users.add(socket);
	}

	@Override
	public void run() {
		try {
			while (true) {
				InputStream input = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
				String work, id, pw;
				work = br.readLine();
				// 클라이언트에서 받은 work로 if문
				if (work.equals("로그인")) {// 로그인
				}
				if (work.equals("회원가입")) {// 회원가입
					id = br.readLine();
					pw = br.readLine();
					DB.join(id, pw);
				}
				if (work.equals("계정삭제")) {// 계정삭제
				}

				String str;
				
				if ((str = br.readLine()) != null) {
					System.out.print(str);
					DB.sql(str);// 로그저장

					if (str.equals("/quit")) {
						PrintWriter wr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
						wr.println(str);
						socket.close();
						break;
					}

					if (str.startsWith("/init_name ")) {
						// name받을때마다 nick에 넣기
						nick = str.split(" ")[1];
						for (int i = 0; i < users.size(); i++) {

							OutputStream out = users.get(i).getOutputStream(); // 쓰기
							PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);
							// PrintWriter writer = new PrintWriter(out, true);
							writer.println(nick + " 님이 입장하셨습니다.");

							// writer.flush();
						}
						continue;
					}

					System.out.println(str);
					for (int i = 0; i < users.size(); i++) {

						OutputStream out = users.get(i).getOutputStream(); // 쓰기
						PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);
						// PrintWriter writer = new PrintWriter(out, true); //
						writer.println(str);
						// writer.flush();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (int i = 0; i < users.size(); i++) {

				OutputStream out;
				try {
					out = users.get(i).getOutputStream();
					PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);
					// PrintWriter writer = new PrintWriter(out, true);
					writer.println(nick + "님이 방을 나갔습니다.");
				} catch (IOException e) {
					e.printStackTrace();
				} // 쓰기

				// writer.flush();
			}
		}
	}

	/*
	 * public static void work() throws IOException { br = new BufferedReader(new
	 * InputStreamReader(System.in)); bw = new BufferedWriter(new
	 * OutputStreamWriter(System.out)); String id, pw; String s = br.readLine();
	 * 
	 * if (s.equals("로그인")) {
	 * 
	 * } if (s.equals("회원가입")) { id = br.readLine(); pw = br.readLine(); DB.join(id,
	 * pw); } if (s.equals("계정삭제")) {
	 * 
	 * } }
	 */

	public static void main(String[] args) {
		int socket = 2400;
		try {
			ServerSocket ss = new ServerSocket(socket);
			System.out.println("서버 열림");
			while (true) {
				Socket user = ss.accept();

				System.out.println("클라이언트 입장 " + user.getLocalAddress() + " : " + user.getLocalPort());
				Thread serverThread = new server(user);
				serverThread.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
