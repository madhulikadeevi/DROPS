package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
public class DBCon{
	private static Connection con;
	static long time;
public static Connection getCon(){
	try{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost/drops","root","root");
	}catch(Exception e){
		e.printStackTrace();
	}
	return con;
}

public static String registerDataUser(String[] input)throws Exception{
	String msg="Error in data user registration";
    boolean flag=false;
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username from users where username='"+input[0]+"'");
    if(rs.next()){
        flag=true;
        msg = "Username already exist";
    }
    if(!flag){
		PreparedStatement stat=con.prepareStatement("insert into users values(?,?,?)");
		stat.setString(1,input[0]);
		stat.setString(2,input[1]);
		stat.setString(3,input[2]);
		int i=stat.executeUpdate();
		if(i > 0){
			msg = "user registration completed";
		}
		stat.close();
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static String UserLogin(String input[])throws Exception{
    String msg="fail";
    con = getCon();
    System.out.println(input[0]);
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username from users where username='"+input[0]+"' && password='"+input[1]+"'");
    if(rs.next()){
        msg = "success";
    }
	rs.close();stmt.close();con.close();
    return msg;
}
}
 