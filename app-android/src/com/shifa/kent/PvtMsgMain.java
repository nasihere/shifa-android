package com.shifa.kent;

import java.sql.Date;


public class PvtMsgMain {
	 String chat;
	 String frm;
	 String to;
	 Date dDateTime;
	 String _id;
	 String datetime;
	 String chatter;
	 String chatWithName;
	 String Status;
	 String Icon;
	 String iRead;
	 String Doctor = "N";
	 public String getchatter() {
		  return chatter;
		 }

		 public void setchatter(String chatter) {
		  this.chatter= chatter;
		 }

		 
	 public String getChat() {
	  return  chat;
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

		 
	 public PvtMsgMain(String SessionId, String Icon, String Status,String Chatter, String msg, String iRead, String datetime) {
	  super();
	  String sColor = getRandomColor();
	  this._id = SessionId;
	  this.datetime = datetime;
	  this.chatWithName = msg + "<br><small><font color='"+sColor +"'>" + Chatter+ "</font> <font size='2' color='#B0B0B0'>"  + datetime+ "</font></small>" ;
	  this.chat = msg;
	  
	  this.Status = Status;
	  this.Icon = Icon;
	  this.iRead = iRead;
	  this.chatter =  Chatter;
	 }
	 public static String getRandomColor() {
	     String[] letters = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	     String color = "#";
	     for (int i = 0; i < 6; i++ ) {
	         color += letters[(int) Math.round(Math.random() * 15)];
	     }
	     return color;
	 }
		
	
}