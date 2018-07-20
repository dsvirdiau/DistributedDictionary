package com.unimelb.edu.au;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
    static DataInputStream dataInputStream = null;
	static DataOutputStream dataOutputStream = null;
	Scanner scanner;
	String line;
	int lineNum = 0;
	String ans = "";
	
    public WorkerRunnable(Socket clientSocket) 
    {
        this.clientSocket = clientSocket;
    }
    

	@Override
	public void run() 
	{
		
			  try 
			  {
				   
			    dataInputStream = new DataInputStream(clientSocket.getInputStream());
			    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
			    String word = dataInputStream.readUTF().toString();
			    
			    URL url = getClass().getResource("Dictionary.txt");
			    File file = new File(url.getPath());
			    
			    System.out.println(word);
			    
			    try 
			    {
			        scanner = new Scanner(file);
			    } 
			    catch(FileNotFoundException e) 
			    { 
			    	System.out.println("File not found");
			    }
			    //now read the file line by line...
			    
			    while (scanner.hasNextLine()) 
			    {
			        line = scanner.nextLine();
			         //System.out.println(line);
			        lineNum++;
			        
			        if(line.toLowerCase().startsWith(word.toLowerCase()))
			        { 
			        	ans = ans + "\n\n" + line.toString();
			        }
			    }
			    
			    if (ans == "") 
			    	ans = "Word not found!!" ;
			    
			    dataOutputStream.writeUTF(ans);
			    
			   } 
			   catch (Exception e){}
//		 }
	}

}
