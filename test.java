package com;
import java.io.*;
import java.net.*;
public class test {
public static void main(String args[])throws Exception {
	Socket socket=new Socket("18.191.143.106",1111);
	ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
	Object req[]={"userlogin","aaa","aaa"};
	out.writeObject(req);
	out.flush();
	
	ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
	Object res[]=(Object[])in.readObject();
	String blocks =(String)res[0];
	System.out.println(blocks);
	
}
}