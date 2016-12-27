package com.shifa.kent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Super_Library_URL {
	 public String ChatTextSend = "";
	   
	   String chatRefID;
	   String SessionID;
	   String URL;
	   Activity parentActivity;	
	   Super_Library_AppClass SLAc; 
	   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
       
		public Super_Library_URL(String URL,    String SessionID,    List<NameValuePair> RequestValue , Activity parentActivity)
		{
			if (SessionID.equals("123456789"))
			{
				Toast.makeText(parentActivity, "You cannot use this section as guest. Please register or login as user..", 1000).show();
				
				return;
			}
			nameValuePairs  = RequestValue;
		     
	          
	          
			this.SessionID = SessionID;
			this.parentActivity = parentActivity;
			
			DownloadWebPageTask task = new DownloadWebPageTask();
			task.execute(new String[] { URL});	
		
		}
	private class DownloadWebPageTask extends AsyncTask<String, Context, String> {
		protected Context ctx;
		@Override
	    protected String doInBackground(String... urls) {
			
		  Log.e("doInBackground","enter");
	      String response = "";
	      String uri = "";
	      for (String url : urls) {
	    	  uri = url;
	    	  Log.e("uri",uri);
	    	  try {
			        DefaultHttpClient client = new DefaultHttpClient();
			        HttpPost httpGet = new HttpPost(url);
			        try {
			          httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			          HttpResponse execute = client.execute(httpGet);
			          InputStream content = execute.getEntity().getContent();
			          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			          String s = "";
			          while ((s = buffer.readLine()) != null) {
			            response += s;
			          }
			          
			        } catch (Exception e) {
			        	 Log.e("Error http:", e.toString());
			        	 e.printStackTrace();
			      		 return "-999";
			        }
			        return response;
			  }
	    	  catch(Exception ex)
	    	  {
	    		  Log.e("Error http:", ex.toString());
	    		  return "-999";
	    	  }
	      }
	   
	 
	    return "";
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    
	    	if (result.equals("-999")) 
	    		{
	    				
	    			return;
	    		}
	    	Toast.makeText(parentActivity, result, 1000).show();	
	    	
	    }
	  }
}
