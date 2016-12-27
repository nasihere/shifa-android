package com.shifa.kent;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_COL1 = "col1";
	public static final String KEY_COL2 = "col2";

	private boolean createDatabase = false;
	private boolean upgradeDatabase = false;
	Context context;
	public String DB_PATH;
	public InputStream DB_PATH_IN;
	private static final String DATABASE_NAME = "testdb";
	private static final String DATABASE_TABLE = "mytable";
	private static final int DATABASE_VERSION = 8;
	private static final String USER_MASTER_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TABLE
			+ "("
			+ KEY_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_COL1
			+ " VARCHAR(15) UNIQUE, " + KEY_COL2 + " VARCHAR(15) )";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DB_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
		Log.e("DBHelper DB_PATH Constructor ", DB_PATH);
		try {
			DB_PATH_IN = (InputStream) context.getAssets().open(DATABASE_NAME);
			this.context = context;
//			initializeDataBase();
			Log.e("DBHelper DB_PATH_IN Constructor ", "done");
		} catch (IOException e) {
			Log.e("DBHelper DB_PATH_IN Constructor ", e.toString());
			e.printStackTrace();
		}
	}

	public void copyStream(InputStream is, OutputStream os) throws IOException {

		byte buf[] = new byte[1024];
		int c = 0;
		while (true) {
			c = is.read(buf);
			if (c == -1)
				break;
			os.write(buf, 0, c);
		}
		is.close();
		os.close();
	}

	public void initializeDataBase() {
		/*
		 * Creates or updates the database in internal storage if it is needed
		 * before opening the database. In all cases opening the database copies
		 * the database in internal storage to the cache.
		 */



		getWritableDatabase();
		Log.e("DBHelper initializeDataBase ", "Entered");
		createDatabase = true;
		if (createDatabase) {
			Log.e("DBHelper createDatabase ", "true");
			/*
			 * If the database is created by the copy method, then the creation
			 * code needs to go here. This method consists of copying the new
			 * database from assets into internal storage and then caching it.
			 */
			try {
				/*
				 * Write over the empty data that was created in internal
				 * storage with the one in assets and then cache it.
				 */
				copyStream(DB_PATH_IN, new FileOutputStream(DB_PATH));
			}
			catch (Exception e) 
			{
				throw new Error("Error copying database");
			}
		} 
		else if (upgradeDatabase) 
		{
			Log.e("DBHelper upgradeDatabase ", "true");
			try
			{
				/*
				 * Write over the empty data that was created in internal
				 * storage with the one in assets and then cache it.
				 */
				copyStream(DB_PATH_IN, new FileOutputStream(DB_PATH));
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
		Log.e("DBHelper clear  ", "true");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.e("DBHelper", "onCreate");

		// db.execSQL(USER_MASTER_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// if DATABASE VERSION changes
		// Drop old tables and call super.onCreate()
		upgradeDatabase = true;
	}

	public void SavePreference(String StringName, String StringValue) {

		SharedPreferences.Editor editor = context.getSharedPreferences(
				"AppNameSettings", 0).edit();
		editor.putString(StringName, StringValue);
		Log.e("SetPreferenceValue " + StringName, StringValue);
		editor.commit();
	}

	public String GetPreferenceValue(String StringName) {
		SharedPreferences prefs = context.getSharedPreferences(
				"AppNameSettings", 0);
		String restoredText = prefs.getString(StringName, null);
		if (restoredText != null) {
			return restoredText;
		}
		return "0";
	}
}
