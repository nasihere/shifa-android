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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.shifa.kent.inappbilling.BlundellActivity;
import com.shifa.kent.inappbilling.MainMenu;
import com.shifa.kent.inappbilling.Navigator;

public class home_menu extends BlundellActivity implements MainMenu {
	ProgressDialog progressDialog ;
	Context ctx;
	boolean bBuyNow = false;
	private Handler mHandler;
	private boolean HomePageUrlSet = false;
	private boolean checkupdate = true;
   ListView listView;
   String CheckUpdateStatus = "";
   int curVersion = 0;
   
   boolean StartupShowAds  = false;
   String CheckUpdateSendLog = "";
	   String SessionID = "";
	  
	   boolean ads = true;
	   boolean HomePageResume = true;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		ctx = this;
		CheckAdsPaid();
		
		setContentView(R.layout.home_menu);
		listView = (ListView) findViewById(R.id.lvhomemenu);
		
	    SendLog();
		SessionID= LoggedIn();
		Log.e("Showads",String.valueOf(ads));
		SetPreferenceValue("Counter~Home~Menu","1");
		refreshmenu("","","-","","",ads,"","");
		
		
		 WebView webView = (WebView) findViewById(R.id.webTmp);
		 webView.getSettings().setJavaScriptEnabled(true);
    	 webView.clearHistory();
    	 webView.setWebViewClient(new WebViewClient() {
		 public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        	Log.e("URL Home Page", url);
	        	
	        	if (url.indexOf("APP_URL_") != -1)
	        	{
	        		
	        		String Selection = url.substring(url.indexOf("APP_URL_"));
	        		Selection = Selection.replace("APP_URL_", "");
	        		Selection = Selection.replace("%20", " ");
	        		Opener(Selection);
	        	}
	        	else
	        	{
	        		view.loadUrl(url);	
	        	}
	            
	            return true;
		    }
		  });		
    	 
    	/* if (ads == true)
    	 {
    		 listView.setBackgroundColor(Color.LTGRAY);
    		 Toast.makeText(getApplicationContext(), "Please Wait.., Menu item will be activate after checking your buy now account...", 5000).show();
    		 listView.setEnabled(false);
    	 webView.setWebViewClient(new WebViewClient() {

    		   public void onPageFinished(WebView view, String url) {
    		        // do your stuff here
    			   listView.setBackgroundColor(Color.WHITE);
    			   listView.setEnabled(true);
    		    }
    		});
    	 }*/
    	 
		//webView.requestFocus(View.FOCUS_DOWN);
		webView.loadUrl("http://kent.nasz.us/app_php/shifa-homepage.php?session_id="+SessionID + "&ver="+curVersion);
    	
    		
    		
       
    		
    		
		Log.e("Home screen progress ", "2.2");
		final Handler mHandler = new Handler();
			 new Thread(new Runnable() {
			        @Override
			        public void run() {
			            // TODO Auto-generated method stub
			            while ( HomePageResume == true) {
			                try {
			                    
			                    mHandler.post(new Runnable() {

			                        @Override
			                        public void run() {
			                        	HomePageResume = false;
			                        	Log.e("Home screen progress ", "2.3");
			                        	if (isOnline() == true){
			                        	   DownloadWebPageTask task = new DownloadWebPageTask();
			                        	   Log.e("Home screen progress ", "2.4");
			                       		task.execute(new String[] { "http://kent.nasz.us/app_php/app_chat_online.php"});
			                       		Log.e("Home screen progress ", "2.5");
			                        	}
			                        }
			                    });
			                    Thread.sleep(3000);
			                } catch (Exception e) {
			                	Toast.makeText(getApplicationContext(), e.toString(), 1000).show();
			                    // TODO: handle exception
			                }
			                
			            }
			        }
			    }).start();
			 showRemoveAdsPopup();
			 //AutoStartUp();
	}	
	private boolean AutoStartUp(String item)
	{
		if (item.equals("AutoStartUpMMLangauge"))
		{
			SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString(item, null);
			if (restoredText == null)  // if its firsttime 
			{
				/** Save item ***/
				SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
				editor.putString(item,"true");
				editor.commit();
			
				
				CharSequence SearchArray[] = new CharSequence[] {"English", "Italian", "Dutch", "German", "French", "Spanish", "Portuguese"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Materia Medica Language");
				builder.setItems(SearchArray, new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        // the user clicked on colors[which]
				    	SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
						editor.putString("MMLang",String.valueOf(which));
						editor.commit();
						
			    		int[] values= new int[2];
				    	int x = 50;
				    	int y = 200;
		
				    	Toast prName = Toast.makeText(getApplicationContext(),
				                "You can change language again in setting.",
				                Toast.LENGTH_LONG);
				            prName.setGravity(Gravity.TOP|Gravity.LEFT, x, y);
				            prName.show();
					            
				    	openMedica();
				    	
				    }
				});
				builder.show();
				 return true;
				
				
			}
		
		}
		 return false;
		/***************** Set Profile Pic *********************/
	/*	SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString("AutoStartUpProfilePic", null);
		if (restoredText == null)  // if its firsttime 
		{
			/** Save item ***/
/*			SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
			editor.putString("AutoStartUpProfilePic","true");
			editor.commit();
		 new AlertDialog.Builder(home_menu.this)
	            .setIcon(R.drawable.ic_launcher)
	            .setTitle("Profile Picture")
	            .setMessage("Do you want to set your profile picture?")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                            openSettings();
	                    		
	                    }
	            })
	            .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                          
	                    }
	            })
	            .show();
			 return;
		}
		/***************** Set Profile Pic *********************/
	/*	 prefs = getSharedPreferences("AppNameSettings",0); 
		 restoredText = prefs.getString("AutoStartUpHelp", null);
		if (restoredText == null)  // if its firsttime 
		{
			/** Save item ***/
		/*	SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
			editor.putString("AutoStartUpHelp","true");
			editor.commit();
			 new AlertDialog.Builder(home_menu.this)
	            .setIcon(R.drawable.ic_launcher)
	            .setTitle("Profile Picture")
	            .setMessage("Do you want to learn how to use shifa app??")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	Opener("HELP");
	                    		
	                    }
	            })
	            .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                          
	                    }
	            })
	            .show();
			 return;
		}
		*/
		
		/***************** Set Profile Pic *********************/
		
		
	}
	private void showRemoveAdsBuyNow()
	{
		Toast.makeText(getApplicationContext(),
				"You are using demo version. Please buy now to get full access!",
				Toast.LENGTH_LONG).show();
		
		navigate().toPurchasePassportActivityForResult();
		  SetPreferenceValue("Counter~Home~Remove~ads","1");
	}
	private void showRemoveAdsPopup()
	{
		try
		{
		Bundle extras = getIntent().getExtras();
		  String sBuyNow = "";
		  if (extras != null) {
			  sBuyNow= extras.getString("OpenBuyNow");
		       if (sBuyNow.equals("true"))
		       {   
		    	   bBuyNow = true;
		    	   showRemoveAdsBuyNow();
		       }
		  }
		}
		catch(Exception ex)
		{}
	}
	private String LoggedIn()
			{
				SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
				String restoredText = prefs.getString("session_id", null);
				if (restoredText != null) 
				{
					return restoredText;
				}
				return "";
			}
		   @Override
		    public void onPurchaseItemClick(View v) {
			   showRemoveAdsBuyNow();
		    }

		    @Override
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		        super.onActivityResult(requestCode, resultCode, data);
		        if (Navigator.REQUEST_PASSPORT_PURCHASE == requestCode) {
		            if (RESULT_OK == resultCode) {
		                dealWithSuccessfulPurchase();
		            } else {
		            	//dealWithSuccessfulPurchase();
		                dealWithFailedPurchase();
		                
		            }
		        }
		    }

		    private void dealWithSuccessfulPurchase() {
		    	bBuyNow = false;
		    	SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
				editor.putString("RemoveAds","true");
				editor.commit();
			    SetPreferenceValue("Counter~Home~Remove~ads~Paid","1");
			    CheckAdsPaid();
			    finish();
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

				
				
			    
		    	//show toast or whatever you need to show
		    }

		    private void dealWithFailedPurchase() {
		       //ask for retry
		    	if (bBuyNow == true)
        		{
		    		
        			 finish();	 
        		}
		    	Toast.makeText(getApplicationContext(), "Please wait connecting server", 100).show();
			    openURL("http://kent.nasz.us/app_php/buynownotification/buynownotification.php?session_id="+SessionID);
		    }

		
		private String SendLog()
		{
			
			if (SessionID == "123456789") return "";
			String SendData = "#Hits#";
		
			try
			{
			 SendData = "";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Kent-Repertory")) ) ;  //0
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Materia-Medica")));   //1
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Reveresed-Repetory")) ); //2
			SendData += "-";
			SendData +=  String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Abbreviation")));  //3
			SendData += "-";
			SendData +=  String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Chat")) ); //4
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Event"))); //5
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Flash"))); //6
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Logout"))); //7
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("ChatIndex")));  //8
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Organon"))); //9
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Menu"))); //10
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~URL"))); //11
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Remove~ads"))); //12
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~Remove~ads~Paid"))); //13
			SendData += "-";
			SendData += String.valueOf( Integer.valueOf(GetPreferenceValue("Counter~Home~PrivateMsg"))); //14
			
			SendData += "";
			}
			catch(Exception ex)
			{
				Log.e("Error SendData",ex.toString());
			}
			Log.e("Sendata",SendData);
			if (CheckUpdateSendLog.equals(SendData)) return "";
			CheckUpdateSendLog = SendData;
			return SendData;
		}
		public void showAds()
	{
		
		/*
		  // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainHomeMenu);

        // Add the adView to it
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);*/
		//ConstructBigAds(); // Big ads Disabled  not to irritate much 

	}
/*
	InterstitialAd interstitialAd;

	public void ConstructBigAds() {
		
		AdRequest adRequest = new AdRequest();
		interstitialAd = new InterstitialAd(this, "a15225150ecdd80");
		interstitialAd.loadAd(adRequest);
	}

	public void onReceiveAd(Ad ad) {
	
	 if (ad == interstitialAd) {
	 
		 if (interstitialAd.isReady()) {
			interstitialAd.show();
			
		}
	 }
	}*/
	   public boolean ShowAds(String StringName)
	    {
	    	SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
			String restoredText = prefs.getString(StringName, null);
			 Log.e("GetPreferenceValue " + StringName, restoredText);
			if (restoredText != null) 
			{
				return false;
			}
			return true;
	    }
	@Override
    protected void onStop() {
        super.onStop();
        HomePageResume = false;
        Log.e("HomePageResume Activity","HomePageResume not longer in the application");
        
    }


    @Override
    protected void onStart() {
        super.onStart();
        HomePageResume = true;
        Log.e("HomePageResume Activity","uHomePageResumeser is back");
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
    public void CheckAdsPaid()
    {
    	try {
			curVersion = getPackageManager().getPackageInfo("com.shifa.kent", 0).versionCode;
			Log.e("Current Version",String.valueOf(curVersion));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString("RemoveAds", null);
		if (restoredText != null) 
		{
			ads = false; // Dont show ads
		//	SavePreference("Settings~Google~ads",String.valueOf(ads));
			
		}
		else
		{
			ads = true; //Show Ads
		//	SavePreference("Settings~Google~ads",String.valueOf(ads));
			
		}
		
    }
  
    public void SetPreferenceValue(String StringName,String StringValue)
    {
    	try
    	{
    		int LastValue = Integer.valueOf(GetPreferenceValue(StringName));
    		int AddOnValue = Integer.valueOf(StringValue);
    		int TotalValue = LastValue + AddOnValue;
    		Log.e("Added Preference Value", String.valueOf(TotalValue));
    		StringValue =  String.valueOf(TotalValue);
    		SavePreference(StringName,StringValue);
    		if (StringName.equals("Counter~Home~Menu") && ads == true )
    		{
    			BuyNowPopupActivate(TotalValue);
    			
    		}
    	}
    	catch(Exception ex)
    	{
    		SavePreference(StringName,"1");
    	}
    }
    public void BuyNowPopupActivate(int iHits)
    {
    	
    	SetPreferenceValue("Counter~BuyNow~Activate",String.valueOf(iHits));
    	int iBuyNowComplete = 40 - Integer.valueOf(GetPreferenceValue("Counter~BuyNow~Activate"));
    	
    
    }
    public void SavePreference(String StringName,String StringValue)
    {
    	
    	SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
		editor.putString(StringName,StringValue);
	    Log.e("SetPreferenceValue " + StringName, StringValue);
	    editor.commit();
    }
	public void refreshmenu(String Status,String AdsFreeStatus,String FlashStatus,String NewsStatus,String MyProfileStatus,boolean ads, String DiscussionStatus, String PrivateMsgStatus)
	{
			Log.e("Home screen progress ", "1");
		 home_adapter.LoadModel(Status,AdsFreeStatus,FlashStatus,NewsStatus,MyProfileStatus,ads,DiscussionStatus,PrivateMsgStatus,SessionID);
		 Log.e("Home screen progress ", "2");
	       
	        String[] ids = new String[home_adapter.Items.size()];
	        for (int i= 0; i < ids.length; i++){

	            ids[i] = Integer.toString(i+1);
	        }
	        Log.e("Home screen progress ", "3");
	        home_classAdapter adapter = new home_classAdapter(this,R.layout.listview_homemenu, ids);
	        Log.e("Home screen progress ", "3.1");
	        //Parcelable state = listView.onSaveInstanceState();
	        listView.setAdapter(adapter);
	        listView.setSelection(0);
	        
	        //listView.onRestoreInstanceState(state);
	        
	       
	    
	        int totalHeight = 0;
	        for (int i = 0; i < adapter.getCount(); i++) {
	            View listItem = adapter.getView(i, null, listView);
	            listItem.measure(0, 0);
	            totalHeight += listItem.getMeasuredHeight();
	        }

	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
	        listView.setLayoutParams(params);
	        listView.requestLayout();
	       
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {
	 			
	    		@SuppressLint("DefaultLocale")	    		@Override
	    		public void onItemClick(AdapterView<?> listView, View view, int position,
	    				long id) {
	    			  String Selection  = ((TextView) view.findViewById(R.id.tvListItemHome)).getText().toString();
	    			  Opener(Selection);
	    			  
	    			  Log.e("Selection",Selection);
	    			
	    		}
	    	});
	        Log.e("Home screen progress ", "4");
	}
	private void Opener(String Selection)
	{
		
		if (Selection.equalsIgnoreCase("Facebook Login Connect"))
		{
			openFBLoginConnect();
		}
			else if (Selection.toUpperCase().equals("KENT REPERTORY"))
		  {
			  //dealWithSuccessfulPurchase();
			  openKent();
		  }
			else if (Selection.equalsIgnoreCase("Shifa Facebook Page"))
			  {
				  //dealWithSuccessfulPurchase();
				  openShifaFacebookPage();
			  }
		
			else if (Selection.equalsIgnoreCase("www.shifa.in"))
			  {
				  //dealWithSuccessfulPurchase();
				  openShifaWebpage();
			  }
		  else if (Selection.toUpperCase().equals("BOENNINGHAUSEN REPERTORY"))
		  {
			  //dealWithSuccessfulPurchase();
			  openBoenninghausen();
		  }
		  else if (Selection.equalsIgnoreCase("Cyrus Maxwell Boger Repertory"))
		  {
			  //dealWithSuccessfulPurchase();
			  openCyrusMaxwell();
		  }
		  
		  else if (Selection.toUpperCase().equals("CHAT") || Selection.indexOf("Chat:") != -1)
		  {
			  openChat();
			  
		  }
		  else if (Selection.toUpperCase().equals("PATIENT MANAGEMENT"))
		  {
			  
			  Toast.makeText(getApplicationContext(), "Please wait connecting server", 100).show();
			  openURL("http://kent.nasz.us/app_php/app_kent_report_index.php?session_id="+SessionID);
		  }
		  else if (Selection.toUpperCase().equals("HELP"))
		  {
			  
			  Toast.makeText(getApplicationContext(), "Please wait connecting server", 100).show();
			  openURL("http://kent.nasz.us/app_php/howtouseshifa/howtouseshifa.php?session_id="+SessionID);
		  }
		  else if (Selection.toUpperCase().equals("SETTINGS"))
		  {
			  openSettings();
		  }
		  else if (Selection.toUpperCase().equals("DISCUSSION"))
		  {
			  openDiscussion();
		  }
		  else if (Selection.toUpperCase().equals("LOGOUT"))
		  {
			  LogOut();
		  }
		  else if (Selection.toUpperCase().equals("MATERIA MEDICA"))
		  {
			  if (AutoStartUp("AutoStartUpMMLangauge") == true) return;;
			  openMedica();
			  //Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		  else if (Selection.toUpperCase().equals("REVERSED REPERTORY"))
		  {
			 if (ads == true)
			 {
				 showRemoveAdsBuyNow();
				 return;
			 }
			 	
			  openReversed();
			  //Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		  else if (Selection.toUpperCase().equals("ABBREVIATION"))
		  {
			  if (ads == true)
				 {
				  
					 showRemoveAdsBuyNow();
					 return;
				 }
			  openAbbreviation();
			 // Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		  else if (Selection.toUpperCase().equals("ORGANON"))
		  {
			  if (ads == true)
				 {
					 showRemoveAdsBuyNow();
					 return;
				 }
			  openOrganon();
			  //Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		 
		  else if (Selection.toUpperCase().indexOf("REMOVE ADS") != -1 || Selection.toUpperCase().indexOf("BUY NOW") != -1)
		  {
			  showRemoveAdsBuyNow();
			  //Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		  else if (Selection.toUpperCase().indexOf("PRIVATE MESSAGE") != -1)
		  {
			  
			  openPrivateMsg();
			  //Toast.makeText(getApplicationContext(), "Coming soon version 3.o", 100).show();
		  }
		  
	}
	private void openFBLoginConnect(){
		
		SharedPreferences.Editor editor = getSharedPreferences(
				"AppNameSettings", 0).edit();
		editor.putString("session_id_Migration", SessionID);
		
		editor.commit();
		Intent intent = new Intent(ctx, login.class);
		
		ctx.startActivity(intent);
	}
	private void openShifaFacebookPage()
	{
		
		try {
		    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/631560593522959"));
		    startActivity(intent);
		} catch(Exception e) {
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/631560593522959")));
		}
	}
	private void openShifaWebpage(){
		try {
		    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shifa.in"));
		    startActivity(intent);
		} catch(Exception e) {
		    
		}
	}
	private void openOrganon()
	{
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
			
		SetPreferenceValue("Counter~Home~Organon","1");
		  Intent intent = new Intent(home_menu.this, activity_organon.class);
		  intent.putExtra("userurl", "A");
		  startActivity(intent);
		  finish();
		
	}
	private void openPrivateMsg()
	{
		if(SessionID.equals("123456789")) 
		{
			Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout.  Please register or login as user..", 1000).show();
			return;
		}
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
			
		SetPreferenceValue("Counter~Home~PrivateMsg","1");
		  Intent intent = new Intent(home_menu.this, activity_privatemsg.class);
		  startActivity(intent);
		  finish();
		
	}
	private void openSettings()
	{
	//	progressDialog =  ProgressDialog.show(home_menu.this, "", 
      //          "Loading. Please wait...", true);
			
		//SetPreferenceValue("Counter~Home~","1");
		  Intent intent = new Intent(home_menu.this, activity_settings.class);
		  //intent.putExtra("userurl", "A");
		  startActivity(intent);
		  finish();
		
	}
	private void openAbbreviation()
	{
		if(SessionID.equals("123456789")) 
		{
			Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout.  Please register or login as user..", 1000).show();
			return;
		}
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
			
		SetPreferenceValue("Counter~Home~Abbreviation","1");
  	  
		  Intent intent = new Intent(home_menu.this, activity_abbreviation.class);
		  startActivity(intent);
		  finish();
		
	}
	 private void openReversed()
		{
		 if(SessionID.equals("123456789")) 
			{
				Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout.  Please register or login as user..", 1000).show();
				return;
			}	
			progressDialog =  ProgressDialog.show(home_menu.this, "", 
	                "Loading. Please wait...", true);
			SetPreferenceValue("Counter~Home~Reveresed-Repetory","1");
	    	  
			  Intent intent = new Intent(home_menu.this, activity_r_repertory.class);
			  startActivity(intent);
			  finish();
		} 
	private void openMedica()
	{
	/*	if(SessionID.equals("123456789")) 
		{
			Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout.  Please register or login as user..", 1000).show();
			return;
		}*/
		//progressDialog =  ProgressDialog.show(home_menu.this, "", 
             //   "Loading. Please wait...", true);
			
		SetPreferenceValue("Counter~Home~Medica","1");
		if (ads == true)
		{
			SetPreferenceValue("Counter~Home~Medica~Ads","1");
			SetPreferenceValue("Counter~Home~Kent~Ads","1");
		}
		else
		{
		   SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
    	   editor.remove("Counter~Home~Medica~Ads");
    	   editor.remove("Counter~Home~Kent~Ads");
    	   
    	   editor.commit();
		}
		  Intent intent = new Intent(home_menu.this, activity_m_medica.class);
		  
		  startActivity(intent);
		  finish();
	}
	

	private void openURL(String URL)
	{
		
		SetPreferenceValue("Counter~Home~URL","1");
		  Intent intent = new Intent(home_menu.this, activity_events.class);
		  intent.putExtra("url", URL);
		  startActivity(intent);
		  //finish();
	}
	private void LogOut()
	{
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
		SetPreferenceValue("Counter~Home~Logout","1");
		  SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
    	  editor.remove("session_id");
    	  editor.remove("Counter~Home~Medica~Ads");
    	  
    	  editor.putString("ChatIndex","1");
    	  
    	  
    	  editor.remove("SaveSearchType");
    	  editor.remove("AutoStartUpProfilePic");
    	  editor.remove("AutoStartUpHelp");
    	  editor.remove("AutoStartUpMMLangauge");
    	  
    	  editor.remove("ShowPublicMsg");
    	  
    	  editor.commit();
    	  
    	  
		  Intent intent = new Intent(home_menu.this, login.class);
		  startActivity(intent);
		  finish();
		
	}
	private void openCyrusMaxwell(){
		
		  progressDialog =  ProgressDialog.show(home_menu.this, "", 
                  "Loading. Please wait...", true);
		  SetPreferenceValue("Counter~Home~Kent-Repertory","1");
		  Intent intent = new Intent(home_menu.this, activity_kent.class);
		  intent.putExtra("book", "Cyrus Maxwell Boger");
		  startActivity(intent);
		  finish();
	}
	private void openBoenninghausen(){
		
		  progressDialog =  ProgressDialog.show(home_menu.this, "", 
                  "Loading. Please wait...", true);
		  SetPreferenceValue("Counter~Home~Kent-Repertory","1");
		  Intent intent = new Intent(home_menu.this, activity_kent.class);
		  intent.putExtra("book", "Boenninghausens");
		  startActivity(intent);
		  finish();
	}
	private void openKent()
	{
		
		  progressDialog =  ProgressDialog.show(home_menu.this, "", 
                  "Loading. Please wait...", true);
		  SetPreferenceValue("Counter~Home~Kent-Repertory","1");
		  Intent intent = new Intent(home_menu.this, activity_kent.class);
		  intent.putExtra("book", "Kent");
		  startActivity(intent);
		  finish();
		  
		
	}
	private void openDiscussion()
	{
		if(SessionID.equals("123456789")) 
		{
			Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout. Please register or login as user..", 1000).show();
			return;
		}
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
		 SetPreferenceValue("Counter~Home~Chat","1");
		 Bundle extras = getIntent().getExtras();
		  String SessionID = "";
		  String SessionName = "";
		  if (extras != null) {
		       SessionID = extras.getString("SessionID");
		       SessionName = extras.getString("session_name");
		  }
		  
		  Intent intent = new Intent(home_menu.this, activity_chatonline.class);
		  intent.putExtra("SessionID", SessionID);
		  intent.putExtra("Session_name", SessionName );
		  startActivity(intent);
		  finish();
	}
	
	private void openChat()
	{
		if(SessionID.equals("123456789")) 
		{
			Toast.makeText(getApplicationContext(), "You cannot use this section as guest. Goto Setting->Logout.  Please register or login as user..", 1000).show();
			return;
		}
		progressDialog =  ProgressDialog.show(home_menu.this, "", 
                "Loading. Please wait...", true);
		 SetPreferenceValue("Counter~Home~Chat","1");
		Bundle extras = getIntent().getExtras();
		  String SessionID = "";
		  String SessionName = "";
		  if (extras != null) {
		       SessionID = extras.getString("SessionID");
		       SessionName = extras.getString("session_name");
		  }
		  
		  Intent intent = new Intent(home_menu.this, chat.class);
		  intent.putExtra("SessionID", SessionID);
		  intent.putExtra("Session_name", SessionName );
		  startActivity(intent);
		  finish();
		
	}
	private class DownloadWebPageTask extends AsyncTask<String, Context, String> {
		protected Context ctx;
		@Override
	    protected String doInBackground(String... urls) {
			
			Log.e("doInBackground","enter");
		      String response = "";
		      String uri = "";
		      for (String url : urls) {
		    	  uri = url;
		    	  Log.e("uri",uri);
		    	  try {
				        DefaultHttpClient client = new DefaultHttpClient();
				        HttpPost httpGet = new HttpPost(url);
				        try {
				        	Log.e("HttpPost", "Progress 0");
				        	  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				        	  nameValuePairs.add(new BasicNameValuePair("session_id", SessionID));
					          nameValuePairs.add(new BasicNameValuePair("hits", SendLog() ));
					          nameValuePairs.add(new BasicNameValuePair("version", String.valueOf(curVersion) ));
					          Log.e("HttpPost", "Progress 1");
					          httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					          Log.e("HttpPost", "Progress 2");
				          HttpResponse execute = client.execute(httpGet);
				          Log.e("HttpPost", "Progress 2.5");
				          InputStream content = execute.getEntity().getContent();
				          Log.e("HttpPost", "Progress 3");
				          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				          String s = "";
				          Log.e("HttpPost", "Progress 4");
				          while ((s = buffer.readLine()) != null) {
				            response += s;
				          }
			
				        } catch (Exception e) {
				        	 Log.e("Error http:", e.toString());
				        
				        	 e.printStackTrace();
				      		return "-999";
				        }
				  }
		    	  catch(Exception ex)
		    	  {
		    		  Log.e("Error http:", ex.toString());
		    		  return "-999";
		    	  }
		      }
	      Log.e("Response",response);
	     
	      return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	if (result.equals("-999")) 
	    		{
	    			return;
	    		}
	    	
	    	ShowFeed(result);
	    	
	    }
	  }
	private void ShowFeed(String result)
	{
		
		if (HomePageUrlSet == true) return;
		result = result.trim();
		Log.e("Response",result);
		if (result.equals("")) return;
		if (CheckUpdateStatus.equals(result)) return;
		CheckUpdateStatus = result;
		SavePreference("SaveHomeStatus", result);
		
    	String[] data ={ "", "","","","", "","","" ,"", "","","" ,"", "","","" ,"", "","","" ,"", "","","","", "","","","", "","","" ,"", "","","" ,"", "","","" ,"", "","","" ,"", "","",""  };
    	String[] tmp = result.split("-:-");
    	for(int i = 0; i < tmp.length ; i++)
    	{
    		data[i] = tmp[i];
    	}
    	Log.e("HttpPost", "Response");
    	ClearPrefrenece();
    		data[0] = data[0] + " Online";
    		//final TextView txtOnlineViewer = (TextView) findViewById(R.id.txtOnlineUser);
    		//txtOnlineViewer.setText(data);
    		//home_adapter.setOnlineChatUser(data);
    		//data[0] - Online User Status 
    		//data[1] - Events Status
    		//data[2] - Flash Message 
    		
			
			data[2] = "http://kent.nasz.us/app_php/shifa-homepage.php?session_id="+SessionID;
			//data[2] = 	 "http://kent.nasz.us/app_php/test-shifa-homepage.php?session_id=000000000000007";
			HomePageUrlSet  = true;
    		//data[3] - ads - Condition show or hide
    		//data[4] - adsremote - to show specific URL on home screen 
    		//data[5] - Fresh  Status
    		//data[6] - My Profile Status
    		//data[7] - App Updated Version 
    		//data[8] - Remove Ads Status
    		//data[9] - Discussion Status
			//data[10] - Private Msg Status
    		CheckUpdateApp(data[7]);
    		refreshmenu(data[0],data[8],data[2],data[5],data[6],ads,data[9],data[10]);
  	        try
  	        {
  	        	if ( ads == true)
  	        	{
	  	        	if (data[3].equals("ads") && StartupShowAds == false)
	  	        	{
	  	        		StartupShowAds = true;
	  	        		 ads = true;
	  	        		showAds();
	  	        	}
	  	        	else if (data[3].equals("adsremote")  && StartupShowAds == false)
	  	        	{
	  	        		 StartupShowAds = true;
	  	        		ads = true;
	  	        		 Intent intent = new Intent(home_menu.this, activity_events.class);
	  	        		 Log.e("data[4] http:/url ads",data[4]);
		  	     		  intent.putExtra("url", data[4]);
		  	     		
		  	     		  startActivity(intent);
	  	        		
	  	        	}
	  	        	 
  	        	}
  	        	
  	  	    
  	        }
  	        catch(Exception ex)
  	        {
  	        	
  	        }
  	        
  	        
	}
	private void ClearPrefrenece()
	{
		try
		{
			 Log.e("ClearPrefrenece","True");
			 if (SessionID == "123456789") return;
			SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
			
			
		    editor.putString("Counter~Home~Kent-Repertory","0");
		    editor.putString("Counter~Home~Materia-Medica","0");
		    editor.putString("Counter~Home~Reveresed-Repetory","0");
		    
		    editor.putString("Counter~Home~Abbreviation","0");
		    editor.putString("Counter~Home~Chat","0");
		    editor.putString("Counter~Home~Event","0");
		    editor.putString("Counter~Home~Flash","0");
		    editor.putString("Counter~Home~Logout","0");

		    editor.putString("Counter~Home~Organon","0");
		    editor.putString("Counter~Home~Menu","0");
		    
		    editor.putString("Counter~Home~URL","0");
		    editor.putString("Counter~Home~Remove~ads","0");
		    
		    editor.putString("Counter~Home~PrivateMsg","0");
		    editor.putString("Counter~Home~Medica~Ads","0");
		    
		    
		    editor.commit();
		}
		catch(Exception ex)
		{
		 Log.e("Clear PReference",ex.toString());	
		}
	}
	
	

    /* This Runnable creates a Dialog and asks the user to open the Market */ 
    private Runnable showUpdate = new Runnable(){
           public void run(){
            try
            {
	        	   new AlertDialog.Builder(home_menu.this)
	            .setIcon(R.drawable.ic_launcher)
	            .setTitle("Shifa Update Available")
	            .setMessage("An update for shifa is available! Open Android Market and see the details? \nWe recommend you to upgrade shifa to sync with our server update.")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                            /* User clicked OK so do some stuff */
	                    		try
	                    		{
	                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.shifa.kent"));
	                            startActivity(intent);
	                            LogOut();
	                    		}
	                    		catch(Exception ex) {
	                    			Toast.makeText(getApplicationContext(), "Please do logout and reloing it.", 1000).show();
	                    		}
	                    }
	            })
	            .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                            /* User clicked Cancel */
	                    	Toast.makeText(getApplicationContext(), "May be Shifa app will not update your properly!", 1000).show();
	                    }
	            })
	            .show();
            }
            	catch(Exception ex) {
            		Toast.makeText(getApplicationContext(), "Please wait connecting server", 100).show();
            	}
            }
     
    };
	/* APP Update checkavailble */
    private void CheckUpdateApp(String UpdateVersion)
    {
    	if (checkupdate == false) return;
    	checkupdate = true;
    	/* Get Last Update Time from Preferences */
		SharedPreferences prefs = getPreferences(0);
     long  lastUpdateTime = prefs.getLong("lastUpdateTime", 0);
		
		/* Should Activity Check for Updates Now? */
		if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System.currentTimeMillis() ) {

		    /* Save current timestamp for next Check*/
		    lastUpdateTime = System.currentTimeMillis();            
		    SharedPreferences.Editor editor = getPreferences(0).edit();
		    editor.putLong("lastUpdateTime", lastUpdateTime);
		    editor.commit();        

		    /* Get current Version Number */
		    
		    int newVersion = Integer.valueOf(UpdateVersion);
		    
		    /* Is a higher version than the current already out? */
		    if (newVersion > curVersion) {
		        /* Post a Handler for the UI to pick up and open the Dialog */
		    	  mHandler = new Handler();
		    	 mHandler.post(showUpdate);
		    }     
		    
		    
		   
		}
		else
		{
			
			return;
		}
    }
    public boolean isOnline() {
		
    	/*
		try {
			//Toast.makeText(home_menu.this,"Please wait.. Checking internet connection..." ,Toast.LENGTH_SHORT).show();
	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 nasz.us");
	        
	        int returnVal = p1.waitFor();
	        boolean reachable = (returnVal==0);
	        if (reachable == false)
	        {
	        	Toast.makeText(home_menu.this,"Warning: checking internet connection" ,Toast.LENGTH_SHORT).show();    	
	        }
	        return true;
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		
		
	    return true;		
	    */
    	return true;
	    
	}
}
