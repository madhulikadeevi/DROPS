package com;
public class ServiceThread extends Thread{
	Server3 main;
public ServiceThread(Server3 main){
	this.main = main;
	start();
}
public void run(){
	main.start();
}
}