package com;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
public class RequestHandler extends Thread{
	Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	
public RequestHandler(Socket soc){
	socket = soc;
	
	try{
		out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}

@Override
public void run(){
	try{
		Object input[]=(Object[])in.readObject();
        String type=(String)input[0];
		if(type.equals("userregister")){
			String user = (String)input[1];
			String pass = (String)input[2];
			String email = (String)input[3];
			String inputdata[]={user,pass,email};
			String msg = DBCon.registerDataUser(inputdata);
			System.out.println(msg+"\n");
			if(msg.equals("user registration completed")){
				File file = new File("CloudUsers/"+user);
				if(!file.exists())
					file.mkdir();
				Object res[] = {msg};
				out.writeObject(res);
				out.flush();
			}else{
				Object res[] = {msg};
				out.writeObject(res);
				out.flush();
			}
		}
		if(type.equals("userlogin")){
			String user = (String)input[1];
			String pass = (String)input[2];
			String inputdata[]={user,pass};
			String msg = DBCon.UserLogin(inputdata);
			if(msg.equals("success")){
				Object res[] = {msg};
				System.out.println(user+" User successfully login\n");
				out.writeObject(res);
				out.flush();
			}else{
				System.out.println(user+" User unsuccessfull login\n");
				Object res[] = {msg};
				out.writeObject(res);
				out.flush();
			}
		}
		if(type.equals("getblocks")){
			String user = (String)input[1];
			String file = (String)input[2];
			File fname = new File("CloudUsers/"+user+"/"+file);
			File list[] = fname.listFiles();
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<list.length;i++){
				sb.append(list[i].getName()+" 1111,");
			}
			if(sb.toString().length() > 0)
				sb.deleteCharAt(sb.length()-1);
			Object res[] = {sb.toString()};
			System.out.println("block names sent to client\n");
			out.writeObject(res);
			out.flush();
		}
		if(type.equals("filename")){
			String user = (String)input[1];
			File fname = new File("CloudUsers/"+user);
			File list[] = fname.listFiles();
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<list.length;i++){
				sb.append(list[i].getName()+",");
			}
			if(sb.toString().length() > 0)
				sb.deleteCharAt(sb.length()-1);
			String arr[] = sb.toString().trim().split(",");
			Object res[] = {arr};
			System.out.println("File names sent to client\n");
			out.writeObject(res);
			out.flush();
		}
		if(type.equals("download")){
			String user = (String)input[1];
			String file = (String)input[2];
			String block = (String)input[3];
			File fname = new File("CloudUsers/"+user+"/"+file+"/"+block);
			FileInputStream fin = new FileInputStream("CloudUsers/"+user+"/"+file+"/"+block);
			byte b[] = new byte[fin.available()];
			fin.read(b,0,b.length);
			fin.close();
			Object res[] = {b};
			System.out.println("block data sent to client\n");
			out.writeObject(res);
			out.flush();
		}
		if(type.equals("blocks")){
			String username = (String)input[1];
			String filename = (String)input[2];
			String blockname = (String)input[3];
			byte blockdata[] = (byte[])input[4];
			File file = new File("CloudUsers/"+username+"/"+filename);
			if(!file.exists())
				file.mkdir();
			FileOutputStream fout = new FileOutputStream(file.getPath()+"/"+blockname);
			fout.write(blockdata,0,blockdata.length);
			fout.close();
			String msg = blockname+" saved at server Cloud1";
			Object res[] = {msg};
			System.out.println(msg+"\n");
			out.writeObject(res);
			out.flush();
		}
	}catch(Exception e){
        e.printStackTrace();
    }
}
}
