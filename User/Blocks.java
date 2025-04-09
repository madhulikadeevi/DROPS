package com;
import java.util.Comparator;
public class Blocks implements Comparator<Blocks>{
	int name;
	int port;
public void setName(int name){
	this.name = name;
}
public int getName(){
	return name;
}

public void setPort(int port){
	this.port = port;
}
public int getPort(){
	return port;
}
public int compare(Blocks p1, Blocks p2){
	double s1 = p1.getName();
    double s2 = p2.getName();
	if (s1 == s2)
		return 0;
    else if (s1 > s2)
    	return 1;
    else
		return -1;
}
}