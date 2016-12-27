package com.shifa.kent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


public class home_classAdapter extends ArrayAdapter<String>  {

    private final Context context;
    private final String[] Ids;
    private final int rowResourceId;
    private String SessionID;
    public ImageManager imageManager;
    public home_classAdapter(Context context, int textViewResourceId, String[] objects) {

        super(context, textViewResourceId, objects);
        imageManager = 
				new ImageManager(context.getApplicationContext());        
        Log.e("Home screen progress ", "3.1.1");
        this.context = context;
        this.Ids = objects;
        this.rowResourceId = textViewResourceId;
        Log.e("Home screen progress ", "3.1.2");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("Home screen progress ", "3.1.1.1");
        View rowView = inflater.inflate(rowResourceId, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView textView = (TextView) rowView.findViewById(R.id.tvListItemHome);
        TextView tvViewStatus = (TextView) rowView.findViewById(R.id.tvViewStatusHome);
       
        ImageView imgViewStatus = (ImageView) rowView.findViewById(R.id.imgViewStatusHome);
        Log.e("Home screen progress ", "3.1.1.1");
        int id = Integer.parseInt(Ids[position]);
        String imageFile = home_adapter.GetbyId(id).IconFile;
        tvViewStatus.setVisibility(View.GONE);
        imgViewStatus.setVisibility(View.GONE);
        textView.setText(home_adapter.GetbyId(id).Name);
        // get input stream
        if (home_adapter.GetbyId(id).Name.equals("")) 
        {
        	 tvViewStatus.setVisibility(View.GONE);
        	 imgViewStatus.setVisibility(View.GONE);
        	 imageView.setVisibility(View.GONE);
        	 textView.setVisibility(View.GONE);
        }
       if (imageFile.equals("1")) 
       {
    	   
    	   imageView.setBackgroundResource(R.drawable.ic_mm_kent_logo);
    	 
       }
       else if (imageFile.equals("19")) 
       {
    	   
    	   imageView.setBackgroundResource(R.drawable.ic_cyrusmaxwellboger);
    	 
       }
       else if (imageFile.equals("21"))
       {
    	   imageView.setBackgroundResource(R.drawable.ic_launcher);
    	   
       }
       else if (imageFile.equals("20")) 
       {
    	   SessionID = home_adapter.GetbyId(id).StatusItem;
    	   imageView.setBackgroundResource(R.drawable.facebook_logo);
    	    if (SessionID.equalsIgnoreCase("000000000000007") || SessionID.equalsIgnoreCase("1111111100000") || SessionID.equalsIgnoreCase("10205304767877899"))
	   		{
    	    			//dont do anything show migrate button	    		   		
	   		}
   			else if (SessionID.matches("[0-9]+") && SessionID.length() > 2) {
   				imageView.setVisibility(View.GONE);
   				textView.setVisibility(View.GONE);
   			}
    	 
       }
       else if (imageFile.equals("18")) 
       {
    	   
    	   imageView.setBackgroundResource(R.drawable.ic_logo_boenninghausens);
    	 
       }
       else if (imageFile.equals("2")) 
    	   imageView.setBackgroundResource(R.drawable.ic_home_materiamedica);
       else if (imageFile.equals("3")) 
    	   imageView.setBackgroundResource(R.drawable.ic_reversed_repertory);
       else if (imageFile.equals("4")) 
    	   imageView.setBackgroundResource(R.drawable.ic_home_abbreviation);
       else if (imageFile.equals("5")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_home_chat);
    	   if (!home_adapter.GetbyId(id).StatusItem.equals(""))
    	   {
	    	   tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
	    	 //  imgViewStatus.setBackgroundResource(R.drawable.ic_home_icon_online);
	    	   imageManager.displayImage(SessionID, imgViewStatus, R.drawable.ic_home_icon_online);
	    	   tvViewStatus.setVisibility(View.VISIBLE);
	           imgViewStatus.setVisibility(View.VISIBLE);
    	   }
       }
       else if (imageFile.equals("6")) 
    	   imageView.setBackgroundResource(R.drawable.ic_home_organion);
       else if (imageFile.equals("7")) 
       {   imageView.setBackgroundResource(R.drawable.ic_home_adsfreestatus);
	       if (!home_adapter.GetbyId(id).StatusItem.equals(""))
	       {
		       tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
			   tvViewStatus.setVisibility(View.VISIBLE);
			   imgViewStatus.setBackgroundResource(R.drawable.ic_home_adsfreestatus);
		       imgViewStatus.setVisibility(View.VISIBLE);
	       }
       }
       else if (imageFile.equals("15")) 
       {   imageView.setBackgroundResource(R.drawable.ic_home_mail_icon);
	       if (!home_adapter.GetbyId(id).StatusItem.equals(""))
	       {
		       tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
			   tvViewStatus.setVisibility(View.VISIBLE);
			   imgViewStatus.setBackgroundResource(R.drawable.ic_home_mail_icon_status);
		       imgViewStatus.setVisibility(View.VISIBLE);
	       }
       }
       else if (imageFile.equals("8")) 
    	   imageView.setBackgroundResource(R.drawable.ic_home_logout);
       else if (imageFile.equals("16")) 
    	   imageView.setBackgroundResource(R.drawable.ic_home_help);
       else if (imageFile.equals("9")) 
    	   {
    	   
    	   imageView.setVisibility(View.GONE);
		   textView.setVisibility(View.GONE);
    	   	
		   //Log.e("URL",home_adapter.GetbyId(id).StatusItem);
      				
		   if (home_adapter.GetbyId(id).StatusItem.equals("-"))
		   {}
		   else  if (!home_adapter.GetbyId(id).StatusItem.equals(""))
		       {
	    		  
	        	 
/*
	               webView.getSettings().setJavaScriptEnabled(true);
	          		webView.clearHistory();
	          		
	          		webView.setWebViewClient(new WebViewClient() {
				        @Override
				        public boolean shouldOverrideUrlLoading(WebView view, String url) {
				        	Log.e("URL", url);
				        		view.requestFocus();
				            view.loadUrl(url);
				            return true;
				        }
				    });
	          		

	          		webView.requestFocus(View.FOCUS_DOWN);

	          		webView.setOnTouchListener(new View.OnTouchListener() {
	          	        @Override
	          	        public boolean onTouch(View v, MotionEvent event) {
	          	            switch (event.getAction()) {
	          	                case MotionEvent.ACTION_DOWN:
	          	                case MotionEvent.ACTION_UP:
	          	                    if (!v.hasFocus()) {
	          	                        
	          	                      InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
	          	                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	          	              //    v.requestFocus();
		          	                 
	          	                    
	          	                    
	          	                    }
	          	                    break;
	          	            }
	          	            return false;
	          	        }
	          	    });
	          	    
	          		webView.loadUrl(home_adapter.GetbyId(id).StatusItem);
	          		
	               */
				 
		       }
	    	 
    	   }
         
       else if (imageFile.equals("10")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_shifa_news);
    	   if (!home_adapter.GetbyId(id).StatusItem.equals(""))
    	   {
	    	   tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
	    	   imgViewStatus.setBackgroundResource(R.drawable.ic_home_newsstatus);
	    	   tvViewStatus.setVisibility(View.VISIBLE);
	           imgViewStatus.setVisibility(View.VISIBLE);
    	   }
       }
       
       else if (imageFile.equals("11")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_myprofile);
    	   if (!home_adapter.GetbyId(id).StatusItem.equals(""))
    	   {
	    	   tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
	    	   imgViewStatus.setBackgroundResource(R.drawable.ic_home_myprofilestatus);
	    	   tvViewStatus.setVisibility(View.VISIBLE);
	           imgViewStatus.setVisibility(View.VISIBLE);
    	   }
       }
       
       else if (imageFile.equals("12")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_chatonline_from);
    	   if (!home_adapter.GetbyId(id).StatusItem.equals(""))
    	   {
    		   
	    	   tvViewStatus.setText(Html.fromHtml(home_adapter.GetbyId(id).StatusItem), TextView.BufferType.SPANNABLE);
	    	   imgViewStatus.setBackgroundResource(R.drawable.ic_chatonline_group);
	    	   tvViewStatus.setVisibility(View.VISIBLE);
	           imgViewStatus.setVisibility(View.VISIBLE);
    	   }
       }
       
       else if (imageFile.equals("14")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_kent_report_menu);
    	 
       }
       else if (imageFile.equals("17")) 
       {
    	   imageView.setBackgroundResource(R.drawable.ic_home_settings);
    	 
       }
        return rowView;

    }
 
}