package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
 

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class CommunicationThread extends Thread {
	
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					String informationType = bufferedReader.readLine();
					Log.i("APP", "[COMMUNICATION THREAD] Waiting for parameters from client (information type)!");					
					Log.i("APP", "[COMMUNICATION THREAD] Getting the information from the webservice...");
					HttpClient httpClient = new DefaultHttpClient();					
					HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=Bucharest,ro");
					HttpResponse httpGetResponse = httpClient.execute(httpGet);
					HttpEntity httpGetEntity = httpGetResponse.getEntity();
					  if (httpGetEntity != null) {  			 
						   JSONObject content = new JSONObject(httpGetEntity.toString());
						   JSONObject weather =  content.getJSONObject("main");
						   String temperature = weather.getString("temp");
						   String humidity = weather.getString("humidity");	
						   if(informationType.compareTo("all")==0 || informationType.compareTo("humidity")==0 ){
						   serverThread.setData("Humidity", humidity);
						   }
						   if(informationType.compareTo("all")==0 || informationType.compareTo("temp")==0 ){
						   serverThread.setData("Temperature", temperature);
						   }
					  }              
					  else {
							Log.e("APP", "[COMMUNICATION THREAD] Error getting the information from the webservice!");
						} 					
				} else {
					Log.e("APP", "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e("APP", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				 
					ioException.printStackTrace();
				 
			} catch (JSONException jsonException) {
				Log.e("APP", "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
				 
					jsonException.printStackTrace();
				 			
			}
		} else {
			Log.e("APP", "[COMMUNICATION THREAD] Socket is null!");
		}
	}

}
