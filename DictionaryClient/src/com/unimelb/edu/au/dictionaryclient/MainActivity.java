package com.unimelb.edu.au.dictionaryclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements Runnable
{
	Button btnSend,btnDialog;
	EditText etSend,etAns,etIP,etPort;
	Thread thread;
	int port;
	String ipAddress,ans;
	TextView txtWord,txtIP,txtPort;
	Dialog dialog;
	
	ActionBar actionBar;
	Typeface face;
	ColorDrawable d;
	
	Socket socket = null;
	SocketAddress socketAdd;
	DataOutputStream dataOutputStream = null;
	DataInputStream dataInputStream = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		face = Typeface.createFromAsset(getAssets(),"fonts/GlametrixLight.otf");
		
		actionBar = getSupportActionBar();
		d = new ColorDrawable(Color.parseColor("#12A5F4"));
		actionBar.setBackgroundDrawable(d);
		actionBar.setTitle("Dictionary Client");
		
		btnSend = (Button) findViewById(R.id.btnSend);
		etAns = (EditText) findViewById(R.id.etAns);
		etSend = (EditText) findViewById(R.id.etSend);
		txtWord = (TextView) findViewById(R.id.textView1);
		
		btnSend.setTypeface(face);
		etAns.setTypeface(face);
		etSend.setTypeface(face);
		txtWord.setTypeface(face);
		
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Enter Details");
		dialog.setCanceledOnTouchOutside(false);
		
		etPort = (EditText) dialog.findViewById(R.id.etPort);
		etIP = (EditText) dialog.findViewById(R.id.etIP);
		btnDialog = (Button) dialog.findViewById(R.id.btnDialog);
		txtIP = (TextView) dialog.findViewById(R.id.textView1);
		txtPort = (TextView) dialog.findViewById(R.id.textView2);
		
		etPort.setTypeface(face);
		etIP.setTypeface(face);
		btnDialog.setTypeface(face);
		txtIP.setTypeface(face);
		txtPort.setTypeface(face);
		
		btnDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				try {
						ipAddress = etIP.getText().toString();
						port = Integer.parseInt(etPort.getText().toString());
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				dialog.dismiss();
				
			}
		});

		dialog.show();
		
		
	btnSend.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
						thread = new Thread(MainActivity.this);
						thread.start();
			}
		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(item.getItemId() == R.id.menu)
			dialog.show();
		
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		Log.d("In RUN Method", "running");
		try 
		{
			socketAdd = new InetSocketAddress(ipAddress, port);
			socket = new Socket();
			socket.connect(socketAdd, 5000);
			
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream.writeUTF(etSend.getText().toString());
			
			
			ans = dataInputStream.readUTF();
			
			this.runOnUiThread(new Runnable() {
		        public void run() 
		        {
		        		try {
							etAns.setText(ans);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		         }
		     });
			
			
		}catch (UnknownHostException e) 
		{
			  // TODO Auto-generated catch block
			Log.d("TIMEOUT", "Unknown Host. Please check IP address!");
			
			this.runOnUiThread(new Runnable() {
		        public void run() 
		        {
		        	etAns.setText("Unknown Host. Please check IP address!");
		         }
		     });
			
		} 
		catch (IOException e) {
			  // TODO Auto-generated catch block
			
			
				 Log.d("TIMEOUT", "Could not make connection. Please check socket!");
				 
				 this.runOnUiThread(new Runnable() {
				        public void run() 
				        {
				        	etAns.setText("Could not make connection. Please check socket!");
				         }
				     });
				 
			 }
			catch(Exception e)
			{
				Log.d("TIMEOUT", "Exception");
			}
		finally
		{
			
			  if (socket != null)
			  {
				   try 
				   {
				    socket.close();
				   } 
				   catch (IOException e) 
				   {
				    e.printStackTrace();
				   }
			  }

			  if (dataOutputStream != null)
			  {
				   try 
				   {
				    dataOutputStream.close();
				   } 
				   catch (IOException e) 
				   {
				    e.printStackTrace();
				   }
			  }

			  if (dataInputStream != null)
			  {
				   try 
				   {
				    dataInputStream.close();
				   } 
				   catch (IOException e) 
				   {
				    e.printStackTrace();
				   }
			  }
		}
	
	
	}
	
	

}
