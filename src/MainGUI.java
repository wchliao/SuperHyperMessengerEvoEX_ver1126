import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dialog;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Font;

import java.io.BufferedReader;


public class MainGUI extends JFrame {

	private JPanel contentPane;
	public String name;
	private String password;
	
	private MainGUI mainGUI;
	private Login login;
	
	private JTextField talkLine;
	private JLabel lblUsername;
	
	private Socket mySocket;
	
	public JTextArea chatRoom;
	private JList userList;
	
	private JPanel allUserPanel;
	
	
	private ArrayList<String> allUserName;
	
	private ArrayList<String> sendNames = new ArrayList<String>();
	private ArrayList<FileInputStream> inputStream = new ArrayList<FileInputStream>();
	
	
	private ArrayList<String> userNames;
	private ArrayList<JTextArea> privateMessage;
	private String filepath;
	private ArrayList<JButton> fileButton;
	
	private ArrayList<JPanel> messagePanel;
	
	private String FileSendTo; 
	private FileOutputStream fos;
	private FileInputStream fis;
	private DataInputStream inStream;
	private BufferedOutputStream outStream;
	
	private DataOutputStream out;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	public void start(String _name, String _password, Login _login, Socket sock)
	{
		this.name = _name;
		this.password = _password;
		login = _login;
		login.setVisible(false);
		mainGUI = this;
		mySocket = sock;
		lblUsername.setText("Your user name : " + _name);
		
		
		this.setVisible(true);
		
		Client_thread client = new Client_thread(mySocket, this);
		Thread c = new Thread(client);
		c.start();
		try{
			inStream = new DataInputStream(mySocket.getInputStream());
			outStream = new BufferedOutputStream(mySocket.getOutputStream());
			out = new DataOutputStream(mySocket.getOutputStream());
			//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
			out.writeBytes("!l " + name);
			out.writeByte('\n');
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		userNames = new ArrayList<String>();
		privateMessage = new ArrayList<JTextArea>();
		fileButton = new ArrayList<JButton>();
		messagePanel = new ArrayList<JPanel>();
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 764, 496);
		
		reset();
	}
	public void typeAction(JTextArea area, JTextField line)
	{
		String m = name + ": " + line.getText();
		if(line.getText().length() != 0){
			area.append(m + "\n");
		}
		line.setText("");
		try{
			//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
			out.writeBytes("!a " + m);
			out.writeByte('\n');
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	public void typeActionPrivate(JTextArea area, JTextField line, String _name)
	{
		String m = name + ": " + line.getText();
		if(line.getText().length() != 0){
			area.append(m + "\n");
		}
		line.setText("");
		try{
			//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
			out.writeBytes("!m " + _name + " " + name + " " + m);
			out.writeByte('\n');
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	public void reset()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(661, 12, 87, 23);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
			    	out.writeBytes("!o");
					out.writeByte('\n');
					out.flush();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				/*try{
					mySocket.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}*/
				login.setVisible(true);
				mainGUI.setVisible(false);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnLogout);
		
		JScrollPane chatRoomScrollPane = new JScrollPane();
		chatRoomScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatRoomScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatRoomScrollPane.setBounds(10, 10, 322, 409);
		contentPane.add(chatRoomScrollPane);
		
		chatRoom = new JTextArea();
		chatRoom.setEditable(false);
		chatRoom.setLineWrap(true);
		chatRoomScrollPane.setViewportView(chatRoom);
		
		JLabel lblChatroom = new JLabel("ChatRoom");
		lblChatroom.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblChatroom.setHorizontalAlignment(SwingConstants.CENTER);
		chatRoomScrollPane.setColumnHeaderView(lblChatroom);
		
		talkLine = new JTextField();
		talkLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeAction(chatRoom, talkLine);
			}
		});
		talkLine.setBounds(10, 427, 228, 21);
		contentPane.add(talkLine);
		talkLine.setColumns(10);
		
		JButton btnChatroom = new JButton("ChatRoom");
		btnChatroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeAction(chatRoom, talkLine);
			}
		});
		btnChatroom.setForeground(new Color(255, 0, 0));
		btnChatroom.setBounds(237, 426, 95, 23);
		contentPane.add(btnChatroom);
		
		JScrollPane userScrollPane = new JScrollPane();
		userScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userScrollPane.setBounds(331, 10, 322, 409);
		contentPane.add(userScrollPane);
		
		allUserPanel = new JPanel();
		userScrollPane.setViewportView(allUserPanel);
		
		allUserPanel.setLayout(new BorderLayout(0, 0));
		//String[] s = userNames.toArray(new String[userNames.size()]);
		userList = new JList();
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2){
					int index = userList.locationToIndex(event.getPoint());
					if (index >= 0) {
			            Object o = userList.getModel().getElementAt(index);
			            if(!userNames.contains(o.toString()))
			            	Message(o.toString());
			        }
					
				}
			}
		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allUserPanel.add(userList);
		
		JPanel namePanel = new JPanel();
		userScrollPane.setColumnHeaderView(namePanel);
		
		lblUsername = new JLabel();
		namePanel.add(lblUsername);
		
		JButton chatRoomFileButton = new JButton("File");
		chatRoomFileButton.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e)
			  {
			    JFileChooser chooser = new JFileChooser();
			    chooser.setMultiSelectionEnabled(false);
			    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    if (chooser.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
			    	String path = chooser.getSelectedFile().getAbsolutePath();
			    	String filename = chooser.getSelectedFile().getName();
			    	
			    	filepath = path;
			    	for(String SendName: allUserName){
			    		if(SendName.equals(name))
			    			continue;
			    		try{
				    		FileInputStream fis = new FileInputStream(filepath);
				    		inputStream.add(fis);
				    		sendNames.add(SendName);
				    		
				    		//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
				    		out.writeBytes("!m " + SendName + " " + name + " " + SendName + " wants to send you a file.");
				    		out.writeByte('\n');
				    		out.writeBytes("!f " + SendName + " " + name + " " + filename);
				    		out.writeByte('\n');
							out.flush();
							FileSendTo = SendName;
				    	}catch(Exception ex){
				    		System.out.println(ex);
				    	}
			    		chatRoom.append("Wait for " + SendName + "'s response...\n");
			    	}
			    	
			    }
			  }
		    });
		chatRoomFileButton.setBounds(331, 426, 87, 23);
		contentPane.add(chatRoomFileButton);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try{
			    	out.writeBytes("!o");
					out.writeByte('\n');
					out.flush();
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		    	login.dispose();
		    	mainGUI.dispose();
		    	System.exit(0);
		    }
		    
		});
		renew();
	}
	
	 public void Message(String SendName)
	 {
		 userNames.add(SendName);
		 
		JFrame frame = new JFrame(); 
		frame.setBounds(100, 100, 487, 476);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		messagePanel.add(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 10, 451, 384);
		contentPane.add(scrollPane);
		
		JLabel lblSendname = new JLabel("SendName");
		lblSendname.setText("Send to : " + SendName);
		scrollPane.setColumnHeaderView(lblSendname);
		
		
		JTextArea textArea = new JTextArea();
		privateMessage.add(textArea);
		
		
		String temp = new String();
		try {
            Scanner input;
            //System.out.print("Enter the file name with extention : ");
            File file = new File("./" + name + "_" + SendName + "_log.txt");

            input = new Scanner(file);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                temp += (line + "\n");
            }
            input.close();

        } catch (Exception ex) {
            //ex.printStackTrace();
        }
		textArea.setText(temp);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		JTextField txtMessageline = new JTextField();
		txtMessageline.setBounds(10, 404, 278, 21);
		txtMessageline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeActionPrivate(textArea, txtMessageline, SendName);
			}
		});
		contentPane.add(txtMessageline);
		txtMessageline.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(288, 403, 87, 23);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeActionPrivate(textArea, txtMessageline, SendName);
			}
		});
		contentPane.add(btnSend);
		
		JButton btnFile = new JButton("File");
		btnFile.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e)
		  {
		    JFileChooser chooser = new JFileChooser();
		    chooser.setMultiSelectionEnabled(false);
		    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    if (chooser.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
		    	String path = chooser.getSelectedFile().getAbsolutePath();
		    	String filename = chooser.getSelectedFile().getName();
		    	
		    	filepath = path;
		    	
		    	try{
		    		FileInputStream fis = new FileInputStream(filepath);
		    		inputStream.add(fis);
		    		sendNames.add(SendName);
		    		//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
		    		out.writeBytes("!m " + SendName + " " + name + " " + SendName + " wants to send you a file.");
		    		out.writeByte('\n');
		    		out.writeBytes("!f " + SendName + " " + name + " " + filename);
		    		out.writeByte('\n');
					out.flush();
					FileSendTo = SendName;
		    	}catch(Exception ex){
		    		System.out.println(ex);
		    	}
		    	textArea.append("Wait for " + SendName + "'s response...\n");
		    	
		    	
		    }
		  }
	    });
		btnFile.setBounds(374, 403, 87, 23);
		contentPane.add(btnFile);
		
		fileButton.add(btnFile);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	int index = userNames.indexOf(SendName);
		    	userNames.remove(index);
		    	privateMessage.remove(index);
		    	fileButton.remove(index);
		    	messagePanel.remove(index);
		    	
		    	try{
		    		FileWriter fw = new FileWriter("./" + name + "_" + SendName + "_log.txt");
	    	        fw.write(textArea.getText());
	    	        fw.flush();
	    	        fw.close();
		    	}catch(Exception e){
					e.printStackTrace();
				}
		    	
		    	frame.dispose();
		    }
		    
		});
		frame.setVisible(true);
		
	}
	 
	 public void renew()
	{
		contentPane.revalidate();
		contentPane.repaint();
	}
	public void renew(JPanel panel)
	{
		panel.revalidate();
		panel.repaint();
	}
	public void editChatRoom(String s)
	{
		chatRoom.append(s + "\n");
	}
	
	public void setUserNames(ArrayList<String> user)
	{
		allUserName = new ArrayList<String>(user);
		String[] s = user.toArray(new String[user.size()]);
		userList.setListData(s);
		renew(allUserPanel);
	}
	
	public void getPrivateMessage(String name, String message)
	{	int index = userNames.indexOf(name);
		if(index >= 0){
			privateMessage.get(index).append(message + "\n");
		}
		else{
			Message(name);
			index = userNames.indexOf(name);
			privateMessage.get(index).append(message + "\n");
		}
	}
	
	
	public void confirmWindow(String FromName, String fileName)
	{
		JDialog dialog = new JDialog ();
        dialog.setTitle("File Message");
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation (JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setBounds(100, 100, 336, 234);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.setContentPane(panel);
		panel.setLayout(null);
		
		JPanel textPanel = new JPanel();
		textPanel.setBounds(39, 28, 244, 51);
		textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(textPanel);
		
		JLabel lblAsk = new JLabel("ask");
		lblAsk.setBounds(49, 34, 46, 15);
		lblAsk.setText(FromName + " want to send you a file " + "\"" + fileName + "\"");
		textPanel.add(lblAsk);
		
		JButton btnYes = new JButton("Accept");
		btnYes.setBounds(49, 143, 87, 23);
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
					out.writeBytes("!y " + FromName + " " + name);
					out.writeByte('\n');
					out.flush();
				}catch(Exception e){
					System.out.println(e);
				}
				try{
					File yourFile = new File(fileName);
					if(!yourFile.exists()) {
					    yourFile.createNewFile();
					} 
					fos = new FileOutputStream(yourFile, false);
				}catch(Exception e){
					e.printStackTrace();
				}
				 
				JPanel temp = messagePanel.get(userNames.indexOf(FromName));
				for(Component component : temp.getComponents()) {
				    component.setEnabled(false);
				}
				dialog.dispose();
			}
		});
		panel.add(btnYes);
		
		JButton btnNo = new JButton("Reject");
		btnNo.setBounds(196, 143, 87, 23);
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
					out.writeBytes("!r " + FromName + " " + name);
					out.writeByte('\n');
					out.flush();
			
				}catch(Exception e){
					System.out.println(e);
				}
				dialog.dispose();
			}
		});
		panel.add(btnNo);
		
		dialog.setVisible(true);
	}
	
	public void rejected(String FromName)
	{
		int index = userNames.indexOf(FromName);
		privateMessage.get(index).append(FromName + " rejected your request\n");
		try{
			int i = sendNames.indexOf(FromName);
			inputStream.get(i).close();
			inputStream.remove(i);
			sendNames.remove(i);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void sendFile(String Sendname){
		byte[] buffer = new byte[4096];
		try{
			
			int i = sendNames.indexOf(Sendname);
			int read = 0;
			if((read = inputStream.get(i).read(buffer)) != -1){
				
				//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
				out.writeBytes("!c " + Sendname + " " + name);
				out.writeByte('\n');
				out.flush();
				
				//System.out.println("3333333");
				
				out.write(buffer, 0, read);
				out.flush();
				//System.out.println("123123");
				
			}else{
				//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
				out.writeBytes("!e " + Sendname + " " + name);
				out.writeByte('\n');
				out.flush();
				inputStream.get(i).close();
				inputStream.remove(i);
				sendNames.remove(i);
			}
			//outStream.close();
			//fis.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void writeFile(String FileFrom)
	{
		
		try{
		//	System.out.println("write file");
			//inStream = new BufferedInputStream(mySocket.getInputStream());
			byte[] buffer = new byte[4096];
			int noOfBytes = inStream.read(buffer);
			//System.out.println("after read");
			fos.write(buffer, 0, noOfBytes);
			//System.out.println("after write");
			//PrintWriter out = new PrintWriter(mySocket.getOutputStream());
			out.writeBytes("!y " + FileFrom + " " + name);
			out.writeByte('\n');
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void disableFileBurtton(String _name)
	{
		int index = userNames.indexOf(_name);
    	fileButton.get(index).setEnabled(false);
	}
	public void enableFileBurtton(String _name)
	{
		int index = userNames.indexOf(_name);
    	fileButton.get(index).setEnabled(true);
	}
	public void finishFile(String FromName)
	{
		JPanel temp = messagePanel.get(userNames.indexOf(FromName));
		for(Component component : temp.getComponents()) {
		    component.setEnabled(true);
		}
		try{
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


