import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;



public class Client_thread implements Runnable{

	private Socket mySock;
	private DataInputStream input;
	private String message = "";
	private MainGUI gui;
	private String fileReceiver = "";
	private String fileName = "";
	private String fileSender = "";
	
	private ArrayList<String> userNames = new ArrayList<String>();

	Client_thread(Socket x, MainGUI g){
		mySock = x;
		gui = g;
	}
	
	public void run(){
		try{
			try{
				input = new DataInputStream(mySock.getInputStream());
				
				while(true){
					//System.out.println("Others said: ");
					//if(!input.hasNext()) return;
					
					message = input.readLine();
					//System.out.println(message);
					if(message.charAt(0) == '!'){
						switch(message.charAt(1)){
							case 'a':
								message = message.substring(3);
								gui.editChatRoom(message);
								break;
							case '!':
							case 'l':
								String s = message.substring(4, message.length() - 1);
								userNames = new ArrayList<String>(Arrays.asList(s.split(", ")));
								gui.setUserNames(userNames);
								break;
							case 'd':
								userNames.remove(message.substring(3));
								gui.setUserNames(userNames);
								break;
							case 'm':
								String[] spliter = message.split(" ");
								String sender = spliter[2];
								String m = "";
								for(int i = 3;i < spliter.length;i++){
									m += spliter[i] + " ";
								}
								gui.getPrivateMessage(sender, m);
								break;
							case 'f':
								String[] temp = message.split(" ");
								fileSender = temp[2];
								fileName = temp[3];
								gui.confirmWindow(fileSender, fileName);
								break;
							case 'c':
								gui.writeFile(fileSender);
								break;
							case 'e':
								//End process
								String[] tempName = message.split(" ");
								gui.finishFile(tempName[2]);
								break;
							case 'r':
								String[] tempt = message.split(" ");
								gui.rejected(tempt[2]);
								break;
							case 'y':
								String sendtoName = message.split(" ")[2];
								gui.sendFile(sendtoName);
								break;
						}
					
					}
					//System.out.println(message);
					
				}
			}finally{
				mySock.close();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	
}