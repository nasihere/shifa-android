package com.shifa.kent;

import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;

public class ChatMainOnline {
	 String chat;
	 String frm;
	 String dateTime;
	 String chatter;
	 String dt;
	 String _id;
	 String reply;
	 DBHelper db1;
	 activity_chatonline aco;
	 public String getDateTime(){
		 return dateTime;
	 }
	 public void setDateTime(String _datetime) {
		  this.dateTime= _datetime;
		 }
	 public String get_id() {
		  return _id;
		 }

		 public void set_id(String _id) {
		  this._id= _id;
		 }


		 
		 public String getreply() {
			  return reply;
			 }

			 public void setreply(String reply) {
			  this.reply= reply;
			 }
		 
		 
	 public String getchatter() {
		  return chatter;
		 }

		 public void setchatter(String chatter) {
		  this.chatter= chatter;
		 }


		 public String getdt() {
		  return dt;
		 }

		 public void setdt(String dt) {
		  this.dt= dt;
		 }

		 
		 
	 public String getChat() {
	  return   chat;
	 }

	 public void setChat(String chat) {
	  this.chat = chat;
	 }

	 public String getFrm() {
		  return frm;
		 }

		 public void setFrm(String frm) {
		  this.frm = frm;
		 }
		 public void DeletePost()
		 {
			 String qry = "UPDATE tbl_app_chat SET dt = '-1' where _id = "+_id ;
			 Log.e("DeletePost",qry);
			 db1.getWritableDatabase().execSQL(qry);
			 aco.populatedatabase(aco.RefreshQry, "Home", false,"");
			 
			 
		 }
	 public ChatMainOnline(activity_chatonline aco, DBHelper db1, String chat, String frm, String chatter, String _id, String dt, String SessionId) {
	  super();
	  this.aco = aco;
	  this.db1 = db1;
	  if (_id.equals("null"))
	  {
		  this.reply = "be the first to comment";	 
	  }
	  else
	  {
		  this.reply ="";
		  String sql = "select * from tbl_app_chat where dt = '"+_id+"' order by _id asc";
		  	Log.e("GetCursor",sql);
			Cursor cursor;
			cursor = db1.getReadableDatabase().rawQuery(sql,null);
			Log.e("GetCursor","Sent");
			if (cursor.getCount() != 0)
			{
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					String color = "green";
					Log.e("from = SessionId",frm + " = " + SessionId);
					
					
					if (SessionId.equals(cursor.getString(cursor.getColumnIndex("frm"))))
						color = "red";
					this.reply += "<p><font color='"+color+"'>" + cursor.getString(cursor.getColumnIndex("chatter")) + "</font><br>";
					this.reply +=  	 cursor.getString(cursor.getColumnIndex("chat")) + "</p>";
					
					cursor.moveToNext();
				}
				
			} 
	  }
	  this.chat = chat;
	  this.frm =  frm;
	  this.setDateTime(dt);
	  this.chatter =  chatter;
	  this._id = _id;
	  this.dt=  dt;
	 }

	}