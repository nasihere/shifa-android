package com.shifa.kent;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class activity_kent extends Activity  {
	
	private SimpleCursorAdapter CursorAdapter;
	public Cursor cursor;
	private ListView myList;
	ProgressDialog progressDialog ;
	Handler mHandler;
	boolean chatactive = true;
	public String notification_id = "";
	public int notifyid = 1;
	public String SessionID  = "";
	String Category,Category1 = "";
	String COLSREPERTORY = " _id,Name,categoy,Intensity,Remedies,newrem,sublevel,selected,book,json ";
	String ShifaInteractiveFlag = "";
	ArrayList<User> userArray = new ArrayList<User>();
	 UserCustomAdapter userAdapter;
	 UserCustomKentAdapter  userKentAdapter;
	 DBclass KentDB = new DBclass();
	 String LayOut = "Category";
		Super_Library_AppClass SLAc; 
	 Context ctx;
	 String SearchType = "";
	String SearchStart = "";
	String book = "Kent";
	public DBHelper db1;
	public String QueryChangedForUpdateList = "";
	public String[][] sHistory = new String[100][3];
	public int iHit = 0; 
	private boolean ReversedRepertory = false;
	private boolean HomePage;
	private Thread timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	{
		super.onCreate(savedInstanceState);
		ctx = this;
		SLAc = new Super_Library_AppClass(ctx); 
		setContentView(R.layout.activity_kent);
		 showAds();
		Nofification_Start();
			SessionID  = SLAc.RestoreSessionIndexID("session_id");
			
		SearchType = RestoreSaveSearchType();
		Log.e("SearchType ",SearchType );
	 	myList = (ListView)findViewById(R.id.lv_result);
	 	Log.e("database","dbclassdn1");
	 	db1 = new DBHelper(this);
	 	
	 	 Bundle extras = getIntent().getExtras();
		 	if (extras != null) {
		 	 	try{
			 		Log.e("activity kent oncreate","trying to read book variable");
			 		if (extras.getString("book") != null){
			 			book = extras.getString("book");
			 		}
		            }catch(Exception ex){
		            	Log.e("error activity kent oncreate","error in book variable reading");
		        }
		 	}
	        if (extras != null) {
	        	String value,value_cat,value_Rem = "";
	        	try{
			        	value = extras.getString("r_repertory_startup");
			            value_cat = extras.getString("r_repertory_category");
			            value_Rem = extras.getString("r_repertory_remedies");
			        	
			         if (value.toLowerCase().equals("true"))
			            {
			            	SearchStart = value_cat;
			            	ReversedRepertory = true;
			            	Toast.makeText(getApplicationContext(), "Expanding... ", 100).show();
			            	populatedatabase("SELECT "+COLSREPERTORY+" FROM " +
			            			"tbl_shifa where UPPER(Remedies) like '%"+ value_Rem.toUpperCase() +"%' and sublevel = '0' and " +
			            					"UPPER(maincategoy) = '"+ value_cat.toUpperCase() +"' " +
			            			"order by newrem","Category",false);
			        	 	
			            }
			            return;
	        	}
	        	catch(Exception ex)
	        	{
	        		Log.e("reversed repertory", "not set");
	        		
	        	}
	        	
	        }
	        
	 	iHit++;
		
	 	populatedatabase("SELECT _id,Name FROM tbl_shifa where level = '0' and book = '"+book+"' order by name","Home",false);
	 	
	 	
	 	Log.e("database","dbclaasdasdasssdn");
	 	int iLastScroll = 0;
	 	/*myList.setOnScrollListener(new OnScrollListener() {
	        private int mLastFirstVisibleItem = 1;

	        
	        public void onScrollStateChanged(AbsListView view, int scrollState) {

	        }
	        
	        public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) {
	        	
	            if(firstVisibleItem < mLastFirstVisibleItem  )
	            {
	            	RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainKentMenu);
	            	layout.setVisibility(view.GONE);
	                Log.i("SCROLLING DOWN","TRUE " + mLastFirstVisibleItem + " " + firstVisibleItem) ;
	            }
	            else if(firstVisibleItem == 0)
	            {
	            	RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainKentMenu);
	            	layout.setVisibility(view.VISIBLE);
	                Log.i("SCROLLING UP","TRUE " + mLastFirstVisibleItem + " " + firstVisibleItem);
	            }
	            mLastFirstVisibleItem=firstVisibleItem;

	        }
	    });*/
	 	myList.setOnItemClickListener(new OnItemClickListener() {
	 			
 		

		@SuppressLint("DefaultLocale")
		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position,
				long id) {
				
						
			 try
			 {
			   if (HomePage == true) 
			   {
				   Category1 = Category  = ((TextView) view.findViewById(R.id.txtHomeName)).getText().toString();
				   SearchStart = Category;
				   StopUser(Category);
			   }
			   else
			   {
				   
				   String Name, Name1;
				   Name1 = Name = ((TextView) view.findViewById(R.id.txtName_l_r_d)).getText().toString();
				   
				   
				   SearchStart = ((TextView) view.findViewById(R.id.txtCategory_l_r_d)).getText().toString();
				   
				   Category1 = Category = SearchStart.replaceAll(", ", "|");
				   Category = Category + "|" + Name;
				   SearchStart = Category;
				   StopUser(Category);
				   if (Name1.indexOf(", ") != -1)
					   {
					   Name1 = Name1.substring(0, Name1.indexOf(", "));
					//   Log.e("Category1 3",Name1);
				   	//	Toast.makeText(getApplicationContext(), "Category3 " + Name1, 100).show();
					   }
				   		
				   Category1 = Category1 + "|" + Name1;
				  // Log.e("Category1 2",Category1);
				  // Toast.makeText(getApplicationContext(), "Category2 " + Category1, 100).show();
			   }
			   Category = Category.toUpperCase();
			   Category1 = Category1.toUpperCase();
			   Category = Category.replaceAll(", ", "|");
			   Category1 = Category1.replaceAll(", ", "|");
			    //Log.e("Category1",Category1);
			    //Toast.makeText(getApplicationContext(), "Category1 " + Category1, 100).show();
			   
			   iHit++;
			   populatedatabase("SELECT "+COLSREPERTORY+" FROM tbl_shifa where (UPPER(categoy) = '"+ Category +"' or  UPPER(categoy) = '"+ Category1 +"' or upper(categoy) = '"+Category1+"') and book = '"+book+"'  order by newrem","Category",false);
			   

				
				 				
			 }
			 catch(Exception ex)
			 {
				 
				 
			 }
		}
	});
	 	
	 	Bind();
    	
	 	
	 	timer = new Thread() { //new thread         
	        public void run() {
	            Boolean b = true;
	            try {
	                do {
	                    sleep(1000);

	                    runOnUiThread(new Runnable() {  
	                    @Override
	                    public void run() {
	                        // TODO Auto-generated method stub
	                    	
	                    	Log.e("runner","running...");
	                    	if (userAdapter != null){
	                    		if (!userAdapter.SLAc.JsonString.equals("")){
	                    			if(QueryChangedForUpdateList != Category){
	                    				QueryChangedForUpdateList = Category;
	                    				Log.e("runner","running in... " + Category);
	                    				userAdapter.notifyDataSetChanged();
	                    				KentDB.KentShifaInData(userAdapter.SLAc.JsonString, Category,  book);
	                    			}
	                    			
	                    		}
	                    	}
	                    		
	                    		
	                    	
	                        
	                    }
	                });


	                }
	                while (b == true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            finally {
	            }
	        };
	    };
	    timer.start();

        Log.e("database","end oncreate kent activity");
	}
	  	
	}
	private void StopUser(String text){
		
		if (!GetPreferenceValue("Counter~Home~Remove~ads~Paid").toString().equals("0"))
		{
			return;
		}
		if (book.equals("Kent")) return;
		Random rand = new Random();
		
	    int randomNum = rand.nextInt(6) + 5;
	    Log.e("random"," " + randomNum);
	    if (randomNum >= 8)
	    {
	    	Log.e("random"," " + "execute");
//		if (text.equals("Abdomen External") || text.equals("Back") || text.equals("Chest") || 
	//			text.equals("Cough") || text.equals("Female") || text.equals("Glends") || text.equals("Mouth") || 
		//		text.equals("Sleep") || text.equals("Urinary") || text.equals("waterbrush"))
		

			if (!GetPreferenceValue("Counter~Home~Kent~Ads").toString().equals("0"))
			{
				Toast.makeText(getApplicationContext(),
						"You are using demo version. Please buy now to get full access!",
						Toast.LENGTH_LONG).show();
				
				Intent intBuyNow = new Intent(activity_kent.this, home_menu.class);
				intBuyNow.putExtra("OpenBuyNow", "true");
    	  		startActivity(intBuyNow);
    	  		finish();
    	  		//return false;
			}
			
		}
	    else
	    {
	    	Toast.makeText(getApplicationContext(),"You are using demo version. Please buy now to get full access!",1000).show();
	    }
		//return true;
		
		
	}
	public void Bind()
	{
		 
		final EditText edtKentSearch = (EditText)findViewById(R.id.edtKentSearch);
		edtKentSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			    	edtKentSearch.setHint("");
			    	//SearchSetting();
			        //Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
			    }
			   }
			});
		edtKentSearch.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		    	Toast.makeText(getApplicationContext(), " type "+ keyCode, 1000).show();
		        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB || keyCode == KeyEvent.KEYCODE_SPACE) {

		        	
		        	SearchStart = edtKentSearch.getText().toString().toUpperCase();
		        	if (SearchStart.equals("")) return true;
			        String sql =  " where (upper(newrem) like '%" + SearchStart + "%' or upper(categoy) = '" + SearchStart + "' or upper(newrem) like '" + SearchStart + "|%' ) and book = '"+book+"' order by newrem, Name limit 100";
            		String q = "select "+COLSREPERTORY+" from tbl_shifa   " + sql;
		        	populatedatabase(q, "Category",true);
		        	
			        //KentSearch();
		          // Perform action on key press
		        	
		          
		        	return true;  
		        }
		        
		        return false;
		    }
		});
		
		 final ImageView img_internet_Connected = (ImageView)findViewById(R.id.img_internet_Connected);
		 img_internet_Connected.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View v) {
					if (ShifaInteractiveFlag.equals("1")){
						img_internet_Connected.setBackgroundResource(R.drawable.ic_action_airplane_mode_on);
						SaveShifaConnectType("0");
						
					}
					else
					{
						
						img_internet_Connected.setBackgroundResource(R.drawable.ic_action_network_wifi);
						SaveShifaConnectType("1");
						
					}
					ShifaInteractiveFlag = RestoreShifaConnectType();
					userAdapter.notifyDataSetChanged();
					
//					populatedatabase("SELECT "+COLSREPERTORY+" FROM tbl_shifa where (UPPER(categoy) = '"+ Category +"' or  UPPER(categoy) = '"+ Category1 +"' or upper(categoy) = '"+Category1+"') and book = '"+book+"'  order by newrem","Category",false);
				}
			});
		
		 ShifaInteractiveFlag = RestoreShifaConnectType();
			if (ShifaInteractiveFlag.equals("1")){
				
				
				img_internet_Connected.setBackgroundResource(R.drawable.ic_action_network_wifi);
				
			}
			else
			{
				   
				img_internet_Connected.setBackgroundResource(R.drawable.ic_action_airplane_mode_on);
			}
			
			
		 ImageView imgReportOpen = (ImageView)findViewById(R.id.imgReportOpen);
		 imgReportOpen.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View v) {
					CommonReport();
				}
			});

		 ImageView imgBackRepertory = (ImageView)findViewById(R.id.imgBackRepertory);
		 imgBackRepertory.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View v) {
					backRepertory();
				}
			});
		 
		 
		 ImageView imgTmp1 = (ImageView)findViewById(R.id.img_kent_overview);
		 imgTmp1.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View v) {
					if (!SearchStart.equals(""))
					{
						iHit++;
						populatedatabase("SELECT _id,newrem,Name,categoy,Intensity,Remedies,sublevel,selected,book,json FROM tbl_shifa where (UPPER(categoy) like '"+ SearchStart +"%') and book = '"+book+"' order by newrem","Overview",false);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Open any chapter to search.", 100).show();
					}
				}
			});
		 
		 
	}
	private void ShifaFeed(){
		
	}
	@SuppressWarnings("deprecation")
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
		LayOut = ScreenLayout;
		Log.e("LayOut","LayOut");
		if (back == false)
		{
			sHistory[iHit][0] = sql;
			sHistory[iHit][1] = ScreenLayout;
			sHistory[iHit][2] = SearchStart;
		}
		
		if (ScreenLayout.equals("Home"))
		{
			String[] ChapterName = new String[cursor.getCount()];
			cursor.moveToFirst();
			String JsonString = "";
			int i = 0;
			while(!cursor.isAfterLast()) {
				
				ChapterName[i] = cursor.getString(cursor.getColumnIndex("Name"));
				i++;
				cursor.moveToNext();
			}
			
			
			//int[] toViewIds = new int[]{R.id.txtHomeName};
			
			//CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_result,cursor,fromFieldName,toViewIds);
			UserCustomAdapterHome CursorAdapter = new UserCustomAdapterHome(activity_kent.this, ChapterName);
			HomePage = true;
			myList.setAdapter(CursorAdapter);
		}
		else if (ScreenLayout.equals("Smart"))
		{
			String[] fromFieldName = new String[]{"Name","categoy","Intensity","Remedies"};
			int[] toViewIds = new int[]{R.id.txtName_l_r_d,R.id.txtCategory_l_r_d, R.id.txtCounter_l_r_d, R.id.txtRemedies_l_r_d};
			CursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_repertory_details,cursor,fromFieldName,toViewIds);
			HomePage = true;
			myList.setAdapter(CursorAdapter);
		}
		else if (ScreenLayout.equals("Category"))
		{

		//	userAdapter = new UserCustomAdapter(this, R.layout.listview_repertory_details,
			//	     userArray,Category);
			
			userAdapter = KentDB.KentItemListing(this,cursor,userArray,myList,book + "~"+ Category, activity_kent.this);
			
		//	userAdapter.SLAc.PostWebApi(new String[] {book + "~"+ Category, "http://shifa.in/api/RepertoryService/Extra","response"});

			
			myList.setItemsCanFocus(false);
			myList.setAdapter(userAdapter);
			
			HomePage = false;
			
			

		}
		else if (ScreenLayout.equals("Overview"))
		{

			Toast.makeText(getApplicationContext(),
					"Please wait....",
					Toast.LENGTH_SHORT).show();
			
			
			userKentAdapter = KentDB.KentItemOverView(this,cursor,userArray,myList);

			myList.setItemsCanFocus(false);
			myList.setAdapter(userKentAdapter);
			myList.setItemsCanFocus(false);
			    
			    
			HomePage = false;
			
			
		}
	//
		
	}
	
	public void activity_Home()
	{
		Intent intent = new Intent(activity_kent.this, home_menu.class);
		  startActivity(intent);
		  finish();
		
	}
	public void SearchSetting()
	{
			Log.e("LayOut SearchSetting",LayOut);
		 if (HomePage == true || LayOut.equals("Overview")) 
		 {
			
			 return ;
			 
		 }
		 
		CharSequence SearchArray[] = new CharSequence[] {"In List", "In List & Nested List",  "Save & Default: In List", "Save & Default: In List & Nested List"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Search By:");
		builder.setItems(SearchArray, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	Log.e("Search Type Save",String.valueOf(which));
		    	SaveSearchType(String.valueOf(which));
             	if (which ==  2 || which == 3)
		    	{
			    	int[] values= new int[2];
			    
			    	int x = 50;
			    	int y = 200;
	
			    	Toast prName = Toast.makeText(getApplicationContext(),
			                "You have option to reset search preference in Settings.",
			                Toast.LENGTH_SHORT);
			            prName.setGravity(Gravity.TOP|Gravity.LEFT, x, y);
			            prName.show();
			            
		    	}
		    	KentSearch();
		    }
		});
		builder.show();
	}
	public boolean OldPhone()
	{
		return false;
		/*if (android.os.Build.VERSION.SDK_INT  == 7 || android.os.Build.VERSION.SDK_INT  == 8 || android.os.Build.VERSION.SDK_INT  == 9 || android.os.Build.VERSION.SDK_INT  == 10 || android.os.Build.VERSION.SDK_INT  == 11 || android.os.Build.VERSION.SDK_INT  == 12 || android.os.Build.VERSION.SDK_INT  == 13)
			return true;
		else
			return false;
			*/
	}
	public void KentSearch()
	{
		 if (OldPhone() == true )
		  {
			
			 DialogSearchBar();
			
           
			 
			  
		  }
		 	if (LayOut.equals("Overview")) return;
	    	if (SearchType.equals("0"))
	    	{
	    		Log.e("Search Type Kent Search","Return 0");
	    		return;
	    	}
	    	else if (SearchType.equals("2"))
	    	{
	    		Log.e("Search Type Kent Search","Return 2 ");
	    		return;
	    	}
	    	
		
		progressDialog =  ProgressDialog.show(activity_kent.this, "","Loading.. All Symtoms of " + SearchStart.replace("|",", ") + " for search", true);
    	
    	
    	if (!SearchStart.equals(""))
    	{
    		Thread thread = new Thread() {
    		    
    		    @Override
    		    public void run() {
    		 
    		        // Block this thread for 2 seconds.
    		        try {
    		            Thread.sleep(500);
    		        } catch (InterruptedException e) {
    		        }
    		 
    		        // After sleep finished blocking, create a Runnable to run on the UI Thread.
    		        runOnUiThread(new Runnable() {
    		            @Override
    		            public void run() {
    		            	
    		            	SearchStart = SearchStart.replaceAll(", ", "|").toUpperCase();
    		            	
    		            	String sql =  "";
    		            	if (SearchType.equals("1") || SearchType.equals("3")) //Subcategores and categories
    		            	{
    		            		sql =  " where (upper(categoy) = '" + SearchStart + "' or upper(newrem) like '" + SearchStart + "|%' ) and book = '"+book+"' order by newrem, Name";
    		            		String q = "select "+COLSREPERTORY+" from tbl_shifa   " + sql;
    	    		        	populatedatabase(q, "Category",true);
    	    		        	
    		            	}
    		            	if (SearchType.equals("4") || SearchType.equals("7")) //current repertory
    		            	{
    		            		sql =  " where (upper(newrem) like '%" + SearchStart + "%' or upper(categoy) = '" + SearchStart + "') and book = '"+book+"' order by newrem, Name limit 200";
    		            		String q = "select "+COLSREPERTORY+" from tbl_shifa   " + sql;
    				        	populatedatabase(q, "Category",true);
    				        	
    	    		        	
    		            	}
    		            	if (SearchType.equals("4") || SearchType.equals("8")) //ALL repertory
    		            	{
    		            		sql =  " where (upper(newrem) like '%" + SearchStart + "%' or upper(categoy) = '" + SearchStart + "')  order by newrem, Name limit 400";
    		            		String q = "select "+COLSREPERTORY+" from tbl_shifa   " + sql;
    				        	populatedatabase(q, "Category",true);
    	    		        	
    		            	}
    		            	progressDialog.dismiss();
    		            }
    		        });
    		        
    		    }
    		    
    		};
    		 
    		// Don't forget to start the thread.
    		thread.start();
    		
    		
    		
    	}
    	else
    	{
    		progressDialog.dismiss();
    	}
    	 
    	 
	}
	@Override
    protected void onStop() {
        super.onStop();
        chatactive = false;
        db1.close();
        
        Log.e("Chat Activity","user not longer in the application");
        
    }


    @Override
    protected void onStart() {
        super.onStart();
        chatactive = true;
        Log.e("Chat Activity","user is back");
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (ReversedRepertory == true)
		{
			
			 return super.onKeyDown(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //code to reset view
			backRepertory();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	private void backRepertory(){
		iHit--;
    	Log.e("HIT", String.valueOf(iHit));
    	if (iHit <= 0) 
    	{
    			chatactive = false;		
    			activity_Home();
    			
    			 
    	}
    	else
    	{
    		SearchStart = sHistory[iHit][2];
    		populatedatabase(sHistory[iHit][0],sHistory[iHit][1],true);
    		
    	}
	}
	@Override
	  public void onDestroy() {
	    super.onDestroy();
	    cursor.close();
	    db1.close();
	  }
	
	private void SaveSearchType(String sTypeName)
	{
  	  SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
  	  editor.putString("SaveSearchType", sTypeName);
  	  editor.commit();
  	  SearchType = sTypeName;

	}
	private void SaveShifaConnectType(String sTypeName)
	{
  	  SharedPreferences.Editor editor = getSharedPreferences("AppNameSettings",0).edit();
  	  editor.putString("SaveShifaConnectType", sTypeName);
  	  editor.commit();
  	  SearchType = sTypeName;

	}
	private String RestoreSaveSearchType()
	{
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString("SaveSearchType", null);
		if (restoredText != null) 
		{
			return restoredText;
		}
		return "";

	}
	private String RestoreShifaConnectType()
	{
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString("SaveShifaConnectType", null);
		
		if (restoredText != null) 
		{
			return restoredText;
		}
		if (restoredText == null){
			restoredText = "1";
		}
		return restoredText;

	}
	
	
	@SuppressLint("NewApi")
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		if (OldPhone() == true )
		{
			  super.onCreateOptionsMenu(menu);
			  MenuInflater mi = getMenuInflater();
			  mi.inflate(R.menu.kent, menu);
			  return true;
		}
		else
		{
			getMenuInflater().inflate(R.menu.kent, menu);
		    SearchView searchView = (SearchView) menu.findItem(R.id.menu_refresh).getActionView();
		    
		    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		    	
		        @Override
		        public boolean onQueryTextChange(String newText) {
		        	
		        	 if (HomePage == true) 
		    		 {
		    			
		        		  if (newText.equals("")) return true;
	        			 if (newText.indexOf(" ")!=-1 || SearchStart.equals(newText) )
	        			 {	
	        				 SearchStart = newText;
	        				 progressDialog =  ProgressDialog.show(activity_kent.this, "","Please wait.. Searching....", true);
	        				 
	        			 }
	        			 else
	        			 {
	        				 SearchStart = newText;
	        				 return true;
	        			 }
	        			 Thread thread = new Thread() {
	        	    		    @Override
	        	    		    public void run() {
	        	    		    	
	        	    		        // Block this thread for 2 seconds.
	        	    		        try {
	        	    		            Thread.sleep(1000);
	        	    		            
	        	    		        } catch (InterruptedException e) {
	        	    		        
	        	    		        }
	        	    		 
	        	    		        // After sleep finished blocking, create a Runnable to run on the UI Thread.
	        	    		        runOnUiThread(new Runnable() {
	        	    		            @Override
	        	    		            public void run() {
	        	    		            	String whereClause = "";
	        	    		            	SearchStart = SearchStart.toUpperCase();
	        	    		            	
	        	    		            	
	        	    		    			if (SearchStart.indexOf(" ")!= -1){
	        	    		    				String[] GetCatName = SearchStart.split(" ");
	        	    		    				for(int i =0;i<=GetCatName.length - 1; i++){
	        	    		    					if (!whereClause.equals("")) whereClause += " or "; 
	        	    		    					whereClause += " upper(newrem) like '%" + GetCatName[i] + "%' ";
	        	    		    				}
	        	    		    			}
	        	    		    			else
	        	    		    			{
	        	    		    				whereClause += " upper(newrem) like '%" + SearchStart + "%' ";
	        	    		    				
	        	    		    			}
	        	    		    			
	        	    		            	String sql =  "";
        	    		            		sql =  " where "+ whereClause + "  order by newrem, Name limit 400";
        	    		            		String q = "select "+COLSREPERTORY+" from tbl_shifa   " + sql;
        	    				        	populatedatabase(q, "Category",true);
        	    	    		        	
	        	    		            	
	        	    		            	progressDialog.dismiss();
	        	    		            }
	        	    		        });
	        	    		        
	        	    		    }
	        	    		    
	        	    		};
	        	    		 
	        	    		// Don't forget to start the thread.
	        	    		thread.start();
	        	    		
	        	    		
		        		Toast.makeText(getApplicationContext(), "Open any chapter to search.", 100).show();
		    		 }
		        	 else if (LayOut.equals("Category"))
		        	 {
		        		 
		        		 try
				        	{
		        			 
		        			 	userAdapter.getFilter().filter(newText);
		        			 	myList.setAdapter(userAdapter);
				        	}
				        	catch(Exception ex){ 
				        		Log.e("Error app", "Error Action bar search category");
				        	}
		        	 }
		        	 else
		        	 {
		        		 try{
			        			
			        			userKentAdapter.getFilter().filter(newText);
								myList.setAdapter(userKentAdapter);
			        		}
			        		catch(Exception err)
			        		{
			        			Log.e("Error app", "Error Action bar search overview");
			        			
			        		}
		        	 }
		            return false;
		        }


		        @Override
		        public boolean onQueryTextSubmit(String query) {
		            if (query.length() != 0) {
		            	try
			        	{
		            		
			        	}
			        	catch(Exception ex){ Log.e("Error app", "Error Action bar search");}
		                // handle search here
		                return true;
		            }
		            return false;
		        }
		    });
		    
		    
			
		}
        
		 return super.onCreateOptionsMenu(menu);
	  }
	
	 public boolean onPrepareOptionsMenu(Menu menu) {
		/* ShifaInteractiveFlag = RestoreShifaConnectType();
		 if (ShifaInteractiveFlag.equals("1")){
			 menu.findItem(R.id.menu_feed).setIcon(R.drawable.ic_action_network_wifi);
 			
 		}
 		else
 		{
 			menu.findItem(R.id.menu_feed).setIcon(R.drawable.ic_action_airplane_mode_off);
 			
 		}
		    
		    */

		    return true;
		}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
        {
            case R.id.menu_list:
            	CommonReport();
            	return true;
            case R.id.menu_refresh:
            	CommonSearch();
            	return true;
            case R.id.menu_overview:
            	if (!SearchStart.equals(""))
				{
					iHit++;
					populatedatabase("SELECT _id,newrem,Name,categoy,Intensity,Remedies,sublevel,selected,book,json FROM tbl_shifa where (UPPER(categoy) like '"+ SearchStart +"%') and book = '"+book+"' order by newrem","Overview",false);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Open any chapter to search.", 100).show();
				}
            	return true;
           /* case R.id.menu_feed:
            	//ShifaFeed();
            	//final MenuItem  img_internet_Connected = (MenuItem)findViewById(R.id.menu_feed);
            	if (ShifaInteractiveFlag.equals("1")){
            		item.setIcon(R.drawable.ic_action_airplane_mode_off);
        			SaveShifaConnectType("0");
        			ShifaInteractiveFlag = "0";
        		}
        		else
        		{
        			
        			item.setIcon(R.drawable.ic_action_network_wifi);
        			SaveShifaConnectType("1");
        			ShifaInteractiveFlag = "1";
        		}
        		//
            	
        		try{
        			userAdapter.notifyDataSetChanged();
        		}catch(Exception ex){
        			Toast.makeText(getApplicationContext(), "Open any chapter to get latest online feed for each rubric.", 100).show();
        		}
            	return true;
            	*/
            default:
                return super.onOptionsItemSelected(item);

        }
	}
	
	public void CommonSearch()
	{
		 if (SearchType.equals("0") || SearchType.equals("1") || SearchType.equals(""))
	   	  {
	   		  SearchSetting();
	   	  }
	   	  else
	   	  {
	   		  
	   			  KentSearch();
	   		 
	   	  }
	}
	public void CommonReport()
	{

		chatactive = false;
		 progressDialog =  ProgressDialog.show(activity_kent.this, "", 
    			"Please wait.. Arranging all remedies & Symptoms..", true);
		 Thread thread = new Thread() {
		    
		    @Override
		    public void run() {
		 
		        // Block this thread for 2 seconds.
		        try {
		            Thread.sleep(2000);
		        } catch (InterruptedException e) {
		        }
		 
		        // After sleep finished blocking, create a Runnable to run on the UI Thread.
		        runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	
		            	 Intent intent = new Intent(activity_kent.this, activity_report.class);
		       		  intent.putExtra("KentRepertory", true);
		       		  startActivity(intent);
		       		 
		            	progressDialog.dismiss();
		            }
		        });
		        
		    }
		    
		};
		 
		// Don't forget to start the thread.
		thread.start();
		
		
		  
	}
	public void DialogSearchBar()
	{
		
		 if (HomePage == true) 
		 {
			 Toast.makeText(getApplicationContext(), "Open any chapter to search.", 100).show();
			 return ;
			 
		 }
	
		 
		 
		String Action = "";//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
			
			 
			final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
			final EditText input = new EditText(ctx);
			input.setText(Action);
			alert.setTitle( "Enter your Search..." );
		    alert.setView(input);
		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            String value = input.getText().toString().trim();
			       
			      if (LayOut.equals("Category"))
			     {
			    	 
			    	 try
			        	{
			        		userAdapter.getFilter().filter(value);
			        		myList.setAdapter(userAdapter);
			        	}
			        	catch(Exception ex){ 
			        		
			        		Log.e("Error app", "Error Action bar search");
			        		
			        		
			        		
			        		
			        	}
			     }
	        	 else
	        	 {
	        		 try
			        	{
			        		userKentAdapter.getFilter().filter(value);
			        		myList.setAdapter(userKentAdapter);
			        	}
			        	catch(Exception exe){ 
			        		
			        		Log.e("Error app", "Error Action bar search");
			        		
			        	}
	        	 }
	           
		            
		        }
		    });

		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
		    alert.show();  
		
	}
	public void showAds()
	{
		
		 if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase().equals("false")) return;
	/*	  // Create the adView
        adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

		 
        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainKentMenu);
        //View child = getLayoutInflater().inflate(R.layout.dialog_popup_chat, null);
        // Add the adView to it
        layout.removeAllViews();
        layout.addView(adView );

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
				Log.e("value pre " + StringName, restoredText);
				return restoredText;
			}
			return "0";
	    }

	   
	   
	   
	   
	   
	   
	   
	   public void Nofification_Start()
	   {
		  LinearLayout layout = (LinearLayout)findViewById(R.id.linNotification);
		   Super_Library_Notification SLN = new Super_Library_Notification(ctx,activity_kent.this,layout);
		   SLN.Nofification_Start();

	   }
	   public void Notification_Show(String result)
	   {
		   
	   }
	  
	   
	   
}
