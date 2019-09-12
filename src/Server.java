import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;



public class Server{
	public static ArrayList<Socket> userSock = new ArrayList<Socket>();
	public static ArrayList<String> userName = new ArrayList<String>();
	public static ArrayList<String> allUserName = new ArrayList<String>();
	public static ArrayList<String> allUserPW = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException{
		try{
			final int PORT = 4444;
			ServerSocket SERVER = new ServerSocket(PORT);
			
			while(true){
				Socket sock = SERVER.accept();
				
				
				System.out.println("Client connected from: " + sock.getLocalAddress().getHostName());
				Server_thread client = new Server_thread(sock);
				Thread c = new Thread(client);
				c.start();	
				
				
				
			}
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
	
	
	private static boolean checkConnection(Socket mySock) throws IOException{
		 // System.out.println("CCCCCC");
		String disName = "";
		if(mySock.isClosed() == true || mySock.isConnected() == false){
			 // System.out.println("BBBBBBB");
			for(int i = 0;i < userSock.size();i++){
				if(userSock.get(i) == mySock){
					userSock.remove(i);
					disName = userName.get(i);
					userName.remove(i);
					System.out.println(disName + " disconnected!");
					break;
				}
			}
			
			for(int i = 0;i < userSock.size();i++){
				Socket temp = userSock.get(i);
				PrintWriter out = new PrintWriter(temp.getOutputStream());
				out.println("!d " + disName);
				out.flush();
			}
			// Thread.currentThread().interrupt();
			return true;
		}
		return false;
	}
	
}
