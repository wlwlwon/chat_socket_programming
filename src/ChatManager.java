import java.io.*;
import java.util.*;
import java.net.*;

public class ChatManager extends Thread {
	Thread t;

	String [] usernames;
	HashMap<String,Socket> resi;
	String message;

	public ChatManager(String[] _usernames,String message,HashMap<String,Socket> resi) throws IOException {
		t= null;
		usernames = _usernames;	
		this.message =message; 
		this.resi = new HashMap<>(resi);
	}

	public void start() {
		OutputStream out;
		PrintWriter pw ;

		String sender = usernames[0];
		String receiver = usernames[1];
		
		try {
			 out = resi.get(sender).getOutputStream();
			 pw = new PrintWriter(out,true);
			 pw.println("["+sender+"] : "+message);
			 pw.flush();
			 
			 out = resi.get(receiver).getOutputStream();
			 pw = new PrintWriter(out,true);
			 pw.println("["+sender+"] : "+message);
			 
			 pw.flush();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (t==null) {
			t = new Thread(this);
			t.start();
		}
	}


}
