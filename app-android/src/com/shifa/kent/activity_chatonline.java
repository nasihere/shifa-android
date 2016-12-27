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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class activity_chatonline extends Activity {
	 Context ctx;
	 ProgressDialog progressDialog ;
	 String ChatIndex = "";
	 boolean ThreadStatus = false;
	 private boolean HomePage = true;
	public String RefreshQry = "select _id, chatter,dt,chat,frm,datetime from tbl_app_chat where  dt = '0' order by _id desc limit 0, 1000";
	 public DBHelper db1;
	 boolean chatactive = false;
	 ArrayList<ChatMainOnline> userArray = new ArrayList<ChatMainOnline>();
		private SimpleCursorAdapter CursorAdapter;

		public String[][] sHistory = new String[100][2];
		 DBclass KentDB = new DBclass();
	 String Selected = "";
	 int iLoadMsg = 200;
	 public Cursor cursor;
	 ChatAdapterOnline userAdapter;
	 public int iHit = 0; 
	 public boolean CategoryMenu = false;
		public String Remedies;
		String SessionID   = "";
		String SessionName = "";
		private ListView myList;

		
		boolean ChatSendEnterKeyHit = false;
		
		Handler mHandler;
		
		String RecentChat = "";
		String ChatTextSend= "";
		String PrevChat = "";
		String _to = "";
		
		@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatonline);
        ctx = this;
        SessionID  = RestoreSessionIndexID("session_id");
		SessionName = RestoreSessionIndexID("session_name");
		myList = (ListView)findViewById(R.id.lv_chatonline_result);
        	db1 = new DBHelper(this);
        	iHit++;
        	populatedatabase(RefreshQry,"Home",false,"");
        	chatactive = true;
			ChatReceiver("",false);
			
			
			for (int i=0; i < 2; i++)
	        {
				
	              Toast.makeText(this, "Please wait.. Connecting internet...", Toast.LENGTH_LONG).show();
	        }
	        
    	 
    	 	
    	     	
    	 
            final ImageView button1 = (ImageView) findViewById(R.id.imgbtnmsgsend);
            button1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { 
            	EditText ed = (EditText) findViewById(R.id.edChatOnlinePostQuickQuestion);
            	String value = ed.getText().toString();
            	if (value.length() <= 14)
            	{
            		 Toast.makeText(ctx, "Question for discussion should be atleast 15 character.", Toast.LENGTH_LONG).show();
            		return;
            	}
            	if (!value.trim().equals(""))
            	{
            		ed.setText("");
            		//Global_chatonline gc = new Global_chatonline(SessionID, SessionName, ctx, db1,chatactive);
    	  			ChatSend(value, "0");
    	  		
    	  			
            	}
            	
            } });
    	        
            myList.setOnItemClickListener(new OnItemClickListener() {
	 			
				@SuppressLint("DefaultLocale")
				@Override
				public void onItemClick(AdapterView<?> listView, View view, int position,
						long id) {
					     String objValue = "";		
					     objValue  = ((TextView) view.findViewById(R.id.tv_chatOnline_loadmsg)).getTag().toString();
					     if (!objValue.equals(""))
					     {
					    	
					    	 int iChatIndex = Integer.valueOf(objValue) - 200;
					    	 
					    	 if (iChatIndex < 0) return;
					    	 iLoadMsg = iLoadMsg + 200;
					    	 userArray.clear();
					    	 
					    	 myList.setSelection(iLoadMsg);
									
								
					    	 
					    	objValue =  String.valueOf(iChatIndex);
					    	
					    		 populatedatabase(RefreshQry,"Home",false,objValue);
					    	 
					    	 
					    	ChatReceiver(objValue,true);
					    	 
					    	 Toast.makeText(getApplicationContext(), "Please wait... ", Toast.LENGTH_LONG).show();
					     }
					     else
					     {
					    	 String _id = ((TextView) view.findViewById(R.id.tv_chatonline_id)).getText().toString();
		    					String chatter = ((TextView) view.findViewById(R.id.tv_chatonline_name)).getText().toString();
		    					Dialog(_id,chatter);
		    					
					     }
				}
			});
 
    			showAds();
    }

	


	
	public void Dialog(String _id, String chatter)
	{
		String Action = "" ;//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
			
			 
			final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
			final EditText input = new EditText(ctx);
			
			input.setText(Action);
			input.setTag(_id);
			alert.setTitle( "Reply by: " + SessionName );
		    alert.setView(input);
		    
		    
		    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
		    
		    
		    alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            
		        		String value = input.getText().toString().trim();
		        		String _id = input.getTag().toString();
		            
		      	  			//Global_chatonline gc = new Global_chatonline(SessionID, SessionName, ctx, db1,chatactive);
		      	  			ChatSend(value, _id);
		            
		        }
		    });

		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
		    
		    alert.show();  
		
	}	
	@Override
	  public void onDestroy() {
	    super.onDestroy();
	    cursor.close();
	    db1.close();
	  }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (ThreadStatus == true){return true;}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			chatactive = false;
			mHandler = null;
			
			Intent intent = new Intent(activity_chatonline.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
      		startActivity(intent);
      		
      		Log.e("Destroy","Start");
      		finish();
      		
      		Log.e("Destroy","end");
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public void populatedatabase(String sql, String ScreenLayout, boolean back, String cRefChatIndexFrom)
	{
		
		String cChatIndexTo = "";
		
		cursor = db1.getReadableDatabase().rawQuery("select * from tbl_app_chat where dt  = '0' order by _id desc limit 0,1",null);
		Log.e("ChatOpen","query executed finish ");
		cursor.moveToFirst();
		Log.e("ChatOpen","Cursor movefirst");
		cChatIndexTo = cursor.getString(cursor.getColumnIndex("_id"));
		Log.e("ChatIndex ", ChatIndex );
		Log.e("ChatOpen","Cursor getting inde ");
	
		
		if (cRefChatIndexFrom.equals(""))
		{
			cRefChatIndexFrom = cChatIndexTo;
		}
		
		sql = "select _id, chatter,dt,chat,frm,datetime from tbl_app_chat where  dt = '0' " +
    	 		" and    _id >= "+ (Integer.valueOf(cRefChatIndexFrom) - iLoadMsg) + " and _id <= "+ 
    			 (Integer.valueOf(cChatIndexTo) + 1) + " order by _id desc limit 0, 1000";
			
		Log.e("sql " , sql);
		
		 
		cursor = db1.getReadableDatabase().rawQuery(sql,null);
		int sCount = cursor.getCount();
		Log.e("SQL Count", String.valueOf(sCount));
		if (sCount == 0)
		{
			
			   iHit--;
			   return;
		}
		
		if (back == false)
		{
			sHistory[iHit][0] = sql;
			sHistory[iHit][1] = ScreenLayout;
		}
		
	
	/*		
			Log.e("ScreenLayout", "enter");
			String[] fromFieldName = new String[]{"chatter","chat","_id"};
			int[] toViewIds = new int[]{R.id.tv_chatonline_name,R.id.tv_chatonline_status,R.id.tv_chatonline_id};
			Log.e("Column adjust", "enter");
			CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_chatonline,cursor,fromFieldName,toViewIds);
			Log.e("Cursor Set", "enter");
			
			myList.setAdapter(CursorAdapter);
			
		*/
		Log.e("NewEntry","UserArrayClear");
		userArray.clear();
		cursor.moveToFirst();
		Log.e("NewEntry","Move First");
		while(!cursor.isAfterLast()) {
			Log.e("Cursor Chatopen", "reading progress ");
			Log.e("Cursor from",cursor.getString(cursor.getColumnIndex("frm")));
			
			userArray.add(new ChatMainOnline(
					this,
					db1,
					cursor.getString(cursor.getColumnIndex("chat")), 
					cursor.getString(cursor.getColumnIndex("frm")), 
					cursor.getString(cursor.getColumnIndex("chatter")),
					cursor.getString(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("datetime")),
					SessionID
					));
		     cursor.moveToNext();
		     Log.e("NewEntry","Loop");
		}
		userAdapter = new ChatAdapterOnline(this, R.layout.listview_chatonline,userArray,SessionID);
		Log.e("NewEntry","State");
		Parcelable state = myList.onSaveInstanceState();
		myList.setAdapter(userAdapter);
		myList.onRestoreInstanceState(state);
		Log.e("NewEntry","End");
		
	//
		
	}

	public void showAds()
	{
		
	/*	if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;
		  // Create the adView
   /*     adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainChatOnlineGoogleAdsMenu);

        // Add the adView to it
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);*/
		
	
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
	    
	    private void SaveChatIndexID()
		{
	  	  SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
	  	  editor.putString("ChatIndex", ChatIndex);
	  	  editor.commit();

		}
	    public void ChatSend(String ChatMsg, String _to) //R.id.txtChatMsg
		{
	    	EditText ed = (EditText) findViewById(R.id.edChatOnlinePostQuickQuestion);
	    	InputMethodManager imm = (InputMethodManager)getSystemService(
	    			Context.INPUT_METHOD_SERVICE);
	    	imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
	    	
			Toast.makeText(ctx, "Please wait....", 3000).show();
			ChatSendEnterKeyHit = true;	
			ChatMsg = ChatMsg.replaceAll("'", "");
			ChatMsg = ChatMsg.replaceAll(",", " ");
			ChatMsg = ChatMsg.replaceAll(":", " ");
			String chat = ChatMsg;
			this._to = _to;
			if (chat.trim().equals("")) return;
		
			 Log.e("ChatSend","enter");
			DownloadWebPageTask task = new DownloadWebPageTask();
			Log.e("DownloadWebPageTask","enter");
			ChatTextSend  = chat;
			task.execute(new String[] { "http://kent.nasz.us/app_php/pp_chat2.php"});	
			Log.e("task.execu","enter");
			
		
		}
		
		private class DownloadWebPageTask extends AsyncTask<String, Context, String> {
			protected Context ctx;
			@Override
		    protected String doInBackground(String... urls) {
				ThreadStatus = true;
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
		    	ThreadStatus = false;
		    	if (result.equals("-999")) 
		    		{
		    				
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


				return;
				
				}
			Log.e("ChatLog","enter");
			//if (PrevChat.equals(str)) return;
			Log.e("ChatLog","enter real");
			PrevChat = str;
			boolean newentry = false;
			//http://kent.nasz.us/app_php/pp_push_chat.php?_id=25
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
							if (!colchatsplit[4].equals("-999") )
							{

								
								ChatIndex = colchatsplit[0];
								newentry = true;	
							}
							
							
							
						}
						catch(Exception e)
						{
							Log.e("currentPosition1 ChatLog erro insert", e.toString());
							
						}
						
						
						
					}
				}
				Log.e("NewEntry",String.valueOf(newentry));
				SaveChatIndexID();
				try
				{
					if (newentry == true )
					{
						Log.e("NewEntry","Start");
						populatedatabase(RefreshQry,"Home",false,ChatIndex);
						Log.e("NewEntry","end");
					}
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
		public void ChatReceiver(String cIndex, boolean bRange)
		{
			
			
			if (cIndex.equals(""))
			{
				cursor = db1.getReadableDatabase().rawQuery("select * from tbl_app_chat order by _id desc limit 0,1",null);
				Log.e("ChatOpen","query executed finish ");
				cursor.moveToFirst();
				Log.e("ChatOpen","Cursor movefirst");
				cIndex = cursor.getString(cursor.getColumnIndex("_id"));
				Log.e("ChatIndex ", ChatIndex );
				Log.e("ChatOpen","Cursor getting inde ");
				
				
			
			}
			chatactive = true;
			final String ChatIndex = cIndex;
			final boolean bFRange = bRange;
		
			
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
			                        	String sRange = "";
			                        	if (bFRange == true) {
			                        		sRange = "&range=true";
			                        	}
			                        	DownloadChatReciever task = new DownloadChatReciever();
			        		    	 	task.execute(new String[] { "http://kent.nasz.us/app_php/pp_push_chat2.php?_id="+String.valueOf(ChatIndex) + "&chat=true" + sRange});
			        		    	 
			                        }
			                    });
			                } catch (Exception e) {
			                	
			                    // TODO: handle exception
			                }
			            }
			        }
			    }).start();
			 
		}
/************** Search bar code /******************************/

		@SuppressLint("NewApi")
		@Override
		  public boolean onCreateOptionsMenu(Menu menu) {
			if (OldPhone() == true )
			{
					super.onCreateOptionsMenu(menu);
				  MenuInflater mi = getMenuInflater();
				  mi.inflate(R.menu.discussion, menu);
				  return true;
			}
			else
			{
				getMenuInflater().inflate(R.menu.discussion, menu);
			    SearchView searchView = (SearchView) menu.findItem(R.id.menu_Discussion_refresh).getActionView();
			    
			    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			        @Override
			        public boolean onQueryTextChange(String newText) {
			        	
			        	 	try
				        	{
				        	userAdapter.getFilter().filter(newText);
							myList.setAdapter(userAdapter);
				        	}
				        	catch(Exception ex){ Log.e("Error app", "Error Action bar search");}
			        	 
			            return false;
			        }


			        @Override
			        public boolean onQueryTextSubmit(String query) {
			            if (query.length() != 0) {
			            	try
				        	{
			            		
				        	}
				        	catch(Exception ex){ Log.e("Error app", "Error Action bar search");}
			                // handle search here
			                return true;
			            }
			            return false;
			        }
			    });
			    
			    
				
			}
	        
			 return super.onCreateOptionsMenu(menu);
		  }
		
		
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId())
	        {
	            case R.id.menu_refresh:
	            	
	            	  
	            		  
	            	ChatOnlineSearch();
	            		
	            	return true;
	             
	            default:
	                return super.onOptionsItemSelected(item);

	        }
		}
		public void DialogSearchBar()
		{
			String Action = "";//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
				
				 
				final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
				final EditText input = new EditText(ctx);
				input.setText(Action);
				alert.setTitle( "Enter your Search..." );
			    alert.setView(input);
			    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            String value = input.getText().toString().trim();
				    	try
			        	{
			        	userAdapter.getFilter().filter(value);
						myList.setAdapter(userAdapter);
			        	}
			        	catch(Exception ex){ Log.e("Error app", "Error Action bar search");}
		        	
			            
			        }
			    });

			    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.cancel();
			        }
			    });
			    alert.show();  
			
		}
		public boolean OldPhone()
		{
			if (android.os.Build.VERSION.SDK_INT  == 7 || android.os.Build.VERSION.SDK_INT  == 8 || android.os.Build.VERSION.SDK_INT  == 9 || android.os.Build.VERSION.SDK_INT  == 10 || android.os.Build.VERSION.SDK_INT  == 11 || android.os.Build.VERSION.SDK_INT  == 12 || android.os.Build.VERSION.SDK_INT  == 13)
				return true;
			else
				return false;
		}
		public void ChatOnlineSearch()
		{
			 if (OldPhone() == true )
			  {
				
				 DialogSearchBar();
				
	           
				 
				  
			  }
			
		}
		/******************** Search Bar +***************************************************************/
}
	