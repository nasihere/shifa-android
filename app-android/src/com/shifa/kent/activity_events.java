package com.shifa.kent;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class activity_events extends Activity {
	ProgressDialog progressDialog ;
	   ListView listView;
	   public Super_Library_AppClass SLAc; 
	   String source = "";
	   private WebView webView;
	   public String SessionID = "";
	   Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_events);
		ctx = this;
		
		 SLAc = new Super_Library_AppClass(this);
		 SessionID  = SLAc.RestoreSessionIndexID("session_id");
	        
		String url = IntentBunddle("url");
		try
		{
			source= IntentBunddle("source");
		}
		catch(Exception ex){}
		webView = (WebView) findViewById(R.id.webView);

		webView.getSettings().setJavaScriptEnabled(true);
		//webView.clearHistory();
		if (url.equals(""))
		{
			url = 	"http://shifa.nasz.us/events.html";
		}
		
		Log.e("URL",url);
				
		//String customHtml = "<html><body><h2>Greetings from JavaCodeGeeks</h2></body></html>";
		//webView.loadData(customHtml, "text/html", "UTF-8");
		
				webView.setWebViewClient(new WebViewClient() {
			        @Override
			        public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        	Log.e("URL", url);
			            
			            if (url.indexOf("APP_KENT_REPORT") != -1)
			            {
			            	String[] splitString = url.split("#-#");
			            	String where = splitString[1].replace("$", "'");
			            	where = where.replace("%20", " ");
			            	//#APP_KENT_REPORT-$Back, Bifida$,$Back, Brown Spots On$
			            	
			            	DBclass db1 = new DBclass(ctx);
			            	String q = "";
			            	q = "update tbl_shifa set selected = '0' where selected = '1'";
			            	db1.DbQry(q);
			            	q = "update tbl_shifa set selected = '1' where upper(newrem) in ("+ where + ")";
			            	db1.DbQry(q);
			            	
			           	 progressDialog =  ProgressDialog.show(activity_events.this, "", 
			         			"Please wait.. Arranging all remedies & Symptoms..", true);
			     		  Intent intent = new Intent(activity_events.this, activity_report.class);
			     		  
			     		
			   		  intent.putExtra("pms_filename", splitString[2]);
			   		  intent.putExtra("pms_comment", splitString[3]);
			   		intent.putExtra("pms_contact", splitString[4]);
			   		
			   		  
			     		  startActivity(intent);
			     		  progressDialog.dismiss();
			     		  return true;
			            }
			            else if (url.indexOf("buynowwebpayment.php?success") != -1)
			            {
			            	SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
							editor.putString("RemoveAds","true");
							editor.commit();
							SLAc.SavePreference("Counter~Home~Remove~ads~Paid","4");
						    
						   
						    Intent intent = new Intent(activity_events.this, home_menu.class);
		        			startActivity(intent);
		        			 finish();
			            }
			            else
			            {
			            	
			            	view.loadUrl(url);
			            }
			            return false;
			        }
			    });
				
				webView.loadUrl(url);
	}	
	
	private String IntentBunddle(String Session_Key)
	{
		try
		{
		 Bundle extras = getIntent().getExtras();
		  String SessionID = "";
		  if (extras != null) {
		       SessionID = extras.getString(Session_Key);
		       return SessionID ;
		  }
		}
		  catch(Exception ex)
		  {
			 return "http://shifa.nasz.us/events.html";
		  }
		return "";
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(webView.canGoBack() == true){
                	webView.goBack();
                }else{
                	//Intent intent = new Intent(activity_events.this, home_menu.class);
        			//startActivity(intent);
        			finish();
                	
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
		
		
	    
	}
}
