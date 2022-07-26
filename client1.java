package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class client1 {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 2400);
			// 서버접속
			System.out.println("서버 접속 성공");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// 버퍼로 읽어들이기
			System.out.println("로그인, 회원가입, 계정삭제 중 수행 할 작업을 고르시오.");
			Sender sThread = new Sender(socket, br.readLine());
			// sender 스레드
			Receiver rThread = new Receiver(socket);
			// Receiver 스레드

			sThread.start();
			rThread.start();

			sThread.join();
			rThread.join();

			socket.close();
		} catch (Exception e) {
			System.out.println("서버 접속 실패");
		}

	}
}

class Receiver extends Thread { // rThread
	Socket socket;

	public Receiver(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		InputStream input;
		// 읽는 stream
		BufferedReader br;
		// input 내용을 buffer로 받아옴
		try {
			while (true) {
				String s = null;
				input = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(input,"UTF-8")); // 읽기
				if ((s = br.readLine()) != null) 
				{
					if (s.equals("/quit"))
						break;
					System.out.println(s);
					// server에서 입력받은 input을 콘솔에 띄움
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Sender extends Thread { // sThread
	Socket socket;
	String work;// name
	BufferedReader br = null;
	BufferedWriter bw = null;
	PrintWriter pwr = null;

	public Sender(Socket s, String n) {
		br = new BufferedReader(new InputStreamReader(System.in));
		socket = s;
		work = n;// name
	}

	@Override
	public void run() {
		try {
			OutputStream out = socket.getOutputStream();// 소켓 보내기 함수
			bw = new BufferedWriter(new OutputStreamWriter(System.out));// 콘솔에서 입력된 것 소켓을 통해 출력
			pwr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);//
			String id = null, pw;

			pwr.println(work);    
			// 서버에 선택된 할 일 전송(if문에)
			if (work.equals("회원가입")) {
				bw.write("아이디를 입력하시오: "); // 버퍼에 저장햇다가
				bw.flush();// 출력
				id = br.readLine();
				pwr.println(id);// 서버로 보내깅
				bw.write("비밀번호를 입력하시오: ");
				bw.flush();
				pw = br.readLine();
				pwr.println(pw);
			}
			
			if (work.equals("로그인")) {
			}
			
			while (true) {
				
				String str = br.readLine();
				// 콘솔에서 입력하는 거 읽어들이기
				if (str.equals("/quit")) {//나가기
					pwr.println(str);
					break;
				}
				
				pwr.println("/init_name " + id);
				
				pwr.println(id + ": " + str);
				// writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}