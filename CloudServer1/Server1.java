package com;
import java.util.ArrayList;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
public class Server1{
	
	ServerSocket server;
	RequestHandler thread;
public void start(){
	try{
		InetAddress addr = InetAddress.getByName("0.0.0.0");
		server = new ServerSocket(1111,50,addr);
		System.out.println("Server1 Service Started\n");
		while(true){
			Socket socket = server.accept();
			socket.setKeepAlive(true);
			thread = new RequestHandler(socket);
			thread.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static void main(String a[])throws Exception{
	Server1 main = new Server1();
	new ServiceThread(main);
}
}