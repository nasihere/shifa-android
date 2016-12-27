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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Super_Library_Notification extends Activity {
	boolean chatactive = true;
	String notification_id = "";
int notifyid = 1;
	Super_Library_AppClass SLAc; 
	Handler mHandler;
	String SessionID  = "";
	Context ctx;
	Activity parentActivity = new Activity();
	LinearLayout baseIdNotification;
	public Super_Library_Notification(Context ctx, Activity c, LinearLayout baseIdNotification)
	{
		SLAc = new Super_Library_AppClass(ctx); 
		
		Nofification_Start();
		SessionID  = SLAc.RestoreSessionIndexID("session_id");
		this.ctx = ctx;
		parentActivity = c;
		this.baseIdNotification = baseIdNotification;

	
	}
	    /**************************************Notification **********************************?
	    * Chat
	    * NotificationAsyn 
	    */
	
	 public void Nofification_Start()
	   {
		    notification_id = SLAc.GetPreferenceValue("Notify_id");
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
			                        	DownloadChatReciever task = new DownloadChatReciever();
			        		    	 	task.execute(new String[] { "http://kent.nasz.us/app_php/app_notification_show.php?session_id=3"+SessionID });
			        		    	 	
			                        }
			                        
			                    });
			                    Thread.sleep(10000);
			                } catch (Exception e) {
			                	Toast.makeText(parentActivity, e.toString(), 1000).show();
			                    // TODO: handle exception
			                }
			            }
			        }
			    }).start();
			

	   }
	  /******************************* Chat Receiver Code Start*******************************************/
	   private class DownloadChatReciever extends AsyncTask<String, Context, String> {
			protected Context ctx;
			@Override
		    protected String doInBackground(String... urls) {
				
			     String response = "";
			      String uri = "";
			      for (String url : urls) {
			    	  uri = url;
			    	  try {
					        DefaultHttpClient client = new DefaultHttpClient();
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
					      		return "-999";
					        }
					  }
			    	  catch(Exception ex)
			    	  {
			    		  Log.e("Error http:", ex.toString());
			    		  return "-999";
			    	  }
			      }
			   
			    return response;
			    }

		    @Override
		    protected void onPostExecute(String result) {
		  	if (result.equals("-999")) 
		    		{
		  			chatactive = false;
		    			
		    			return;
		    		}
		    	try{
		    		if (result.equals("")) return; 
		    	
		    		String[] sSplit = result.split(",");
		    		
		    		String tmp =  sSplit[0];
		    		if (!tmp.equals(notification_id) )
		    		{
		    			   		notification_id   = tmp;
		    			
		    		
		    			View child = parentActivity.getLayoutInflater().inflate(R.layout.dialog_popup_notifation_chat, null);
		    			TextView tv_chat= (TextView) child.findViewById (R.id.tv_d_p_n_c_chat);
		    			TextView tv_chat_title= (TextView) child.findViewById (R.id.tv_d_p_c_chat_show1);
		    			TextView tv_chatter= (TextView) child.findViewById (R.id.tv_d_p_n_c_chatter);
		    			ImageView img_reply = (ImageView) child.findViewById (R.id.tv_d_p_n_c_img_reply);
		    			ImageView img_icon = (ImageView) child.findViewById (R.id.tv_d_p_n_c_icon);
		    			tv_chat.setText( ": " + sSplit[1]);
		    			tv_chatter.setText(sSplit[2]);
		    			tv_chat_title.setText(sSplit[3]);
		    			baseIdNotification.removeAllViews();
		    			if (sSplit[2].indexOf("Dr.") != -1)
		    			{}
		    			else
		    			{
		    				img_icon.setBackgroundResource(R.drawable.ic_chat_student);
		    			}
		    				
		    				
		    			img_reply.setTag(result);
		    			img_reply.setOnClickListener(new OnClickListener() {
		   				 
		   					@Override
		   					public void onClick(View v) {
		   						String result =v.getTag().toString();
		   						String[] sSplit = result.split(",");
		   			    		try
		   			    		{
		   						CustomDialogClass cdd=new CustomDialogClass(parentActivity,SessionID ,"chat",sSplit[1],sSplit[3],sSplit[2],sSplit[3],sSplit[4]);
		   						cdd.show();
		   			    		}
		   			    		catch(Exception ex){}
		   					}
		   				});
		   			
		    	        baseIdNotification.addView(child);
		    	        notifyid = 1;
		    	        SLAc.SavePreference("Notify_id",notification_id);
		    	        
		    		}
		    	
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("error Chat msg",e.toString());
		    		Toast.makeText(parentActivity, e.toString(), 1000).show();
		    	}
		    }
		  }
	  /******************************* Chat Receiver Code End *******************************************/
	   
}
