package ro.pub.cs.systems.pdsd.practicaltest02var01;
import Utils.ClientThread;
import Utils.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var01MainActivity extends Activity {
	//Server widgets
		private EditText serverPortEditText   = null;
		private Button  connectButton = null;
		private ServerThread serverThread   = null;
		private TextView     weatherForecastTextView  = null;
		private ClientThread clientThread             = null;
		private Button all = null;
		private Button temp = null;
		private Button hum = null;
	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private GetWeatherForecastButtonClickListener clk = new GetWeatherForecastButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e("App", "[MAIN ACTIVITY] Could not creat server thread!");
			}
			
		}
	}
	private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String clientAddress = "localhost";
			String clientPort    = "8085";
			if (clientAddress == null || clientAddress.isEmpty() ||
				clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Client connection parameters should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e("App", "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			 Button b = (Button)view;
			    String informationType = b.getText().toString();
			
			 
			weatherForecastTextView.setText("");
			
			clientThread = new ClientThread(
					clientAddress,
					Integer.parseInt(clientPort),					
					informationType,
					weatherForecastTextView);
			clientThread.start();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var01_main);
		weatherForecastTextView = (TextView)findViewById(R.id.text_view);
		serverPortEditText = (EditText)findViewById(R.id.editText1);
		connectButton = (Button)findViewById(R.id.button1);
		connectButton.setOnClickListener(connectButtonClickListener);
		all = (Button)findViewById(R.id.all);
		all.setOnClickListener(clk);
		temp = (Button)findViewById(R.id.temp);
		temp.setOnClickListener(clk);
		hum = (Button)findViewById(R.id.hum);
		hum.setOnClickListener(clk);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var01_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
