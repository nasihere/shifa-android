package com.shifa.kent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.animation.IntEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
@SuppressLint("NewApi")
public class activity_report extends Activity  {
	
	Cursor cursor;
	Context ctx;
	  SimpleCursorAdapter CursorAdapter;
	  String ShareEmail = "";
	  String ShareSymptoms = "";
	  Activity parentActivity = new Activity();
	  String ShareRemedies = "";
	  DBHelper db1;
	  TableLayout t1;
	  String pms_filename = "";
	  String pms_comment = "";
	  String pms_contact = "";
	  public String Kent_lang_Field = "kent";
		public String allen_lang_Field = "allen";
		public String borik_lang_Field = "data";
	public void Init()
	{
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Log.e("Bundle","True");
            if (extras.getString("pms_filename") != null) pms_filename = extras.getString("pms_filename");
            if (extras.getString("pms_comment") != null) pms_comment = extras.getString("pms_comment");
            if (extras.getString("pms_contact") != null) pms_contact = extras.getString("pms_contact");
        
        }
        
        
        
		db1.getWritableDatabase().execSQL("DELETE FROM tbl_report where 1");
		cursor = db1.getReadableDatabase().rawQuery("Select _id,Remedies,Name,categoy,maincategoy,newrem from tbl_shifa where selected = '1'",null);
		String q = "";
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			//(new ChatMain(cursor.getString(cursor.getColumnIndex("chat")), cursor.getString(cursor.getColumnIndex("frm"))));
			Log.e("Cursor Result : 1", cursor.getString(cursor.getColumnIndex("_id")));
			Log.e("Cursor Result : 2", cursor.getString(cursor.getColumnIndex("Remedies")));
			Log.e("Cursor Result : 3", cursor.getString(cursor.getColumnIndex("Name")));
			Log.e("Cursor Result : 4", cursor.getString(cursor.getColumnIndex("categoy")));
			Log.e("Cursor Result : 5", cursor.getString(cursor.getColumnIndex("maincategoy")));
			
			String sRemedies = cursor.getString(cursor.getColumnIndex("Remedies"));
			 
				String[] sRemediesSplit = sRemedies.split(":");
				Log.e("Cursor sRemediesSplit ", String.valueOf(sRemediesSplit.length));
				String _id = cursor.getString(cursor.getColumnIndex("_id"));
				String _maincategory = cursor.getString(cursor.getColumnIndex("maincategoy"));
				String _name = cursor.getString(cursor.getColumnIndex("categoy")) + ", " + cursor.getString(cursor.getColumnIndex("Name"));
				_name  = _name.replace("|", ", ");
				try
				{
					for(int i = 0; i <= sRemediesSplit.length - 1 ; i++)
					{
						Log.e("Cursor sRemediesSplit i ", String.valueOf(i));
						Log.e("Cursor sRemediesSplit i ", String.valueOf(sRemediesSplit[i]));
						
						String[] sTemp = sRemediesSplit[i].split(",");
						if(sTemp == null) break;
						 q = "Insert Into tbl_report (sync, remedies,  intensity, _idd,Name,maincategoy) values (0,'"+sTemp[0]+"'," + sTemp[1] + "," + _id + ",'" +
						 _name + "','"  + _maincategory + "');";
						 Log.e("report _maincategory ", "Insert Into tbl_report (sync, remedies,  intensity, _idd,Name,maincategoy) values (0,'"+sTemp[0]+"'," + sTemp[1] + "," + _id + ",'" +
								 _name + "','"  + _maincategory + "');");
							
						 db1.getWritableDatabase().execSQL(q);
						 
						 
					}
				}
				catch(Exception ex)
				{
					Log.e("Cursor Exception Error ", ex.toString());
				}
				ShareRemedies +=   "<ITEM><NAME>" + cursor.getString(cursor.getColumnIndex("newrem"))  + "</NAME><REMEDIES>" + sRemedies  + "</REMEDIES></ITEM>"; 
		     cursor.moveToNext();
		}
	
		Bind();
		
		
		
	}
	private void setLangField()
	{
		/*
		 * 0 	"English", 
		 * 1	"Italian", 
		 * 2	"Dutch", 
		 * 3	"German", 
		 * 4	"French", 
		 * 5	"Spanish", 
		 * 6	"Portuguese"};
		 */
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString("MMLang", null);
		if (restoredText != null)  // if its Has some value 
		{
			
			if (restoredText.equals("1"))
			{
				Kent_lang_Field =  "kent_Italian kent";
				allen_lang_Field =   "allen_Italian allen";
				borik_lang_Field =   "data_Italian data";
			}
			else if (restoredText.equals("2"))
			{
				Kent_lang_Field =  "kent_dutch kent";
				allen_lang_Field =   "allen_dutch allen";
				borik_lang_Field =   "data_dutch data";
			}
			else if (restoredText.equals("3"))
			{
				Kent_lang_Field =  "kent_german kent";
				allen_lang_Field =   "allen_german allen";
				borik_lang_Field =   "data_german data";
			}
			
			else if (restoredText.equals("4"))
			{
				Kent_lang_Field =  "kent_french kent";
				allen_lang_Field =   "allen_french allen";
				borik_lang_Field =   "data_french data";
			}
			else if (restoredText.equals("5"))
			{
				Kent_lang_Field =  "kent_spanish kent";
				allen_lang_Field =   "allen_spanish allen";
				borik_lang_Field =   "data_spanish data";
			}
			
			else if (restoredText.equals("6"))
			{
				Kent_lang_Field =  "kent_portuguese kent";
				allen_lang_Field =   "allen_portuguese allen";
				borik_lang_Field =   "data_portuguese data";
			}
			
			
		}
	}
	private void Bind()
	{
		 final Button button2 = (Button) findViewById(R.id.btnKentOverviewSave);
		 button2.setOnClickListener(new View.OnClickListener() {

	    	  @Override
	    	  public void onClick(View view) {
	    		  
	    			String Action = "";//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
	    			
	   			 
	    			final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
	    			alert.setTitle( "Save Report" );
	    			
	    			alert.setMessage("Please fill up the following details");
	    			
	    			LinearLayout layout = new LinearLayout(ctx);
	    			layout.setOrientation(LinearLayout.VERTICAL);
	    		
	    			final EditText contact = new EditText(ctx);
	    			contact.setHint("Contact: Example 99200855XX / 323-300-47XX / nasihere@gmaXX.com");
	    			
	    			if (!pms_contact.equals("")) contact.setText(pms_contact);
	    			layout.addView(contact);

	    			final EditText filename = new EditText(ctx);
	    			filename.setHint("Name: Example 20/F/Shifa Sayed");
	    			if (!pms_filename.equals("")) filename.setText(pms_filename); 
	    			layout.addView(filename);
	    			

	    			
	    			final EditText comment = new EditText(ctx);
	    			comment.setHint("Comment: Example You are sufferring from _________ ");
	    			if (!pms_comment.equals("")) comment.setText(pms_comment);
	    			layout.addView(comment);
	    			
	    			
	    			final TextView Note = new TextView(ctx);
	    			Note.setText("We request you to please fill contact information such as mobile no. or email id to track your patient history further.");
	    			layout.addView(Note);
	    			
	    			
	    			alert.setView(layout);
	    			
	    			
	    			
	    		    
	    			
	    			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		        public void onClick(DialogInterface dialog, int whichButton) {
	    		        	Super_Library_AppClass SLAc = new Super_Library_AppClass(ctx); 
	    		    		  
	    		            String value1 = contact.getText().toString().trim();
	    		            String value2 = comment.getText().toString().trim();
	    		            String value3 = filename.getText().toString().trim();
	    		            String SessionID = SLAc.RestoreSessionIndexID("session_id");
	    		            
	    		            if (value1.equals("")) 
	    		            {
	    		            	Toast.makeText(ctx, "Contact Required", 1000).show();
	    		            	return;
	    		            }
	    		            else if (value3.equals("")) 
	    		            {
	    		            	Toast.makeText(ctx, "Age/Sex/Name Required!", 1000).show();
	    		            	return;
	    		            }
	    		              String ShareSymptomsTmp = ShareSymptoms.substring(0,ShareSymptoms.length() - 1);
	    		              ShareSymptomsTmp = ShareSymptomsTmp.replace(", ", "|");
	    		              ShareSymptomsTmp = ShareSymptomsTmp.toUpperCase();
	    		    		  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    		    		  nameValuePairs.add(new BasicNameValuePair("session_id", SessionID));
	    		    		  nameValuePairs.add(new BasicNameValuePair("general", ShareRemedies));
	    		    		  nameValuePairs.add(new BasicNameValuePair("apphtml", ShareEmail));
	    		    		  nameValuePairs.add(new BasicNameValuePair("symptoms", ShareSymptomsTmp));
	    		    		  
	    		    		  nameValuePairs.add(new BasicNameValuePair("filename", value3));
	    		    		  nameValuePairs.add(new BasicNameValuePair("contact", value1));
	    			          nameValuePairs.add(new BasicNameValuePair("comment", value2));
	    			          
	    		   		  Super_Library_URL SLU = new Super_Library_URL( "http://kent.nasz.us/app_php/app_kent_report_sync.php", SessionID  , nameValuePairs, parentActivity  );
	    		    		  
	    		    		  
	    	           
	    		            
	    		        }
	    		    });

	    		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		        public void onClick(DialogInterface dialog, int whichButton) {
	    		            dialog.cancel();
	    		        }
	    		    });
	    		    alert.show();  
	    		
	    		    
	    		   
	    		    
	    	  }

    	});
	        
	    /*    
		 final Button button = (Button) findViewById(R.id.btnTShareTable1);
	        button.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		  View u = findViewById(R.id.main_table);
	        	         u.setDrawingCacheEnabled(true);                                                
	        	         View  z = (View) findViewById(R.id.main_table);
	        	         int totalHeight = z.getHeight();
	        	         int totalWidth = z.getWidth();
	        	         u.layout(0, 0, totalWidth, totalHeight);    
	        	         u.buildDrawingCache(true);
	        	         Bitmap b = Bitmap.createBitmap(u.getDrawingCache());             
	        	         u.setDrawingCacheEnabled(false);

	        			    try 
	        			    {
	        			    		String path = Images.Media.insertImage(getContentResolver(), b,"title", null);
	        			    	    Uri screenshotUri = Uri.parse(path);
	        			    	    final Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
	        			    	    emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			    	    emailIntent1.putExtra(Intent.EXTRA_STREAM, screenshotUri);
	        			    	    emailIntent1.setType("image/png");
	        			    	    startActivity(Intent.createChooser(emailIntent1, "Send email using"));

	        			    } 
	        			    catch (Exception e) 
	        			    {
	        			        e.printStackTrace();
	        			    }
	        	  }

	        	});
	        
	        
	        final Button button1 = (Button) findViewById(R.id.btnTShareTable2);
	        button1.setOnClickListener(new View.OnClickListener() {

	        	  @Override
	        	  public void onClick(View view) {
	        		  View u = findViewById(R.id.main_table_total);
	        	         u.setDrawingCacheEnabled(true);                                                
	        	         View  z = (View) findViewById(R.id.main_table_total);
	        	         int totalHeight = z.getHeight();
	        	         int totalWidth = z.getWidth();
	        	         u.layout(0, 0, totalWidth, totalHeight);    
	        	         u.buildDrawingCache(true);
	        	         Bitmap b = Bitmap.createBitmap(u.getDrawingCache());             
	        	         u.setDrawingCacheEnabled(false);

	        			    try 
	        			    {
	        			    		String path = Images.Media.insertImage(getContentResolver(), b,"title", null);
	        			    	    Uri screenshotUri = Uri.parse(path);
	        			    	    final Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
	        			    	    emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			    	    emailIntent1.putExtra(Intent.EXTRA_STREAM, screenshotUri);
	        			    	    emailIntent1.setType("image/png");
	        			    	    startActivity(Intent.createChooser(emailIntent1, "Send email using"));

	        			    } 
	        			    catch (Exception e) 
	        			    {
	        			        e.printStackTrace();
	        			    }
	        	  }

	        	});
		    */
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			
			  finish();
	    		
	    		   return true;
	    			 
	    
	    }
		
	   return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		parentActivity = this;
		setContentView(R.layout.report);
		
		

		
    	db1 = new DBHelper(this);
    	setLangField();
		Init();
		CreateTable1();
		CreateTable2();
		CreateTable3();
		ShareEmail += "</table>";
		Toast.makeText(getApplicationContext(), "Report aligned by horizontally. Use Horizontal Scroll!!", 1000).show();
	}
	@SuppressWarnings("deprecation")
	public void CreateTable1()
	{
		ShareEmail += "<table style=\"font-size: small;\" border=1>";
		
		t1 = (TableLayout) findViewById(R.id.main_table);
		cursor = db1.getReadableDatabase().rawQuery("select   * from tbl_report  order by remedies",null);
		Log.e("cursor ", "cursor");
		int iCount = cursor.getCount();
		if (iCount == 0) 
			{
			Intent intent1 = new Intent(activity_report.this, activity_kent.class);
			startActivity(intent1);
	  		finish();
			return;
			}
		Log.e("COUNT",String.valueOf(iCount));
//////////////////////// START NEW CODE	
		ArrayList<String[]> dataList = new ArrayList<String[]>();
		Log.e("Arra4441 ", "cursor 1 ");
		String tempX = "";
		ArrayList<String> xList = new ArrayList<String>();
		int xListCounter = 0;
		Log.e("ArrayList 1 ", "cursor 1 ");
		String tempY = "";
		String tempZ = "";
		ArrayList<String> yList = new ArrayList<String>();
		
		 ArrayList<String> zList = new ArrayList<String>();
		Log.e("ArrayList ", "cursor");
		if (cursor.moveToFirst()){
			try
			{
				do{
					
					String iIntensity = cursor.getString(cursor.getColumnIndex("intensity"));
					if (iIntensity.equals("") || iIntensity.equals(null)) iIntensity = "-";
					dataList.add(new String[]{ 
							cursor.getString(cursor.getColumnIndex("_id")), 
							cursor.getString(cursor.getColumnIndex("remedies")),
							cursor.getString(cursor.getColumnIndex("Name")),
							iIntensity
							});
					Log.e("ArrayList2 ", "cursor 1 ");
					tempX = cursor.getString(cursor.getColumnIndex("remedies"));
					if (!xList.contains(tempX)){
						Log.e("xlist ", tempX);
						
						xList.add(tempX);
						xListCounter++;
						tempX = "";
					}
					Log.e("ArrayList 3 ", "cursor 1 ");
					tempY = cursor.getString(cursor.getColumnIndex("Name"));
					tempZ = cursor.getString(cursor.getColumnIndex("_idd"));
					if (!yList.contains(tempY)){
						yList.add(tempY);
						zList.add(tempZ);
						tempY = "";
					}
					Log.e("ArrayList4 ", "cursor 1 ");
				} while(cursor.moveToNext());
			}
			catch (Exception e) {
				Log.e("error ", "e xceptopmn e");
			}
		}
		Log.e("dataList ", "dataList.size() == 0");
		if(dataList.size() == 0)
			return;
		Log.e("dataList data ", "String[] data : dataList");
	/*	for(String[] data : dataList){
			String out = "";
			for(String str : data){
				out = out + "/" +str;
			}
			Log.d("TESTTOOLZ", out);
		}*/
		Log.d("TESTTOOLZ 0 11 ","new array size ");
		int xIndex;
		int yIndex;
		//Rearrange array
		//Collections.sort(xList);//sorting X name list
		//Collections.sort(yList);//sorting Y name list
		Log.d("TESTTOOLZ 1 11 ","new array size ");
		String[][] dataArray = new String[xList.size()][yList.size()];//generating new data array
		Log.d("TESTTOOLZ","new array size "+dataArray.length+" ("+xList.size()+"*"+yList.size()+")");
		//filling array with empty strings
		for(xIndex = 0; xIndex < xList.size(); xIndex++){
			Arrays.fill(dataArray[xIndex], "");
		}
		Log.d("TESTTOOLZ","filling array with possible non empty data");
		
		for(String[] item : dataList){//filling array with possible non empty data
			xIndex = xList.indexOf(item[1]);
			yIndex = yList.indexOf(item[2]);
			Log.d("TESTTOOLZ",item[1] + "(" + xIndex + ") / " + item[2] + "(" + yIndex + ") / " + item[3]);
			dataArray[xIndex][yIndex] = item[3];
		}

		TableRow tr;
		TextView tvItem;
		ImageView ImItem;
		ShareEmail += "<tr>";
		
		//add table name header
		tr = new TableRow(this);
		tr.setId(100);
		tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tvItem = new TextView(this);
		tvItem.setText("Remove"); tvItem.setPadding(5, 5, 5, 5);
		
		tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
		tr.addView(tvItem);
		
	
		tvItem = new TextView(this);
		tvItem.setText("Symptom"); tvItem.setPadding(5, 5, 5, 5);
		ShareEmail += "<th>Symptom</th>";
		tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
		tr.addView(tvItem);

		tvItem = new TextView(this);
		tvItem.setText(""); tvItem.setPadding(5, 5, 5, 5);
		
		tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
		tr.addView(tvItem);
		
		
		for(xIndex = 0; xIndex < xList.size() ; xIndex++){
			tvItem = new TextView(this);
			Log.e("xIndex",String.valueOf(xIndex) +  " = "+ xList.get(xIndex));
			tvItem.setText(xList.get(xIndex)); tvItem.setPadding(5, 5, 5, 5);
			tvItem.setPaintFlags(tvItem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			tvItem.setTag(xList.get(xIndex));
			tvItem.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			       String sItem  = (String) v.getTag();
					populatedatabase(
							"SELECT _id,"+borik_lang_Field+","+allen_lang_Field+","+Kent_lang_Field+" FROM tbl_rem_info where upper(rem)  = '"
									+ sItem.toUpperCase() + "'", "Data",
									false);
			    }
			});
			ShareEmail += "<th>"+xList.get(xIndex)+"</th>";
			tvItem.setBackgroundColor(Color.GRAY); tvItem.setTextColor(Color.WHITE);
			tvItem.setGravity(Gravity.CENTER);
			tr.addView(tvItem);
		}
		t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ShareEmail += "</tr>";
		//set table cells

	
		int reportTableBG = getResources().getColor(R.color.reportTableBG);
		for(yIndex = 0; yIndex < yList.size(); yIndex++){
			ShareEmail += "<tr>";
			//add row number and name
			tr = new TableRow(this);
			tr.setId(101+yIndex);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			
			/* android:id="@+id/column_one"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_cateogryminus"
            android:contentDescription="@string/app_name"*/
			
			ImItem = new ImageView(this);
			ImItem.setTag(zList.get(yIndex));
			
			ImItem.setPadding(5, 5, 5, 5);
			ImItem.setImageResource(R.drawable.ic_cateogryminus);
			ImItem.setOnClickListener(new OnClickListener() {
			    public void onClick(View v)
			    {
			    	
			    	Toast.makeText(getApplicationContext(), "Please wait.. Updating.." , 100).show();
				    String _id = (String) v.getTag();
			    	Log.e("Query Update","UPDATE tbl_shifa set selected = '0' where _id = " + _id);
			    	db1.getWritableDatabase().execSQL("UPDATE tbl_shifa set selected = '0' where selected = '1' and _id = " + _id);
			    	
			    	finish();
			    	startActivity(getIntent());
			    } 

			});

				tr.addView(ImItem);
			
			
			tvItem = new TextView(this);
			tvItem.setText(yList.get(yIndex));
			ShareEmail += "<td>"+yList.get(yIndex)+"</td>"; //Symptoms Name
			ShareSymptoms += "$" + yList.get(yIndex) + "$,";
			//tvItem.setBackgroundColor(Color.GRAY); 
			tvItem.setTextColor(Color.GRAY);
			tr.addView(tvItem);


			tvItem = new TextView(this);
			tvItem.setText(""); tvItem.setPadding(5, 5, 5, 5);
			
			tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
			tr.addView(tvItem);
			
			
			//add cells in row
			for(xIndex = 0; xIndex < xList.size(); xIndex++){
				tvItem = new TextView(this);
				tvItem.setText(dataArray[xIndex][yIndex]);
				
				ShareEmail += "<td>"+dataArray[xIndex][yIndex]+"</td>";
				tvItem.setPadding(5, 30, 5, 0);
				tvItem.setBackgroundColor(reportTableBG); tvItem.setTextColor(Color.BLACK);
				tvItem.setGravity(Gravity.CENTER);
				tr.addView(tvItem);
			}
			t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ShareEmail += "</tr>";
		}
		
		
		
	}
	public void populatedatabase(String sql, String ScreenLayout, boolean back) {
		Log.e("sql ", sql);
		

		cursor = db1.getReadableDatabase().rawQuery(sql, null);
		int sCount = cursor.getCount();
		Log.e("SQL Count", String.valueOf(sCount));
		
		GetData(cursor);
		

	}
	public void GetData(Cursor cursor) {
		cursor.moveToFirst();

		// (new ChatMain(cursor.getString(cursor.getColumnIndex("chat")),
		// cursor.getString(cursor.getColumnIndex("frm"))));
		String sData = "";
		try {;
			Log.e("Cursor Result : 1",
					cursor.getString(cursor.getColumnIndex("data")));
			 sData = cursor.getString(cursor.getColumnIndex("data"));

			
		} catch (Exception ex) {
			
		}
		String sDataKent = "";
		try {
			Log.e("Cursor Result : 1",
					cursor.getString(cursor.getColumnIndex("kent")));
			sDataKent = cursor.getString(cursor.getColumnIndex("kent"));

			
		} catch (Exception ex) {
			
		}
		String sDataallen = "";
		try {
			Log.e("Cursor Result : 2",
					cursor.getString(cursor.getColumnIndex("allen")));
			sDataallen = cursor.getString(cursor.getColumnIndex("allen"));

			
		} catch (Exception ex) {
			
		}
		ShowFeed(sData, sDataallen,sDataKent);

	}

	public void ShowFeed(String sData, String sDataAllen, String sDataKent) {
		Toast.makeText(getApplicationContext(),
				"Please wait....",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(activity_report.this,
				activity_m_medica_tabviewer.class);
		
		
		intent.putExtra("SearchKeyWord", "");
		intent.putExtra("medica_data", sData.trim());
		sDataAllen = sDataAllen.replace(":", ".__").trim();
		intent.putExtra("allen_data", sDataAllen);
		sDataKent = sDataKent.replace(":", ".__").trim();
		Log.e("sDataKent",sDataKent);
		intent.putExtra("kent_data", sDataKent);
		startActivity(intent);

	}
	public void CreateTable2()
	{
		t1 = (TableLayout) findViewById(R.id.main_table_total);
		cursor = db1.getReadableDatabase().rawQuery("select sum(intensity) cnt , remedies from tbl_report group by remedies order by cnt  desc",null);
		Log.e("cursor ", "cursor");
		int iCount = cursor.getCount();
		Log.e("COUNT",String.valueOf(iCount));
//////////////////////// START NEW CODE	
		Log.e("Arra4441 ", "cursor 1 ");
		String tempX = "";
		ArrayList<String> xList = new ArrayList<String>();
		Log.e("ArrayList 1 ", "cursor 1 ");
		String tempY = "";
		ArrayList<String> yList = new ArrayList<String>();
		Log.e("ArrayList ", "cursor");
		if (cursor.moveToFirst()){
			try
			{
				do{
					tempX = cursor.getString(cursor.getColumnIndex("remedies"));
					xList.add(tempX);
					Log.e("ArrayList 3 ", "cursor 1 ");
					tempY = cursor.getString(cursor.getColumnIndex("cnt"));
					yList.add(tempY);
					Log.e("ArrayList4 ", "cursor 1 ");
				} while(cursor.moveToNext());
			}
			catch (Exception e) {
				Log.e("error ", "e xceptopmn e");
			}
		}
		Log.e("dataList ", "dataList.size() == 0");
		
		
		

		TableRow tr;
		TextView tvItem;
		ShareEmail += "</table><table  style=\"font-size: small;\"  border=1><tr>";
		//add table name header
		tr = new TableRow(this);
		tr.setId(200);
		tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tvItem = new TextView(this);
		tvItem.setText("Remedies"); tvItem.setPadding(5, 5, 5, 5);
		ShareEmail += "<th>Remedies</th>";
		tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
		tr.addView(tvItem);
		
		
		for(String str : xList){
			tvItem = new TextView(this);
			tvItem.setText(str);  tvItem.setPadding(5, 5, 5, 5);
			tvItem.setPaintFlags(tvItem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			tvItem.setTag(str);
			tvItem.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			       String sItem  = (String) v.getTag();
					populatedatabase(
							"SELECT _id,"+borik_lang_Field+","+allen_lang_Field+","+Kent_lang_Field+" FROM tbl_rem_info where upper(rem)  = '"
									+ sItem.toUpperCase() + "'", "Data",
									false);
			    }
			});
			ShareEmail += "<th>"+str+"</th>";
			tvItem.setBackgroundColor(Color.GRAY); tvItem.setTextColor(Color.WHITE);
			tvItem.setGravity(Gravity.CENTER);
			tr.addView(tvItem);
		}
		
		
		t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//set table cells
		ShareEmail += "</tr>";
		ShareEmail += "<tr>";
		int reportTableBG = getResources().getColor(R.color.reportTableBG);
		
			//add row number and name
			tr = new TableRow(this);
			tr.setId(201);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tvItem = new TextView(this);
			tvItem.setText("Total"); tvItem.setPadding(5, 5, 5, 5);
			ShareEmail += "<td>Total</td>";
			tvItem.setBackgroundColor(Color.GRAY); tvItem.setTextColor(Color.WHITE);
			tvItem.setGravity(Gravity.CENTER);
			tr.addView(tvItem);
			
			//add cells in row
			for(String str : yList){
				tvItem = new TextView(this);
				
				tvItem.setText(str);  tvItem.setPadding(5, 5, 5, 5);
				ShareEmail += "<td>"+str+"</td>";
				tvItem.setBackgroundColor(reportTableBG); tvItem.setTextColor(Color.BLACK);
				tvItem.setGravity(Gravity.CENTER);
				tr.addView(tvItem);
			}
			t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ShareEmail += "</tr>";
		
		
	}
	public void CreateTable3()
	{
		t1 = (TableLayout) findViewById(R.id.main_Count_total);
		cursor = db1.getReadableDatabase().rawQuery("select count(intensity) cnt , remedies from tbl_report group by remedies order by cnt  desc",null);
		Log.e("cursor ", "cursor");
		int iCount = cursor.getCount();
		Log.e("COUNT",String.valueOf(iCount));
//////////////////////// START NEW CODE	
		Log.e("Arra4441 ", "cursor 1 ");
		String tempX = "";
		ArrayList<String> xList = new ArrayList<String>();
		Log.e("ArrayList 1 ", "cursor 1 ");
		String tempY = "";
		ArrayList<String> yList = new ArrayList<String>();
		Log.e("ArrayList ", "cursor");
		if (cursor.moveToFirst()){
			try
			{
				do{
					tempX = cursor.getString(cursor.getColumnIndex("remedies"));
					xList.add(tempX);
					Log.e("ArrayList 3 ", "cursor 1 ");
					tempY = cursor.getString(cursor.getColumnIndex("cnt"));
					yList.add(tempY);
					Log.e("ArrayList4 ", "cursor 1 ");
				} while(cursor.moveToNext());
			}
			catch (Exception e) {
				Log.e("error ", "e xceptopmn e");
			}
		}
		Log.e("dataList ", "dataList.size() == 0");
		
		
		

		TableRow tr;
		TextView tvItem;
		ShareEmail += "</table><table  style=\"font-size: small;\"  border=1><tr>";
		//add table name header
		tr = new TableRow(this);
		tr.setId(200);
		tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tvItem = new TextView(this);
		tvItem.setText("Remedies"); tvItem.setPadding(5, 5, 5, 5);
		ShareEmail += "<th>Remedies</th>";
		tvItem.setBackgroundColor(Color.GRAY);	tvItem.setTextColor(Color.WHITE);
		tr.addView(tvItem);
		
		
		for(String str : xList){
			tvItem = new TextView(this);
			tvItem.setText(str);  tvItem.setPadding(5, 5, 5, 5);
			tvItem.setPaintFlags(tvItem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			tvItem.setTag(str);
			tvItem.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			       String sItem  = (String) v.getTag();
					populatedatabase(
							"SELECT _id,"+borik_lang_Field+","+allen_lang_Field+","+Kent_lang_Field+" FROM tbl_rem_info where upper(rem)  = '"
									+ sItem.toUpperCase() + "'", "Data",
									false);
			    }
			});
			ShareEmail += "<th>"+str+"</th>";
			tvItem.setBackgroundColor(Color.GRAY); tvItem.setTextColor(Color.WHITE);
			tvItem.setGravity(Gravity.CENTER);
			tr.addView(tvItem);
		}
		
		
		t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//set table cells
		ShareEmail += "</tr>";
		ShareEmail += "<tr>";
		int reportTableBG = getResources().getColor(R.color.reportTableBG);
		
			//add row number and name
			tr = new TableRow(this);
			tr.setId(201);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tvItem = new TextView(this);
			tvItem.setText("Coubt"); tvItem.setPadding(5, 5, 5, 5);
			ShareEmail += "<td>Grading</td>";
			tvItem.setBackgroundColor(Color.GRAY); tvItem.setTextColor(Color.WHITE);
			tvItem.setGravity(Gravity.CENTER);
			tr.addView(tvItem);
			
			//add cells in row
			for(String str : yList){
				tvItem = new TextView(this);
				
				tvItem.setText(str);  tvItem.setPadding(5, 5, 5, 5);
				ShareEmail += "<td>"+str+"</td>";
				tvItem.setBackgroundColor(reportTableBG); tvItem.setTextColor(Color.BLACK);
				tvItem.setGravity(Gravity.CENTER);
				tr.addView(tvItem);
			}
			t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ShareEmail += "</tr>";
		
		
	}
	@SuppressLint("NewApi")	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (OldPhone() == true )
		{
				super.onCreateOptionsMenu(menu);
			  MenuInflater mi = getMenuInflater();
			  mi.inflate(R.menu.menu_report, menu);
			  return true;
		}
		else
		{
		
			// Inflate the menu items for use in the action bar
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_report, menu);
		}
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	       case R.id.menu_clear:
	        	db1.getWritableDatabase().execSQL("DELETE FROM tbl_report where 1");
	           	db1.getWritableDatabase().execSQL("UPDATE tbl_shifa set selected = '0' where selected = '1'");
	           	Toast.makeText(getApplicationContext(), "All selection are cleared...", 100).show();
	           	
	           	
	           	Intent intent1 = new Intent(activity_report.this, activity_kent.class);
				startActivity(intent1);
		  		finish();
		  		
	           	return true;
	           	
	       case R.id.menu_report_back:
	        	
		  		finish();
		  		
	           	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public boolean OldPhone()
	{
		if (android.os.Build.VERSION.SDK_INT  == 7 || android.os.Build.VERSION.SDK_INT  == 8 || android.os.Build.VERSION.SDK_INT  == 9 || android.os.Build.VERSION.SDK_INT  == 10 || android.os.Build.VERSION.SDK_INT  == 11 || android.os.Build.VERSION.SDK_INT  == 12 || android.os.Build.VERSION.SDK_INT  == 13)
			return true;
		else
			return false;
	}
	@Override
	  public void onDestroy() {
	    super.onDestroy();
	    cursor.close();
	    db1.close();
	  }
}