package com.unimelb.edu.au;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolServer implements Runnable 
{
	
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	private boolean isStopped = false;
	private Thread runningThread = null;
	
	ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	
	

	 private void openServerSocket() {
	        try {
	            serverSocket = new ServerSocket(7777);
	        } catch (IOException e) {
	            throw new RuntimeException("Cannot open port 7777", e);
	        }
	    }
	 
	 public synchronized void stop()
	 {
	        this.isStopped = true;
	        try 
	        {
	            serverSocket.close();
	        } 
	        catch (IOException e) 
	        {
	            throw new RuntimeException("Error closing server", e);
	        }
	  }
	 
	 private synchronized boolean isStopped() 
	 {
	        return this.isStopped;
	 }
	 
	@Override
	public void run() {
		// TODO Auto-generated method stub
			  
			   synchronized(this){
		            runningThread = Thread.currentThread();
		        }
			   
		        openServerSocket();
		        
		        while(! isStopped())
		        {
		            Socket clientSocket = null;
		            try 
		            {
		                clientSocket = serverSocket.accept();
		            } 
		            catch (IOException e) 
		            {
		                if(isStopped()) 
		                {
		                    System.out.println("Server Stopped.") ;
		                    return;
		                }
		                throw new RuntimeException("Error accepting client connection", e);
		            }
		            
		            this.threadPool.execute(
		                new WorkerRunnable(clientSocket));
		        }
		        this.threadPool.shutdown();
		        System.out.println("Server Stopped.") ;
		 }

}
	

