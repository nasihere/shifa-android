package com.shifa.kent;

import java.io.InputStream;
import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatAdapter extends ArrayAdapter<ChatMain> {
	Context context;
	
	int layoutResourceId;
	UserHolder holder = null;
	public ImageManager imageManager;
	String SessionID = "";
	ArrayList<ChatMain> data = new ArrayList<ChatMain>();
	private static final int TAG_IMAGE_BTN_1 = 0;
	private static final int TAG_IMAGE_BTN_2 = 1;
	
	public ChatAdapter(Context context, int layoutResourceId,
			ArrayList<ChatMain> data, String SessionID) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	
		this.context = context;
		
		this.data = data;
		
		this.SessionID = SessionID;
		imageManager = 
				new ImageManager(context.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
			View row = convertView;
			
			
		       
			if (row == null) {
			
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.txtChatLeft = (TextView) row.findViewById(R.id.view_chatmsg);
				holder.txtChatRight = (TextView) row.findViewById(R.id.view_chatmsgright);
				
				holder.imgChatIcoRightPM = (ImageView) row.findViewById(R.id.view_chaticorightPM);
				
				holder.imgChatIcoLeft = (ImageView) row.findViewById(R.id.view_chaticoleft);
				holder.imgChatIcoRight = (ImageView) row.findViewById(R.id.view_chaticoright);
				holder.txtLoadMsg = (TextView) row.findViewById(R.id.tv_chat_loadmsg);
			
				
				
			} else {
				holder = (UserHolder) row.getTag();
			}
	
			row.setTag(holder);
			ChatMain user = data.get(position);
			String sFrm = "";
			String sChat = "";
			try
			{
				Log.e("Chatadapter",SessionID);
				sFrm = user.getFrm().toString();
				sChat  = user.getChat().toString();
				Log.e("Chatadapter",sFrm);
			}
			catch(Exception e)
			{
				sFrm = "---";
				sChat = "";
				Log.e("Error listview",e.toString());
				
			}
			if (sFrm.equals("---"))
			{
				Log.e("Error listview","left -- " + sChat);
				holder.txtChatLeft.setVisibility(View.GONE);
				holder.imgChatIcoLeft.setVisibility(View.GONE);
					
				holder.txtChatRight.setVisibility(View.GONE);
				holder.imgChatIcoRight.setVisibility(View.GONE);
				holder.imgChatIcoRightPM.setVisibility(View.GONE);
			}
			else if (sFrm.equals(SessionID))
			{
				Log.e("Error listview","left -- " + sChat);
				holder.txtChatRight.setVisibility(View.GONE);
				holder.imgChatIcoRight.setVisibility(View.GONE);
				holder.imgChatIcoRightPM.setVisibility(View.GONE);
				
				
				
				holder.txtChatLeft.setVisibility(View.VISIBLE);
				holder.imgChatIcoLeft.setVisibility(View.VISIBLE);
				
				
				holder.txtChatLeft.setText(Html.fromHtml(sChat), TextView.BufferType.SPANNABLE);
				
				
			}
			else
			{
				Log.e("Error listview","Right -- " + sChat);
				holder.txtChatLeft.setVisibility(View.GONE);
				holder.imgChatIcoLeft.setVisibility(View.GONE);
				
				holder.txtChatRight.setVisibility(View.VISIBLE);;
				holder.imgChatIcoRight.setVisibility(View.VISIBLE);
				holder.imgChatIcoRightPM.setVisibility(View.GONE);
				
				holder.txtChatRight.setText(Html.fromHtml(sChat), TextView.BufferType.SPANNABLE);

				
			}
			
			if (user.Doctor.equals("Y"))
			{
				holder.imgChatIcoRight.setBackgroundResource(R.drawable.ic_chat_doctor);
				holder.imgChatIcoLeft.setBackgroundResource(R.drawable.ic_chat_doctor);
			}
			else
			{
				holder.imgChatIcoRight.setBackgroundResource(R.drawable.ic_chat_student);
				holder.imgChatIcoLeft.setBackgroundResource(R.drawable.ic_chat_student);
			}
			
			
			try
			{
				
				imageManager.displayImage(sFrm, holder.imgChatIcoRight, R.drawable.ic_launcher);
				imageManager.displayImage(sFrm, holder.imgChatIcoLeft, R.drawable.ic_launcher);
			}
			catch(Exception ex){}
			
			if (position == 0)
			{
				holder.txtLoadMsg.setVisibility(View.VISIBLE);
				holder.txtLoadMsg.setTag(user._id);
			}
			else
			{
				holder.txtLoadMsg.setVisibility(View.GONE);
				holder.txtLoadMsg.setTag("");
			}
			holder.imgChatIcoRight.setTag(R.string.ListPosition, user.frm);
			holder.txtLoadMsg.setTag(R.string.ListPosition, user.frm);
			 holder.imgChatIcoRight.setOnClickListener(new OnClickListener() {
				 
					@Override
					public void onClick(View v) {
						
						  	Log.e("sTag","Enter Correct");
						  	String sTag = (String) v.getTag(R.string.ListPosition);
						  	Log.e("sTag","Enter Correct 1");
			                Log.e("sTag",String.valueOf(sTag));
			                
			               Intent intent = new Intent(context, activity_privatemsg.class);
			  			   intent.putExtra("Screen", "1");
			  			   intent.putExtra("session_id_to", sTag);
			  			   context.startActivity(intent);
			  			  
		
					}
				});
				
			
		return row;

	}

	static class UserHolder {
		TextView txtChatLeft;
		TextView txtChatRight;
		ImageView imgChatIcoLeft;
		
		ImageView imgChatIcoRight;
		ImageView imgChatIcoRightPM;
		TextView txtLoadMsg;
	}
	
}
