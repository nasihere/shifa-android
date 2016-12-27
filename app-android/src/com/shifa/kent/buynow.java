package com.shifa.kent;



import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class buynow extends Activity{
	 Context ctx;
	 boolean bBuyNow  = false;
	 Super_Library_AppClass SLAc; 
	 public String SessionID = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BuyNow","---------------Start--------------");
        setContentView(R.layout.buynow);
        ctx = this;
        SLAc = new Super_Library_AppClass(this);
        SessionID  = SLAc.RestoreSessionIndexID("session_id");
        Log.e("BuyNow","---------------2--------------");
        TextView tvBuyNowActivateTimeLeft = (TextView)  findViewById(R.id.tvCancelButtonNotify);
        int BuyNowLimit = BuyNowPopupActivate();
        if(BuyNowLimit >= 2 && BuyNowLimit <= 10) BuyNowLimit = 2;
        if(BuyNowLimit >= 10 && BuyNowLimit <= 15) BuyNowLimit = 5;
        if(BuyNowLimit >= 16 && BuyNowLimit <= 30) BuyNowLimit = 10;
        if(BuyNowLimit >= 31 && BuyNowLimit <= 40) BuyNowLimit = 20;
        if(BuyNowLimit >= 41 && BuyNowLimit <= 50) BuyNowLimit = 25;
        if(BuyNowLimit >= 51 && BuyNowLimit <= 60) BuyNowLimit = 30;
        if(BuyNowLimit >= 61 && BuyNowLimit <= 70) BuyNowLimit = 35;
        if(BuyNowLimit >= 71) BuyNowLimit = 40;
        BuyNowLimit = 2;
        Log.e("BuyNow","---------------3--------------");
        tvBuyNowActivateTimeLeft.setText("Wait!!! Cancel button will enabled after " + (BuyNowLimit) + " Secs");
        if (BuyNowLimit < 0) bBuyNow = true;
        	
      
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	if (bBuyNow == true  ) return;
	            	SharedPreferences prefs = getPreferences(0);
	                long  lastUpdateTime = prefs.getLong("lastUpdateTimeBuyNow", 0);
	           		
	            	lastUpdateTime = System.currentTimeMillis();
	       			SharedPreferences.Editor editor = getPreferences(0).edit();
	    		    editor.putLong("lastUpdateTimeBuyNow", lastUpdateTime);
	    		    editor.commit();
	    		   
	    		    
	            	final Button button = (Button) findViewById(R.id.itm_buynw_5);
	            	button.setEnabled(true);
	            	/*if (interstitialAd.isReady()) {
	    				interstitialAd.show();
	    			}*/
	            	
	            	
	            }
	        }, (BuyNowLimit * 1000));
        
	        final Button button1 = (Button) findViewById(R.id.itm_buynw_3);
	        button1.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		  bBuyNow = true;
	      			Intent intent1 = new Intent(buynow.this, home_menu.class);
	    			intent1.putExtra("OpenBuyNow", "true");
	    	  		startActivity(intent1);
	    	  		finish();
	    	  			
	        		 
	        	  }

	        	});
	   
	        final Button button2 = (Button) findViewById(R.id.itm_buynw_5);
	        button2.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		  bBuyNow = true;
	      			Intent intent1 = new Intent(buynow.this, login.class);
	      			intent1.putExtra("CancelBuyNow", "true");
	    			startActivity(intent1);
	    	  		finish();

	        		 
	        	  }

	        	});
	        
	        final Button button3 = (Button) findViewById(R.id.itm_buynw_help);
	        button3.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		 
	        		  Intent intent = new Intent(buynow.this, activity_events.class);
	        		  intent.putExtra("url", "http://kent.nasz.us/app_php/howtouseshifa/howtouseshifa.php?request=buynow");
	        		  startActivity(intent);
	        	
	        		 
	        	  }

	        	});
	             
	        final Button button4 = (Button) findViewById(R.id.itm_buynw_web_4);
	        if (SLAc.isPaidMember() == true) button4.setText("Donate"); 
	        	
	        button4.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		 
	        		  Intent intent = new Intent(buynow.this, activity_events.class);
	        		  if (SLAc.isPaidMember() == true) 
	        			  intent.putExtra("url", "http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id="+SessionID+"&ispaid=true");
	        		  else 
	        			  intent.putExtra("url", "http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id="+SessionID);
	        		  startActivity(intent);
	        	
	        		 
	        	  }

	        	});
	        
	        final Button button5 = (Button) findViewById(R.id.itm_buynw_tellafriend);
	        if (SLAc.isPaidMember() == true) button4.setText("Donate"); 
	        	
	        button5.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		 
	        		  Intent intent = new Intent(Intent.ACTION_SEND);
	        		  intent.setType("text/html");
	        		  intent.putExtra(Intent.EXTRA_EMAIL, "");
	        		  intent.putExtra(Intent.EXTRA_SUBJECT, "ShifaApp Kent + Repertory + Materia + Medica For Android");
	        		  intent.putExtra(Intent.EXTRA_TEXT, "Hey, \n I just downloaded Shifa Kent Repertory Materia Medica on my Android. \n\nGet it now from https://play.google.com/store/apps/details?id=com.shifa.kent  \n\nThe Shifa – Kent Repertory is a complete repertorising tool. A handheld repertory couldn’t be easier to use. A perfect application for homeopaths who wants a powerful yet easy to use application offline. Quick search allows you to quickly go through the whole repertory. Each chapter and rubrics are arranged alphabetically for easy navigation. Clean and intuitive design allows for easier navigation and quick reference. Beta version offers the whole repertory with direct reference to remedies in a particular rubrics. \n\nIt marks the beginning of a more robust and advance application to come \n\nThe Shifa - Materia Medica enables you to carry different top book Boericke,J.T.Kent,Allen.Shifa allow you to search any book content words and compare with ALLEN,KENT,BOERICKE Books. Complete offline Section. You do not need any internet \n\nGet it now from https://play.google.com/store/apps/details?id=com.shifa.kent");

	        		  startActivity(Intent.createChooser(intent, "Send Email"));
	        		 
	        	  }

	        	});
	        
	        
	        
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
    public int BuyNowPopupActivate()
    {
    	
    	
    	return  Integer.valueOf(GetPreferenceValue("Counter~BuyNow~Activate"));
    	
    }
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    finish();
		return super.onKeyDown(keyCode, event);
	    
	}

}
	