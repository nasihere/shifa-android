package com.shifa.kent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Super_Advertisement_BuyNow extends Activity {
	boolean chatactive = true;
	String notification_id = "";
int notifyid = 1;
	Super_Library_AppClass SLAc; 
	Handler mHandler;
	String SessionID  = "";
	public boolean showBigAds = false;
	
	Context ctx;
	Activity parentActivity = new Activity();
	LinearLayout baseIdNotification;
	public Super_Advertisement_BuyNow(Context ctx, Activity c, LinearLayout baseIdNotification)
	{
		SLAc = new Super_Library_AppClass(ctx); 
		SessionID  = SLAc.RestoreSessionIndexID("session_id");
		this.ctx = ctx;
		parentActivity = c;
		this.baseIdNotification = baseIdNotification;
	}
	
	   
}
