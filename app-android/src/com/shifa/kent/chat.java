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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class chat extends Activity {
	 Cursor cursor;
	  SimpleCursorAdapter CursorAdapter;
	  public String ChatTextSend = "";
	  Handler mHandler;
	 DBHelper db1;
	 ListView myList;
	 boolean autoscroll = true;
	 String RecentChat = "";
		
	 String ChatIndex;
	 boolean chatactive = true;
	 String PrevChat = "";
	 EditText txtChatMsg;
	 int iLoadMsg = 200;
	 boolean ChatSendEnterKeyHit = false;
	 ChatAdapter userAdapter;
	 ArrayList<ChatMain> userArray = new ArrayList<ChatMain>();
	public String SessionID = "";
	public String SessionName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		db1 = new DBHelper(this);
	 
		setContentView(R.layout.chat);
		
		SessionID  = RestoreSessionIndexID("session_id");
		SessionName = RestoreSessionIndexID("session_name");
		Log.e("SessionName ", SessionName );
		Log.e("SessionID",SessionID);
        
		final ImageView button1 = (ImageView) findViewById(R.id.imgbtnmsgsend);
        button1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChatSend(); } });
        
        myList = (ListView)findViewById(R.id.lstViewChat);
        ChatOpen();
        myList.setSelection(Integer.valueOf(ChatIndex));
        
        
        ChatReveiverAsync(ChatIndex,false);
		
		
		 myList.setOnItemClickListener(new OnItemClickListener() {
	 			
				@SuppressLint("DefaultLocale")
				@Override
				public void onItemClick(AdapterView<?> listView, View view, int position,
						long id) {
				/*	     String objValue = "";		
					     objValue  = ((TextView) view.findViewById(R.id.tv_chat_loadmsg)).getTag().toString();
					     
					     if (!objValue.equals(""))
					     {
					    	
					    	 int iChatIndex = Integer.valueOf(objValue) - 200;
					    	 
					    	 if (iChatIndex < 0) return;
					    	 iLoadMsg = iLoadMsg + 100;
					    	 userArray.clear();
					    	 ChatOpen();
					    	 myList.setSelection(50);
									
								
					    	 
					    	 
					    	 objValue =  String.valueOf(iChatIndex);
					    	 ChatReveiverAsync(objValue,true);
					    	 
					    	 Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
					     }
					     */
				}
			});
		 
		 
		 txtChatMsg = (EditText) findViewById(R.id.txtChatMsg);
		 txtChatMsg.setFocusableInTouchMode(true);
		 txtChatMsg.requestFocus();
		 
		 txtChatMsg.setOnKeyListener(new View.OnKeyListener() {
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        // If the event is a key-down event on the "enter" button
			        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
			            (keyCode == KeyEvent.KEYCODE_ENTER)) {
			        	ChatSend();
			          return true;
			        }
			        return false;
			    }
			});
		 
		 
			
		showads();
		
	}	
	private void ChatReveiverAsync(String cIndex, boolean bRange)
	{
		
		final String ChatIndex = cIndex;
		final boolean bFRange = bRange;
		mHandler = new Handler();
		 new Thread(new Runnable() {
		        @Override
		        public void run() {
		            // TODO Auto-generated method stub
		            while (chatactive == true) {
		                try {
		                    mHandler.post(new Runnable() {

		                        @Override
		                        public void run() {
		                        	String sRange = "";
		                        	if (bFRange == true) {
		                        		sRange = "&range=true";
		                        	}
		                        	DownloadChatReciever task = new DownloadChatReciever();
		        		    	 	task.execute(new String[] { "http://kent.nasz.us/app_php/pp_push_chat2.php?_id="+ChatIndex + "&chat=true" + sRange});
		                        }
		                    });
		                    Thread.sleep(3000);
			                 
		                } catch (Exception e) {
		           //     	Toast.makeText(getApplicationContext(), e.toString(), 1000).show();
		                    // TODO: handle exception
		                }
		                
		            }
		        }
		    }).start();
		 for (int i=0; i < 1; i++)
	        {
			 	
	              Toast.makeText(this, "Please wait.. Connecting internet...", Toast.LENGTH_LONG).show();
	        }
	     
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			chatactive = false;
	    	Intent intent = new Intent(chat.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
      		startActivity(intent);
      		finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@SuppressWarnings("deprecation")
	public void ChatOpen()
	{
		Log.e("ChatOpen","Chat open entered");
		cursor = db1.getReadableDatabase().rawQuery("select * from tbl_app_chat order by _id desc limit 0,1",null);
		Log.e("ChatOpen","query executed finish ");
		cursor.moveToFirst();
		Log.e("ChatOpen","Cursor movefirst");
		ChatIndex = cursor.getString(cursor.getColumnIndex("_id"));
		Log.e("ChatIndex ", ChatIndex );
		Log.e("ChatOpen","Cursor getting inde ");
		
		String q = "select _id,chat,frm,chatter,dt,datetime from tbl_app_chat where dt is null   and    _id >= "+ (Integer.valueOf(ChatIndex) - iLoadMsg) + " and _id <= "+ (Integer.valueOf(ChatIndex) + 1)+" order by _id asc limit 0, 1000";
		Log.e("QueryQuery",q);
		cursor = db1.getReadableDatabase().rawQuery(q,null);
	
		chatactive = true;
		try
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Log.e("Cursor Chatopen", "reading progress ");
				userArray.add(new ChatMain(cursor.getString(cursor.getColumnIndex("chat")), cursor.getString(cursor.getColumnIndex("frm")), cursor.getString(cursor.getColumnIndex("chatter")), cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("datetime"))));
			     cursor.moveToNext();
			}
		}
		catch(Exception ex)
		{
		//	Toast.makeText(getApplicationContext(), "Please wait.." + ex.toString() , 1000).show();
		}
		userAdapter = new ChatAdapter(this, R.layout.listview_chat,userArray,SessionID);
		

		myList.setItemsCanFocus(false);
		myList.setAdapter(userAdapter);
		myList.setSelection(Integer.valueOf(ChatIndex));
		
		
	}
	@Override
    protected void onStop() {
        super.onStop();
        chatactive = false;
        db1.close();
        finish();
        Log.e("Chat Activity","user not longer in the application");
        
    }


    @Override
    protected void onStart() {
        super.onStart();
        chatactive = true;
        Log.e("Chat Activity","user is back");
    }
	public void ChatSend()
	{
		ChatSendEnterKeyHit = true;	
		EditText txtChatMsg = (EditText) findViewById(R.id.txtChatMsg);
		
		String chat = txtChatMsg.getText().toString();
		txtChatMsg.setText("");
		txtChatMsg.setHint("Sending...");
		if (chat.trim().equals("")) return;
	
		 Log.e("ChatSend","enter");
		DownloadWebPageTask task = new DownloadWebPageTask();
		Log.e("DownloadWebPageTask","enter");

		chat = chat.replaceAll("'", "");
		chat = chat.replaceAll(",", " ");
		chat = chat.replaceAll(":", " ");
		ChatTextSend  = chat;
		
		task.execute(new String[] { "http://kent.nasz.us/app_php/pp_chat2.php"});	
		Log.e("task.execu","enter");
		
		
		
		//db1.getWritableDatabase().execSQL("insert into tbl_app_chat (chat,) values ('"+chat+"')");
		//ChatOpen();
		//userArray.add(new ChatMain(chat,SessionID));
		//userAdapter = new ChatAdapter(this, R.layout.listview_chat,userArray,SessionID);
		//myList.setAdapter(userAdapter);


		
			
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
			          
			          
			          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			          nameValuePairs.add(new BasicNameValuePair("_frm", SessionID));
			          nameValuePairs.add(new BasicNameValuePair("chat", ChatTextSend ));
			          nameValuePairs.add(new BasicNameValuePair("chatter", SessionName));
			          nameValuePairs.add(new BasicNameValuePair("to", "-999"));
			          
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
	    
	    	txtChatMsg.setHint("Write a message");
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
	private void ChatLog(String str)
	{
		if (str.equals("-999")) 
			{
			Toast.makeText(getApplicationContext(), "Internet or Server is down." , 3000).show();
			return;
			
			}
		Log.e("ChatLog","enter");
		//if (PrevChat.equals(str)) return;
		Log.e("ChatLog","enter real");
		PrevChat = str;
		//http://kent.nasz.us/app_php/pp_push_chat.php?_id=25
		boolean newchat = false;
		boolean toastrecent = false;
		String[] chatSplit = str.split("-:-");
		Log.e("ChatLog chatSplit " , String.valueOf(chatSplit.length));
			for(int i = chatSplit.length - 1 ; i >= 0; i--)
			{
				Log.e("ChatLog chatlog " , String.valueOf(i) + " - " + chatSplit[i]);
				String[] colchatsplit = chatSplit[i].split("-,-");
				
				Log.e("ChatLog ins", colchatsplit[0]);
				Log.e("ChatLog ins", colchatsplit[1]);
				Log.e("ChatLog ins", String.valueOf(colchatsplit.length));
				Log.e("ChatLog ins", "here");
				
				if (colchatsplit.length != 2)
				{
					
					Log.e("before ins ", String.valueOf(colchatsplit.length));
					String ins = "";
					if (colchatsplit[4].equals("-999"))
						ins = "insert into tbl_app_chat (chat,_id,frm,chatter,datetime) values ('"+colchatsplit[2]+"',"+colchatsplit[0]+",'"+colchatsplit[1]+"','"+colchatsplit[3]+"','"+colchatsplit[5]+"')";
					else
						ins = "insert into tbl_app_chat (chat,_id,frm,chatter,dt,datetime) values ('"+colchatsplit[2]+"',"+colchatsplit[0]+",'"+colchatsplit[1]+"','"+colchatsplit[3]+"','"+colchatsplit[4]+"','"+colchatsplit[5]+"')";
					Log.e("ins", ins);
					try
					{
						db1.getWritableDatabase().execSQL(ins);
						if (colchatsplit[4].equals("-999") )
						{
							userArray.add(new ChatMain(colchatsplit[2],colchatsplit[1],colchatsplit[3],colchatsplit[0],colchatsplit[5]));
							newchat = true;
						}
						
						ChatIndex = colchatsplit[0];
						String sNewRecentChat = "From:" + colchatsplit[3] + ": " + colchatsplit[2];
						Log.e("B currentPosition1 NewRecent Chat",sNewRecentChat);
						Log.e("B currentPosition1 RecentChat ",RecentChat);
						if (RecentChat.trim().toLowerCase().equals(sNewRecentChat.trim().toLowerCase())) 
								{
							
									RecentChat = "";
								}
						else
						{
							toastrecent = true;
							RecentChat  = sNewRecentChat;
						}
						
					}
					catch(Exception e)
					{
						Log.e("currentPosition1 ChatLog erro insert", e.toString());
						
					}
					
					
					
				}
			}
			Log.e("currentPosition1 ChatLog ChatIndex ", ChatIndex );
			SaveChatIndexID();
			
			int currentPosition = myList.getLastVisiblePosition();
			int currentPosition1 = myList.getCount() - 6;
			int iPosition = myList.getFirstVisiblePosition();

			Log.e("B currentPosition",String.valueOf(currentPosition));
			Log.e("B currentPosition1",String.valueOf(currentPosition1));
			Log.e("B iPosition",String.valueOf(iPosition));
			
			Parcelable state = myList.onSaveInstanceState();
			
			
			
			userAdapter = new ChatAdapter(this, R.layout.listview_chat,userArray,SessionID);
			myList.setAdapter(userAdapter);
			if (ChatSendEnterKeyHit == true || newchat == true)
			{
				 ChatSendEnterKeyHit = false;
				 myList.setSelection(userAdapter.getCount() - 1);
			}
			else
			{
				
				myList.onRestoreInstanceState(state);
				
			}
//			if (autoscroll == true) myList.setSelection(Integer.valueOf(ChatIndex));
			
	}
	private String LoggedIn(String Session_Key)
	{
		 Bundle extras = getIntent().getExtras();
		  String SessionID = "";
		  if (extras != null) {
		       SessionID = extras.getString(Session_Key);
		  }
		return SessionID;
	}
	private void SaveChatIndexID()
	{
  	  SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
  	  editor.putString("ChatIndex", ChatIndex);
  	  editor.commit();

	}
	private String RestoreSessionIndexID(String SessionKey)
	{
		
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString(SessionKey, null);
		if (restoredText != null) 
		{
			return restoredText;
		}
		return "1";

	}
	public void showads()
	{
		/*if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;

		 // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.MainChatView);

        // Add the adView to it
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        myList.setSelection(cursor.getCount() - 1);*/
		
	}
	 public String GetPreferenceValue(String StringName)
	    {
	    	SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString(StringName, null);
			if (restoredText != null) 
			{
				return restoredText;
			}
			return "0";
	    }
	
}
