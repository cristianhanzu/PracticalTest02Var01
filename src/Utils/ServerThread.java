package Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class ServerThread extends Thread {
	
	private int          port         = 0;
	private ServerSocket serverSocket = null;
	private HashMap<String, String> data = null;
	private String hum = null;
	private String temp = null;
	public ServerThread(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e("App", "An exception has occurred: " + ioException.getMessage());
			 
				ioException.printStackTrace();
			 
		}
		this.data = new HashMap<String, String>();
		HttpClient httpClient = new DefaultHttpClient();					
		HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=Bucharest,ro");
		HttpResponse httpGetResponse;
		try {
			httpGetResponse = httpClient.execute(httpGet);
			HttpEntity httpGetEntity = httpGetResponse.getEntity();
			  if (httpGetEntity != null) {  			 
				   JSONObject content;				
					content = new JSONObject(httpGetEntity.toString());
					JSONObject weather =  content.getJSONObject("main");
					   temp = weather.getString("temp");
					   hum = weather.getString("humidity");
					   Log.e("APP temp",temp );
					   Log.e("APP hum",hum );
			  }
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			   		 
		                	
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setServerSocker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	public synchronized void setData(String info , String value) {
		this.data.put(info, value);
	}
	
	public synchronized HashMap<String, String> getData() {
		return data;
	}
	@Override
	public void run() {
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				Log.i("APP", "[SERVER] Waiting for a connection...");
				Socket socket = serverSocket.accept();
				Log.i("APP", "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				CommunicationThread communicationThread = new CommunicationThread(this, socket);
				communicationThread.start();
			}			
		} catch (ClientProtocolException clientProtocolException) {
			Log.e("APP", "An exception has occurred: " + clientProtocolException.getMessage());
		
				clientProtocolException.printStackTrace();
					
		} catch (IOException ioException) {
			Log.e("APP", "An exception has occurred: " + ioException.getMessage());
		
				ioException.printStackTrace();
			
		}
	}
	
	public void stopThread() {
		if (serverSocket != null) {
			interrupt();
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				Log.e("App", "An exception has occurred: " + ioException.getMessage());
				
					ioException.printStackTrace();
						
			}
		}
	}

}
