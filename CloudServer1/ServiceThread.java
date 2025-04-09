package com;
public class ServiceThread extends Thread{
	Server1 main;
public ServiceThread(Server1 main){
	this.main = main;
	start();
}
public void run(){
	main.start();
}
}