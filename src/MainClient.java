import java.io.*;
import java.util.*;
import java.net.*;


public class MainClient {
	static Socket cSocket;
	static HashMap<String, String> list;



	public static void main(String[] args) throws IOException {
		SWThread t1 = null;
		CWThread t2 = null;
		try {
			cSocket = new Socket("172.30.1.23", 8888);
			System.out.println("메인서버 접속 시도");

			t1 = new SWThread(cSocket);
			t2 = new CWThread(cSocket);

			t1.start();
			t2.start();

		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}


}
