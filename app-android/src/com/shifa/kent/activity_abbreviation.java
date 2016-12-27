	package com.shifa.kent;



import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class activity_abbreviation extends Activity {
	 Context ctx;
	 ProgressDialog progressDialog ;
		
	 private boolean HomePage = true;
	 public DBHelper db1;
	 ArrayList<User> userArray = new ArrayList<User>();
		private SimpleCursorAdapter CursorAdapter;

		public String[][] sHistory = new String[100][2];
		 DBclass KentDB = new DBclass();
	 String Selected = "";
	 public Cursor cursor;
	 UserCustomAdapter userAdapter;
	 public int iHit = 0; 
	 public boolean CategoryMenu = false;
		public String Remedies;
		
		private ListView myList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abbreviation);
        ctx = this;
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.lin_abbreviation_Notification);
        Super_Library_Notification SLN = new Super_Library_Notification(ctx , activity_abbreviation.this,layout );
        SLN.Nofification_Start();
       
        ImageView imgSearch = (ImageView)findViewById(R.id.img_abbreviation_notify_search);
		 imgSearch.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View v) {
					DialogSearchBar();
				}
			});
        
        myList = (ListView)findViewById(R.id.lv_abbreviation_result);
        	db1 = new DBHelper(this);
        	iHit++;
    	 	populatedatabase("SELECT _id,rem, '' RemediesName FROM tbl_rem_info where level = '0' order by rem ","Home",false);
    	 
    	 	myList.setOnItemClickListener(new OnItemClickListener() {
	 			
    			@SuppressLint("DefaultLocale")
    			@Override
    			public void onItemClick(AdapterView<?> listView, View view, int position,
    					long id) {
    				String Category = "";
    				
    					Category  = ((TextView) view.findViewById(R.id.txtabbreviation_item)).getText().toString();
    					
    			   if (Category.length() == 1) 
    				   {
    					   CategoryMenu = false;
    					   Category = Category.toUpperCase();
        				   iHit++;
        				   populatedatabase("SELECT _id,rem,RemediesName FROM tbl_rem_info where level = '1' and rem  like  '"+Category+"%' order by rem ","Catgory",false);
        				   HomePage = false;
    				   }
    				   
    				   
    				   
    			}
    			

    		
    	        
    			
    		 	
    	 	});

    	        
    			showAds();
    }

	
	@Override
	  public void onDestroy() {
	    super.onDestroy();
	    cursor.close();
	    db1.close();
	  }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if (keyCode == KeyEvent.KEYCODE_BACK) {
		        //code to reset view
		    	iHit--;
		    	Log.e("HIT", String.valueOf(iHit));
		    	if (iHit <= 0) 
		    	{
		    		Intent intent = new Intent(activity_abbreviation.this, home_menu.class);
		  		  startActivity(intent);
		  		  finish();
		  		
		    			
		    			 
		    	}
		    	else
		    	{
		    		populatedatabase(sHistory[iHit][0],sHistory[iHit][1],true);
		    	}
		        return true;
		    }
	    return super.onKeyDown(keyCode, event);
	}
	public void populatedatabase(String sql, String ScreenLayout, boolean back)
	{
		Log.e("sql " , sql);
		
		Toast.makeText(getApplicationContext(), "Expanding...", 100).show();
		cursor = db1.getReadableDatabase().rawQuery(sql,null);
		int sCount = cursor.getCount();
		Log.e("SQL Count", String.valueOf(sCount));
		if (sCount == 0)
		{
			
				
			
			   Toast.makeText(getApplicationContext(), "Oops!! Nothing inside...", 200).show();
			   iHit--;
			   return;
		}
		
		if (back == false)
		{
			sHistory[iHit][0] = sql;
			sHistory[iHit][1] = ScreenLayout;
		}
		
	
			
			Log.e("ScreenLayout", "enter");
			String[] fromFieldName = new String[]{"rem","RemediesName"};
			int[] toViewIds = new int[]{R.id.txtabbreviation_item,R.id.tv_abbreviation_remediesName};
			Log.e("Column adjust", "enter");
			CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_abbreviation,cursor,fromFieldName,toViewIds);
			Log.e("Cursor Set", "enter");
			
			myList.setAdapter(CursorAdapter);
			
		
	
	//
		
	}

	public void showAds()
	{
		
		/*if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;
		  // Create the adView
        /*adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainabbreviationMenu);

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
	  /************** Search bar code /******************************/

		@SuppressLint("NewApi")
		@Override
		  public boolean onCreateOptionsMenu(Menu menu) {
			if (OldPhone() == true )
			{
					super.onCreateOptionsMenu(menu);
				  MenuInflater mi = getMenuInflater();
				  mi.inflate(R.menu.discussion, menu);
				  return true;
			}
			else
			{
				getMenuInflater().inflate(R.menu.discussion, menu);
			    SearchView searchView = (SearchView) menu.findItem(R.id.menu_Discussion_refresh).getActionView();
			    
			    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			        @Override
			        public boolean onQueryTextChange(String newText) {
			        	
			        	 	try
				        	{
					    		  
				        	}
				        	catch(Exception ex){ Log.e("Error app", "Error Action bar search 2");}
			        	 
			            return false;
			        }


			        @Override
			        public boolean onQueryTextSubmit(String query) {
			            if (query.length() != 0) {
			            	try
				        	{
			            		iHit++;
		        				 
		        				 populatedatabase("SELECT _id,rem,RemediesName FROM tbl_rem_info where level = '1' and rem  like  '%"+query+"%' or RemediesName like  '%"+query+"%' order by rem ","Catgory",false);
				        	}
				        	catch(Exception ex){ Log.e("Error app", "Error Action bar search 1 ");}
			                // handle search here
			                return true;
			            }
			            return false;
			        }
			    });
			    
			    
				
			}
	        
			 return super.onCreateOptionsMenu(menu);
		  }
		
		
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId())
	        {
	            case R.id.menu_Discussion_refresh:
	            	
	            	  
	            		  
	            	SearchNow();
	            		
	            	return true;
	             
	            default:
	                return super.onOptionsItemSelected(item);

	        }
		}
		public void DialogSearchBar()
		{
			String Action = "";//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
				
				 
				final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
				final EditText input = new EditText(ctx);
				input.setText(Action);
				alert.setTitle( "Enter your Search..." );
			    alert.setView(input);
			    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            String value = input.getText().toString().trim();
				    	try
			        	{
				    		iHit++;
				    		  populatedatabase("SELECT _id,rem,RemediesName FROM tbl_rem_info where level = '1' and rem  like  '%"+value+"%'  or RemediesName like  '%"+value+"%' order by rem ","Catgory",false);	   
			        	}
			        	catch(Exception ex){ Log.e("Error app", ex.toString() +  " Error Action bar search");}
		        	
			            
			        }
			    });

			    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.cancel();
			        }
			    });
			    alert.show();  
			
		}
		public boolean OldPhone()
		{
			if (android.os.Build.VERSION.SDK_INT  == 7 || android.os.Build.VERSION.SDK_INT  == 8 || android.os.Build.VERSION.SDK_INT  == 9 || android.os.Build.VERSION.SDK_INT  == 10 || android.os.Build.VERSION.SDK_INT  == 11 || android.os.Build.VERSION.SDK_INT  == 12 || android.os.Build.VERSION.SDK_INT  == 13)
				return true;
			else
				return false;
		}
		public void SearchNow()
		{
			 if (OldPhone() == true )
			  {
				
				 DialogSearchBar();
				
	           
				 
				  
			  }
			
		}
		/******************** Search Bar +***************************************************************/
}
	