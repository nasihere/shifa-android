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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class activity_privatemsg extends Activity {
	 Context ctx;
		//ProgressDialog progressDialog ;
		//ProgressBar progressDialog ;

	 private boolean HomePage = true;
	 public DBHelper db1;
		private SimpleCursorAdapter CursorAdapter;
		public String[][] sHistory = new String[100][2];
		 DBclass KentDB = new DBclass();
	 String Selected = "";
	 public Cursor cursor;
	 PvtMsgAdapter userAdapter;
	 public int ListViewPostion = 0;
	 public int ListViewPostionTop = 0;
	 ArrayList<PvtMsgMain> userArray = new ArrayList<PvtMsgMain>();
	 public int iHit = 0; 
	 public boolean CategoryMenu = false;
	 public boolean Screen1 = false;
		public String Remedies;
		public String SessionID = "";
		public String SessionName  = "";
		public String ChatTextSend = "";
		public boolean chatactive = true;
		public String Session_id_to  = "";
		public String  session_id_to1 = ""; // this is for delete the chat
		
		String Category = "";
		boolean LongClick = false;
		private ListView myList;
		public  EditText tvChatSend;
		public boolean bProcessDialogShow = false;
		public String CachePublicResponse = "";
		public Parcelable state;
		public String CachePrivateResponse = "";
		public  Button btnSend;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privatemsg);
        ctx = this;
        myList = (ListView)findViewById(R.id.PvtMsgListView);
        
        	db1 = new DBHelper(this);
        	iHit++;
        	SessionID  = RestoreSessionIndexID("session_id");
        	SessionName = RestoreSessionIndexID("session_name");    	
    	 	tvChatSend = (EditText) findViewById(R.id.PvtChatPersonalmsgSend);
    	 	btnSend = (Button) findViewById(R.id.PvtChatPersonalviewBtnSend);
        	
    	 	
    	 	Bundle extras = getIntent().getExtras();
  		 
  		  if (extras != null) {
  			String  Screen = extras.getString("Screen");
  			session_id_to1 = extras.getString("session_id_to");
  			if (!session_id_to1.equals(""))
  			{
  				Session_id_to = session_id_to1;
  				PvtMsgReceivedAsync(Session_id_to); 
  				
  			}
  				
  		  }
  		  else
  		  {
  			  
  			ShowPublicMsg(RestoreSessionIndexID("ShowPublicMsg"));
  			Session_id_to= "";
  			PvtMsgReceivedAsync("");
  			  
  		  }
  		  
  		  
  		  
        	
    	 	
        	 for (int i=0; i < 1; i++)
             {
     		 	
                   Toast.makeText(this, "Loading.. Connecting internet.", Toast.LENGTH_SHORT).show();
             }
        	
        	
        	btnSend.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		  	try
	        		  	{
	        		  	    //  final EditText tvChatSend = (EditText) findViewById(R.id.PvtChatPersonalmsgSend);		  		  
	        		  		  ChatTextSend = tvChatSend.getText().toString();
	        		  		   if (ChatTextSend.trim().equals("")) return;
		        		  		/*userArray.add(new PvtMsgMain(Session_id_to,"","",SessionName,ChatTextSend,"1","Now"));
		        				Log.e("Pvt Msg, Adapter ", "Reached");
		        			
		        				PvtMsgPrivateAdapter userAdapter = new PvtMsgPrivateAdapter(ctx, R.layout.listview_pvt_chat,userArray,SessionID);
		        				Log.e("Pvt Msg, MyList", "Reached");
	
		        				myList.setItemsCanFocus(false);
		        				Log.e("Pvt Msg, SetAdapter", "Reached");
		        				myList.setAdapter(userAdapter);
		        				Log.e("Pvt Msg, Over", "Reached");
		        				myList.setSelection(userArray.size()-1);*/
	        		  		   
		        				tvChatSend.setText("");
		        				tvChatSend.setHint("Sending... wait..");
		        				
	        				  DownloadChatSend task = new DownloadChatSend();
	        				  task.execute(new String[] { "http://kent.nasz.us/app_php/privatemsg/InsertPrivateMessage.php"});
	        				  
	        		  	}
	        		  	catch(Exception ex){
	        		  		Toast.makeText(getApplicationContext(), "Internet not connected" , 100).show();
	        		  	}
	        	  }
	        });
	        		
        	
        	myList.setOnItemLongClickListener(new OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                        int pos, long id) {
                	 // TODO Auto-generated method stub
                	String session_id_to2 = ((TextView) arg1.findViewById(R.id.PvtMsgTvTimeSessionIdTo)).getText().toString();
                	final TextView input = new TextView(ctx);
	    			
	    			input.setTag(session_id_to2);
	    			input.setVisibility(View.GONE);
                	new AlertDialog.Builder(ctx)
                	.setView(input)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this messages?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                            // continue with delete
                        	DownloadPvtMsgReciever task = new DownloadPvtMsgReciever();
                        	session_id_to1 =  (String) input.getTag();
                        	if (!session_id_to1.equals(""))
                        	{
	        				  task.execute(new String[] { "http://kent.nasz.us/app_php/privatemsg/ActionPrivateMessage.php?type=2&session_id="+SessionID+"&session_id_to="+session_id_to1});
                        	}
	        				  
                        }
                     })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                            // do nothing
                        	
                        }
                     })
                    .setIcon(R.drawable.ic_pvtchat_delete)
                     .show();
                	
                	
                    return true;
                }
            }); 
        
        	
    	 	myList.setOnItemClickListener(new OnItemClickListener() {
	 			
    			@SuppressLint("DefaultLocale")
    			@Override
    			public void onItemClick(AdapterView<?> listView, View view, int position,
    					long id) {
    				
    					
    					try
    					{
    						Session_id_to  = ((TextView) view.findViewById(R.id.PvtMsgTvTimeSessionIdTo)).getText().toString();
    						if (!Session_id_to.equals(""))
    						{
    						bProcessDialogShow = true;
    						Toast.makeText(getApplicationContext(), "Please wait..." , 2000).show();		
    						//	progressDialog =  ProgressDialog.show(activity_privatemsg.this, "", 
    						//		  "Connecting internet... Please wait", true);
    						//progressDialog.setVisibility(View.VISIBLE);
    				    	
    						
    						userArray.clear();
    						
    				
    						PvtMsgReceivedAsync(Session_id_to); 
    						}
    					}
    					catch(Exception ex){}
    				

    				   
    				   
    			}
    			

    		
    	        
    			
    		 	
    	 	});

    }
    private class DownloadChatSend extends AsyncTask<String, Context, String> {
		
		@Override
	    protected String doInBackground(String... urls) {
			
			//progressDialog.setVisibility(View.VISIBLE);
			
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
			          nameValuePairs.add(new BasicNameValuePair("session_id", SessionID));
			          nameValuePairs.add(new BasicNameValuePair("session_id_to", Session_id_to ));
			          
			          nameValuePairs.add(new BasicNameValuePair("msg", ChatTextSend ));
			          nameValuePairs.add(new BasicNameValuePair("chatter", SessionName));
			          
			          
				    	ChatTextSend = "";
				    	
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
			      		
			        }
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
	    	//EditText tvChatSend = (EditText) findViewById(R.id.PvtChatPersonalmsgSend);
	    	//progressDialog.setVisibility(View.GONE);
	    	tvChatSend.setHint("Write a message");
		
	    	
	    }
	  }
    private String RestoreSessionIndexID(String SessionKey)
	{
		
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString(SessionKey, null);
		if (restoredText != null) 
		{
			return restoredText;
		}
		return "";

	}
    private void PvtMsgReceivedAsync(String SessionIdTo)
	{
    	Screen1 = true;
    	chatactive = true;
    	if (CachePublicResponse.equals("")) {
    		bProcessDialogShow = true;
    		//progressDialog =  ProgressDialog.show(activity_privatemsg.this, "", 
				//  "Connecting internet... Please wait", true);
    		Toast.makeText(getApplicationContext(), "Please wait..." , 2000).show();
    		//progressDialog.setVisibility(View.VISIBLE);
    		
    	}
		final String SessionIdTo1 = SessionIdTo;
		final Handler mHandler = new Handler();
		 new Thread(new Runnable() {
		        @Override
		        public void run() {
		        	
		            // TODO Auto-generated method stub
		               while(chatactive == true)
		               {
		            	  // chatactive = false;
			                try {
			                    mHandler.post(new Runnable() {
	
			                        @Override
			                        public void run() {
			                        	
			                        	DownloadPvtMsgReciever task = new DownloadPvtMsgReciever();
			                        	//SessionID = "652f21113d3d4d855621e09740c62583";
			                        	
		                        		if (Session_id_to.equals(""))
			                        	{
			                        		
			                        		task.execute(new String[] { "http://kent.nasz.us/app_php/privatemsg/ShowPrivateMsg.php?session_id="+SessionID});
	
			                        		
			                        	}
		                        		else if (!(Session_id_to.equals("") && SessionID.equals("")))
		                        		{
		                        			task.execute(new String[] { "http://kent.nasz.us/app_php/privatemsg/ShowPrivateMsg.php?session_id="+SessionID+"&session_id_to="+Session_id_to});
		                        			
		                        			
		                        		}
			                        }
			                    });
			                    Thread.sleep(3000);
			                } catch (Exception e) {
			                //	Toast.makeText(getApplicationContext(), e.toString(), 1000).show();
			                    // TODO: handle exception
			                }
		               }
	                    
		        }
		    }).start();
		
	     
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
        //userArray.clear();
        chatactive = true;
        Log.e("Chat Activity","user is back");
    }
	private class DownloadPvtMsgReciever extends AsyncTask<String, Context, String> {
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
	    
	    	try
	    	{
		    	if (result == null) return;
		    	if (result.trim().equals("")) return;
		    	if (chatactive == false) return;
		    	if (result.equals("-999")) 
		    		{
		    		
		    			
		    			return;
		    		}
		    	result = result.trim();
		    	if (result.substring(0,6).equals("Public") && Session_id_to.equals(""))
		    		ShowPublicMsg(result);
		    	else if (result.substring(0,7).equals("Private") && !Session_id_to.equals("")) 
		    		ShowPvtMsg(result);
	    	}
	    	catch(Exception ex)
	    	{
	    		
	    		
	    	}
	    		Log.e("Pvt Msg, PostExecute","Value of Session " + Session_id_to);
		    	 
		    	if (Session_id_to == null) Session_id_to = "";
		    	if (Session_id_to.equals(""))
	    		{
		    	//	progressDialog.dismiss();
	            	tvChatSend.setVisibility(View.GONE);
	            	btnSend.setVisibility(View.GONE);
	            	
	    		}
				else if (!Session_id_to.equals(""))
				{
					//progressDialog.dismiss();
					tvChatSend.setVisibility(View.VISIBLE);
		    		btnSend.setVisibility(View.VISIBLE);
		    		
				}
	    		Log.e("Pvt Msg, PostExecute","Value of Session " + Session_id_to);
	    	
	    }
	  }
	
	public void ShowPvtMsg(String result) // session_id_to1 is important to see 
	{
		try
		{
		  
		//Log.e("Response",result);
		result = result.replace("Private", "");
		if (result.indexOf("-,,-") == -1) return;
		
		
		
		
		
		String str = result;
    	String[] MsgSplit = str.split("-,,-");
    	
		Log.e("PVT MSG MsgSplit " , String.valueOf(MsgSplit.length));
		if ( MsgSplit.length == 0) return;
		
		String CurrentResult_id = MsgSplit[MsgSplit.length - 1].split("-,-")[7];
		Log.e("CurrentResult_id",CurrentResult_id);
		if (CachePrivateResponse.equals(""))
		{
			userArray.clear();
			Log.e("PVT MSG MsgSplit " , "Cache Set " +CachePrivateResponse ); 
		}
		else if (CurrentResult_id.equals(CachePrivateResponse)) 
		{
			Log.e("PVT MSG MsgSplit " , "Cache Returned");
			return;
		}
		else
		{
			int iIndex = str.lastIndexOf("-"+CachePrivateResponse  + "-,,-");
			iIndex = iIndex + CachePrivateResponse.length() + 5;
			Log.e("PVT MSG MsgSplit " , "iIndex " +   String.valueOf(iIndex));
				str = str.substring(iIndex);
			
			//str = str.substring(str.indexOf("-,,-")+4);
			Log.e("PVT MSG MsgSplit " , "str substing response "  + str);
		}
		
		boolean anychangeindataflag = false;
		 MsgSplit = str.split("-,,-");
		 if ( MsgSplit.length == 0) return;
		 
		 CachePrivateResponse = MsgSplit[MsgSplit.length - 1].split("-,-")[7];
		for(int i = 0 ; i <= MsgSplit.length -1 ; i++)
		{
			String[] MsgSubSplit = MsgSplit[i].split("-,-");
			
			Log.e("Pvt Msg, session_id ", MsgSubSplit[0]);
			Log.e("Pvt Msg, icon ", MsgSubSplit[1]);
			Log.e("Pvt Msg, status ", MsgSubSplit[2]);
			Log.e("Pvt Msg, chatter ", MsgSubSplit[3]);
			Log.e("Pvt Msg, msg ", MsgSubSplit[4]);
			Log.e("Pvt Msg, iRead ", MsgSubSplit[5]);
			Log.e("Pvt Msg, datetime ", MsgSubSplit[6]);
			
			userArray.add(new PvtMsgMain(MsgSubSplit[0],MsgSubSplit[1],MsgSubSplit[2],MsgSubSplit[3],MsgSubSplit[4],MsgSubSplit[5],MsgSubSplit[6]));
			anychangeindataflag = true;
			
		}
		if (anychangeindataflag ==false) return;
		Log.e("Pvt Msg, Adapter ", "Reached");
		
		PvtMsgPrivateAdapter userAdapter = new PvtMsgPrivateAdapter(this, R.layout.listview_pvt_chat,userArray,SessionID,Session_id_to);
		Log.e("Pvt Msg, MyList", "Reached");

		
		ListViewPostion = myList.getFirstVisiblePosition();
		View v = myList.getChildAt(0);
		ListViewPostionTop = (v == null) ? 0 : v.getTop();

		
		myList.setItemsCanFocus(false);
		Log.e("Pvt Msg, SetAdapter", "Reached");
		myList.setAdapter(userAdapter);
		Log.e("Pvt Msg, Over", "Reached");
		myList.setSelection(userArray.size()-1);
		tvChatSend.setHint("Write a message");

		
		}
		catch(Exception ex)
		{
			Log.e("Pvt Msg, Error",ex.toString());
			
		}
		Log.e("Pvt Msg, Done","True");
	}

	public void ShowPublicMsg(String result)
	{
		try
		{
		Screen1 = false;
		Log.e("Response",result);
		if (result.equals("")) return;
		SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
		editor.putString("ShowPublicMsg",result);
		editor.commit();
	 
		
		result = result.replace("Public", "");
		if (result.indexOf("-,,-") == -1) return;
		
		
	
		boolean anychangeindataflag = false;
		String str = result;
    	String[] MsgSplit = str.split("-,,-");
		Log.e("PVT MSG MsgSplit " , String.valueOf(MsgSplit.length));
		if ( MsgSplit.length == 0) return;
		
		boolean fresh = false;
		//String CurrentResult_id = MsgSplit[0].split("-,-")[1];
		if (CachePublicResponse.equals(""))
		{
			fresh = true;
			userArray.clear();
			Log.e("PVT MSG MsgSplit " , "Cache Set " +CachePublicResponse ); 
		}
		else if (str.equals(CachePublicResponse)) 
		{
			Log.e("PVT MSG MsgSplit " , "Cache Returned");
			//userArray.clear();
			return;
		}
		else
		{
			userArray.clear();
		}
		
		CachePublicResponse = str;
		
		for(int i = 0 ; i <= MsgSplit.length -1 ; i++)
		{
			anychangeindataflag = true;
			Log.e("PVT MSG MsgSplit " , "reached 1");
			String[] MsgSubSplit = MsgSplit[i].split("-,-");
			Log.e("PVT MSG MsgSplit " , "reached 2");
			Log.e("Pvt Msg, session_id ", MsgSubSplit[0]);
			Log.e("Pvt Msg, icon ", MsgSubSplit[1]);
			Log.e("Pvt Msg, status ", MsgSubSplit[2]);
			Log.e("Pvt Msg, chatter ", MsgSubSplit[3]);
			Log.e("Pvt Msg, msg ", MsgSubSplit[4]);
			Log.e("Pvt Msg, iRead ", MsgSubSplit[5]);
			Log.e("Pvt Msg, datetime ", MsgSubSplit[6]);
			
			userArray.add(new PvtMsgMain(MsgSubSplit[0],MsgSubSplit[1],MsgSubSplit[2],MsgSubSplit[3],MsgSubSplit[4],MsgSubSplit[5],MsgSubSplit[6]));
			
			
		}
		if (anychangeindataflag == false) return;
		Log.e("Pvt Msg, Adapter ", "Reached");
		
		userAdapter = new PvtMsgAdapter(this, R.layout.listview_privatemsg,userArray,SessionID);
		Log.e("Pvt Msg, MyList", "Reached");
		
		//if (fresh == false) state = myList.onSaveInstanceState();
		
		ListViewPostion = myList.getFirstVisiblePosition();
		View v = myList.getChildAt(0);
		ListViewPostionTop = (v == null) ? 0 : v.getTop();
		
		
		myList.setItemsCanFocus(false);
		Log.e("Pvt Msg, SetAdapter", "Reached");
		
		myList.setAdapter(userAdapter);
		
		Log.e("Pvt Msg, Over", "Reached");
		//if (fresh == true) myList.setSelection(0);
		myList.setSelectionFromTop(ListViewPostion, ListViewPostionTop);
		}
		catch(Exception ex)
		{
			SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
			editor.putString("ShowPublicMsg","");
			editor.commit();
		 
			
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if (keyCode == KeyEvent.KEYCODE_BACK) {
			   
			   if (!Session_id_to.equals(""))
			   {
				   tvChatSend.setVisibility(View.GONE);
	            	btnSend.setVisibility(View.GONE);
				   Session_id_to = "";
				   chatactive = false;
				   CachePrivateResponse = "";
				   CachePublicResponse = "";
				   
				   userArray.clear();
				   ShowPublicMsg(RestoreSessionIndexID("ShowPublicMsg"));
				   chatactive = true;
				   
			   }
			   else
			   {
				   chatactive = false;
		    	  Intent intent = new Intent(activity_privatemsg.this, home_menu.class);
		  		  startActivity(intent);
		  		  finish();
			   }
		    
		        return true;
		    }
	    return super.onKeyDown(keyCode, event);
	}
	
		

}
	