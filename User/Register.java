package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Random;
public class Register extends JFrame
{	
	JPanel p1,p2;
	JLabel l1,l2,l3;
	JButton b1,b2;
	Font f1,f2;
	JTextField tf1,tf2,tf3;
	User users;
public int getRandom(){
	Random r = new Random();
	return r.nextInt(2);
}
public Register(User usr){
	users = usr;
	setTitle("Cloud User Screen");
	f1 = new Font("Courier New",Font.BOLD+Font.ITALIC,18);
	p1 = new JPanel();
    l1 = new JLabel("<HTML><BODY><CENTER>Securing Cloud Data under Key Exposure</CENTER></BODY></HTML>".toUpperCase());
	l1.setFont(this.f1);
    l1.setForeground(new Color(125,254,120));
    p1.add(l1);
    p1.setBackground(new Color(100,30,40));

    f2 = new Font("Courier New",Font.BOLD,14);
	
	p2 = new JPanel();
	p2.setLayout(null);

	l1 = new JLabel("Username");
	l1.setFont(f2);
	l1.setBounds(100,20,120,30);
	p2.add(l1);
	tf1 = new JTextField();
	tf1.setFont(f2);
	tf1.setBounds(220,20,120,30);
	p2.add(tf1);

	l2 = new JLabel("Password");
	l2.setFont(f2);
	l2.setBounds(100,70,120,30);
	p2.add(l2);
	tf2 = new JPasswordField();
	tf2.setFont(f2);
	tf2.setBounds(220,70,120,30);
	p2.add(tf2);

	l3 = new JLabel("Email ID");
	l3.setFont(f2);
	l3.setBounds(100,120,120,30);
	p2.add(l3);
	tf3 = new JTextField();
	tf3.setFont(f2);
	tf3.setBounds(220,120,120,30);
	p2.add(tf3);

	b1 = new JButton("Register");
	b1.setFont(f2);
	b1.setBounds(120,170,200,30);
	p2.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			register();
		}
	});

	b2 = new JButton("Back");
	b2.setFont(f2);
	b2.setBounds(340,170,100,30);
	p2.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			setVisible(false);
			users.setVisible(true);
		}
	});
	
	getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
}
public void register(){
	String user = tf1.getText();
	String pass = tf2.getText();
	String email = tf3.getText();
	if(user == null || user.trim().length() <= 0){
		JOptionPane.showMessageDialog(this,"Username must be enter");
		tf1.requestFocus();
		return;
	}
	if(pass == null || pass.trim().length() <= 0){
		JOptionPane.showMessageDialog(this,"Password must be enter");
		tf2.requestFocus();
		return;
	}
	if(email == null || email.trim().length() <= 0){
		JOptionPane.showMessageDialog(this,"Emailid must be enter");
		tf3.requestFocus();
		return;
	}
	if(!CheckMail.checkMail(email)){
		JOptionPane.showMessageDialog(this,"Enter valid mailid");
		tf3.requestFocus();
		return;
	}
	try{
		Socket socket = new Socket("18.191.143.106",1111);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		Object req[]={"userregister",user,pass,email};
		out.writeObject(req);
		out.flush();
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		Object res[] = (Object[])in.readObject();
		String response = (String)res[0];
		JOptionPane.showMessageDialog(Register.this,response);
		if(response.equals("user registration completed")){
			/**
			socket = new Socket("localhost",1111);
			out = new ObjectOutputStream(socket.getOutputStream());
			Object req1[]={"userregister",user,pass,email};
			out.writeObject(req1);
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			Object res1[] = (Object[])in.readObject();
			**/

			socket = new Socket("localhost",2222);
			out = new ObjectOutputStream(socket.getOutputStream());
			Object req2[]={"userregister",user};
			out.writeObject(req2);
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			Object res2[] = (Object[])in.readObject();

			socket = new Socket("localhost",3333);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(req2);
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			Object res3[] = (Object[])in.readObject();

			setVisible(false);
			JOptionPane.showMessageDialog(Register.this,response);
			users.setVisible(true);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
}