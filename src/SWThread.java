
import java.io.*;
import java.util.*;
import java.net.*;

public class SWThread extends Thread{

	static Socket sc = null;

	SWThread(Socket sc){
		this.sc = sc;
	}

	public void run() {
		try {
			InputStream input = sc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			while(true) {
				if(!sc.isClosed())
					System.out.println(br.readLine());
				else
					break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
