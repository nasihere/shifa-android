package com.shifa.kent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Global_chatonline {
	boolean ChatSendEnterKeyHit = false;
	String ChatIndex = "";
	activity_chatonline ChatOnline;
	Handler mHandler;
	
	String RecentChat = "";
	String ChatTextSend= "";
	String SessionID = "";
	boolean chatactive = false;
	String PrevChat = "";
	String SessionName = "";
	String _to = "";
	Context ctx ;
	DBHelper db1;
	public Global_chatonline(String SessionID,String SessionName, Context ctx,DBHelper db1,boolean chatactive)
	{
		this.SessionID = SessionID;
		this.SessionName = SessionName;
		this.ctx = ctx;
		this.db1 = db1;
		this.chatactive = chatactive;
		this.ChatIndex = RestoreSessionIndexID("ChatIndex"); // cursor.getString(cursor.getColumnIndex("_id"));
	}
	public void ChatSend(String ChatMsg, String _to) //R.id.txtChatMsg
	{
		Toast.makeText(ctx, "Please wait....", 1000).show();
		ChatSendEnterKeyHit = true;	
		String chat = ChatMsg;
		this._to = _to;
		if (chat.trim().equals("")) return;
	
		 Log.e("ChatSend","enter");
		DownloadWebPageTask task = new DownloadWebPageTask();
		Log.e("DownloadWebPageTask","enter");
		ChatTextSend  = chat;
		task.execute(new String[] { "http://kent.nasz.us/app_php/pp_chat.php"});	
		Log.e("task.execu","enter");
		
	
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
	    	  Log.e("global uri",uri);
	    	  try {
			        DefaultHttpClient client = new DefaultHttpClient();
			        HttpPost httpGet = new HttpPost(url);
			        try {
			          
			          
			          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			          nameValuePairs.add(new BasicNameValuePair("_frm", SessionID));
			          nameValuePairs.add(new BasicNameValuePair("chat", ChatTextSend ));
			          nameValuePairs.add(new BasicNameValuePair("chatter", SessionName));
			          nameValuePairs.add(new BasicNameValuePair("to", _to));
			          
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
			  }
	    	  catch(Exception ex)
	    	  {
	    		  Log.e("Error http:", ex.toString());
	    		  return "-999";
	    	  }
	      }
	   
	    if (uri.indexOf("pp_chat") != -1)
	    {
	    	return  "A" + response;
	    }
	    else if (uri.indexOf("pp_push_chat") != -1)
	    {
	    	return  "B" + response;
	    }
	    return "";
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    
	    	if (result.equals("-999")) 
	    		{
	    		chatactive = false;
	    				
	    			return;
	    		}
	    	Log.e("Response",result.substring(0,1));
	    	try{
	    		if (result.substring(0,1).equals("A")) // Send Chat Data
	    			Log.e("Response A",result.substring(1).trim());
	    		else
	    			{
	    				Log.e("Response B",result.substring(1).trim()); // Push Data
	    				ChatLog(result.substring(1).trim());
	    			}
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("error Chat msg",e.toString());
	    		
	    	}
	    }
	  }
	public void ChatLog(String str)
	{
		if (str.equals("-999")) 
			{
			Toast.makeText(ctx, "Internet or Server is down." , 3000).show();
			return;
			
			}
		Log.e("ChatLog","enter");
		//if (PrevChat.equals(str)) return;
		Log.e("ChatLog","enter real");
		PrevChat = str;
		//http://kent.nasz.us/app_php/pp_push_chat.php?_id=25
		boolean toastrecent = false;
		String[] chatSplit = str.split(":");
		Log.e("ChatLog chatSplit " , String.valueOf(chatSplit.length));
			for(int i = chatSplit.length - 1 ; i >= 0; i--)
			{
				Log.e("ChatLog chatlog " , String.valueOf(i) + " - " + chatSplit[i]);
				String[] colchatsplit = chatSplit[i].split(",");
				
				Log.e("ChatLog ins", colchatsplit[0]);
				Log.e("ChatLog ins", colchatsplit[1]);
				Log.e("ChatLog ins", String.valueOf(colchatsplit.length));
				Log.e("ChatLog ins", "here");
				
				if (colchatsplit.length != 2)
				{
					
					Log.e("before ins ", "before ins");
					String ins = "insert into tbl_app_chat (chat,_id,frm,chatter,dt) values ('"+colchatsplit[2]+"',"+colchatsplit[0]+",'"+colchatsplit[1]+"','"+colchatsplit[3]+"','"+colchatsplit[4]+"')";
					Log.e("ins", ins);
					try
					{
						db1.getWritableDatabase().execSQL(ins);
						
						
					}
					catch(Exception e)
					{
						Log.e("currentPosition1 ChatLog erro insert", e.toString());
						
					}
					
					
					
				}
			}
			
			SaveChatIndexID();
			try
			{
				ChatOnline.populatedatabase(ChatOnline.RefreshQry,"Home",false,"");
			}
			catch(Exception ex)
			{}
		
	}
	private class DownloadChatReciever extends AsyncTask<String, Context, String> {
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
			        Log.e("url",url);
			        HttpGet httpGet = new HttpGet(url);
			        try {
			          

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
			  }
	    	  catch(Exception ex)
	    	  {
	    		  Log.e("Error http:", ex.toString());
	    		  return "-999";
	    	  }
	      }
	   
	    if (uri.indexOf("pp_chat") != -1)
	    {
	    	return  "A" + response;
	    }
	    else if (uri.indexOf("pp_push_chat") != -1)
	    {
	    	return  "B" + response;
	    }
	    return "";
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    
	    	if (result.equals("-999")) 
	    		{
	    		chatactive = false;
	    			
	    			return;
	    		}
	    	Log.e("Response",result.substring(0,1));
	    	try{
	    		if (result.substring(0,1).equals("A")) // Send Chat Data
	    			Log.e("Response A",result.substring(1).trim());
	    		else
	    			{
	    				Log.e("Response DownloadChatReciever  B",result.substring(1).trim()); // Push Data
	    				
	    				ChatLog(result.substring(1).trim());
	    			}
	    	}
	    	catch(Exception e)
	    	{
	    		Log.e("error Chat msg",e.toString());
	    		
	    	}
	    }
	  }
	public void ChatReceiver()
	{
		
		mHandler = new Handler();
		 new Thread(new Runnable() {
		        @Override
		        public void run() {
		            // TODO Auto-generated method stub
		            while (chatactive == true) {
		                try {
		                	Log.e("ChatActive","Working....");
		                    Thread.sleep(3000);
		                    mHandler.post(new Runnable() {

		                        @Override
		                        public void run() {
		                        	DownloadChatReciever task = new DownloadChatReciever();
		        		    	 	task.execute(new String[] { "http://kent.nasz.us/app_php/pp_push_chat1.php?_id="+String.valueOf(ChatIndex)});
		        		    	 
		                        }
		                    });
		                } catch (Exception e) {
		                	Toast.makeText(ctx, e.toString(), 1000).show();
		                    // TODO: handle exception
		                }
		            }
		        }
		    }).start();
		 
	}
	private void SaveChatIndexID()
	{
  	  SharedPreferences.Editor editor = ctx.getSharedPreferences("AppNameSettings",0).edit();
  	  editor.putString("ChatIndex", ChatIndex);
  	  editor.commit();

	}
	private String RestoreSessionIndexID(String SessionKey)
	{
		SharedPreferences prefs = ctx.getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString(SessionKey, null);
		if (restoredText != null) 
		{
			return restoredText;
		}
		return "1";

	}
}
