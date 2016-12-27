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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class activity_r_repertory extends Activity {
	 Context ctx;
	 ProgressDialog progressDialog ;
	
	 private boolean HomePage = true;
	 public DBHelper db1;
	 ArrayList<User> userArray = new ArrayList<User>();
		private SimpleCursorAdapter CursorAdapter;
		public String[][] sHistory = new String[100][2];
		 DBclass KentDB = new DBclass();
	 String Selected = "";
	 String book = "Kent";
	 public Cursor cursor;
	 UserCustomAdapter userAdapter;
	 public int iHit = 0; 
	 public boolean CategoryMenu = false;
		public String Remedies;
		
		private ListView myList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_repertory);
        ctx = this;
        myList = (ListView)findViewById(R.id.lv_r_repertory_result);
        	db1 = new DBHelper(this);
        	iHit++;
    	 	populatedatabase("SELECT distinct _id,book rem,'' status FROM tbl_shifa group by book","Home",false);
    	 
    	 	myList.setOnItemClickListener(new OnItemClickListener() {
	 			
    			@SuppressLint("DefaultLocale")
    			@Override
    			public void onItemClick(AdapterView<?> listView, View view, int position,
    					long id) {
    				String Category = "";
    				Log.e("OnItemClick","here1");
    				if ( CategoryMenu == true)
    				{
    					Log.e("OnItemClick","here2");
						Category  = ((TextView) view.findViewById(R.id.txtName)).getText().toString();
    					ShowFeed(Category);
    					return;
    				}
    				else
    				{
    					Log.e("OnItemClick","here3");
    					Category  = ((TextView) view.findViewById(R.id.txtm_r_reprtory)).getText().toString();
    					
    				}
    				Log.e("OnItemClick","here4");
    				if (Category.equalsIgnoreCase("Kent") || Category.equalsIgnoreCase("Boenninghausens")){
    					Log.e("OnItemClick","here5");
    					CategoryMenu = false;
    					book = 	Category;		
    					iHit++;			
    					populatedatabase("SELECT _id,rem,'' status FROM tbl_rem_info where level = '0' order by rem ","Home",false);
    					HomePage = false;
    				}
    				else if (Category.length() == 1) 
    				   {
    					Log.e("OnItemClick","here6");
    					   CategoryMenu = false;
    					   Category = Category.toUpperCase();
        				   iHit++;
        				 
        				   populatedatabase("SELECT _id,rem,status FROM tbl_rem_info where level = '1' and rem  like  '"+Category+"%' order by rem ","Home",false);
        				   HomePage = false;
    				   }
    				   else
    				   {
    					   Log.e("OnItemClick","here7");
    					   Category = Category.toUpperCase();
    					   Log.e("OnItemClick","here8");
    					   Remedies = Category.toLowerCase();
    					   Log.e("OnItemClick","here9");
    					   String qry = 
    							   		   " Select maincategoy Name,'" +
    									   " Remedies - " + Remedies + "'  categoy,  " +
    									   " '' Remedies,_id," +
    									   " level, '' selected, '' " +
    									   " sublevel, '' Intensity, book,json,newrem " +
    									   " from tbl_shifa where" +
    									   " (UPPER(Remedies) like '%"+Remedies.toUpperCase()+"%' and maincategoy != '') and book = '"+book+"' " +
    									   " group by maincategoy order by Name limit 0, 150";
    					   Log.e("OnItemClick","here10 " + qry);
    					   CategoryMenu = true;
    					   iHit++;
    					   Log.e("OnItemClick","here11");
        				   populatedatabase(qry,"Category",true);
        				   Log.e("OnItemClick","here12");
    				   }
    				   
    				   
    			}
    			

    		
    	        
    			
    		 	
    	 	});

    	        
    	 	
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
	    		Intent intent = new Intent(activity_r_repertory.this, home_menu.class);
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
			
				if (CategoryMenu == true){
					CategoryMenu = false;//This is important to cancel categorymeny because no record found so user has not went to that category
					
				}
			   Toast.makeText(getApplicationContext(), "Oops!! Nothing inside...", 200).show();
			   iHit--;
			   return;
		}
		
		if (back == false)
		{
			sHistory[iHit][0] = sql;
			sHistory[iHit][1] = ScreenLayout;
		}
		
		if (ScreenLayout.equals("Home"))
		{
			 CategoryMenu = false;
			Log.e("ScreenLayout", "enter");
			String[] fromFieldName = new String[]{"rem"};
			int[] toViewIds = new int[]{R.id.txtm_r_reprtory};
			Log.e("Column adjust", "enter");
			CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_r_repertory,cursor,fromFieldName,toViewIds);
			Log.e("Cursor Set", "enter");
			
			myList.setAdapter(CursorAdapter);
			
		}
		else if (ScreenLayout.equals("Category"))
		{
			Log.e("Cursor Category", "enter");
			String[] fromFieldName = new String[]{"Name","categoy","Intensity"};
			int[] toViewIds = new int[]{R.id.txtName,R.id.txtCategory,R.id.txtCounter};
			CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_category,cursor,fromFieldName,toViewIds);
			HomePage = false;


	/*		userArray.clear();
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				userArray.add(new User(cursor.getString(cursor.getColumnIndex("Name")), cursor.getString(cursor.getColumnIndex("categoy")), cursor.getString(cursor.getColumnIndex("Intensity")), cursor.getString(cursor.getColumnIndex("newrem")), cursor.getInt(cursor.getColumnIndex("sublevel")), cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("selected")), cursor.getString(cursor.getColumnIndex("book")), cursor.getString(cursor.getColumnIndex("newrem")), cursor.getString(cursor.getColumnIndex("json"))));
			     cursor.moveToNext();
			}*/
			
			
		  
			myList.setAdapter(CursorAdapter);
			//CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_category,cursor,fromFieldName,toViewIds);
			/*
			//userAdapter = new UserCustomAdapter(this, R.layout.listview_category,
			//	     userArray,"");
            userAdapter = KentDB.KentItemListing(this,cursor,userArray,myList,"",activity_r_repertory.this);
            myList.setItemsCanFocus(false);
            myList.setAdapter(userAdapter);*/

			
		}
	//
		
	}
	public void GetData(Cursor cursor)
	{
		cursor.moveToFirst();
		
			//(new ChatMain(cursor.getString(cursor.getColumnIndex("chat")), cursor.getString(cursor.getColumnIndex("frm"))));
			try
			{
				Log.e("Cursor Result : 1", cursor.getString(cursor.getColumnIndex("data")));
				String sData = cursor.getString(cursor.getColumnIndex("data"));
			
				 
			}
			catch(Exception ex)
			{
				
			}
			
		
		
	
	}
	public void ShowFeed(String sData)
    {

  	  Intent intent = new Intent(activity_r_repertory.this, activity_kent.class);
  	  			intent.putExtra("r_repertory_startup", "true");
			 intent.putExtra("r_repertory_category", sData);
			 intent.putExtra("r_repertory_remedies", Remedies);
			 
			 startActivity(intent);
    	
    }
	
	public void showAds()
	{
		
	/*	if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;

		  // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainr_repertoryMenu);

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

		        				  populatedatabase("SELECT _id,rem,status FROM tbl_rem_info where level = '1' and rem  like  '%"+query+"%' order by rem ","Home",false);	
		        				
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
	        		
	        				  populatedatabase("SELECT _id,rem,status FROM tbl_rem_info where level = '1' and rem  like  '%"+value+"%' order by rem ","Home",false);	
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
	