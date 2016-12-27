package com.shifa.kent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.shifa.kent.Base64.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Visibility;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageGallery extends Activity{
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_GALLERY = 2;


	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private ImageView imgView;
	private Button cancel,gallery,camera;
	private TextView upload;
	
	private Bitmap bitmap;
	private String SessionID;
	private ProgressDialog dialog;
	Uri imageUri;
	String FBProfilePicURL;
	MediaPlayer mp=new MediaPlayer();
	
	Super_Library_AppClass SLAc;
	String title = "";
	String uniqueID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_gallery);
    	SessionID  = RestoreSessionIndexID("session_id");
    	//FBProfilePicURL = RestoreSessionIndexID("profilepic");
		imgView = (ImageView) findViewById(R.id.ImgViewProfilePic);
		upload = (TextView) findViewById(R.id.txtVBtnPhotoPost);
		gallery = (Button) findViewById(R.id.imgGallerydbtn);
		camera = (Button) findViewById(R.id.imgCameradbtn);
		//cancel = (Button) findViewById(R.id.imgcancelbtn);
		this.SLAc = new Super_Library_AppClass(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
	    	upload.setVisibility(View.GONE);
	    }
		else
		{
			if (extras.getString("uniqueID") != null){
				uniqueID = extras.getString("uniqueID").toString();
			}
			
			title = extras.getString("title"); // important line to save in the database
		}
		
		
		upload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(),
							"Please select image", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(ImageGallery.this, "Uploading",
							"Please wait...", true);
					uniqueID = UUID.randomUUID().toString();
					Log.e("image",uniqueID);
					new ImageGalleryTask().execute();
					
				}
			}
		});

		
		gallery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*try {
					
					Intent gintent = new Intent();
					gintent.setType("image/*");
					gintent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
					Intent.createChooser(gintent, "Select Picture"),
					PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
					e.getMessage(),
					Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}*/

				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// call android default gallery
				intent.setType("image/*");
				
				intent.setAction(Intent.ACTION_GET_CONTENT);
				// ******** code for crop image
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 150);
				intent.putExtra("scale", false);
		            intent.putExtra("scaleUpIfNeeded", true);

				try {

				intent.putExtra("return-data", true);
				startActivityForResult(Intent.createChooser(intent,"Complete action using"), PICK_FROM_GALLERY);

				} catch (ActivityNotFoundException e) {
				// Do nothing for now
				}



				//Read more: http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz2wjw4UlQE
					
			}
		});
		
		camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
/*				// TODO Auto-generated method stub
	        	//define the file-name to save photo taken by Camera activity
	        	String fileName = "new-photo-name.jpg";
	        	//create parameters for Intent with filename
	        	ContentValues values = new ContentValues();
	        	values.put(MediaStore.Images.Media.TITLE, fileName);
	        	values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
	        	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
	        	imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        	//create new Intent
	        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        	startActivityForResult(intent, PICK_Camera_IMAGE);
	*/

				try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				intent.putExtra(MediaStore.EXTRA_OUTPUT,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
				// ******** code for crop image
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 150);


				intent.putExtra("return-data", true);
				startActivityForResult(intent, 1);

				} catch (ActivityNotFoundException e) {
				// Do nothing for now
					
				}

			}
		});
		if (uniqueID != ""){
			new DownloadImageTask((ImageView) imgView).execute("http://kent.nasz.us/app_php/shifaappsettings/"+uniqueID+".jpg");
			//new DownloadImageTask((ImageView) imgView).execute(FBProfilePicURL);
		}
		
	}
	private void doCrop(String filePath)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");  
		intent.setClassName("com.android.camera", "com.android.camera.CropImage");  
		File file = new File(filePath);  
		Uri uri = Uri.fromFile(file);  
		intent.setData(uri);  
		intent.putExtra("crop", "true");  
		intent.putExtra("aspectX", 1);  
		intent.putExtra("aspectY", 1);  
		intent.putExtra("outputX", 96);  
		intent.putExtra("outputY", 96);  
		intent.putExtra("noFaceDetection", true);  
		intent.putExtra("return-data", true);                                  
		startActivityForResult(intent, 1);
		
		
		
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if (result == null) return;
	        bmImage.setImageBitmap(result);
	    }
	}
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
                 MenuInflater inflater = getMenuInflater();
                 inflater.inflate(R.menu.activity_image_gallery, menu);
                 return true;
        }
    private String RestoreSessionIndexID(String SessionKey)
	{
		
		SharedPreferences prefs = getSharedPreferences("AppNameSettings",0); 
		String restoredText = prefs.getString(SessionKey, null);
		if (restoredText != null) 
		{
			return restoredText;
		}
		return "";

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.camera:
	        	//define the file-name to save photo taken by Camera activity
	        	String fileName = "new-photo-name.jpg";
	        	//create parameters for Intent with filename
	        	ContentValues values = new ContentValues();
	        	values.put(MediaStore.Images.Media.TITLE, fileName);
	        	values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
	        	//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
	        	imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        	//create new Intent
	        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        	startActivityForResult(intent, PICK_Camera_IMAGE);
	            return true;
	        
	        case R.id.gallery:
	        	try {
				Intent gintent = new Intent();
				gintent.setType("image/*");
				gintent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
				Intent.createChooser(gintent, "Select Picture"),
				PICK_IMAGE);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
				e.getMessage(),
				Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
	        	return true;
        }
		return false;
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try{
			Uri selectedImageUri = null;
			String filePath = null;
			Log.e("ImageUpload","1");
			if (requestCode == PICK_FROM_CAMERA) {
				Log.e("ImageUpload","2");
				Bundle extras = data.getExtras();
				Log.e("ImageUpload","4");
				if (extras != null) {
					Log.e("ImageUpload","3");
					Bitmap photo = extras.getParcelable("data");
					bitmap = photo;
					imgView.setImageBitmap(photo);
					decodeFile("/sdcard/shifa/profilepic.jpg");
					
					}
				}
	
				if (requestCode == PICK_FROM_GALLERY) {
					Bundle extras2 = data.getExtras();
					if (extras2 != null) {
						Bitmap photo = extras2.getParcelable("data");
						bitmap = photo;
						imgView.setImageBitmap(photo);
	/*
						Bitmap b = Bitmap.createBitmap(imgView.getWidth(), imgView.getHeight(),        Bitmap.Config.ARGB_8888);
				        Canvas canvas = new Canvas(b);
				        imgView.draw(canvas);
				        FileOutputStream fOut = null;
				        new File("/sdcard/shifa").mkdir();
				        try {
							fOut = new FileOutputStream("/sdcard/shifa/profilepic.jpg");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        b.compress(CompressFormat.JPEG, 95, fOut);
	
				        
				        
		*/				
						decodeFile("/sdcard/shifa/profilepic.jpg");
						
	
						}
					}
				}
				catch(Exception ex){
					Log.e("ImageUpload","error " + ex.toString());
				}
		}

/*
				
				
				
				
				
				
		switch (requestCode) {
				case PICK_IMAGE:
					if (resultCode == Activity.RESULT_OK) {
						//selectedImageUri = data.getData();
						Bundle extras2 = data.getExtras();
		 		    	Bitmap photo = extras2.getParcelable("data");
						 imgView.setImageBitmap(photo);
					}
					break;
				case PICK_Camera_IMAGE:
					 if (resultCode == RESULT_OK) {
						 

								 
		 		        //use imageUri here to access the image
		 		    	selectedImageUri = imageUri;
		 		    	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
						//selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));
				    } else if (resultCode == RESULT_CANCELED) {
		 		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		 		    } else {
		 		    	Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
		 		    }
					 break;
			}
		
			if(selectedImageUri != null){
					try {
						// OI FILE Manager
						String filemanagerstring = selectedImageUri.getPath();
			
						// MEDIA GALLERY
						String selectedImagePath = getPath(selectedImageUri);
			
						if (selectedImagePath != null) {
							filePath = selectedImagePath;
						} else if (filemanagerstring != null) {
							filePath = filemanagerstring;
						} else {
							Toast.makeText(getApplicationContext(), "Unknown path",
									Toast.LENGTH_LONG).show();
							Log.e("Bitmap", "Unknown path");
						}
			
						if (filePath != null) {
							decodeFile(filePath);
							doCrop(filePath);
						} else {
							bitmap = null;
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Internal error",
								Toast.LENGTH_LONG).show();
						Log.e(e.getClass().getName(), e.getMessage(), e);
					}
			}
	*/
	//}

	class ImageGalleryTask extends AsyncTask<Void, Void, String> {
		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... unsued) {
				InputStream is;
			    BitmapFactory.Options bfo;
			    Bitmap bitmapOrg;
			    ByteArrayOutputStream bao ;
			   
			    bfo = new BitmapFactory.Options();
			    bfo.inSampleSize = 2;
			    //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
			      
			    bao = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte [] ba = bao.toByteArray();
				String ba1 = Base64.encodeBytes(ba);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image",ba1));
				nameValuePairs.add(new BasicNameValuePair("cmd","image_android"));
				Log.v("log_tag", System.currentTimeMillis()+".jpg");	       
				try{
				        HttpClient httpclient = new DefaultHttpClient();
				        HttpPost httppost = new 
                      //  Here you need to put your server file address
				        HttpPost("http://kent.nasz.us/app_php/shifaappsettings/shifaappsettings.php?session_id="+uniqueID);
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
				        HttpEntity entity = response.getEntity();
				        is = entity.getContent();
				        Log.v("log_tag", "In the try Loop" );
				   }catch(Exception e){
				        Log.v("log_tag", "Error in http connection "+e.toString());
				   }
			return "Success";
			// (null);
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				SLAc.PostWebApi(new String[] {title + "~" + SessionID + "-:-" + uniqueID, "http://shifa.in/api/RepertoryService/ExtraSaveImgPath"});
				if (dialog.isShowing())
					dialog.dismiss();
				
				
				finish();
				
			   
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),"here 5",Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		
		bitmap = decodeFile1(filePath);
		//imgView.setImageBitmap(bitmap);
		
	}
	public  Bitmap decodeFile1(String path) {//you can provide file path here 
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size 
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
            scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            //Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bm = bitmap;
            Bitmap bitmap = bm;
            
            ExifInterface exif = new ExifInterface(path);

            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.e("ExifInteface .........", "rotation ="+orientation);

//          exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
//              m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, 50,
                        50, m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90); 
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, 50,
                        50, m, true);
                return bitmap;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, 50,
                        50, m, true);
                return bitmap;
            } 
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if (keyCode == KeyEvent.KEYCODE_BACK) {
			   
				  //Intent intent = new Intent(ImageGallery.this, home_menu.class);
		  		 // startActivity(intent);
		  		  finish();
			   
		        return true;
		    }
	    return super.onKeyDown(keyCode, event);
	}
}

