package com.shifa.kent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageManager {

	private long cacheDuration;
	private DateFormat mDateFormatter;

	private HashMap<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();

	private File cacheDir;
	private ImageQueue imageQueue = new ImageQueue();
	private Thread imageLoaderThread = new Thread(new ImageQueueManager());
	public Context ctx;
	public ImageManager(Context context, long _cacheDuration) {

		ctx = context;
		cacheDuration = _cacheDuration;
		//TODO Make this match the date format of your server
		mDateFormatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");

		// Make background thread low priority, to avoid affecting UI performance
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

		// Find the dir to save cached images
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();		
			cacheDir = new File(sdDir,"data/shifapics");

		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
			
			try {
				File output = new File(cacheDir, ".nomedia");
				boolean filecreated = output.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public ImageManager(Context context) {

		ctx = context;
		cacheDuration = 1200000;
		//TODO Make this match the date format of your server
		mDateFormatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");

		// Make background thread low priority, to avoid affecting UI performance
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

		// Find the dir to save cached images
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();		
			cacheDir = new File(sdDir,"data/shifapics");

		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}
	public void displayImage(String url, ImageView imageView, int defaultDrawableId) {

		try
		{
			if (url == null) return;
			Log.e("DisplayImage","data " + imageMap + url);
		/*	
			if(imageMap.containsKey(url)) {
				Log.e("DisplayImage","found in memory" );
				if (imageMap.get(url).get() == null) return;
				imageView.setImageBitmap(imageMap.get(url).get());
			}
			
			if (url.indexOf("http") != -1){
				imageView.setBackgroundResource(defaultDrawableId);
				queueImage(url, imageView, defaultDrawableId);
				Log.e("DisplayImage","not found in memory " );
				//imageView.setBackgroundResource(defaultDrawableId);
				
					return;
				
			}
			*/
			if (url.trim().equals("")) return;
			
			if (url.length() > 2) {
				File image = new  File("/sdcard/data/shifapics/"+url+".jpg"); 
			    if(image.exists()){
			    		Log.e("DisplayImage","FileGotit");
			             imageView.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
			             return;
		    	}
			    else
			    {
			    	queueImage(url, imageView, defaultDrawableId);
					Log.e("DisplayImage","imageview "  + defaultDrawableId);
					imageView.setBackgroundResource(defaultDrawableId);	
			    	
			    }
			}
			else
			{
				return;
				
			}
			
		    
		    
				Log.e("DisplayImage","url " + url);
				Log.e("DisplayImage","Entered " + imageMap);
				String session = url;//.substring(url.indexOf("shifaappsettings/")+17,url.indexOf(".jpg"));
				Log.i("DisplayImage","session " + session);
			
			if(imageMap.containsKey(session)) {
				Log.e("DisplayImage","IF");
				if (imageMap.get(url).get() == null) return;
				imageView.setImageBitmap(imageMap.get(url).get());
	
			} else {
				Log.e("DisplayImage","else");
				queueImage(url, imageView, defaultDrawableId);
				Log.e("DisplayImage","imageview "  + defaultDrawableId);
				imageView.setImageResource(defaultDrawableId);
				
			}
		}
		catch(Exception ex)
		{}
	}

	private void queueImage(String url, ImageView imageView, int defaultDrawableId) {
		// This ImageView might have been used for other images, so we clear 
		// the queue of old tasks before starting.
		Log.d("queueImage","Entre");
		imageQueue.Clean(imageView);
		Log.d("queueImage","1");
		ImageRef p = new ImageRef(url, imageView, defaultDrawableId);
		Log.d("queueImage","2");
		synchronized(imageQueue.imageRefs) {
			Log.d("queueImage","5");
			imageQueue.imageRefs.push(p);
			Log.d("queueImage","3");
			imageQueue.imageRefs.notifyAll();
			Log.d("queueImage","4");
		}
		Log.d("queueImage","6");
		// Start thread if it's not started yet
		if(imageLoaderThread.getState() == Thread.State.NEW) {
			Log.d("queueImage","7");
			imageLoaderThread.start();
			Log.d("queueImage","8");
		}
	}
	private boolean isTablet(Context context) {
		
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	private Bitmap getBitmap(String url) {
		URLConnection openConnection;
		Log.i("getBitmap", "url " + url);
		try {
			if (url.indexOf("-")!=-1){
				Log.i("getBitmap", "full url found" + url);
				
				//String modeURL = url.substring(0,1);
				if (url.indexOf("-") == 0)
				{
					Log.i("getBitmap", "inside " + url);
					if (isTablet(ctx) == true){
						Log.i("getBitmap", "inside tab " + url);
						//Tab Icons
						openConnection = new URL("http://kent.nasz.us/app_php/shifaappsettings/tablet/" + url + ".png").openConnection(); //48x48
					}
					else
					{
						Log.i("getBitmap", "inside mobile " + url);
						//Mobile Icons
						openConnection = new URL("http://kent.nasz.us/app_php/shifaappsettings/mobile/" + url + ".png").openConnection(); // 144x144
					}
				}
				else
				{
					// Feed Images
					openConnection = new URL("http://kent.nasz.us/app_php/shifaappsettings/" + url + ".jpg").openConnection();
				}
				
			}
			else
			{
				openConnection = new URL("https://graph.facebook.com/"+url+"/picture?type=small").openConnection();
			}
			Log.i("getBitmap",url);
			String filename = url + ".jpg";
			Log.i("getBitmap","1");
			File bitmapFile = new File(cacheDir, filename);
			Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getPath());
			Log.i("getBitmap","2");
			long currentTimeMillis = System.currentTimeMillis();
			Log.i("getBitmap","3");
			// Is the bitmap in our cache?
			if (bitmap != null) {
				Log.i("getBitmap","Bitmap cache");
				return bitmap;
				/*// Has it expired?
				long bitmapTimeMillis = currentTimeMillis - 8000; //bitmapFile.lastModified();
				//if ( (currentTimeMillis - bitmapTimeMillis) < cacheDuration ) {
					
				//}
				Log.i("getBitmap","4");
				// Check also if it was modified on the server before downloading it
				String lastMod = openConnection.getHeaderField("Last-Modified");
				Log.i("getBitmap","4.1 ");
				Log.i("getBitmap bitmapTimeMillis",String.valueOf(bitmapTimeMillis));
				Log.i("getBitmap","4.1.1 ");
				Log.i("getBitmap lastMod",String.valueOf(lastMod));
				long lastModTimeMillis = mDateFormatter.parse(lastMod).getTime();
				Log.i("getBitmap","4.2");
				//if ( lastModTimeMillis <= bitmapTimeMillis ) {
					//Log.i("getBitmap","4.3");
					//Discard the connection and return the cached version
					return bitmap;
			//	}
				//Log.i("getBitmap","5");*/
			}
				
			// Nope, have to download it
			bitmap = BitmapFactory.decodeStream(openConnection.getInputStream());
			Log.i("getBitmap","6");
			// save bitmap to cache for later
			writeFile(bitmap, bitmapFile);
			Log.i("getBitmap","7");
			return bitmap;

		} catch (Exception ex) {
			Log.i("getBitmap","8 " + ex.toString());
			ex.printStackTrace();
			
			return null;
		}
	}

	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally { 
			try { if (out != null ) out.close(); }
			catch(Exception ex) {} 
		}
	}

	/** Classes **/

	private class ImageRef {
		public String url;
		public ImageView imageView;
		public int defDrawableId;

		public ImageRef(String u, ImageView i, int defaultDrawableId) {
			url=u;
			imageView=i;
			defDrawableId = defaultDrawableId;
		}
	}

	//stores list of images to download
	private class ImageQueue {
		private Stack<ImageRef> imageRefs = 
				new Stack<ImageRef>();

		//removes all instances of this ImageView
		public void Clean(ImageView view) {

			for(int i = 0 ;i < imageRefs.size();) {
				if(imageRefs.get(i).imageView == view)
					imageRefs.remove(i);
				else ++i;
			}
		}
	}

	private class ImageQueueManager implements Runnable {
		@Override
		public void run() {
			try {
				while(true) {
					// Thread waits until there are images in the 
					// queue to be retrieved
					Log.i("ImageQueueManager","1");
					if(imageQueue.imageRefs.size() == 0) {
						Log.i("ImageQueueManager","2");
						
						synchronized(imageQueue.imageRefs) {
							Log.i("ImageQueueManager","3");
							
							imageQueue.imageRefs.wait();
						}
					}
					Log.i("ImageQueueManager","4");
					
					// When we have images to be loaded
					if(imageQueue.imageRefs.size() != 0) {
						Log.i("ImageQueueManager","5");
						
						ImageRef imageToLoad;

						synchronized(imageQueue.imageRefs) {
							Log.i("ImageQueueManager","6");
							
							imageToLoad = imageQueue.imageRefs.pop();
						}

						Bitmap bmp = getBitmap(imageToLoad.url);
						if (bmp != null){
							Log.i("ImageQueueManager","7 " + imageToLoad.url);
							String session = imageToLoad.url;//.substring(imageToLoad.url.indexOf("shifaappsettings/")+17,imageToLoad.url.indexOf(".jpg"));
							Log.i("ImageQueueManager","session " + session);
							
							imageMap.put(session, new SoftReference<Bitmap>(bmp));
							
							Object tag = imageToLoad.imageView.getTag();
							Log.i("ImageQueueManager","8" );
							
							// Make sure we have the right view - thread safety defender
							if(tag != null && ((String)tag).equals(imageToLoad.url)) {
								Log.i("ImageQueueManager","9" );
								BitmapDisplayer bmpDisplayer = 
										new BitmapDisplayer(bmp, imageToLoad.imageView, imageToLoad.defDrawableId);
								Log.i("ImageQueueManager","10" );
								Activity a = 
										(Activity)imageToLoad.imageView.getContext();
								Log.i("ImageQueueManager","11" );
								a.runOnUiThread(bmpDisplayer);
								Log.i("ImageQueueManager","12" );
							}
						}
						else
						{
							BitmapDisplayer bmpDisplayer = 
									new BitmapDisplayer(null, imageToLoad.imageView, imageToLoad.defDrawableId);
						}
					}

					if(Thread.interrupted())
						break;
					Log.i("ImageQueueManager","13" );
				}
			} catch (InterruptedException e) {Log.i("ImageQueueManager err","14 "  + e.toString()  );}
		}
	}

	//Used to display bitmap in the UI thread
	private class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;
		int defDrawableId;

		public BitmapDisplayer(Bitmap b, ImageView i, int defaultDrawableId) {
			bitmap=b;
			imageView=i;
			defDrawableId = defaultDrawableId;
		}

		public void run() {
			if(bitmap != null)
				imageView.setImageBitmap(bitmap);
			else
				imageView.setImageResource(defDrawableId);
		}
	}
}