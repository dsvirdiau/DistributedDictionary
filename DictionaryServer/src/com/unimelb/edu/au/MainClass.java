package com.unimelb.edu.au;


public class MainClass 
{

	public static void main(String args[])
	{
		
		ThreadPoolServer server = new ThreadPoolServer();
		new Thread(server).start();

//		try {
//		    Thread.sleep(20 * 1000);
//		} catch (InterruptedException e) {
//		    e.printStackTrace();
//		}
//		System.out.println("Stopping Server");
//		server.stop();
		
		      
	}
	
}
