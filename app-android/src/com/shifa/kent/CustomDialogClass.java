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
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialogClass extends Dialog implements
	android.view.View.OnClickListener {
	
	public Activity c;
	public Dialog d;
	public Button yes, no;
	public TextView tv_chat,tv_time,tv_chatter;
	public String SessionID;
	public String header;
	public String chat;
	public String time;
	public String chatter;
	public String chattitle;
	public String chatRefID;
	
	public CustomDialogClass(Activity a, String SessionID, String header, String chat, String time, String chatter, String chattitle, String chatRefID) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.header = header;
		this.SessionID = SessionID;
		this.chat = chat;
		this.time = time;
		this.chatter = chatter;
		this.chattitle = chattitle;
		this.chatRefID = chatRefID;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_popup_chat);
		yes = (Button) findViewById(R.id.tv_d_p_c_reply);
		no = (Button) findViewById(R.id.tv_d_p_c_cancel);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		
		ImageView img_reply = (ImageView) findViewById(R.id.tv_d_p_c_icon);
		tv_chat= (TextView) findViewById(R.id.tv_d_p_c_chat_show1);
		tv_time= (TextView) findViewById(R.id.tv_d_p_c_time);
		tv_chatter= (TextView) findViewById(R.id.tv_d_p_c_Name);
		
	
		
		tv_chat.setText(chat);
		tv_time.setText(time);
		tv_chatter.setText(chatter);
		
		 if (chatter.indexOf("Dr.") != -1)
		 {}
		  else
		  {
			  img_reply.setBackgroundResource(R.drawable.ic_chat_student);
			  
			}
		}
		catch(Exception ex) {}
			
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_d_p_c_reply:
			ChatSend();
			dismiss();
			 	break;
		case R.id.tv_d_p_c_cancel:
		  dismiss();
		  break;
		default:
			 
		  break;
		}
		dismiss();
	}
	
	
	/****************Chat Send ********************/
	  public void ChatSend()
		{
		  
			EditText txtChatMsg = (EditText) findViewById(R.id.tv_d_p_c_chatmsg);
			String chat = txtChatMsg.getText().toString();
			if (chat.trim().equals("")) return;
		
			 Log.e("ChatSend","enter");
			
			Log.e("DownloadWebPageTask","enter");

			chat = chat.replaceAll("'", "");
			chat = chat.replaceAll(",", " ");
			chat = chat.replaceAll(":", " ");
			String ChatTextSend= chat;
			txtChatMsg.setText("");
		
			Log.e("task.execu","enter");
			
			Super_Library_ChatSend SLCs = new Super_Library_ChatSend (chatter, ChatTextSend,   chatRefID, SessionID, c ); 
			
			//db1.getWritableDatabase().execSQL("insert into tbl_app_chat (chat,) values ('"+chat+"')");
			//ChatOpen();
			//userArray.add(new ChatMain(chat,SessionID));
			//userAdapter = new ChatAdapter(this, R.layout.listview_chat,userArray,SessionID);
			//myList.setAdapter(userAdapter);


			
				
		}
		
		/*********************************************/
}