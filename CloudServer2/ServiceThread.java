package com;
public class ServiceThread extends Thread{
	Server2 main;
public ServiceThread(Server2 main){
	this.main = main;
	start();
}
public void run(){
	main.start();
}
}