import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;



public class Server_thread implements Runnable{

	private Socket mySock;
	//private Scanner input;
	//private PrintWriter out;
	private String message = "";

	Server_thread(Socket x){
		mySock = x;
	}
	
	private void checkConnection() throws IOException{
		String disName = "";
		 // System.out.println("CCCCCC");
		if(mySock.isClosed() == true || mySock.isConnected() == false){
			 // System.out.println("BBBBBBB");
			for(int i = 0;i < Server.userSock.size();i++){
				if(Server.userSock.get(i) == mySock){
					Server.userSock.remove(i);
					disName = Server.userName.get(i);
					Server.userName.remove(i);
					System.out.println(disName + " disconnected!");
					break;
				}
			}
			
			for(int i = 0;i < Server.userSock.size();i++){
				Socket temp = Server.userSock.get(i);
				PrintWriter out = new PrintWriter(temp.getOutputStream());
				out.println("!d " + disName);
				out.flush();
				
			}
			// Thread.currentThread().interrupt();
		}
	}
	
	private boolean checkConnection(Socket Sock) throws IOException{
		 // System.out.println("CCCCCC");
		String disName = "";
		if(Sock.isClosed() == true || Sock.isConnected() == false){
			//System.out.println("BBBBBBB");
			for(int i = 0;i < Server.userSock.size();i++){
				if(Server.userSock.get(i) == Sock){
					Server.userSock.remove(i);
					disName = Server.userName.get(i);
					Server.userName.remove(i);
					System.out.println(disName + " disconnected!");
					break;
				}
			}
			
			for(int i = 0;i < Server.userSock.size();i++){
				Socket temp = Server.userSock.get(i);
				PrintWriter out = new PrintWriter(temp.getOutputStream());
				out.println("!d " + disName);
				out.flush();
			}
			// Thread.currentThread().interrupt();
			return true;
		}
		return false;
	}
	
	private boolean addUserName(Socket sock) throws IOException{
		String user;
		String password;
		int indexOfUser = -1;
		try{
			//Scanner input = new Scanner(sock.getInputStream());
			//String user = input.nextLine();
			
			DataInputStream dataIn = new DataInputStream(sock.getInputStream());
			while((user = dataIn.readLine()) == null);
			while((password = dataIn.readLine()) == null);
		
			//String password = dataIn.readLine();
			System.out.println(password);
			if(Server.userName.indexOf(user) >= 0){
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				out.println("!R");
				out.flush();
				return false;
				
			}else if((indexOfUser = Server.allUserName.indexOf(user)) < 0){
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				out.println("!u");
				out.flush();
				return false;
			}else if(!password.equals(Server.allUserPW.get(indexOfUser))){
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				out.println("!p");
				out.flush();
				return false;
			}else{
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				out.println("success");
				out.flush();
			}
			
			Server.userName.add(user);
			Server.userSock.add(sock);
			for(int i = 0;i < Server.userSock.size();i++){
				
				Socket temp = Server.userSock.get(i);
				if(checkConnection(temp)) continue;
				PrintWriter out = new PrintWriter(temp.getOutputStream());
				out.println("!! " + Server.userName);
				out.flush();
			}
			return true;
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
		return false;
		
	}
	
	private boolean addNewUser(Socket sock) throws IOException{
		String user;
		String password;
		try{
			//Scanner input = new Scanner(sock.getInputStream());
			//String user = input.nextLine();
			
			DataInputStream dataIn = new DataInputStream(sock.getInputStream());
			while((user = dataIn.readLine()) == null);
			while((password = dataIn.readLine()) == null);
		
			//String password = dataIn.readLine();
			System.out.println("New user wants to register: " + user);
			System.out.println("New user's password: " + password);
			if(Server.allUserName.indexOf(user) >= 0){
				PrintWriter out = new PrintWriter(sock.getOutputStream());
				out.println("!N");
				out.flush();
				System.out.println("Register failed: repeated username");
				return false;
				
			}
			
			Server.allUserName.add(user);
			Server.allUserPW.add(password);
			PrintWriter out = new PrintWriter(sock.getOutputStream());
			out.println("!Y");
			out.flush();
			System.out.println("Register Successed");
			return true;
		}catch(Exception e){
			Thread.currentThread().interrupt();
		}
		return false;
	}
	
	public void run(){
		// System.out.println("Client said: " + message);
		String type;
		
		
		try{
			DataInputStream dataIn = new DataInputStream(mySock.getInputStream());
			while((type = dataIn.readLine()) == null);
			if(type.charAt(1) == 'L'){
				if(!addUserName(mySock)){
					return;
				}
			}else if(type.charAt(1) == 'S'){
				addNewUser(mySock);
				return;
			}
			
		}catch(IOException e){
			System.out.println(e);
		}
		
		
		try{
			try{
				Scanner input_S = new Scanner(mySock.getInputStream());
				// out = new PrintWriter(mySock.getOutputStream());
				
				// BufferedInputStream inStream = new BufferedInputStream(mySock.getInputStream());
				
				DataInputStream input = new DataInputStream(mySock.getInputStream());
				//DataOutputStream out = new DataOutputStream(mySock.getOutputStream());
				
				while(true){
					// System.out.println("aaaaaa");
					checkConnection();
					
					// if(!input.isReachable()) {
						// System.out.println("2123123");
						// mySock.close();
						// checkConnection();
						// return;			
					// }
					
					message = input.readLine();
					
					//private message
					if(message.indexOf("!") == 0){
						Socket temp;
						DataOutputStream out;
						String receiver;
						int index;
						switch(message.charAt(1)){
							
							case 'c':
								System.out.println(message);
								receiver = message.split(" ")[1];
								index = Server.userName.indexOf(receiver);
								temp = Server.userSock.get(index);
								if(checkConnection(temp)) continue;
								out = new DataOutputStream(temp.getOutputStream());
								out.writeBytes(message);
								out.writeByte('\n');
								out.flush();
								System.out.println("Client said: " + message);
								System.out.println("Send to: " + temp.getLocalAddress().getHostName());
								
								DataOutputStream outStream = new DataOutputStream(temp.getOutputStream());
								// BufferedInputStream inStream = new BufferedInputStream(mySock.getInputStream());
								byte[] buffer = new byte[4096];
								System.out.println("before");
								int read = input.read(buffer);
								System.out.println("after" + " size = " + read);
								if(read != -1){
									outStream.write(buffer, 0, read);
									outStream.flush();
									System.out.println("after write");
								}
								break;
							case 'm':
							case 'f':
							case 'e':
							case 'r':
							case 'y':
								receiver = message.split(" ")[1];
								index = Server.userName.indexOf(receiver);
								temp = Server.userSock.get(index);
								if(checkConnection(temp)) continue;
								out = new DataOutputStream(temp.getOutputStream());
								out.writeBytes(message);
								out.writeByte('\n');
								out.flush();
								System.out.println("Client said: " + message);
								System.out.println("Send to: " + temp.getLocalAddress().getHostName());
								break;
							case 'a':
								System.out.println("Client said: " + message);
								for(int i = 0;i < Server.userSock.size();i++){
						
									temp = Server.userSock.get(i);
									if(temp == mySock) continue;
									if(checkConnection(temp)) continue;
									out = new DataOutputStream(temp.getOutputStream());
									out.writeBytes(message);
									out.writeByte('\n');
									out.flush();
									System.out.println("Send to: " + temp.getLocalAddress().getHostName());
								}
								break;
							
							case 'l':
								receiver = message.split(" ")[1];
								index = Server.userName.indexOf(receiver);
								temp = Server.userSock.get(index);
								if(checkConnection(temp)) continue;
								// out = new PrintWriter(temp.getOutputStream());
								// out.println("!l " + Server.userName);
								// out.flush();
								out = new DataOutputStream(temp.getOutputStream());
								out.writeBytes("!l " + Server.userName);
								out.writeByte('\n');
								out.flush();
								break;
								
							case 'o':
								mySock.close();
								checkConnection();
								return;
							default:
								System.out.println("Yout should not see this");
						}										
						continue;
					}					


					System.out.println("Client said: " + message);
					
					for(int i = 0;i < Server.userSock.size();i++){
						
						Socket temp = Server.userSock.get(i);
						if(temp == mySock) continue;
						if(checkConnection(temp)) continue;
						PrintWriter out = new PrintWriter(temp.getOutputStream());
						out.println(message);
						out.flush();
						System.out.println("Send to: " + temp.getLocalAddress().getHostName());
					}
				}
			}finally{
				mySock.close();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	
}
