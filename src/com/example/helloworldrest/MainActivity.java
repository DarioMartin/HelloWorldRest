package com.example.helloworldrest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.my_button).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Button b = (Button) findViewById(R.id.my_button);
		b.setClickable(false);
		new LongRunningGetIO().execute();
	}
	
	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}

//		@Override
//		protected String doInBackground(Void... params) {
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpContext localContext = new BasicHttpContext();
//			HttpGet httpGet = new HttpGet("/chat-kata/api/chat");
//			String text = null;
//			try {
//				HttpResponse response = httpClient.execute(httpGet, localContext);
//				HttpEntity entity = response.getEntity();
//				text = getASCIIContentFromEntity(entity);
//			} catch (Exception e) {
//				return e.getLocalizedMessage();
//			}
//			return text;
//		}
		
		@Override
		protected String doInBackground(Void... params) {
			AndroidHttpClient client = AndroidHttpClient.newInstance("http.agent");
			String text = null;
			try {
				HttpResponse response = client.execute(new HttpHost("172.16.100.51", 8080), 
						new HttpGet("/chat-kata/api/chat"), new BasicHttpContext());
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (IOException e) {
				return e.getLocalizedMessage();
			}
			return text;
			
		}

		protected void onPostExecute(String results) {
			if (results != null) {
				EditText et = (EditText) findViewById(R.id.my_edit);
				et.setText(results);
			}
			Button b = (Button) findViewById(R.id.my_button);
			b.setClickable(true);
		}
	}
}


