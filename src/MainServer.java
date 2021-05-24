
import java.io.*;
import java.util.*;
import java.net.*;

public class MainServer{
	ServerSocket serSocket;
	MainServer() throws IOException {
		serSocket = new ServerSocket(8888); 

	}

	public static void main(String args[]){
		try {
			MainServer ms = new MainServer();
			while(true) {
				try {
					Socket connSocket = ms.serSocket.accept();
					ClientHandler ch;
					ch = new ClientHandler(connSocket, ms);
					System.out.println("Main Server Open");
					ch.start();
				} catch (IOException ex) {
					System.out.println("Cannot accept conn");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}



