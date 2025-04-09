package com;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.net.Socket;
import java.net.ServerSocket;
public class Server2 extends JFrame{
	JLabel l1;
	JPanel p1,p2,p3;
	Font f1;
	JScrollPane jsp;
	static JTextArea area;
	ServerSocket server;
	RequestHandler thread;
public void start(){
	try{
		server = new ServerSocket(2222);
		area.append("Server2 Service Started\n");
		while(true){
			Socket socket = server.accept();
			socket.setKeepAlive(true);
			thread = new RequestHandler(socket,area);
			thread.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public Server2(){
	super("Cloud Server Node2");
	p1 = new JPanel();
	p1.setPreferredSize(new Dimension(600,100));
	p1.setBackground(new Color(223,184,126,234));
	l1 = new JLabel("<html><body><center><br/>DROPS: Division and Replication of Data in Cloud for Optimal<br/>Performance and Security<br/></center></body></html>".toUpperCase()); 
	l1.setFont(new Font("Times New Roman",Font.BOLD,14));
	l1.setForeground(Color.blue);
	p1.add(l1);
	getContentPane().add(p1,BorderLayout.NORTH);

	f1 = new Font("Courier New",Font.BOLD,14);

	p2 = new JPanel();
	p2.setPreferredSize(new Dimension(800,100));
	p2.setLayout(new BorderLayout());
	
	area = new JTextArea();
	area.setFont(f1);
	area.setEditable(false);
	area.setLineWrap(true);
	jsp = new JScrollPane(area);
	p2.add(jsp,BorderLayout.CENTER);
	
	getContentPane().add(p2,BorderLayout.CENTER);
}
public static void main(String a[])throws Exception{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	Server2 main = new Server2();
	main.setVisible(true);
	main.setExtendedState(JFrame.MAXIMIZED_BOTH);
	new ServiceThread(main);
}
}