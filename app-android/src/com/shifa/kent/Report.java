package com.shifa.kent;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class Report {
	 Cursor cursor;
	  SimpleCursorAdapter CursorAdapter;
	  DBHelper db1;
	  public Report(Context ctx)
	  {
			db1 = new DBHelper(ctx);
	  }
	public void Init()
	{
		cursor = db1.getReadableDatabase().rawQuery("Select _id,Remedies,Name,categoy,maincategoy from tbl_shifa where selected = '1'",null);
		
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
						String q = "Insert Into tbl_report (sync, remedies,  intensity, _idd,Name,maincategoy) values (0,'"+sTemp[0]+"'," + sTemp[1] + "," + _id + ",'" +
						 _name + "','"  + _maincategory + "')";
						Log.e("report query", q);
						db1.getReadableDatabase().execSQL(q);
						Log.e("report query ", "inserted");
					}
				}
				catch(Exception ex)
				{
					Log.e("Cursor Exception Error ", ex.toString());
				}
			
		     cursor.moveToNext();
		}

		
	}
}
