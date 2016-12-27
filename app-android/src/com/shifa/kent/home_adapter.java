package com.shifa.kent;


import java.util.ArrayList;

import android.util.Log;

public class home_adapter {

    public static ArrayList<home_modal> Items;

    public static void LoadModel(String ItemStatus, String AdsFreeStatus, String FlashStatus, String NewsStatus, String MyProfileStatus,boolean ads, String DiscussionStatus, String PrivateStatus, String SessionID) {
    	
    	if (PrivateStatus.equals("")) PrivateStatus = "<b>Loading..</b>";
    	if (DiscussionStatus.equals("")) DiscussionStatus = "<b>Loading..</b>";
    	if (AdsFreeStatus.equals("")) AdsFreeStatus = "<b>Loading..</b>";
    	if (FlashStatus.equals("")) FlashStatus = "<b>Loading..</b>";
    	if (NewsStatus.equals("")) NewsStatus = "<b>Loading..</b>";
    	
    	//15 no is highest 
        Items = new ArrayList<home_modal>();
        Items.add(new home_modal(1, "20", "Facebook Login Connect",SessionID));
        Items.add(new home_modal(2, "1", "Kent Repertory",ItemStatus));
        Items.add(new home_modal(3, "18", "Boenninghausen Repertory",ItemStatus));
        Items.add(new home_modal(4, "19", "Cyrus Maxwell Boger Repertory",ItemStatus));
        
        Items.add(new home_modal(5, "14", "Patient Management",ItemStatus));
        Items.add(new home_modal(6, "3", "Reversed Repertory",ItemStatus));
        Items.add(new home_modal(7, "2", "Materia Medica",ItemStatus));
        Items.add(new home_modal(8, "4", "Abbreviation",ItemStatus));
        Items.add(new home_modal(9, "6", "Organon",ItemStatus));
        Items.add(new home_modal(10, "15", "Private Message",PrivateStatus));
        Items.add(new home_modal(11, "12", "Discussion",DiscussionStatus));
        Items.add(new home_modal(12, "5", "Chat",ItemStatus));
        Log.e("Home screen progress ", "3.1.1.1.1");
        if (ads == true)
        {
        	Items.add(new home_modal(13, "7", "Buy Now",AdsFreeStatus));
        }
        else
        {
        	Items.add(new home_modal(13, "", "",""));
        }
        Log.e("Home screen progress ", "3.1.1.1.2");
        Items.add(new home_modal(14, "17", "Settings",ItemStatus));
        
        
        
        //Items.add(new home_modal(12, "8", "Logout",ItemStatus));
        int talka = (int)(Math.random() * 2); // between 0 and 1
        if (talka == 0){
        	Items.add(new home_modal(15, "21", "www.shifa.in",ItemStatus));
        }
        else
        {
        	Items.add(new home_modal(15, "21", "Shifa Facebook Page",ItemStatus));
        	
        }
        
    }

    public static home_modal setOnlineChatUser(String ChatOnline){

        for(home_modal item : Items) {
        	Log.e("id"," item.Id catch");
            if (item.Id == 5) {
            	Log.e("id","5 catch");
                item.setStatus(ChatOnline);
            }
        }
        return null;
    }
    public static home_modal GetbyId(int id){

        for(home_modal item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }

}