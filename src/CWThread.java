
import java.io.*;
import java.util.*;
import java.net.*;

public class CWThread extends Thread{
	static Socket sc = null;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


	CWThread(Socket sc){
		this.sc = sc;
	}

	public void run() {
		try {
			OutputStream out = sc.getOutputStream();
			PrintWriter pw = new PrintWriter(out,true);

			while(true) {
				if(!sc.isClosed()) {
					pw.println(br.readLine());
				}
				else
					break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
