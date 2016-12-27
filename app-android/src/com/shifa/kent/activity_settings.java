package com.shifa.kent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.internal.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class activity_settings extends Activity {
	private ProgressDialog progress;
	
	ProgressDialog pd;
	String source = "";
	int iServerCounter = 0;
	boolean DownloadingData = false;
	Context ctx;
	Super_Library_AppClass SLAc;
	public String SessionID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ctx = this;
		SLAc = new Super_Library_AppClass(this);
		SessionID = SLAc.RestoreSessionIndexID("session_id");
		
		final Button button1 = (Button) findViewById(R.id.btn_setting_help);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(activity_settings.this,
						activity_events.class);
				intent.putExtra(
						"url",
						"http://kent.nasz.us/app_php/howtouseshifa/howtouseshifa.php?request=buynow&section=settings");
				startActivity(intent);

			}

		});

		final Button button2 = (Button) findViewById(R.id.btn_setting_Profile);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(activity_settings.this,
						ImageGallery.class);
				startActivity(intent);

			}

		});

		final Button button3 = (Button) findViewById(R.id.btn_setting_BuyNow);

		if (SLAc.isPaidMember() == true)
			button3.setText("Donate");
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = null;

				if (SLAc.isPaidMember() == true) {
					intent = new Intent(activity_settings.this,
							activity_events.class);
					intent.putExtra("url",
							"http://kent.nasz.us/app_php/buynow/buynowwebpayment.php?session_id="
									+ SessionID + "&ispaid=true");
				} else
					intent = new Intent(activity_settings.this, buynow.class);
				// Intent intent = new Intent(activity_settings.this,
				// buynow.class);
				startActivity(intent);

			}

		});

		final Button button4 = (Button) findViewById(R.id.btn_setting_KentSearchByReset);
		button4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				SharedPreferences.Editor editor = getSharedPreferences(
						"AppNameSettings", 0).edit();
				editor.remove("SaveSearchType");
				editor.commit();
				Toast.makeText(
						getApplicationContext(),
						"Kent Search Dialog Reset. You might see difference in kent search.",
						Toast.LENGTH_SHORT).show();
			}

		});

		final Button button5 = (Button) findViewById(R.id.btn_setting_logout);
		button5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				LogOut();

			}

		});
		
				
		final Button button6 = (Button) findViewById(R.id.btn_setting_TellAFriend);
		button6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, "");
				intent.putExtra(Intent.EXTRA_SUBJECT,
						"ShifaApp Kent + Repertory + Materia + Medica For Android");
				intent.putExtra(
						Intent.EXTRA_TEXT,
						"Hey, \n I just downloaded Shifa Kent Repertory Materia Medica on my Android. \n\nGet it now from https://play.google.com/store/apps/details?id=com.shifa.kent  \n\nThe Shifa � Kent Repertory is a complete repertorising tool. A handheld repertory couldn�t be easier to use. A perfect application for homeopaths who wants a powerful yet easy to use application offline. Quick search allows you to quickly go through the whole repertory. Each chapter and rubrics are arranged alphabetically for easy navigation. Clean and intuitive design allows for easier navigation and quick reference. Beta version offers the whole repertory with direct reference to remedies in a particular rubrics. \n\nIt marks the beginning of a more robust and advance application to come \n\nThe Shifa - Materia Medica enables you to carry different top book Boericke,J.T.Kent,Allen.Shifa allow you to search any book content words and compare with ALLEN,KENT,BOERICKE Books. Complete offline Section. You do not need any internet \n\nGet it now from https://play.google.com/store/apps/details?id=com.shifa.kent");

				startActivity(Intent.createChooser(intent, "Send Email"));

			}

		});

		final Button button7 = (Button) findViewById(R.id.btn_setting_Materia_Medica_lang);
		button7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				CharSequence SearchArray[] = new CharSequence[] { "English",
						"Italian", "Dutch", "German", "French", "Spanish",
				"Portuguese" };

				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle("Materia Medica Language");
				builder.setItems(SearchArray,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// the user clicked on colors[which]
						SharedPreferences.Editor editor = getSharedPreferences(
								"AppNameSettings", 0).edit();
						editor.putString("MMLang",
								String.valueOf(which));
						editor.commit();

						int[] values = new int[2];
						int x = 50;
						int y = 200;

						Toast prName = Toast.makeText(
								getApplicationContext(),
								"Language Saved!", Toast.LENGTH_LONG);
						prName.setGravity(Gravity.TOP | Gravity.LEFT,
								x, y);
						prName.show();

					}
				});
				builder.show();

			}

		});

		final Button button8 = (Button) findViewById(R.id.btn_setting_TalkToDeveloper);
		button8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ctx, activity_privatemsg.class);
				intent.putExtra("Screen", "1");
				intent.putExtra("session_id_to", "10205304767877899"); //facebook id 
				ctx.startActivity(intent);
			}

		});
		
		final Button button9 = (Button) findViewById(R.id.btn_setting_Migrate2Fb);
		button9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				
				SharedPreferences.Editor editor = getSharedPreferences(
						"AppNameSettings", 0).edit();
				editor.putString("session_id_Migration", SessionID);
				
				editor.commit();
				Intent intent = new Intent(ctx, login.class);
				
				ctx.startActivity(intent);
				
			}

		});
		if (SessionID.equalsIgnoreCase("000000000000007") || SessionID.equalsIgnoreCase("1111111100000") || SessionID.equalsIgnoreCase("10205304767877899"))
		{
			button9.setVisibility(View.VISIBLE);
		}
		else if (SessionID.matches("[0-9]+") && SessionID.length() > 2) {
			button9.setVisibility(View.GONE);
		}
		
		
		final Button button10 = (Button) findViewById(R.id.btn_setting_MemoryClean);
		button10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				
			try{	
				long bytesDeleted = 0;
				File cacheDir;
				String sdState = android.os.Environment.getExternalStorageState();
				if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
					File sdDir = android.os.Environment.getExternalStorageDirectory();		
					cacheDir = new File(sdDir,"data/shifapics");

				} else {
					cacheDir = ctx.getCacheDir();
				}
				
				File[] files = cacheDir.listFiles();

		        for (File file : files) {
		            file.delete();
		            //bytesDeleted += file.length();
		            

		        }
				Toast.makeText(
						getApplicationContext(),
						"Shifa downloaded pictures are cleared successfully.", 
						Toast.LENGTH_SHORT).show();
			}
				catch(Exception ex){
					Toast.makeText(
							getApplicationContext(),
							"Error:" + ex.toString(),
							Toast.LENGTH_SHORT).show();
					
				}
			}

		});
		
		
		
		final Button button11 = (Button) findViewById(R.id.btnAskAExpert);
		button11.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				
				Intent intent = new Intent(ctx, activity_privatemsg.class);
				intent.putExtra("Screen", "1");
				intent.putExtra("session_id_to", "100000404746202"); //nazim facebook id 
				ctx.startActivity(intent);
			}

		});
		
		
		final Button button12 = (Button) findViewById(R.id.btnFacebookLikePage);
		button12.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
				    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/631560593522959"));
				    startActivity(intent);
				} catch(Exception e) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/631560593522959")));
				}
				
			}

		});
		
		
		final Button button13 = (Button) findViewById(R.id.btn_UpdateDatabaseb);
		button13.setVisibility(View.GONE);
		button13.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				final Handler myHandler = new Handler();
				progress = new ProgressDialog(ctx);
				progress.setMessage("Downloading Data ");
				progress.setIndeterminate(false);
				progress.setCanceledOnTouchOutside(false);
				progress.show();
				(new Thread(new Runnable() {
						
					@Override
					public void run() {
						for(int i =0; i < 100; i++){
						
						
						
							if (DownloadingData ==true){
								Log.e("Setting","Sleep 5Sec");
								try {
									Thread.sleep(6000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
							else
							{
								
								new pullJson()
										.execute("http://kent.nasz.us/app_php/APIDatabaseUpdate.php?id="+iServerCounter);
								
								iServerCounter++;
							}
					
						}
						myHandler.post(new Runnable() {
							
							@Override
							public void run() {
								
								if (progress != null){
									progress.dismiss();
								}
							}
						});
					}
				})).start();
								
							
/*
				int iServerCounter = 0;
				if (isOnline() == true) {
					//for(int i =0; i < 100; i++){
					
						
						
							if (DownloadingData ==true){
								Log.e("Setting","Sleep 5Sec");
								try {
									Thread.sleep(6000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
							else
							{
								progress = new ProgressDialog(ctx);
								progress.setMessage("Downloading Records "+iServerCounter);
								progress.setIndeterminate(false);
								progress.setCanceledOnTouchOutside(false);
								progress.show();
			
								new pullJson()
										.execute("http://nasihere-001-site5.smarterasp.net/api/Server/"+iServerCounter);
								
								iServerCounter++;
							}
						
					//}
				} else {
					Toast.makeText(getApplicationContext(),
							"Network not available to perform this task!",
							Toast.LENGTH_LONG).show();
					return;
				}
				*/
			}
				

		});

		
		/*
		 * 
		 * */
	}
	public static Intent getOpenFacebookIntent(Context context) {

		   try {
		    context.getPackageManager().getPackageInfo("com.shifa.kent", 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/ShifaKentRepertory"));
		   } catch (Exception e) {
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ShifaKentRepertory"));
		   }
		}
	private void LogOut() {
		SharedPreferences.Editor editor = getSharedPreferences(
				"AppNameSettings", 0).edit();
		editor.remove("session_id");

		editor.putString("ChatIndex", "1");

		editor.remove("SaveSearchType");
		editor.remove("AutoStartUpProfilePic");
		editor.remove("AutoStartUpHelp");
		editor.remove("AutoStartUpMMLangauge");
		editor.remove("ShowPublicMsg");
        editor.remove("gcmregset");
        editor.remove("gcmreg");


		editor.remove("session_id_Migration");
		
		editor.commit();

		// clear fb
		Session session =  Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				Utility.clearFacebookCookies(this);
			}
		}	

		Intent intent = new Intent(activity_settings.this, login.class);
		startActivity(intent);
		finish();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:

				Intent intent = new Intent(activity_settings.this,
						home_menu.class);
				startActivity(intent);
				finish();

				return true;
			}

		}
		return super.onKeyDown(keyCode, event);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////Class Start ///////////////////////////////////////////////////////////
	class pullJson extends AsyncTask<String, Integer, String> {

		private int myProgressCount;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// super.onPreExecute();
			DownloadingData = true;

		}

		@Override
		protected void onProgressUpdate(final Integer... values) {
			// TODO Auto-generated method stub
			if (progress != null) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.setProgress(values[0]);

					}
				});
				// progress.setProgress(values[0]);
			}
		}

		@Override
		protected String doInBackground(String... uri) {

			
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				Log.e("Setting","URL " + uri[0]);
				response = httpclient.execute(new HttpGet(uri[0]));
			
				Log.e("Setting","Level 1");
						
				
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Log.e("Setting","Level 21");
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					Log.e("Setting","Level 2");
					
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			Log.e("Setting","Level 3 " + responseString );
			
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// super.onPostExecute(result);
			// Do anything with response..
			// System.out.println(result);
			Log.e("Setting","Level 4 " );
			if (result.indexOf("insert")==-1)
			{
				DownloadingData = false;
				
				progress.dismiss();
				progress = null;
				
				return;
				
			}
			//if (progress != null) {
				//	Log.e("Setting","Level 5 " );
			
					//Log.e("Setting","Level 6 " );
					
				storeData(result);
				//Log.e("Setting","Level 7 " );
				DownloadingData = false;	
			//}

		}
		////////////////////////////////////////////////Class End
		
		
		
///////////////////////////////////////////////////////// Call when api server respond data		
		public void storeData(String result) {
			
			
			final String DownloadedResponse = result;
			JSONArray jArray1 = null;
			try {
				jArray1 = new JSONArray(DownloadedResponse);
				  
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			final JSONArray jArray = jArray1;
			
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				
				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(ctx);
					if (jArray.length() < 9999)
						pd.setTitle("Final Update processing");
					else
						pd.setTitle("Updating database..." + iServerCounter + " / 5");
					pd.setMessage("Please wait..");
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.setCancelable(false);
					pd.setProgress(0);
					pd.setMax(jArray.length());
					
					pd.show();
				}
				


				@Override
				protected Void doInBackground(Void... arg0) {
	

						
						try {
                    		DBHelper db1 = new DBHelper(ctx);
                    		
        					for (int i = 0; i < jArray.length(); i++) {
        						//myProgressCount++;

								pd.incrementProgressBy(1);
											
        						String API_qry = jArray.getString(i);
        						Log.e("Setting",API_qry );
        						db1.getWritableDatabase().execSQL(API_qry);
        						//if (API_qry.indexOf(" values ") != -1)
        						//{
        							//pd.setMessage("nasir");
        							//publishProgress(API_qry.substring((API_qry.indexOf(" values ") + 10),((API_qry.indexOf(" values ") + 10) + 15)));
        							
        						//}
        							//pd.setMessage();					
        						
        					}
        				} catch (JSONException e) {
        					Log.e("Setting", "Error " + e.toString() );
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        					
        				}
//						Thread.sleep(5000);
	
					return null;
				}
				
				private void publishProgress(final String string) {
					// TODO Auto-generated method stub
					if (pd!= null) {
						new Handler().post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//progress.setProgress(values[0]);
								pd.setMessage(string);

							}
						});
						// progress.setProgress(values[0]);
					}					
				}

				@Override
				protected void onPostExecute(Void result) {
					if (pd!=null) {
						pd.dismiss();
						//b.setEnabled(true);
					}
				}
					
			};
			task.execute((Void[])null);
			 
			
			
			
			
			
			
			
			
			// TODO Auto-generated method stub
			//DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//			Log.e("Setting","Store Data" );
			
			
			
			
			
			

			
			
			//progress.setMessage("Updating database..");
			//progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			//progress.setIndeterminate(false);
			//progress.setCanceledOnTouchOutside(false);
			
			
			/*
			
			try {
				DBHelper db1 = new DBHelper(ctx);
				JSONArray jArray = new JSONArray(result);
				//progress.setProgress(0);
				//progress.setMax(jArray.length());
				progress.show();
				Log.e("Setting","Max Count" + jArray.length() ); 
				Log.e("Setting","Level 8" );
				if (progress != null) {
					while (myProgressCount < 100) {
						myProgressCount++;

						publishProgress(myProgressCount);
						SystemClock.sleep(30);
					}
				}	
				for (int i = 0; i < jArray.length(); i++) {
					String API_qry = jArray.getString(i);
					Log.e("Setting",API_qry );
					db1.getWritableDatabase().execSQL(API_qry);
					//progress.setProgress(i);
					publishProgress(i / jArray.length() * 100);
					SystemClock.sleep(30);
				}
			} catch (JSONException e) {
				Log.e("Setting", "Error " + e.toString() );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			progress.dismiss();
			*/
		}		
		////////////////////////////////////////////////////////////////
		
		
		
		
		
		
	}
	

	
	
	
	
	public boolean isOnline() {
		
		try {
			//Toast.makeText(home_menu.this,"Please wait.. Checking internet connection..." ,Toast.LENGTH_SHORT).show();
	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 nasz.us");
	        
	        int returnVal = p1.waitFor();
	        boolean reachable = (returnVal==0);
	        if (reachable == false)
	        {
	        	Toast.makeText(activity_settings.this,"Internet Connection Not Present" ,Toast.LENGTH_SHORT).show();    	
	        }
	        return reachable;
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		
		
	    return false;		
	    
	    
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
}
