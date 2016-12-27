package com.shifa.kent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.IabResult;
import com.google.android.gcm.GCMRegistrar;
import com.shifa.kent.inappbilling.Log;
import com.shifa.kent.inappbilling.PurchaseActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that In App Purchasing is available on this device
 * 
 * @author Blundell
 * 
 */
public class StartUpActivity extends PurchaseActivity implements OnIabSetupFinishedListener {
	private static int SPLASH_TIME_OUT = 3000;
	Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startupactivity);
        ctx  = this;
        Log.d("App started");
//        popBurntToast("Please wait.. Shifa App is loading...");
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            Log.d("In-app Billing set up" + result);
            dealWithIabSetupSuccess();
        } else {
            Log.d("Problem setting up In-app Billing: " + result);
            dealWithIabSetupFailure();
        }
    }
    public String GetPreferenceValue(String StringName) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				"AppNameSettings", 0);
		String restoredText = prefs.getString(StringName, null);
		if (restoredText != null) {
        	return restoredText;
		}
		return "0";
	}

    public void SavePreference(String StringName, String StringValue) {

        SharedPreferences.Editor editor = ctx.getSharedPreferences(
                "AppNameSettings", 0).edit();
        editor.putString(StringName, StringValue);
        android.util.Log.e("SetPreferenceValue " + StringName, StringValue);
        editor.commit();
    }

    private void StartUp(){
        try {
            String curVersion = "vern97";

            //GCM id setting...
            if (GetPreferenceValue("gcmregset").equals("true")) {
                // Toast.makeText(getApplicationContext(), "Push notification already set " + GetPreferenceValue("gcmreg"), Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    if (!GetPreferenceValue("session_id").equals("0")) {
                        GCMRegistrar.checkDevice(ctx);
                        android.util.Log.e("gcmlogin", "reached 1");
                        GCMRegistrar.checkManifest(ctx);

                        android.util.Log.e("gcmlogin", "reached 2");
                        GCMRegistrar.register(StartUpActivity.this,
                                GCMIntentService.SENDER_ID);

                        android.util.Log.e("gcmlogin", "reached 3");
                        String gcmreg = GetPreferenceValue("gcmreg");

                        android.util.Log.e("gcmlogin", "reached 1");
                        if (gcmreg.equalsIgnoreCase("0")) {
                            try {
                                Thread.sleep(3000);
                                gcmreg = GetPreferenceValue("gcmreg");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        android.util.Log.e("gcmlogin", "reached 4");
                        Toast.makeText(getApplicationContext(), "Push notification ID Set " + gcmreg, Toast.LENGTH_SHORT).show();
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("gcmreg", gcmreg));

                        nameValuePairs.add(new BasicNameValuePair("session_id", GetPreferenceValue("session_id")));
                        Super_Library_URLV2 SLU1 = new Super_Library_URLV2("http://kent.nasz.us/app_php/gcmreg.php", nameValuePairs, ((Activity) ctx));
                        android.util.Log.e("gcmlogin", "reached 6");
                        SavePreference("gcmregset", "true");
                    }

                } catch (Exception ex1) {
                   // Toast.makeText(getApplicationContext(), "Push notification not set " + ex1.toString(), Toast.LENGTH_SHORT).show();
                    android.util.Log.e("gcmlogin ", ex1.toString());
                }
            }

            String sGetCurVersion = GetPreferenceValue("PackageVersion");
            Log.d("Package Version 0");
            if (sGetCurVersion.equals(curVersion)) {
                Log.d("Package Version 1");






                Log.d("Database don't need to copy  in startupactivity");
                navigate().toMainActivity();
                finish();
                return;

            } else {
                Log.d("database need to copy so show splash screen ");
                Log.d("Package Version 2");
                popBurntToast("Please wait.. Updating database.. ");
                SavePreference("PackageVersion", String.valueOf(curVersion));

                new Handler().postDelayed(new Runnable() {

    		            /*
    		             * Showing splash screen with a timer. This will be useful when you
    		             * want to show case your app logo / company
    		             */

                    @Override
                    public void run() {

                        // This method will be executed once the timer is over
                        // Start your app main activity
                        try {
                            DBHelper db = new DBHelper(ctx);
                            db.initializeDataBase();


                            if (!GetPreferenceValue("session_id").equals("0")) {
                                try {
                                    GCMRegistrar.checkDevice(ctx);
                                    GCMRegistrar.checkManifest(ctx);
                                    GCMRegistrar.register(StartUpActivity.this,
                                            GCMIntentService.SENDER_ID);


                                    String gcmreg = GetPreferenceValue("gcmreg");

                                    if (gcmreg.equalsIgnoreCase("0")) {
                                        try {
                                            Thread.sleep(3000);
                                            gcmreg = GetPreferenceValue("gcmreg");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    popBurntToast("Push notification ID Set " + gcmreg);
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                    nameValuePairs.add(new BasicNameValuePair("gcmreg", gcmreg));

                                    nameValuePairs.add(new BasicNameValuePair("session_id", GetPreferenceValue("session_id")));
                                     Super_Library_URLV2 SLU1 = new Super_Library_URLV2("http://kent.nasz.us/app_php/gcmreg.php", nameValuePairs, ((Activity) ctx));


                                } catch (Exception ex1) {
                                    android.util.Log.e("GCM ", ex1.toString());
                                }
                            }

                            navigate().toMainActivity();
                            finish();

                        }catch(Exception ex){}
                        return;
                    }
                }, SPLASH_TIME_OUT);
                return;

            }

        } catch (Exception e1) {
            popBurntToast("Please contact App Developer... nasihere@gmail.com" );
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    @Override
    protected void dealWithIabSetupSuccess() {
         
    	try{
    		

    		StartUp();
		 
	    	 
    	}
    	catch(Exception ex){
    		popBurntToast("Please contact App Developer... nasihere@gmail.com ");
    	}
    	Log.d("loading login screen");
        navigate().toMainActivity();
        finish();
        
        
    }

    @Override
    protected void dealWithIabSetupFailure() {
        popBurntToast("Sorry In App Billing isn't available on your device");
        ctx = this;
        StartUp();
    }
}
