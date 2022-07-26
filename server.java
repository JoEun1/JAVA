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
	// ConnectDBŬ���� �ҷ����� , �� Ŭ���̾�Ʈ ���� ������ �����ͺ��̽� ���� Ŭ������ ��
	PreparedStatement pstmt = null;
	// �������� �־��� ���� �б�

	static ArrayList<Socket> users = new ArrayList<Socket>();//Ŭ���̾�Ʈ��������
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
				// Ŭ���̾�Ʈ���� ���� work�� if��
				if (work.equals("�α���")) {// �α���
				}
				if (work.equals("ȸ������")) {// ȸ������
					id = br.readLine();
					pw = br.readLine();
					DB.join(id, pw);
				}
				if (work.equals("��������")) {// ��������
				}

				String str;
				
				if ((str = br.readLine()) != null) {
					System.out.print(str);
					DB.sql(str);// �α�����

					if (str.equals("/quit")) {
						PrintWriter wr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
						wr.println(str);
						socket.close();
						break;
					}

					if (str.startsWith("/init_name ")) {
						// name���������� nick�� �ֱ�
						nick = str.split(" ")[1];
						for (int i = 0; i < users.size(); i++) {

							OutputStream out = users.get(i).getOutputStream(); // ����
							PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);
							// PrintWriter writer = new PrintWriter(out, true);
							writer.println(nick + " ���� �����ϼ̽��ϴ�.");

							// writer.flush();
						}
						continue;
					}

					System.out.println(str);
					for (int i = 0; i < users.size(); i++) {

						OutputStream out = users.get(i).getOutputStream(); // ����
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
					writer.println(nick + "���� ���� �������ϴ�.");
				} catch (IOException e) {
					e.printStackTrace();
				} // ����

				// writer.flush();
			}
		}
	}

	/*
	 * public static void work() throws IOException { br = new BufferedReader(new
	 * InputStreamReader(System.in)); bw = new BufferedWriter(new
	 * OutputStreamWriter(System.out)); String id, pw; String s = br.readLine();
	 * 
	 * if (s.equals("�α���")) {
	 * 
	 * } if (s.equals("ȸ������")) { id = br.readLine(); pw = br.readLine(); DB.join(id,
	 * pw); } if (s.equals("��������")) {
	 * 
	 * } }
	 */

	public static void main(String[] args) {
		int socket = 2400;
		try {
			ServerSocket ss = new ServerSocket(socket);
			System.out.println("���� ����");
			while (true) {
				Socket user = ss.accept();

				System.out.println("Ŭ���̾�Ʈ ���� " + user.getLocalAddress() + " : " + user.getLocalPort());
				Thread serverThread = new server(user);
				serverThread.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
