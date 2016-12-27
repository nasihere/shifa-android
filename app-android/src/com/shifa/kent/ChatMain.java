package com.shifa.kent;

import java.security.Timestamp;
import java.sql.Date;
import java.util.Random;

import android.graphics.Color;

public class ChatMain {
	 String chat;
	 String frm;
	 Date dDateTime;
	 String _id;
	 String datetime;
	 String chatter;
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

		 
	 public ChatMain(String chat, String frm, String chatter,String _id, String datetime) {
	  super();
	  this._id = _id;
	  this.datetime = datetime;
	  
	  this.chat = chat + "<br><small><font color='"+getRandomColor()+"'>" + chatter+ "</font> <font size='2' color='#B0B0B0'>"  + datetime+ "</font></small>" ;
	  this.frm =  frm;
	  if (chatter.indexOf("Dr.") != -1)
	  {
		  this.Doctor = "Y";
	  }
	  
	  this.chatter =  chatter;
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