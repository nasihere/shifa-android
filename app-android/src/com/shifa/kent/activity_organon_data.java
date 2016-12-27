	package com.shifa.kent;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class activity_organon_data extends Activity {
	 Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_organon_data);
        ctx = this;
       
        LinearLayout layout = (LinearLayout)findViewById(R.id.lin_organon_data_Notification);
        Super_Library_Notification SLN = new Super_Library_Notification(ctx , activity_organon_data.this,layout );
        SLN.Nofification_Start();
       
        
        
     
		 
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("organon_data");
            String value_head = extras.getString("organon_data_heading");
            //
            Log.e("Organon",value);
            TextView tv = (TextView) findViewById(R.id.txtorganon_Data);
            tv.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
            
            TextView tv1 = (TextView) findViewById(R.id.txtorganon_data_heading);
            tv1.setText(Html.fromHtml(value_head), TextView.BufferType.SPANNABLE);
            
        }
        showAds();
    }

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    finish();
	    return super.onKeyDown(keyCode, event);
	}
	public void showAds()
	{
		/*
		if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;
		  // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainOrganonDataMenu);

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
}
	