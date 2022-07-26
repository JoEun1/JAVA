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
			// ��������
			System.out.println("���� ���� ����");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// ���۷� �о���̱�
			System.out.println("�α���, ȸ������, �������� �� ���� �� �۾��� ���ÿ�.");
			Sender sThread = new Sender(socket, br.readLine());
			// sender ������
			Receiver rThread = new Receiver(socket);
			// Receiver ������

			sThread.start();
			rThread.start();

			sThread.join();
			rThread.join();

			socket.close();
		} catch (Exception e) {
			System.out.println("���� ���� ����");
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
		// �д� stream
		BufferedReader br;
		// input ������ buffer�� �޾ƿ�
		try {
			while (true) {
				String s = null;
				input = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(input,"UTF-8")); // �б�
				if ((s = br.readLine()) != null) 
				{
					if (s.equals("/quit"))
						break;
					System.out.println(s);
					// server���� �Է¹��� input�� �ֿܼ� ���
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
			OutputStream out = socket.getOutputStream();// ���� ������ �Լ�
			bw = new BufferedWriter(new OutputStreamWriter(System.out));// �ֿܼ��� �Էµ� �� ������ ���� ���
			pwr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")), true);//
			String id = null, pw;

			pwr.println(work);    
			// ������ ���õ� �� �� ����(if����)
			if (work.equals("ȸ������")) {
				bw.write("���̵� �Է��Ͻÿ�: "); // ���ۿ� �����޴ٰ�
				bw.flush();// ���
				id = br.readLine();
				pwr.println(id);// ������ ������
				bw.write("��й�ȣ�� �Է��Ͻÿ�: ");
				bw.flush();
				pw = br.readLine();
				pwr.println(pw);
			}
			
			if (work.equals("�α���")) {
			}
			
			while (true) {
				
				String str = br.readLine();
				// �ֿܼ��� �Է��ϴ� �� �о���̱�
				if (str.equals("/quit")) {//������
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