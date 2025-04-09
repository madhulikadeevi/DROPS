package com;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.FlowLayout;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
public class DownloadFile extends JFrame
{
	JLabel l1;
	JButton b1;
	JComboBox c1;
	String user;

public DownloadFile(String usr){
	user=usr;
	setTitle("Download File");
	getContentPane().setBackground(Color.white);
	getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

	l1 = new JLabel("File Name");
	getContentPane().add(l1);

	c1 = new JComboBox();
	getContentPane().add(c1);

	b1 = new JButton("Download");
	getContentPane().add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			try{
				String file = c1.getSelectedItem().toString();
				File del_file = new File("E:/"+file);
				if(del_file.exists())
					del_file.delete();
				ArrayList<Blocks> list = new ArrayList<Blocks>();
				Socket socket=new Socket("18.191.143.106",1111);
				ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
				Object req[]={"getblocks",user,file};
				out.writeObject(req);
				out.flush();
				Object res[]=(Object[])in.readObject();
				String blocks =(String)res[0];
				String arr1[] = blocks.split(",");
				for(int i=0;i<arr1.length;i++){
					String temp[] = arr1[i].split("\\s+");
					Blocks bc = new Blocks();
					bc.setName(Integer.parseInt(temp[0]));
					bc.setPort(Integer.parseInt(temp[1]));
					list.add(bc);
				}


				socket=new Socket("localhost",2222);
				out=new ObjectOutputStream(socket.getOutputStream());
				in=new ObjectInputStream(socket.getInputStream());
				Object req1[]={"getblocks",user,file};
				out.writeObject(req1);
				out.flush();
				Object res1[]=(Object[])in.readObject();
				blocks =(String)res1[0];
				String arr2[] = blocks.split(",");
				for(int i=0;i<arr2.length;i++){
					String temp[] = arr2[i].split("\\s+");
					Blocks bc = new Blocks();
					bc.setName(Integer.parseInt(temp[0]));
					bc.setPort(Integer.parseInt(temp[1]));
					list.add(bc);
				}

				socket=new Socket("localhost",3333);
				out=new ObjectOutputStream(socket.getOutputStream());
				in=new ObjectInputStream(socket.getInputStream());
				Object req2[]={"getblocks",user,file};
				out.writeObject(req2);
				out.flush();
				Object res2[]=(Object[])in.readObject();
				blocks =(String)res2[0];
				String arr3[] = blocks.split(",");
				for(int i=0;i<arr3.length;i++){
					String temp[] = arr3[i].split("\\s+");
					Blocks bc = new Blocks();
					bc.setName(Integer.parseInt(temp[0]));
					bc.setPort(Integer.parseInt(temp[1]));
					list.add(bc);
				}
				java.util.Collections.sort(list,new Blocks());
				FileOutputStream fout=new FileOutputStream(del_file,true);
				for(int i=0;i<list.size();i++){
					Blocks bc = list.get(i);
					System.out.println(bc.getPort());
					if(bc.getPort() == 1111)
						socket=new Socket("18.191.143.106",1111);
					else
						socket=new Socket("localhost",bc.getPort());
					out=new ObjectOutputStream(socket.getOutputStream());
					in=new ObjectInputStream(socket.getInputStream());
					Object req4[]={"download",user,file,bc.getName()+""};
					out.writeObject(req4);
					out.flush();
					Object res4[]=(Object[])in.readObject();
					byte data[] =(byte[])res4[0];
					fout.write(data,0,data.length);
				}
				fout.close();
				JOptionPane.showMessageDialog(DownloadFile.this,"File downloaded in d directory");
				setVisible(false);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
}
public void getFileName(){
	try{
		 c1.removeAllItems();
		 Socket socket=new Socket("18.191.143.106",1111);
         ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		 ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
		 Object req[]={"filename",user};
		 out.writeObject(req);
		 out.flush();
		 Object res[]=(Object[])in.readObject();
		 String files[] =(String[])res[0];
		 for(int i=0;i<files.length;i++){
			 c1.addItem(files[i]);
		 }
		 out.close();
		 in.close();
		 socket.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}