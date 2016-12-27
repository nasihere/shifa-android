package com.shifa.kent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.shifa.kent.inappbilling.BlundellActivity;
import com.shifa.kent.inappbilling.MainMenu;
import com.shifa.kent.inappbilling.Navigator;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class login extends BlundellActivity implements MainMenu {

	private String TAG = login.class.getSimpleName();
	private Session session;
	boolean FBLoginAvailable = false;
	String FBHashKey = ""; 
	private UiLifecycleHelper lifeCycleHelper;
	public static final List<String> LOGIN_PERMISSIONS = Arrays.asList("email",
			"user_location", "user_birthday", "user_likes",
			"user_relationship_details", "user_about_me", "user_photos");
	private SessionStatusCallback statusCallback = new SessionStatusCallback();
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data);
		try{
		

		if (Navigator.REQUEST_PASSPORT_PURCHASE == requestCode) {
			if (RESULT_OK == resultCode) {
				dealWithSuccessfulPurchase();
			} else {
				dealWithFailedPurchase();
			}
		}
		lifeCycleHelper.onActivityResult(requestCode, resultCode, data);
		}catch(Exception ex){}
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
		lifeCycleHelper.onResume();
	}catch(Exception ex){}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
		lifeCycleHelper.onPause();
		}catch(Exception ex){}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try{
		lifeCycleHelper.onDestroy();
		}catch(Exception ex){}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try{
		lifeCycleHelper.onSaveInstanceState(outState);
		}catch(Exception ex){}
	}

	ProgressDialog progressDialog;
	boolean onlybackgroundthread = false;

	Context ctx;
	private GraphUser user;
	public String SessionID = "";
	String AutoFBLogin= "";
	public String ShifaSessionID = "";
	// For In-App billing

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		
		ShifaSessionID = GetPreferenceValue("session_id_Migration");
		AutoFBLogin = GetPreferenceValue("FBLoginRequest");
		
		SessionID = LoggedIn();
		Log.e("SessionID", SessionID); 
		if (SessionID.equals("")) { // if no login found then ask as login window
			
			//setContentView(R.layout.startupactivity);
			Log.e("database", "dbclassdn");
			
			LoginCreate();
			

		} else {
			if (ShifaSessionID != "0" || AutoFBLogin != "0"){
				
					Log.e("login","bypass login screen becasue user want to migrate shifa account to fb");
			}else
			{
				
				Intent intent = new Intent(login.this, home_menu.class);
							//Intent intent = new Intent(login.this, activity_kent.class);
							intent.putExtra("book", "Kent");
				intent.putExtra("SessionID", SessionID);
				startActivity(intent);
				finish();
				return;
			}
		}
		
		
		

		//	SetPreferenceValue("FBLoginRequest", "");
		try{
			lifeCycleHelper = new UiLifecycleHelper(this, callback);
			lifeCycleHelper.onCreate(savedInstanceState);
			generateHashKey();
			FBLoginAvailable = true;
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Facebook login will not work in old android OS " + ex.toString(), Toast.LENGTH_SHORT).show();
		}
		if (ShifaSessionID != "0" || AutoFBLogin != "0"){
			SetPreferenceValue("session_id_Migration", "");
			SetPreferenceValue("FBLoginRequest", "");
			FBLogin();
			
			return;
		}

		


		/*
		 * ctx = this; SessionID = LoggedIn(); Log.e("SessionID",SessionID);
		 * Intent intent1 = new Intent(login.this, activity_events.class);
		 * startActivity(intent1); finish();
		 */

	}

	protected void FBLogin() 
	{
		session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			//if (isOnline() == true){
			
					session.openForRead(new Session.OpenRequest(this).setCallback(
					statusCallback).setPermissions(LOGIN_PERMISSIONS));
		//	}

		} else {

			try{
			// clear fb
					//Toast.makeText(getApplicationContext(),
					//"Load facebook existing information.",
					//Toast.LENGTH_LONG).show();
					
					Session.openActiveSession(this, true, statusCallback);
					
					/*
					
					try{
					Session session =  Session.getActiveSession();
					if (session != null) {
						if (!session.isClosed()) {
							session.closeAndClearTokenInformation();
							Utility.clearFacebookCookies(this);
						}
					}	
					}
					catch(Exception ex){
						Toast.makeText(getApplicationContext(),
						"Error in FB 101 " + ex.toString() ,
						Toast.LENGTH_SHORT).show();	
					}
					
					session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback).setPermissions(LOGIN_PERMISSIONS));
					return;
					*/
			}
			catch(Exception ex)
			{
					Toast.makeText(getApplicationContext(),
						"Facebook error: " + ex.toString() ,
						Toast.LENGTH_SHORT).show();	
					LoginCreate();
			}
			
			
	
				
			
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session.isOpened()) {
				
				Request.newMeRequest(session, new GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						
						if (user != null) 
						{
							Toast.makeText(getApplicationContext(),"Please wait... Facebook account registering..",Toast.LENGTH_SHORT).show();
							
							Log.v(TAG," User Json >>> "+ user.getInnerJSONObject());
							System.out.println("Response=" + response);
							String FB_ID ="", Email = "", Username = "", FirstName ="", LastName = "", DOB ="", Gender = "",
									Country = "", City = "", Occupation ="", ReferEmail ="", Password ="", Language="",
									Relation ="", Sports ="", ProfilePic ="";

							try{
								if(user.getProperty("email") != null && user.getProperty("email").toString().trim().length() > 0)
									Email = user.getProperty("email").toString().trim();
							}catch(Exception ex){}
							try{
								if(user.getFirstName() != null && user.getFirstName().trim().length() > 0)
									FirstName = user.getFirstName().trim();
							}catch(Exception ex){}
							try{
								if(user.getLastName() != null && user.getLastName().trim().length() > 0)
									LastName = user.getLastName().trim();
							}catch(Exception ex){}
							try{
								if(user.getUsername() != null && user.getUsername().trim().length() > 0)
									Username = user.getUsername().trim();
							}catch(Exception ex){}
							try{
								if(user.getBirthday() != null && user.getBirthday().trim().length() > 0)
									DOB = user.getBirthday().trim();
							}catch(Exception ex){}
							try{
								if(user.getProperty("gender")  != null && user.getProperty("gender").toString().length() > 0)
									Gender = user.getProperty("gender").toString();
							}catch(Exception ex){}
							try{
							if(user.getId() != null && user.getId().length() > 0)
							{
								FB_ID = user.getId();
								ProfilePic = "https://graph.facebook.com/" + FB_ID + "/picture?type=small";
							}
							}catch(Exception ex){}

                            String gcmreg = "";

                            try {
                                GCMRegistrar.checkDevice(ctx);
                                GCMRegistrar.checkManifest(ctx);
                                GCMRegistrar.register(login.this,
                                        GCMIntentService.SENDER_ID);

                                 gcmreg = GetPreferenceValue("gcmreg");


                                if (gcmreg.equalsIgnoreCase("0")) {
                                    try {
                                        Thread.sleep(3000);
                                        gcmreg = GetPreferenceValue("gcmreg");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }catch(Exception ex1){
                                Log.e("GCM ",ex1.toString());
                            }

							/*Log.v(TAG,"FB ID >>> " + FB_ID);
							Log.v(TAG,"email >>> " + Email);
							Log.v(TAG,"FirstName() >>> " + FirstName);
							Log.v(TAG,"Lastname >>> " + LastName);
							Log.v(TAG,"Username >>> " + Username);
							Log.v(TAG,"rthday >>> " + DOB);
							Log.v(TAG,"Gender >>> " +  Gender);
							
							Log.v(TAG,"ProfilePic >>> " + ProfilePic);
							*/	
							String url = "";
								url = "http://kent.nasz.us/app_php/app_reg_1.php?fname="
									+ FirstName+ "&lname="+ LastName+ "&session_id="+FB_ID+"&email="+ Email+ "&dob="+DOB+"&sex="
									+ Gender+ "&country="+ Country+ "&city="+ City+ "&occupation="+Occupation+"&referemailid="
									+ ReferEmail +"&password="+ "&languages="+ Language+ "&relationship_status="+Relation
									+ "&sports="+Sports+"&username="+Username+"&ProfilePic="+ProfilePic +"&gcmreg="+gcmreg + "&ShifaSessionID="+ ShifaSessionID;
							
							
							Log.v(TAG,"URL >>> " + url);

							
							
							
							
							
							
							
							String[] profiledata = { "", "", "", "", "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "", "", "",
							"" };
							
							//showDialog(login.this, "value " + response);	
							SharedPreferences.Editor editor = getSharedPreferences(
									"AppNameSettings", 0).edit();
							editor.putString("session_id", FB_ID);
							editor.putString("session_name", FirstName);
							editor.putString("session_Shifa_id", ShifaSessionID);
							editor.putString("session_id_Migration", "");
							editor.putString("profilepic", ProfilePic);
							editor.putString("email", Email);
							editor.putString("sex", Gender);
							editor.commit();

							
							
							
							
							
							
							
							FBLoginTask task = new FBLoginTask();
							task.execute(new String[] {url});

						}
					}
				}).executeAsync();
			}
			else
			{
				
				LoginCreate();
				
			}

			if (exception != null) {
				exception.printStackTrace();
			}
		}
	}

	private boolean cancelBuyNow() {
		try {
			Bundle extras = getIntent().getExtras();
			String SessionID = "";
			if (extras != null) {
				SessionID = extras.getString("CancelBuyNow");
				if (SessionID.equals("true")) {
					return true;
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	private void ShowBuyNowAlert() {
		if (cancelBuyNow() == true) {
			Log.e("Error Else Homemenu", "--------------5-----------");
			Intent intent = new Intent(login.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
			startActivity(intent);
			finish();
			return;
		}
		SharedPreferences prefs = getPreferences(0);
		long lastUpdateTime = 0;
		try {
			Log.e("Error Else Homemenu", "-------------4------------");
			lastUpdateTime = prefs.getLong("lastUpdateTimeBuyNow", 0);

		} catch (Exception ex) {
			Log.e("Error Else Homemenu", "-----------------3--------");
			Intent intent = new Intent(login.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
			startActivity(intent);
			finish();

			Log.e("Error in assiging Variable ", "lastUpdateTime");
			return;
		}
		/* Should Activity Check for Updates Now? */

		if ((lastUpdateTime + (0.01 * 60 * 60 * 1000)) < System
				.currentTimeMillis()) {
			Log.e("Error Else Homemenu", "---------------1----------");
			CheckAdsPaid(); // date changed..

		} else {

			Log.e("Error Else Homemenu", "----------------2---------");
			Intent intent = new Intent(login.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
			startActivity(intent);
			finish();
		}
	}

	public void CheckAdsPaid() {
		try {

			SharedPreferences prefs = getSharedPreferences("AppNameSettings", 0);
			String restoredText = prefs.getString("RemoveAds", null);
			if (restoredText != null) // Register user
			{
				Log.e("Error Else checkads", "-------3------------------");
				Intent intent = new Intent(login.this, home_menu.class);
				intent.putExtra("SessionID", SessionID);
				startActivity(intent);
				finish();
			} else {
				Log.e("Error Else checkads", "-------2------------------");
				Intent intent = new Intent(login.this, buynow.class);
				startActivity(intent);
				finish();

			}
		} catch (Exception ex) {
			Log.e("Error Else checkads", "-------1------------------");
			Intent intent = new Intent(login.this, buynow.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public void onPurchaseItemClick(View v) {
		navigate().toPurchasePassportActivityForResult();
	}

	private void dealWithSuccessfulPurchase() {
		// show toast or whatever you need to show
		
	}

	private void dealWithFailedPurchase() {
		// ask for retry
	}

	private void LoginCreate() {
		DBHelper db = new DBHelper(this);
		// db.initializeDataBase();
	//	 databasecopy dbcopy = new databasecopy(this);

		setContentView(R.layout.login);

		final Button button = (Button) findViewById(R.id.btnSignin);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				EditText edtemailid = (EditText) findViewById(R.id.edtLoginEmailId);
				EditText edtpass = (EditText) findViewById(R.id.edtLoginpassword);
				
				//if (isOnline() == true){
					progressDialog = ProgressDialog.show(login.this, "",
							"Connecting internet... Please wait", true);
					onlybackgroundthread = false;



					DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(new String[] { "http://kent.nasz.us/app_php/app_login.php?email="
							+ edtemailid.getText().toString()
							+ "&password="
							+ edtpass.getText().toString()
							 });
				//}

				// task.execute(new String[] {
				// "http://kent.nasz.us/app_php/app_login.php?email=nasihere@gmail.com&password=111"});

			}
		});

		final Button btnFB = (Button) findViewById(R.id.btnFB);
		btnFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				if (FBLoginAvailable == false){
					SetPreferenceValue("FBLoginRequest", "true");
					Intent intent = new Intent(ctx, login.class);
					ctx.startActivity(intent);
					finish();
				}
				else
				{
				FBLogin();
				}
			}
		});

		final TextView tvLoginGuest = (TextView) findViewById(R.id.tvLoginasguest);
		tvLoginGuest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				SharedPreferences.Editor editor = getSharedPreferences(
						"AppNameSettings", 0).edit();
				editor.putString("session_id", "123456789");
				editor.putString("session_name", "Demo");
				Log.e("session_name", "Demo");
				editor.commit();

				Intent intent = new Intent(login.this, home_menu.class);
				intent.putExtra("SessionID", "123456789");
				startActivity(intent);
				finish();
			}
		});

		final TextView tvLoginweb = (TextView) findViewById(R.id.tvLoginThroughWeb);
		tvLoginweb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://kent.nasz.us/app_php/register/form.php"));
				startActivity(browserIntent);

			}

		});

		final TextView textview1 = (TextView) findViewById(R.id.view_home_chat);
		textview1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(login.this, registration.class);
				intent.putExtra("SessionID", SessionID);
				startActivity(intent);

			}

		});
		final TextView tvLoginForgotPassword = (TextView) findViewById(R.id.tvLoginForgotPassword);
		tvLoginForgotPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Dialog();
			}
		});

		final TextView tvLoginHelp = (TextView) findViewById(R.id.tvLoginHelp);
		tvLoginHelp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(login.this, activity_events.class);
				intent.putExtra("url",
						"http://kent.nasz.us/app_php/howtouseshifa/howtouseshifa.php");
				startActivity(intent);
			}
		});
	}

	private class FBLoginTask extends AsyncTask<String, Context, String> {
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
					HttpGet httpGet = new HttpGet(new URI(url.replace(" ", "%20")));
					try {
						HttpResponse execute = client.execute(httpGet);
						InputStream content = execute.getEntity().getContent();

						BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
						String s = "";
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
			Log.e("Response data background",response);

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			if(progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();

			
			
			
			if (result.equals("-999"))
				return;
			try {
				Log.e("Response result Data", result);
				String login = result.trim();

				Log.e("Response Data", login);
								
				if(!login.equalsIgnoreCase("-1"))
				{
					
					//showDialog(login.this, "Registration has been successful ");
					
					String[] profiledata = { "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
					"" };
					String[] tmp = login.split(":");
					if (tmp[4].equalsIgnoreCase("Success"))
					{
					
						for (int i = 0; i < tmp.length; i++) {
							profiledata[i] = tmp[i];
						}
	
						SharedPreferences.Editor editor = getSharedPreferences(
								"AppNameSettings", 0).edit();
						
						if (GetPreferenceValue("Counter~Home~Remove~ads~Paid") == "0"){
							if (profiledata[2].equals("1")) {
									editor.putString("RemoveAds", "true");
								} else {
									editor.remove("RemoveAds");
								}
			
								if (profiledata[3].equals("1")) {
			
									editor.putString("Settings~Google~ads", "false");
								} else {
			
									editor.putString("Settings~Google~ads", "true");
							}
						}
						else
						{
							editor.putString("RemoveAds", "true");// important flag to decide home page menu to show buynow or not
							editor.putString("Settings~Google~ads", "false"); // important flag to decide home page menu to show buynow or not
						}
						
						editor.commit();
					}
					
				}
			} catch (Exception ex) {
				// Toast.makeText(getApplicationContext(), ex.toString(),
				// Toast.LENGTH_SHORT).show();

			}
			
			
			Intent intent = new Intent(login.this, home_menu.class);
			intent.putExtra("SessionID", SessionID);
			startActivity(intent);
			finish();

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

		}
	}

	public static void showDialog(final Activity activity, String message) 
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		dialog.setTitle(R.string.app_name);
		dialog.setMessage(message);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
				Intent intent = new Intent(activity, home_menu.class);
				//				intent.putExtra("SessionID", login);
				activity.startActivity(intent);
				activity.finish();
			}
		});
		dialog.show();
	}


	public void Dialog() {
		String Action = "nasiXXXX@gmail.com";// ((TextView)
		// view.findViewById(R.id.lv_tv_note)).getText().toString();

		final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
		final EditText input = new EditText(ctx);
		input.setText(Action);
		alert.setTitle("Enter your email address.");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				//if (isOnline() == true){
					progressDialog = ProgressDialog.show(login.this, "",
							"Connecting internet... Please wait", true);
					onlybackgroundthread = false;
					DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(new String[] { "http://kent.nasz.us/app_php/app_forgotpassword.php?email="
							+ value });
				//}

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();

	}

	private class DownloadWebPageTask extends
	AsyncTask<String, Context, String> {
		protected Context ctx;

		@Override
		protected String doInBackground(String... urls) {
			
			Log.e("doInBackground", "enter");
			String response = "";
			String uri = "";
			for (String url : urls) {
				uri = url;
				Log.e("uri", uri);
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);
					try {
						HttpResponse execute = client.execute(httpGet);
						InputStream content = execute.getEntity().getContent();

						BufferedReader buffer = new BufferedReader(
								new InputStreamReader(content));
						String s = "";
						while ((s = buffer.readLine()) != null) {
							response += s;
						}

					} catch (Exception e) {
						Log.e("Error http:", e.toString());

						e.printStackTrace();
						return "-999";
					}
				} catch (Exception ex) {
					Log.e("Error http:", ex.toString());
					return "-999";
				}
			}
			Log.e("Response data background", response);

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if (result.equals("-999"))
				return;
			try {
				Log.e("Response result Data", result);
				String login = result.trim();

				Log.e("Response Data", login);
				if (login.equals("-1")) {
					Toast.makeText(getApplicationContext(),
							"Email id and password invalid.",
							Toast.LENGTH_SHORT).show();
				} else if (login.indexOf("-101-") != -1) {
					Log.e("Response Condition", "-=-101-=-");
					String[] data = login.split("-:-"); // Login Should return =
					// "-=-101-=--:-Password Sent you by email."
					Log.e("Response Condition", "Split");
					Toast.makeText(getApplicationContext(), data[1],
							Toast.LENGTH_SHORT).show();
				} else {
					
					String[] profiledata = { "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "",
					"" };
					String[] tmp = login.split(":");
					if (tmp[4].equalsIgnoreCase("Success"))
					{
						
						for (int i = 0; i < tmp.length; i++) {
							profiledata[i] = tmp[i];
						}
	
						SharedPreferences.Editor editor = getSharedPreferences(
								"AppNameSettings", 0).edit();
						editor.putString("session_id", profiledata[0]);
						editor.putString("session_name", profiledata[1]);
						
						if (GetPreferenceValue("Counter~Home~Remove~ads~Paid") == "0"){
							if (profiledata[2].equals("1")) {
									editor.putString("RemoveAds", "true");
								} else {
									editor.remove("RemoveAds");
								}
			
								if (profiledata[3].equals("1")) {
			
									editor.putString("Settings~Google~ads", "false");
								} else {
			
									editor.putString("Settings~Google~ads", "true");
							}
						}
						else
						{
							editor.putString("RemoveAds", "true");// important flag to decide home page menu to show buynow or not
							editor.putString("Settings~Google~ads", "false"); // important flag to decide home page menu to show buynow or not
						}
						Log.e("session_name", profiledata[1]);
						editor.commit();




					}
					Intent intent = new Intent(login.this, home_menu.class);
					intent.putExtra("SessionID", SessionID);
					startActivity(intent);
					finish();
				}
			} catch (Exception ex) {
				// Toast.makeText(getApplicationContext(), ex.toString(),
				// Toast.LENGTH_SHORT).show();

			}

		}
	}

	private String LoggedIn() {
		SharedPreferences prefs = getSharedPreferences("AppNameSettings", 0);
		String restoredText = prefs.getString("session_id", null);
		if (restoredText != null) {

			return restoredText;
		}
		return "";
	}
	
	private boolean NewVersionAppUpdateEvent(String restoredText) {
		if (restoredText.equals(""))
			return false;
		try {
			int curVersion = ctx.getPackageManager().getPackageInfo(
					"com.shifa.kent", 0).versionCode;

			String sGetCurVersion = GetPreferenceValue("PackageVersion");
			if (sGetCurVersion.equals(String.valueOf(curVersion))) {
				Log.e("Login Credential",
						"Do not check on server .. do nothing...");
				return false;

			} else {
				Log.e("Login Credential",
						"Version number is different now check on server with session id is it paid or not");


				return true;

			}

		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return false;

	}

	public String GetPreferenceValue(String StringName) {
		SharedPreferences prefs = ctx
				.getSharedPreferences("AppNameSettings", 0);
		String restoredText = prefs.getString(StringName, null);
		if (restoredText != null && restoredText != "") {
			return restoredText;
		}
		return "0";
	}

	public void SetPreferenceValue(String Key, String Value) {
		SharedPreferences.Editor editor = getSharedPreferences(
				"AppNameSettings", 0).edit();
		editor.putString(Key, Value);
		editor.commit();
	}

	private void openURL(String URL) {

		Intent intent = new Intent(login.this, activity_events.class);
		intent.putExtra("url", URL);
		intent.putExtra("source", "Login");
		startActivity(intent);
		finish();
	}

	private void generateHashKey() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				FBHashKey = Base64.encodeToString(md.digest(),Base64.DEFAULT);
				Log.e("KeyHash:","KeyHash: HashKey1: " + FBHashKey);
				Log.e("KeyHash:","KeyHash: HashKey2: " + Base64.encodeToString(md.digest(),Base64.DEFAULT));
				Log.e("KeyHash:","KeyHash: HashKey3: " + Base64.encodeToString(md.digest(),Base64.DEFAULT));
				Toast.makeText(getApplicationContext(), "Initializing: "+ FBHashKey, Toast.LENGTH_SHORT).show();
				
			}
		} catch (NameNotFoundException e) {
			Toast.makeText(getApplicationContext(), "some problem with facebook init "+ e.toString(), Toast.LENGTH_SHORT).show();
			Log.d("KeyHash:", "KeyHash: " + e);
		} catch (NoSuchAlgorithmException e) {
			Toast.makeText(getApplicationContext(), "some problem with facebook init "+ e.toString(), Toast.LENGTH_SHORT).show();
			Log.d("KeyHash:", "KeyHash: " + e);
		}
	}
	
	public boolean isOnline() {
		
		try {
			Toast.makeText(login.this,"Please wait.. Checking internet connection..." ,Toast.LENGTH_SHORT).show();
	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 nasz.us");
	        
	        int returnVal = p1.waitFor();
	        boolean reachable = (returnVal==0);
	        if (reachable == false)
	        {
	        	Toast.makeText(login.this,"internet connection required" ,Toast.LENGTH_SHORT).show();    	
	        }
	        return true;
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		
		
	    return true;		
	    
	    
	}
}
