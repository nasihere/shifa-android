package com.shifa.kent;

import java.io.InputStream;
import java.util.ArrayList;

import com.shifa.kent.ChatAdapterOnline.UserHolder;


import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PvtMsgAdapter extends ArrayAdapter<PvtMsgMain> {
	Context context;
	int layoutResourceId;
	UserHolder holder = null;
	String SessionID = "";
	public ImageManager imageManager;
	ArrayList<PvtMsgMain> filterdata = new ArrayList<PvtMsgMain>();	
	ArrayList<PvtMsgMain> data = new ArrayList<PvtMsgMain>();
	private static final int TAG_IMAGE_BTN_1 = 0;
	private static final int TAG_IMAGE_BTN_2 = 1;

	public PvtMsgAdapter(Context context, int layoutResourceId,
			ArrayList<PvtMsgMain> data, String SessionID) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	
		this.context = context;
		this.data = data;
		this.filterdata = data;
		this.SessionID = SessionID;
		imageManager = 
				new ImageManager(context.getApplicationContext());
	}
	@Override
	public int getCount() {
	    return filterdata.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
			View row = convertView;
			
		
		       
			if (row == null) {
			
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.PvtMsgTvTime = (TextView) row.findViewById(R.id.PvtMsgTvTime);

				holder.PvtMsgTvTimeSessionIdTo = (TextView) row.findViewById(R.id.PvtMsgTvTimeSessionIdTo);
				holder.PvtMsgName = (TextView) row.findViewById(R.id.PvtMsgName);
				holder.PvtMsgMsg = (TextView) row.findViewById(R.id.PvtMsgMsg);
				holder.PvtMsgChatterIcon = (ImageView) row.findViewById(R.id.PvtMsgChatterIcon);
				//holder.PvtMsgImageOnline= (ImageView) row.findViewById(R.id.PvtMsgChatterIcon1);
				
				
			} else {
				holder = (UserHolder) row.getTag();
			}
			row.setTag(holder);
			PvtMsgMain user = filterdata.get(position);
			
			
			
			
			holder.PvtMsgTvTimeSessionIdTo.setText(user._id);
			
			
			holder.PvtMsgTvTime.setText(Html.fromHtml(user.datetime), TextView.BufferType.SPANNABLE);
			holder.PvtMsgName.setText(Html.fromHtml(user.chatter), TextView.BufferType.SPANNABLE);
			holder.PvtMsgMsg.setText(Html.fromHtml(user.chat), TextView.BufferType.SPANNABLE);
			holder.PvtMsgChatterIcon.setImageBitmap(null);
			holder.PvtMsgChatterIcon.setTag(user.Icon);
			if (user.Icon.indexOf("http:") == -1)
			{
				if (user.Status.equals("Doctor"))
					holder.PvtMsgChatterIcon.setBackgroundResource(R.drawable.ic_chat_doctor);
				else
					holder.PvtMsgChatterIcon.setBackgroundResource(R.drawable.ic_chat_student);
				
				imageManager.displayImage(user._id, holder.PvtMsgChatterIcon, R.drawable.ic_launcher);
			}
			else
			{
				
				//new DownloadImageTask((ImageView) holder.PvtMsgChatterIcon).execute(user.Icon);
				
			}
			/*
			if (user.datetime.equals("Online"))
			{
				holder.PvtMsgImageOnline.setBackgroundResource(R.drawable.ic_pvtchat_onlinemark);
			}
			else
			{
				holder.PvtMsgImageOnline.setBackgroundResource(R.drawable.ic_pvtchat_offline);
			}
			*/
			 
		return row;

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
	        bmImage.setImageBitmap(result);
	    }
	}
	static class UserHolder {
		TextView PvtMsgName;
		TextView PvtMsgMsg;
		TextView PvtMsgTvTime;
		
		ImageView PvtMsgChatterIcon;
	//	ImageView PvtMsgImageOnline;
		TextView PvtMsgTvTimeSessionIdTo;
		
		
		
		
		
	}
	@Override
    public Filter getFilter() {
		  return new Filter() {
			  @Override
		        protected FilterResults performFiltering(CharSequence constraint) {
		             FilterResults oReturn = new FilterResults();
		             ArrayList<PvtMsgMain> results = new ArrayList<PvtMsgMain>();
		          
		            ArrayList<PvtMsgMain> orig = new ArrayList<PvtMsgMain>();
		            Log.e("orig","empty");
		           
		            	Log.e("orig","true");
		            	orig = data;
		            	Log.e("orig","false");
		            	
		            Log.e("orig",String.valueOf(orig.size()));
		            if (constraint != null) {
		                if (orig != null && orig.size() > 0) {
		                    for (final PvtMsgMain g : orig) {
		                    	String[] sWords = constraint.toString().split(" ");
		                    	int iMatched = 0;
		                    	for (String sWrd : sWords) {
		                    		
		                    		Log.e("sWrd ", sWrd);
		                    		if (g.getchatter().toLowerCase()
			                                .contains(sWrd.toLowerCase().toString()))
			                        {
		                    			iMatched++;
			                        	

			                        }
			                    	else if (g.getChat().toLowerCase()
			                                .contains(sWrd.toLowerCase().toString()))
			                        {
			                    		iMatched++;
			                        	
				                    	    
			                        }
			                    	else if (g.getFrm().toLowerCase()
			                                .contains(sWrd.toLowerCase().toString()))
			                        {
			                    		iMatched++;
			                        	
				                    	    
			                        }
			                    	
			                    	else if (g.getChat().toLowerCase()
			                                .contains(sWrd.toLowerCase().toString()))
			                        {
			                    		iMatched++;
			                        	
				                    	    
			                        }
			                    	
		                    		
		                    	}
		                    	Log.e("sWrd.length()", String.valueOf(sWords.length));
	                    		Log.e("iMatched ", String.valueOf(iMatched));
	                    		if (iMatched == sWords.length )
	                    		{
		                        	
		                    	    results.add(g);

	                    		}
		                    	
		                    }
		                }
		                oReturn.count = results.size();
		                oReturn.values = results;
		            }
		            return oReturn;
		        }

		        @SuppressWarnings("unchecked")
		        @Override
		        protected void publishResults(CharSequence constraint,
		                FilterResults results) {
		        	
		        	  ArrayList<PvtMsgMain> items;
		        	  Log.e("orig","notifyDataSetChanged");
		        	  
		            items = (ArrayList<PvtMsgMain>) results.values;
		           
		            Log.e("orig","notifyDataS items etChanged");
		            notifyDataSetChanged(items);
		        }
		    };
		}

		public void notifyDataSetChanged(ArrayList<PvtMsgMain> performfilter) {
		    super.notifyDataSetChanged();
		    this.filterdata = performfilter;
		   // notifyChanged = true;
		}
	
}
