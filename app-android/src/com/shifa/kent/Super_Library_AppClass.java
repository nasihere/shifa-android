package com.shifa.kent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Super_Library_AppClass {
	Context ctx;
 	public JSONArray jsonObject = null;
 	public String JsonString = "";
	public Super_Library_AppClass(Context ctx)
	{
		this.ctx = ctx;
	}
	 public String RestoreSessionIndexID(String SessionKey)
		{
			
			SharedPreferences prefs = ctx.getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString(SessionKey, null);
			if (restoredText != null) 
			{
				return restoredText;
			}
			return "1";

		}	
	 public String GetPreferenceValue(String StringName)
	    {
	    	SharedPreferences prefs =ctx.getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString(StringName, null);
			
			if (restoredText != null) 
			{
				Log.e("value pre " + StringName, restoredText);
				return restoredText;
			}
			return "0";
	    }
	 
	 
	   public void SavePreference(String StringName,String StringValue)
	    {
	    	
	    	SharedPreferences.Editor editor = ctx.getSharedPreferences("AppNameSettings",0).edit();
			editor.putString(StringName,StringValue);
		    Log.e("SetPreferenceValue " + StringName, StringValue);
		    editor.commit();
	    }
	   public boolean isPaidMember()
	   {
		   SharedPreferences prefs = ctx.getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString("RemoveAds", null);
			if (restoredText != null) // Register user 
			{
				return true;
			}
			else
			{
				return false;
					
			}
	   }
	   public void PostWebApi(String[] values){
		   new MyAsyncTask().execute(values);
	   }
	   public class MyAsyncTask extends AsyncTask<String, Integer, Double>{
			 
			@Override
			protected Double doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				if (params.length == 2) 
					postData(params[0],params[1],false);
				else
					postData(params[0],params[1],true);
				return null;
			}
	 
			protected void onPostExecute(Double result){
	//			pb.setVisibility(View.GONE);
				
			}
			protected void onProgressUpdate(Integer... progress){
//				pb.setProgress(progress[0]);
			}
	 
			public void postData(String valueIWantToSend, String URL, Boolean flagresponse) {
				// Create a new HttpClient and Post Header
				StringBuilder builder = new StringBuilder();
				if(!URL.endsWith("?"))
			        URL += "?";
				InputStream inputStream = null;
		        String result = "";
		        try {
		 
		            // create HttpClient
		            HttpClient httpclient = new DefaultHttpClient();
		            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		            params.add(new BasicNameValuePair("value", valueIWantToSend));
		            String paramString = URLEncodedUtils.format(params, "utf-8");
		            URL += paramString;
		            Log.e("url", URL);
					Log.e("url param", valueIWantToSend);
					    // make GET request to the given URL
		            HttpResponse httpResponse = httpclient.execute(new HttpGet(URL));
		            Log.e("url response flag" ,flagresponse.toString());
		            if (flagresponse == true){
		            	
	            	HttpEntity entity = httpResponse.getEntity();
	    			InputStream content = entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    			String line;
	    			while((line = reader.readLine()) != null){
	    				Log.e("jsonObject", line);	
	    				builder.append(line);
	    			}
	    			Log.e("jsonObject", "insert jsonObject");
	    			JsonString =  builder.toString();
	    			jsonObject = new JSONArray(builder.toString());
	    			Log.e("jsonObject", "received jsonObject");
		            }
		            
		        } catch (Exception e) {
		        	JsonString =  "";
		            jsonObject = null;
		            Log.e("url error", e.getLocalizedMessage());
		            Log.d("InputStream", e.getLocalizedMessage());
		            
		            
		        }
		 
			}
	 
		}
	public HttpClient MyAsyncTask() {
		// TODO Auto-generated method stub
		return null;
	}
}
