	package com.shifa.kent;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.TextView;


public class activity_kent_overview extends Activity {
	 Context ctx;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kent_overview);
        ctx = this;
       
        
     
    	
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("kent_reportary_data");
            
            TextView tv = (TextView) findViewById(R.id.tv_kent_overview_data);
            tv.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
            
            
            
        }
     //   showAds();
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
	