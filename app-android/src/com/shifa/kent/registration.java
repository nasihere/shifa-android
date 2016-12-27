package com.shifa.kent;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class registration extends Activity {
	 ProgressDialog progressDialog ;
	 boolean onlybackgroundthread = false;
	 Context ctx;
	 public String gcmreg = "";
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        ctx = this;

        try {
            GCMRegistrar.checkDevice(ctx);
            GCMRegistrar.checkManifest(ctx);
            GCMRegistrar.register(registration.this,
                    GCMIntentService.SENDER_ID);

        }catch(Exception ex1){
            Log.e("GCM ",ex1.toString());
        }
        final Button button = (Button) findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {

        	  @Override
        	  public void onClick(View view) {
        		  
        		EditText fname = (EditText) findViewById(R.id.edRegFName);
      			EditText lname = (EditText) findViewById(R.id.edRegLName);
      			 Log.e("Error http:", "1");
      			EditText email = (EditText) findViewById(R.id.edRegEmail);
      			EditText dob = (EditText) findViewById(R.id.edRegDob);
      			EditText referemailid  = (EditText) findViewById(R.id.edRegReferEmailId);
      			RadioButton sex = (RadioButton) findViewById(R.id.radioMale);
      			
      			EditText country = (EditText) findViewById(R.id.edRegCountry);
      			EditText city = (EditText) findViewById(R.id.edRegCity);
      			Log.e("Error http:", "2");
      			RadioButton occupation = (RadioButton) findViewById(R.id.radio_occu_Student);
      			EditText password = (EditText) findViewById(R.id.edRegPassword);
      			
      			RadioGroup radioButtonGroup = (RadioGroup) findViewById(R.id.radioSex);
      			int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
      			View radioButton = radioButtonGroup.findViewById(radioButtonID);
      			int idx = radioButtonGroup.indexOfChild(radioButton);
      			String  sexs = String.valueOf(idx);
      			Log.e("idx",sexs);
      			
      			
      			RadioGroup radioButtonGroup1 = (RadioGroup) findViewById(R.id.radioOcc);
      			int radioButtonID1 = radioButtonGroup1.getCheckedRadioButtonId();
      			View radioButton1 = radioButtonGroup1.findViewById(radioButtonID1);
      			int idx1 = radioButtonGroup1.indexOfChild(radioButton1);
      			
      			String  occ1 = String.valueOf(idx1);
      			Log.e("idx1",occ1);
      			
      			String occ = "Doctor";
      			if (occ1.equals("0"))
      			{
      				occ = "Student";
      			}
      			
      			Log.e("idx1 occ",occ);
      			
      			if (fname.getText().toString().equals("")) 
      			{
      				Toast.makeText(getApplicationContext(), "First name is required...", 100).show();
      				return;
      			}
      				if (email.getText().toString().equals("")) 
      			{
      				Toast.makeText(getApplicationContext(), "Email is required...", 100).show();
      				return;
      			}
				if (password.getText().toString().equals("")) 
				{
					Toast.makeText(getApplicationContext(), "Password is required...", 100).show();
					return;
				}
				
      		
      			Log.e("Error http:", "3");
      			
      			
      		  onlybackgroundthread = true;
    			DownloadWebPageTask task = new DownloadWebPageTask();


                  Super_Library_AppClass SLAc = new Super_Library_AppClass(ctx);

                  gcmreg = SLAc.GetPreferenceValue("gcmreg");



      			
      			String web = "http://kent.nasz.us/app_php/app_reg.php?fname=" + fname.getText().toString() + "&" +
      			"lname=" + lname.getText().toString() + "&email=" + email.getText().toString() + "&dob=" + dob.getText().toString() + "&" +
    			"sex=" + sexs + "&country=" + country.getText().toString() + "&city=" + city.getText().toString() + "&" +
    					"occupation=" + occ + "&" +
    	    					"referemailid=" + referemailid.getText().toString()  +
      			 "&password=" + password.getText().toString() + "&gcmreg="+gcmreg;
      			progressDialog =  ProgressDialog.show(registration.this, "", 
                        "Loading. Please wait...", true);
      				Log.e("web",web);
      				web = web.replaceAll(" ", "NASSPACENAS");	
      			task.execute(new String[] { web });

        		 
        	  }

        	});
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
				        HttpGet httpGet = new HttpGet(url);
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
	      Log.e("Response",response);
	     
	      return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	progressDialog.dismiss();
	    
	    	if (result.equals("-999") ) return;
	    	if (result.indexOf("-404") != -1)	
	    	{
	    			try
	    			{
	    				String[] login = result.trim().split("-:-"); 
	    				Toast.makeText(getApplicationContext(), login[1], 1000).show();
	    			}
	    			catch(Exception Ex)
	    			{
	    				
	    			}
	    			return;
	    		}
	    	
	    			Intent intent = new Intent(registration.this, login.class);
					startActivity(intent);
		      		finish();
		     
	    }
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
		
    
}
	