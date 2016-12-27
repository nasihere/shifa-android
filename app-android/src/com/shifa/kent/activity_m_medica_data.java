package com.shifa.kent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


public class activity_m_medica_data extends Activity {
	Context ctx;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_m_medica_data);
		ctx = this;

		LinearLayout layout = (LinearLayout) findViewById(R.id.lin_mm_data_Notification);
		Super_Library_Notification SLN = new Super_Library_Notification(ctx,
				activity_m_medica_data.this, layout);
		SLN.Nofification_Start();

		

/*
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	if (showBigAds == true) {
						// load more content
						showBigAds = ShowBigAds();
						if (showBigAds == false) {
							ConstructBigAds();
						}
					}
	            }
	        }, 10000);
	        */
			
		RadioGroup radioButtonGroup = (RadioGroup) findViewById(R.id.radioMM);
		radioButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						int radioButtonID = group.getCheckedRadioButtonId();
						View radioButton = group.findViewById(radioButtonID);
						int index = group.indexOfChild(radioButton);
						Log.e("idx", String.valueOf(index));
						TextView tv1 = (TextView) findViewById(R.id.txtm_m_allen);
						TextView tv = (TextView) findViewById(R.id.txtm_m_Data);
						TextView tv2 = (TextView) findViewById(R.id.txtm_m_Kent);
						if (index == 0) {
							tv.setVisibility(View.VISIBLE);
							tv1.setVisibility(View.VISIBLE);
							tv2.setVisibility(View.VISIBLE);
						} else if (index == 1) {
							tv.setVisibility(View.VISIBLE);
							tv1.setVisibility(View.GONE);
							tv2.setVisibility(View.GONE);
						} else if (index == 2) {
							tv.setVisibility(View.GONE);
							tv1.setVisibility(View.VISIBLE);
							tv2.setVisibility(View.GONE);
						} else if (index == 3) {
							tv.setVisibility(View.GONE);
							tv1.setVisibility(View.GONE);
							tv2.setVisibility(View.VISIBLE);
						}
					}
				});

		Bundle extras = getIntent().getExtras();
		String value = "";
		String SearchKeyWord = "";
		
		if (extras != null) {
			SearchKeyWord = extras.getString("SearchKeyWord");
			
			if (!SearchKeyWord.equals(""))
			{
				SearchKeyWord = SearchKeyWord.trim();
				SearchKeyWord = SearchKeyWord.replace("*,*", " ");
				SearchKeyWord = SearchKeyWord.replace("*", "");
			}
			String ssk[] = SearchKeyWord.split(" ");
			
			value = extras.getString("medica_data");
			if (!SearchKeyWord.equals(""))
			{
				for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
				{
					value = value.toLowerCase().replaceAll(ssk[iMulC].toLowerCase(),
							"<b><font size='8' color='#009900'>" + ssk[iMulC] + "</font></b>");
				}
			}
			TextView tv = (TextView) findViewById(R.id.txtm_m_Data);
			tv.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);

			value = extras.getString("allen_data");
			if (!SearchKeyWord.equals(""))
			{
			for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
			{
				value = value.toLowerCase().replaceAll(ssk[iMulC].toLowerCase(),
						"<b><font  size='8' color='#009900'>" + ssk[iMulC] + "</font></b>");
			}
			}
			TextView tv1 = (TextView) findViewById(R.id.txtm_m_allen);
			tv1.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);

			
			value = extras.getString("kent_data");
			if (!SearchKeyWord.equals(""))
			{
			for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
			{
				value = value.toLowerCase().replaceAll(ssk[iMulC].toLowerCase(),
						"<b><font  size='8' color='#009900'>" + ssk[iMulC] + "</font></b>");
			}
			}
			TextView tv2 = (TextView) findViewById(R.id.txtm_m_Kent);
			tv2.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);

			
		}
//		showAds();

	}
/*	InterstitialAd interstitialAd;
	boolean showBigAds = false;
	public void ConstructBigAds() {
		AdRequest adRequest = new AdRequest();
		interstitialAd = new InterstitialAd(this, "a15225150ecdd80");
		interstitialAd.loadAd(adRequest);
	}
*/
/*	public boolean ShowBigAds() {

		if (interstitialAd.isReady()) {
			interstitialAd.show();
			return false;
		}
		return true;
	}*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return super.onKeyDown(keyCode, event);
	}

	public void showAds() {
/*
		if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase()
				.equals("false"))
			return; // Create the adView
		adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainMedicaDataMenu);

		// Add the adView to it
		layout.addView(adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
*/
	}

/*	public String GetPreferenceValue(String StringName) {
		SharedPreferences prefs = getSharedPreferences("AppNameSettings", 0);
		String restoredText = prefs.getString(StringName, null);
		if (restoredText != null) {
			return restoredText;
		}
		return "0";
	}*/
}
