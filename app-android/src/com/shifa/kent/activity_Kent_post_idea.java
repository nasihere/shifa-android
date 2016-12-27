package com.shifa.kent;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class activity_Kent_post_idea extends Activity {
 Context ctx;
 Super_Library_AppClass SLAc;
 String title = "";
 String SessionID = "";
 String SessionName = "";
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_kent_idea_post);
    ctx = this;
    this.SLAc = new Super_Library_AppClass(ctx);
    
    
    
    
    Bundle extras = getIntent().getExtras();
    if (extras == null) {
    	finish();
    }
    title = extras.getString("title"); // important line to save in the database
    SessionID  = SLAc.RestoreSessionIndexID("session_id");
    SessionName  = SLAc.RestoreSessionIndexID("session_name");
    final EditText EditTextPostData = (EditText) findViewById(R.id.editVIdeaReadMe);
    
    
    TextView button1 = (TextView) findViewById(R.id.txtVBtnIdeaPost);
    button1.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			String Data = EditTextPostData.getText().toString();
			if (Data != ""){
				
				SLAc.PostWebApi(new String[] {title + "~" + SessionID + "-:-" + SessionName + "-:-" + Data, "http://shifa.in/api/RepertoryService/ExtraSaveReadMe"});
				finish();
				
			}
		}
	});
    
    TextView button2 = (TextView) findViewById(R.id.txtVBtnIdeaCancel);
    button2.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	});

    
    
 
	 
}



@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
		finish();
		return true;
	}
	
    return super.onKeyDown(keyCode, event);
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
