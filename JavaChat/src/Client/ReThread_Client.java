package Client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
//���� ������
public class ReThread_Client extends Thread {
	InputStream is;//�۽ſ��� ���� �� ����
	BufferedReader br_in;
	Socket socket = null;
	String inMessage = null;

	public ReThread_Client(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			is = socket.getInputStream();

			br_in = new BufferedReader(new InputStreamReader(is));
			while (true) {
				inMessage = br_in.readLine();
				System.out.println(inMessage+"��");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
