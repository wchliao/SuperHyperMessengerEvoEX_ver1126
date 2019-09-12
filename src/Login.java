import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Login extends JFrame {

	public JPanel contentPane;
	public JTextField txtUser;
	public JTextField txtPassword;
	
	public String name;
	public String password;
	public MainGUI mainGUI;
	public Login login;
	
	public Socket mySocket;
	
	public JLabel lblWarning;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		login = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 423, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(150, 65, 96, 21);
		txtUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getInput()){
					mainGUI = new MainGUI();
					
					mainGUI.start(name, password, login, mySocket);
				}
			}
		});
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(150, 121, 96, 21);
		txtPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getInput()){
					mainGUI = new MainGUI();
					
					mainGUI.start(name, password, login, mySocket);
				}
			}
		});
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(140, 40, 46, 15);
		contentPane.add(lblUser);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(140, 96, 64, 15);
		contentPane.add(lblPassword);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(getInput()){
					mainGUI = new MainGUI();
					
					mainGUI.start(name, password, login, mySocket);
				}

			}
		});
		btnLogin.setBounds(159, 152, 87, 23);
		contentPane.add(btnLogin);
		
		JButton btnChangepw = new JButton("Register");
		btnChangepw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				registerFrame();
				
			}
		});
		btnChangepw.setBounds(150, 185, 105, 23);
		contentPane.add(btnChangepw);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 221, 387, 31);
		contentPane.add(panel);
		
		lblWarning = new JLabel();
		lblWarning.setForeground(Color.RED);
		lblWarning.setFont(new Font("Vrinda", Font.PLAIN, 14));
		panel.add(lblWarning);
	}
	public void loginFailFrame()
	{
		JDialog dialog = new JDialog ();
        dialog.setTitle("Fail");
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
        dialog.setBounds(100, 100, 336, 234);
		
		//JFrame frame = new JFrame ();
        //frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        //frame.setSize(300, 250);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        dialog.getContentPane().add(panel);
        
        panel.add(new JLabel("Wrong Username or Password !"));
        
        dialog.setVisible(true);
        
	}
	public void warning(String warningMessage)
	{
		//lblWarning.setText(warningMessage);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lblWarning.setText(warningMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void registerFrame()
	{
		JDialog dialog = new JDialog ();
        dialog.setModal (true);
        dialog.setAlwaysOnTop (true);
        dialog.setModalityType (Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
        dialog.setBounds(100, 100, 336, 271);
		
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextField txtRegisteruser = new JTextField();
		txtRegisteruser.setBounds(167, 20, 96, 21);
		contentPane.add(txtRegisteruser);
		txtRegisteruser.setColumns(10);
		
		JTextField txtRegisterpw = new JTextField();
		txtRegisterpw.setBounds(167, 61, 96, 21);
		contentPane.add(txtRegisterpw);
		txtRegisterpw.setColumns(10);
		
		JTextField txtCheckpw = new JTextField();
		txtCheckpw.setBounds(167, 103, 96, 21);
		contentPane.add(txtCheckpw);
		txtCheckpw.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username : ");
		lblUsername.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		lblUsername.setBounds(49, 23, 79, 15);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		lblPassword.setBounds(49, 64, 79, 15);
		contentPane.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password: ");
		lblConfirmPassword.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		lblConfirmPassword.setBounds(49, 106, 119, 15);
		contentPane.add(lblConfirmPassword);
		
		JPanel panel = new JPanel();
		panel.setBounds(51, 141, 212, 23);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblWarningmessage = new JLabel();
		lblWarningmessage.setHorizontalAlignment(SwingConstants.LEFT);
		lblWarningmessage.setForeground(Color.RED);
		panel.add(lblWarningmessage);
		
		
		JButton btnYes = new JButton("Submit");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtRegisteruser.getText().length() == 0 || txtRegisterpw.getText().length() == 0 || txtCheckpw.getText().length() == 0 ){
					lblWarningmessage.setText("Empty username or password !");
				}
				else if(!txtRegisterpw.getText().equals(txtCheckpw.getText()) ){
					lblWarningmessage.setText("Please check your password again !");
				}
				else{
					try{
						mySocket = new Socket("localhost", 4444);
						PrintWriter out = new PrintWriter(mySocket.getOutputStream());
						out.println("!S");
						out.println(txtRegisteruser.getText());
						out.println(txtRegisterpw.getText());
						out.flush();
						Scanner in = new Scanner(mySocket.getInputStream());
						String RegisterInfo = in.nextLine();
						if(RegisterInfo.equals("!Y")){
							warning("Regist success !");
							dialog.dispose();
						}else if(RegisterInfo.equals("!N")){
							lblWarningmessage.setText("Username already exsits !");
						} 
					}catch(Exception e){
						System.out.println(e);
					}
				}
			}
		});
		btnYes.setBounds(51, 183, 87, 23);
		contentPane.add(btnYes);
		
		JButton btnNo = new JButton("Cancel");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		btnNo.setBounds(184, 183, 87, 23);
		contentPane.add(btnNo);
        
        dialog.setVisible(true);
        
	}
	
	public boolean getInput()
	{
		name = txtUser.getText();
		password = txtPassword.getText();
		
		/*if(name.length() == 0)
			name = " ";
		if(password.length() == 0)
			password = " ";*/
		
		try{
			mySocket = new Socket("localhost", 4444);
			PrintWriter out = new PrintWriter(mySocket.getOutputStream());
			out.println("!L");
			out.println(name);
			out.println(password);
			out.flush();
			Scanner in = new Scanner(mySocket.getInputStream());
			String loginInfo = in.nextLine();
			//System.out.println("loginInfo:" + loginInfo);
			if(loginInfo.equals("!R")){
				mySocket.close();
				warning("Already login !");
				//loginFailFrame();
				return false;
			}else if(loginInfo.equals("!p")){
				mySocket.close();
				warning("Wrong password !");
				//loginFailFrame();
				return false;
			} else if(loginInfo.equals("!u")){
				mySocket.close();
				warning("Wrong username !");
				//loginFailFrame();
				return false;
			} 
		}catch(Exception e){
			System.out.println(e);
			//return false;
		}
		return true;
	}
	
}
