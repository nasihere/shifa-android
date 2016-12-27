package com.shifa.kent;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;


public class DBclass {

	DBHelper db;
	Context ctx;
	Cursor cursor; 
	ArrayList<User> userArray;
	UserCustomKentAdapter userAdapter;
	ListView myList;
	private Handler handler;
	public DBclass()
	{
		
		
	}
	public DBclass(Context ctx)
	{
		db = new DBHelper(ctx);
		handler = new Handler();
	}
	public void KentItemSeleted(String SelectValue, int _id)
    {
		Log.e("Qyert","UPDATE tbl_shifa set selected = '"+SelectValue+"' where _id = " + _id);
		db.getReadableDatabase().execSQL("UPDATE tbl_shifa set selected = '"+SelectValue+"' where _id = " + _id);
		Log.e("Qyert","Done");
    	   
    }
	public void KentShifaInData(String jsonValue,  String Category, String Book)
    {
		try{
			String Name = "";
			String Cat = "";
			if (Category.indexOf("|")!= -1){
				String[] GetCatName = Category.split("\\|");
				for(int i =0;i<=GetCatName.length - 1; i++){
					if (!Cat.equals("")) Cat += "|";
					Log.e("runner"," " + i);
					Cat += GetCatName[i];
					Name = GetCatName[i];
				}
			}
			else
			{
				Name = Category;
				Cat = "";
			}
			String qry = "UPDATE tbl_shifa set json = '"+jsonValue+"' where upper(book) = '" + Book.toUpperCase() + "' and upper(Name) = '" + Name.toUpperCase() + "' and upper(categoy) = '"+Cat.toUpperCase()+"'";
				Log.e("runner",qry);
				db.getReadableDatabase().execSQL(qry);
		}catch(Exception ex){
			Log.e("runner","error " + ex.toString());
		}
		Log.e("Qyert","Done");
    	   
    }
	public void DbQry(String qry)
    {
		Log.e("qry",qry);
		db.getReadableDatabase().execSQL(qry);
		Log.e("qry","Done");
    	   
    }
	
	public UserCustomAdapter KentItemListing(Context ctx, Cursor cursor, ArrayList<User> userArray, ListView myList, String Category, Activity act)
	{
		//int count = cursor.getCount();
		//int LoadItem = 0;
		userArray.clear();
		cursor.moveToFirst();
		String JsonString = "";
		while(!cursor.isAfterLast()) {
			//LoadItem ++;
			//Toast.makeText(ctx, "Loading.." + (LoadItem / count * 100 ) + "%", 100).show();

			userArray.add(new User(cursor.getString(cursor.getColumnIndex("Name")), cursor.getString(cursor.getColumnIndex("categoy")), cursor.getString(cursor.getColumnIndex("Intensity")), cursor.getString(cursor.getColumnIndex("Remedies")), cursor.getInt(cursor.getColumnIndex("sublevel")), cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("selected")), cursor.getString(cursor.getColumnIndex("book")), cursor.getString(cursor.getColumnIndex("newrem")), cursor.getString(cursor.getColumnIndex("json"))));
			
			cursor.moveToNext();
		}
		
		UserCustomAdapter  userAdapter = new UserCustomAdapter(ctx, R.layout.listview_repertory_details,userArray,Category);

		return userAdapter;


				
	}
	
	public void KentItemOverViewRunable(Context ctx, Cursor cursor, ArrayList<User> userArray,UserCustomKentAdapter userAdapter, ListView myList)
	{
		this.ctx = ctx;
		this.cursor = cursor;
		this.userArray = userArray;
		this.userAdapter = userAdapter;
		this.myList = myList;
//		run();
	}
	public UserCustomKentAdapter KentItemOverView(Context ctx, Cursor cursor, ArrayList<User> userArray, ListView myList)
	{

		//int count = cursor.getCount();
		//int LoadItem = 0;
		userArray.clear();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			try{
			//	LoadItem ++;
				//Toast.makeText(ctx, "Loading.." + (LoadItem / count * 100 ) + "%", 100).show();	
			userArray.add(new User(cursor.getString(cursor.getColumnIndex("newrem")), cursor.getString(cursor.getColumnIndex("categoy")), cursor.getString(cursor.getColumnIndex("Intensity")), cursor.getString(cursor.getColumnIndex("Remedies")), cursor.getInt(cursor.getColumnIndex("sublevel")), cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("selected")), cursor.getString(cursor.getColumnIndex("book")), cursor.getString(cursor.getColumnIndex("newrem")), cursor.getString(cursor.getColumnIndex("json"))));
			
				
			}
			catch(Exception ex){}
		     cursor.moveToNext();
		     
		}
		UserCustomKentAdapter userAdapter;
		userAdapter = new UserCustomKentAdapter(ctx, R.layout.listview_kent_overview, 
			     userArray);
	                  
			return userAdapter;
	}
	
	public String getRemediesKent(String r)
	{
	    try
	    {
		    if (r == "" || r == null) return "-";
		    String[] remS = r.split(":");
		    Log.e("remS",String.valueOf(remS.length));
		    String remedies = " ";
		    //window.MyCls.log("remedies 1 remS.length" + remS.length) ;
		    String str = "";
		    for(int i=0;i<= remS.length-1;i++)
		    {
		        String[] spi = remS[i].split(",");
		        if (spi[1].equals("1"))
		        {
		            str = "<font color='red'>" + spi[0] + "</font>, ";
		        }
		        else if (spi[1].equals("2"))
		        {
		            str = "<font color='blue'>" + spi[0] + "</font>, ";
		        }
		        else if (spi[1].equals("3"))
		        {
		            str = "<font color='black'>" + spi[0] + "</font>, ";
		        }
		        remedies = remedies + str;
		        
		    }
		    return remedies;
	    }
	    catch(Exception ex)
	    {
	    	return "-";
	    }
	    //window.MyCls.log(remedies);
	    
	}
	/*@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				// Thread waits until there are images in the 
				// queue to be retrieved
				int count = cursor.getCount();
				int LoadItem = 0;
				userArray.clear();
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					try{
						LoadItem ++;
						Toast.makeText(ctx, "Loading.." + (LoadItem / count * 100 ) + "%", 100).show();	
					userArray.add(new User(cursor.getString(cursor.getColumnIndex("newrem")), cursor.getString(cursor.getColumnIndex("categoy")), cursor.getString(cursor.getColumnIndex("Intensity")), cursor.getString(cursor.getColumnIndex("Remedies")), cursor.getInt(cursor.getColumnIndex("sublevel")), cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("selected")), cursor.getString(cursor.getColumnIndex("book"))));
					
						
					}
					catch(Exception ex){}
				     cursor.moveToNext();
				     
				}
				
				userAdapter = new UserCustomKentAdapter(ctx, R.layout.listview_kent_overview, 
					     userArray);
			  
				myList.setItemsCanFocus(false);
				myList.setAdapter(userAdapter);
				myList.setItemsCanFocus(false);

			}
		} catch (Exception e) {Log.e("DBCLass","14 "  + e.toString()  );}
	}*/
}