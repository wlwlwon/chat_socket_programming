import java.io.*;
import java.util.*;
import java.net.*;

public class ClientHandler extends Thread {
	Thread t;
	private final Socket cSocket;
	static Hashtable<String,Socket> chatresi;
	static Hashtable<String,Socket> resi;

	public ClientHandler(Socket _cSocket, MainServer _parentServer){
		t = null;
		cSocket = _cSocket;
		resi = new Hashtable<>();
		chatresi = new Hashtable<>();
	}

	public void start() {		
		if (t==null) {
			t = new Thread(this);
			t.start();
		}
	}

	@Override
	public void run() {

		try {
			System.out.println("서버  : << "+cSocket.getInetAddress()+">> ip의 클라이언트와 연결");

			InputStream input = cSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			OutputStream out = cSocket.getOutputStream();
			PrintWriter pw = new PrintWriter(out,true);


			String readValue;
			String name =null;
			boolean login = false;
			pw.println("사용하실 아이디를 입력하세요");

			try {
				while((readValue = br.readLine())!=null) {
					
					if(!login) {
						name = readValue;
						if(resi.containsKey(name)) {
							pw.println("존재하는 아이디입니다.");
							continue;
						}


						login = true;
						System.out.println(name+"님이 접속하셨습니다.");
						pw.println(name+"님이 접속하셨습니다.");
						resi.put(name, cSocket);
						continue;
					}

					String[] s = readValue.split(" ");

					if(readValue.equals("help")) {
						printhelp(name);						
					}else if(readValue.equals("online_users")) {					
						showUsers(out,resi);
						//showUsers(out,chatresi); show chatresi

					}else if(s[0].equals("connect")) {
						String ip = "/"+s[1];
						String nport = s[2];
						int pp = 0;
						try {
							pp = Integer.parseInt(nport);
						}catch (NumberFormatException e) {
							pw.println("port번호를  Integer형태로 다시 입력해주세요");
							continue;
						}

						if((cSocket.getPort()==pp) && (cSocket.getLocalAddress().toString().equals(ip))) {
							pw.println("자신의 정보입니다.");
							pw.flush();
							continue;
						}else {
							if(findpip(pp,ip,name,resi)) {

								chatresi.put(name, cSocket);

								pw.println("대화창으로 이동합니다.");
								pw.println("----------------");
								pw.flush();
							}else {
								pw.println("일치하는 peer가 없습니다.");
								pw.flush();
								continue;

							}				
						}


					}else if(s[0].equals("disconnect")) {
						String rename = s[1];
						if(chatresi.containsKey(rename)) {
							Set<String> keys = chatresi.keySet();

							for(String key :keys) {
								OutputStream ops = chatresi.get(key).getOutputStream();
								PrintWriter pr = new PrintWriter(ops,true);
								pr.println("대화가 종료됩니다.");
								pr.flush();
							}

							chatresi.clear();

						}else {
							pw.println("peer가 존재하지않습니다.");
							pw.flush();
							continue;
						}


					}else if(s[0].equals("talk")) {
						String message = s[2];									
						ChatManager cm;
						String otherusername = s[1];
						String[] usernames = {name, otherusername};
						if(chatresi.containsKey(otherusername)) {
							try {
								cm = new ChatManager(usernames,message,chatresi);
								cm.start();
							} catch (IOException e1) {
							}
						}else {
							pw.println("대화 할 수있는 peer가 존재하지않습니다.");
							pw.flush();
							continue;
						}


					}else if(readValue.equals("logoff")) {

						resi.remove(name);
						removeFromList();
						cSocket.close();
						break;
					}

				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}catch (IOException e) {
			e.printStackTrace();

		}

	}


	private static void printhelp(String name) throws IOException {

		OutputStream out = resi.get(name).getOutputStream();
		PrintWriter pw = new PrintWriter(out,true);
		pw.println("online_users");
		pw.println("connect [ip] [port]");
		pw.println("disconnect [peer]");
		pw.println("talk [peer] [message]");
		pw.println("logoff");
		pw.flush();
	}


	//find port, ip at resiServer
	private  boolean findpip(int port, String address,String name,Hashtable<String,Socket> resi) throws IOException {
		Set<String> keys = resi.keySet();

		OutputStream ops;
		boolean flag =false;
		for (String key : keys) {
			Socket tmp = resi.get(key);
			if(tmp.getPort()==port && tmp.getLocalAddress().toString().equals(address)) {
				flag = true;
				if(!chatresi.containsKey(key)) {
					ops = tmp.getOutputStream();
					PrintWriter pr = new PrintWriter(ops,true);
					pr.println(name+"님으로부터 대화요청이 왔습니다.");
					pr.flush();
				}
				break;

			}else {
				flag =  false;
			}
		}
		return flag;
	}
	private void showUsers(OutputStream out,Hashtable<String,Socket> gg) throws IOException{

		if(!gg.isEmpty()) {
			Set<String> keys = gg.keySet();
			PrintWriter pw = new PrintWriter(out,true);
			for (String key : keys) {
				pw.println(key+" : "+gg.get(key));
			}
			pw.println("----------------");
		}

	}

	private void removeFromList() {
		System.out.println("<< "+cSocket.getLocalAddress()+">> 접속을 종료합니다.");

	}
}
