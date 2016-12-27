	package com.shifa.kent;

import java.util.ArrayList;
import java.util.HashMap;



import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class activity_search extends Activity {
	
	// List view
	 DBclass KentDB = new DBclass();
	 ArrayList<User> userArray = new ArrayList<User>();
	 UserCustomAdapter userAdapter;
	Cursor cursor;
	String SearchStart = "";
	private ListView lv;
	DBHelper db1;
	boolean searchprogress = false;
	// Listview Adapter
	SimpleCursorAdapter CursorAdapter;
	
	// Search EditText
	EditText inputSearch;
	
	Handler handler = new Handler();
	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle extras = getIntent().getExtras();
		
		  if (extras != null) {
			  SearchStart = extras.getString("SearchStart");
			  Log.e("SearchStart",SearchStart);
		  }
		  
        db1 = new DBHelper(this);
        // Listview Data
        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
        						"iPhone 4S", "Samsung Galaxy Note 800",
        						"Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
        
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        
        // Adding items to listview
        handler.postDelayed(new Runnable(){
        	
			@Override
			      public void run(){
				Search("");
				searchprogress = false;
			   }
			}, 100);
        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				Log.e("Text cahnge","11");
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				Log.d("After ", "*** Search value changed: " + arg0.toString());
				
				activity_search.this.userAdapter.getFilter().filter(arg0.toString());
				lv.setAdapter(userAdapter);
		
			}
		});
    }
    public class BigTask implements Runnable {
    	  private Thread bigTaskThread;

    	  public void startBigTask() {
    	    bigTaskThread = new Thread(this);
    	    bigTaskThread.start();
    	  }

    	  public void run() {
    	    // Main entry point for the BigTask's thread
    	  }
    	}
    public void Search(String srch)
    {
    	
    	String sql = "";
    	if (!srch.equals(""))
    	{
    		sql +=  " where Name like '%" + srch + "%'";
    	}
    	if (!SearchStart.equals(""))
    	{
    		if (srch.equals("")) 
    			sql +=  " where ";
    		else
    			sql +=  " and ";
    		sql +=  "  categoy = '" + SearchStart + "'  ";
    	}
    	String q = "select _id,Name,categoy,Intensity,Remedies,sublevel from tbl_shifa  " + sql + " limit 0, 100";
    	Log.e("Query",q);
    	cursor = db1.getReadableDatabase().rawQuery(q,null);
    	int sCount = cursor.getCount();
		Log.e("SQL Count", String.valueOf(sCount));
		if (sCount == 0)
		{
				
			   return;
		}
		Log.e("userAdapter", "userAdapter - start");
    	//userAdapter = new UserCustomAdapter(this, R.layout.listview_category,
			//     userArray,"");
    	Log.e("userAdapter", "userAdapter - end");
        /*String[] fromFieldName = new String[]{"Name"};
		int[] toViewIds = new int[]{R.id.txtHomeName};
		CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_result,cursor,fromFieldName,toViewIds);
		lv.setAdapter(CursorAdapter);
        */
    	Log.e("KentItemListing", "KentItemListing - start");
    	KentDB.KentItemListing(this,cursor,userArray,lv,"",activity_search.this);
    	Log.e("KentItemListing", "KentItemListing - end");
    
    }
    
}
	