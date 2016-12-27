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

public class activity_m_medica extends Activity {
	Context ctx;
	
	boolean showBigAds = false;
	
	String SearchKeyWord = "";
	int iQry = 0;
	ProgressDialog progressDialog;
	private boolean DownloadStatus = false;
	private boolean RemediesProcess = false;
	private String[] RemediesNames = new String[900];
	private boolean HomePage = true;
	public DBHelper db1;
	ArrayList<User> userArray = new ArrayList<User>();
	public SimpleCursorAdapter CursorAdapter;
	public String[][] sHistory = new String[100][2];
	DBclass KentDB = new DBclass();
	String Selected = "";
	
	public Cursor cursor;
	UserCustomAdapter userAdapter;
	public int iHit = 0;
	public String Remedies;
	public String Kent_lang_Field = "kent";
	public String allen_lang_Field = "allen";
	public String borik_lang_Field = "data";
	private ListView myList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m_medica);
		ctx = this;
		LinearLayout layout = (LinearLayout) findViewById(R.id.lin_mm_Notification);
		Super_Library_Notification SLN = new Super_Library_Notification(ctx,
				activity_m_medica.this, layout);
		
		ImageView imgSearch = (ImageView) findViewById(R.id.img_mm_notify_search);
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogSearchBar();
			}
		});

		
		setLangField();
		
		
		SLN.Nofification_Start();
		myList = (ListView) findViewById(R.id.lv_m_medica_result);
		db1 = new DBHelper(this);
		iHit++;
		populatedatabase(
				"SELECT _id,rem,RemediesName FROM tbl_rem_info where level = '0' order by rem ",
				"Home", false);

	

		myList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				String Category = "";
				
				try {
					
					Category = ((TextView) view.findViewById(R.id.txtm_m_item))
							.getText().toString();
					
					if (Category.length() == 1) {
						if (Category.equals("S") || Category.equals("U") || Category.equals("D") || Category.equals("W") || Category.equals("Z") || Category.equals("R") || Category.equals("C") || Category.equals("O"))
						{
		
							
							if (!GetPreferenceValue("Counter~Home~Medica~Ads").toString().equals("0"))
							{
								Toast.makeText(getApplicationContext(),
										"You are using demo version. Please buy now to get full access!",
										Toast.LENGTH_LONG).show();
								
								Intent intBuyNow = new Intent(activity_m_medica.this, home_menu.class);
								intBuyNow.putExtra("OpenBuyNow", "true");
				    	  		startActivity(intBuyNow);
				    	  		finish();
				    	  		return;
							}
							
						}
						Category = Category.toUpperCase();
						iHit++;
						populatedatabase(
								"SELECT _id,rem,RemediesName FROM tbl_rem_info where (data != '' or allen != '' or kent != '') and upper(rem)  like  '"
										+ Category + "%' order by rem ",
								"Home", false);
						HomePage = false;
						SearchKeyWord = "";
					} else {

						Category = Category.toUpperCase();
						Remedies = Category.toLowerCase();
						iHit++;
						populatedatabase(
								"SELECT _id,"+borik_lang_Field+","+allen_lang_Field+","+Kent_lang_Field+" FROM tbl_rem_info where upper(rem)  = '"
										+ Category.toUpperCase() + "'", "Data",
										true);
						SearchKeyWord = "";
					}
				} catch (Exception ex) {
					Category = ((TextView) view
							.findViewById(R.id.tvm_medica_search_heading))
							.getText().toString();
					Category = Category.toUpperCase();
					Remedies = Category.toLowerCase();
					iHit++;
					populatedatabase(
							"SELECT _id,"+borik_lang_Field+","+allen_lang_Field+","+Kent_lang_Field+" FROM tbl_rem_info where  upper(rem) = '"
									+ Category.toUpperCase() + "'", "Data",
									true);
				}

			}

		});

		showAds();
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		cursor.close();
		db1.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// code to reset view
			iHit--;
			Log.e("HIT", String.valueOf(iHit));
			if (iHit <= 0) {
				Intent intent = new Intent(activity_m_medica.this,
				home_menu.class);
				startActivity(intent);
				finish();

			} else {
				try
				{
					if (!sHistory[iHit][0].equals(""))
						populatedatabase(sHistory[iHit][0], sHistory[iHit][1], true);
				}
				catch(Exception ex)
				{
					Intent intent = new Intent(activity_m_medica.this,
							home_menu.class);
							startActivity(intent);
							finish();
				
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void populatedatabase(String sql, String ScreenLayout, boolean back) {
		Log.e("sql ", sql);
		
		
		cursor = db1.getReadableDatabase().rawQuery(sql, null);
		int sCount = cursor.getCount();
		Log.e("SQL Count", String.valueOf(sCount));
		if (sCount == 0) {

			if (ScreenLayout.equals("SearchData")) {
				userArray.clear();
				Toast.makeText(getApplicationContext(),
						"You search did not match any documents.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			iHit--;
			return;
		}
		
		if (back == false) {
			sHistory[iHit][0] = sql;
			sHistory[iHit][1] = ScreenLayout;
		}

		if (ScreenLayout.equals("Home")) {
			Log.e("ScreenLayout", "enter");
			String[] fromFieldName = new String[] { "rem", "RemediesName"};
			int[] toViewIds = new int[] { R.id.txtm_m_item,R.id.tvm_medica_status };
			Log.e("Column adjust", "enter");
			CursorAdapter = new SimpleCursorAdapter(this,
					R.layout.listview_m_medica, cursor, fromFieldName,
					toViewIds);
			Log.e("Cursor Set", "enter");

			myList.setAdapter(CursorAdapter);

		} else if (ScreenLayout.equals("Data")) {
			Log.e("ScreenLayout", "Data");
			GetData(cursor);

		} else if (ScreenLayout.equals("SearchData")) {
			Log.e("ScreenLayout", "SearchData");
			activity_m_medica_data_search_adapter userAdapter;
			ArrayList<activity_m_medica_data_search_main> userArray = new ArrayList<activity_m_medica_data_search_main>();
			try {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Log.e("Cursor Chatopen", "reading progress ");
					String bData = "";
					String AData = "";
					String KData = "";
					String FData = "";
					if (cursor.getString(cursor.getColumnIndex("data")) != null)
						bData = cursor.getString(cursor.getColumnIndex("data"));
					if (cursor.getString(cursor.getColumnIndex("allen")) != null)
						AData = cursor.getString(cursor.getColumnIndex("allen"));
					if (cursor.getString(cursor.getColumnIndex("kent")) != null)
						KData = cursor.getString(cursor.getColumnIndex("kent"));
					if (cursor.getString(cursor.getColumnIndex("RemediesName")) != null)
						FData = cursor.getString(cursor.getColumnIndex("RemediesName"));
					userArray.add(new activity_m_medica_data_search_main(cursor
							.getString(cursor.getColumnIndex("_id")), 
							bData, 
							cursor.getString(cursor.getColumnIndex("rem")),
							SearchKeyWord, 
							sCount, 
							AData,
							KData,
							FData
							));
					cursor.moveToNext();
				}
			} catch (Exception ex) {
				Log.e("Error ",ex.toString());
			}
			userAdapter = new activity_m_medica_data_search_adapter(this,
					R.layout.listview_m_medica_search, userArray);

			myList.setAdapter(userAdapter);
			
		}

	}

	public void GetData(Cursor cursor) {
		Log.e("GetData","in"); 
try
{
		cursor.moveToFirst();
}
catch(Exception ex)
{
Log.e("MoveFirst",ex.toString()); 
}
		// (new ChatMain(cursor.getString(cursor.getColumnIndex("chat")),
		// cursor.getString(cursor.getColumnIndex("frm"))));
		String sData = "";
		try {
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
		Intent intent = new Intent(activity_m_medica.this,
				activity_m_medica_tabviewer.class);
		
		
		intent.putExtra("SearchKeyWord", SearchKeyWord.replace(" ", ", "));
		intent.putExtra("medica_data", sData.trim());
		sDataAllen = sDataAllen.replace(":", ".__").trim();
		intent.putExtra("allen_data", sDataAllen);
		sDataKent = sDataKent.replace(":", ".__").trim();
		Log.e("sDataKent",sDataKent);
		intent.putExtra("kent_data", sDataKent);
		startActivity(intent);

	}

	public void showAds() {
		/*if (GetPreferenceValue("Settings~Google~ads").toString().toLowerCase()
				.equals("false"))
			return;

		adView = new AdView(this, AdSize.BANNER, "a15225150ecdd80");

		LinearLayout layout = (LinearLayout) findViewById(R.id.mainMedicaMenu);

		layout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adView.loadAd(adRequest);
		*/

		
	}



	public String GetPreferenceValue(String StringName) {
		SharedPreferences prefs = getSharedPreferences("AppNameSettings", 0);
		String restoredText = prefs.getString(StringName, null);
		if (restoredText != null) {
			return restoredText;
		}
		return "0";
	}

	/************** Search bar code / ******************************/

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (OldPhone() == true) {
			super.onCreateOptionsMenu(menu);
			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.discussion, menu);
			return true;
		} else {
			getMenuInflater().inflate(R.menu.discussion, menu);
			SearchView searchView = (SearchView) menu.findItem(
					R.id.menu_Discussion_refresh).getActionView();

			searchView
					.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

						@Override
						public boolean onQueryTextChange(String newText) {

							try {

							} catch (Exception ex) {
								Log.e("Error app", "Error Action bar search 2");
							}

							return false;
						}

						@Override
						public boolean onQueryTextSubmit(String query) {
							if (query.length() != 0) {
								try {
									iHit++;
									SearchKeyWord = query.toString().toUpperCase();
									String s = SearchKeyWord;
									
									//GLOB '*epigastrium*,*confusion*'
									s = s.trim();
									s = s.replace(" ", "*,*");
									s = "*" + s + "*";
									s = s.toUpperCase();
									// s = hot hands
									// s = hot*,*hands
									// s = *hot*.*hands*
									
									populatedatabase(
											
											"SELECT _id,"+borik_lang_Field+",rem,"+allen_lang_Field+","+Kent_lang_Field+",RemediesName FROM tbl_rem_info where UPPER(rem) GLOB '"
													+ s + "' or UPPER("+borik_lang_Field+") GLOB '" + s
													+ "' or UPPER("+allen_lang_Field+") GLOB '" + s
													+ "' or UPPER("+Kent_lang_Field+") GLOB '" + s
													+ "' or UPPER(RemediesName) GLOB '" + s
													+ "' or UPPER(rem) GLOB '" + s
													+ "' order by rem limit 0,25", "SearchData",
													true);
									
			
								} catch (Exception ex) {
									Log.e("Error app",
											"Error Action bar search 1 ");
								}
								return true;
							}
							return false;
						}
					});

		}

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_Discussion_refresh:

			SearchNow();

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	public void DialogSearchBar() {
		String Action = "";// ((TextView)
							// view.findViewById(R.id.lv_tv_note)).getText().toString();

		final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
		final EditText input = new EditText(ctx);
		input.setText(Action);
		alert.setTitle("Enter your Search...");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				try {
					/*iHit++;
					populatedatabase(
							"SELECT _id,rem,status FROM tbl_rem_info where level = '1' and rem  like  '%"
									+ value + "%' order by rem ", "Home", false);
*/
					
					SearchKeyWord = value.toString().toUpperCase();
					String s = SearchKeyWord;
					s = s.trim();
					s = s.replace(" ", "*,*");
					s = "*" + s + "*";
					s = s.toUpperCase();
					
					populatedatabase(
							"SELECT _id,"+borik_lang_Field+",rem,"+allen_lang_Field+","+Kent_lang_Field+",RemediesName FROM tbl_rem_info where UPPER(rem) GLOB '"
									+ s + "' or UPPER("+borik_lang_Field+") GLOB '" + s
									+ "' or UPPER("+allen_lang_Field+") GLOB '" + s
									+ "' or UPPER("+Kent_lang_Field+") GLOB '" + s
									+ "' or UPPER(RemediesName) GLOB '" + s
									+ "' or UPPER(rem) GLOB '" + s
									+ "' order by rem limit 0,25", "SearchData",
									true);
					
				} catch (Exception ex) {
					Log.e("Error app", ex.toString()
							+ " Error Action bar search");
				}

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		alert.show();

	}

	public boolean OldPhone() {
		if (android.os.Build.VERSION.SDK_INT == 7
				|| android.os.Build.VERSION.SDK_INT == 8
				|| android.os.Build.VERSION.SDK_INT == 9
				|| android.os.Build.VERSION.SDK_INT == 10
				|| android.os.Build.VERSION.SDK_INT == 11
				|| android.os.Build.VERSION.SDK_INT == 12
				|| android.os.Build.VERSION.SDK_INT == 13)
			return true;
		else
			return false;
	}

	public void SearchNow() {
		if (OldPhone() == true) {

			DialogSearchBar();

		}

	}
	/******************** Search Bar + ***************************************************************/

}
