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

public class PvtMsgPrivateAdapter extends ArrayAdapter<PvtMsgMain> {
	Context context;
	int layoutResourceId;
	UserHolder holder = null;
	public ImageManager imageManager;
	String SessionID = "";
	String session_id_to = "";
	ArrayList<PvtMsgMain> filterdata = new ArrayList<PvtMsgMain>();	
	ArrayList<PvtMsgMain> data = new ArrayList<PvtMsgMain>();
	private static final int TAG_IMAGE_BTN_1 = 0;
	private static final int TAG_IMAGE_BTN_2 = 1;

	public PvtMsgPrivateAdapter(Context context, int layoutResourceId,
			ArrayList<PvtMsgMain> data, String SessionID,String session_id_to) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	
		this.context = context;
		this.data = data;
		this.filterdata = data;
		this.SessionID = SessionID;
		this.session_id_to = session_id_to;
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
			
				holder.PvtChatPersonalview_chatmsgright = (TextView) row.findViewById(R.id.PvtChatPersonalview_chatmsgright);
				holder.PvtChatPersonalview_chatmsg = (TextView) row.findViewById(R.id.PvtChatPersonalview_chatmsg);
				holder.PvtChatPersonalviewLeftIco = (ImageView) row.findViewById(R.id.PvtChatPersonalviewLeftIco);
				holder.PvtChatPersonalviewRightIco = (ImageView) row.findViewById(R.id.PvtChatPersonalviewRightIco);
			} else {
				holder = (UserHolder) row.getTag();
			}
			row.setTag(holder);
			PvtMsgMain user = filterdata.get(position);
			
			
			
			
			
			
			//SessionID = "652f21113d3d4d855621e09740c62583";
			if (user._id.equals(SessionID))
			{ // nasz id = 10205304767877899
				
				holder.PvtChatPersonalview_chatmsgright.setText(Html.fromHtml(user.chatWithName ), TextView.BufferType.SPANNABLE);
				holder.PvtChatPersonalview_chatmsgright.setVisibility(View.VISIBLE);
				holder.PvtChatPersonalviewLeftIco.setVisibility(View.VISIBLE);
				holder.PvtChatPersonalview_chatmsg.setVisibility(View.GONE);
				holder.PvtChatPersonalviewRightIco.setVisibility(View.GONE);
				if (user.Status.equals("Doctor"))
					holder.PvtChatPersonalviewLeftIco.setBackgroundResource(R.drawable.ic_chat_doctor);
				else
					holder.PvtChatPersonalviewLeftIco.setBackgroundResource(R.drawable.ic_chat_student);
			
					
					imageManager.displayImage(session_id_to, holder.PvtChatPersonalviewLeftIco, R.drawable.ic_launcher);
					
					
				

				
			
				
			}
			else
			{
				holder.PvtChatPersonalview_chatmsgright.setVisibility(View.GONE);
				holder.PvtChatPersonalview_chatmsg.setText(Html.fromHtml(user.chatWithName), TextView.BufferType.SPANNABLE);
				holder.PvtChatPersonalview_chatmsg.setVisibility(View.VISIBLE);
				holder.PvtChatPersonalviewLeftIco.setVisibility(View.GONE);
				holder.PvtChatPersonalviewRightIco.setVisibility(View.VISIBLE);
				
					if (user.Status.equals("Doctor"))
						holder.PvtChatPersonalviewRightIco.setBackgroundResource(R.drawable.ic_chat_doctor);
					else
						holder.PvtChatPersonalviewRightIco.setBackgroundResource(R.drawable.ic_chat_student);
					
					imageManager.displayImage(SessionID, holder.PvtChatPersonalviewRightIco, R.drawable.ic_launcher);
				
				
			}
		
			
			
			
			 
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
		TextView PvtChatPersonalview_chatmsgright;
		TextView PvtChatPersonalview_chatmsg;
		ImageView PvtChatPersonalviewLeftIco;
		ImageView PvtChatPersonalviewRightIco;
		
		
		
		
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
