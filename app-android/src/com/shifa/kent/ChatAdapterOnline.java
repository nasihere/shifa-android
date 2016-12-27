package com.shifa.kent;

import java.util.ArrayList;

import com.shifa.kent.UserCustomAdapter.UserHolder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatAdapterOnline extends ArrayAdapter<ChatMainOnline> {
	Context context;
	int layoutResourceId;
	UserHolder holder = null;
	String SessionID = "";
	ArrayList<ChatMainOnline> filterdata = new ArrayList<ChatMainOnline>();	
	ArrayList<ChatMainOnline> data = new ArrayList<ChatMainOnline>();
	private static final int TAG_IMAGE_BTN_1 = 0;
	private static final int TAG_IMAGE_BTN_2 = 1;
	public ImageManager imageManager;
	public ChatAdapterOnline(Context context, int layoutResourceId,
			ArrayList<ChatMainOnline> data, String SessionID) {
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
				holder.txtChatterName = (TextView) row.findViewById(R.id.tv_chatonline_name);
				holder.tvDateTIme = (TextView) row.findViewById(R.id.tvDateTIme);
				holder.txtChatPost = (TextView) row.findViewById(R.id.tv_chatonline_status);
				holder.txtChatId = (TextView) row.findViewById(R.id.tv_chatonline_id);
				holder.txtChatreply = (TextView) row.findViewById(R.id.tv_chatonline_replies		);
				holder.imgChatIcoLeft = (ImageView) row.findViewById(R.id.img_chatonline_post);
				holder.imgChatDelete = (ImageView) row.findViewById(R.id.img_chatonline_delete1);
				holder.imgChatPoster = (ImageView) row.findViewById(R.id.img_chatonline_rem2);
				holder.txtChatOnlineLoadMsg = (TextView) row.findViewById(R.id.tv_chatOnline_loadmsg);
				holder.img_chatonline_PM = (ImageView) row.findViewById(R.id.img_chatonline_PM);
				
				
			} else {
				holder = (UserHolder) row.getTag();
			}
			row.setTag(holder);
			ChatMainOnline user = filterdata.get(position);
			
			
			holder.imgChatDelete.setTag(R.string.ListPosition, position);
			holder.imgChatDelete.setTag(R.string.RemediesShowHide, user.get_id());
			
			holder.img_chatonline_PM.setTag(R.string.ListPosition, position);
			holder.img_chatonline_PM.setTag(R.string.RemediesShowHide, user.get_id());
			
			String s_dtTime = "";
			String sFrm = "";
			String sChat = "";
			String s_id = "";
			String s_chatter ="";
			String s_reply ="";
			
			try
			{
				Log.e("Chatadapter",SessionID);
				s_chatter= user.getchatter().toString();
				sChat  = user.getChat().toString();
				s_id = user.get_id().toString();
				s_reply = user.getreply().toString();
				s_dtTime = user.getDateTime().toString();
				Log.e("Chatadapter",sFrm);
				
				
				
				
			}
			catch(Exception e)
			{
				sFrm = "---";
				sChat = "";
				
				Log.e("Error listview",e.toString());
				
			}
			holder.tvDateTIme.setText(s_dtTime);
			holder.txtChatPost.setText(Html.fromHtml(sChat), TextView.BufferType.SPANNABLE);
			holder.txtChatId.setText(s_id);
			holder.txtChatterName.setText(s_chatter);
			imageManager.displayImage(user.frm, holder.imgChatPoster, R.drawable.ic_chatonline_to);
			if (s_reply.equals(""))
			{
				
				holder.txtChatreply.setVisibility(View.GONE);
				holder.imgChatIcoLeft.setVisibility(View.GONE);
			}
			else
			{
				holder.txtChatreply.setText(Html.fromHtml(s_reply), TextView.BufferType.SPANNABLE);
				holder.txtChatreply.setVisibility(View.VISIBLE);
				holder.imgChatIcoLeft.setVisibility(View.VISIBLE);
				
			}
			
			 holder.img_chatonline_PM.setOnClickListener(new OnClickListener() {
				 
					@Override
					public void onClick(View v) {
						
						Log.e("iTag","Enter Correct");
					  	int iTag = (Integer) v.getTag(R.string.ListPosition);
					  	Log.e("iTag","Enter Correct 1");
		                Log.e("iTag",String.valueOf(iTag));
		                Log.e("iTag","Enter Correct 2");
		                ChatMainOnline us = data.get(iTag);
		                Log.e("iTag","Enter Correct 3");
		                String _id = us.get_id();
		                Log.e("iTag","Enter Correct 4");
		                Log.e("_id",_id ); 
		                
		                
		                
			                
			               Intent intent = new Intent(context, activity_privatemsg.class);
			  			   intent.putExtra("Screen", "1");
			  			   intent.putExtra("session_id_to", us.frm);
			  			   context.startActivity(intent);
			  			  
		
					}
				});
			 
			 holder.imgChatDelete.setOnClickListener(new OnClickListener() {
				 
					@Override
					public void onClick(View v) {
						
						  	Log.e("iTag","Enter Correct");
						  	int iTag = (Integer) v.getTag(R.string.ListPosition);
						  	Log.e("iTag","Enter Correct 1");
			                Log.e("iTag",String.valueOf(iTag));
			                Log.e("iTag","Enter Correct 2");
			                ChatMainOnline us = data.get(iTag);
			                Log.e("iTag","Enter Correct 3");
			                String _id = us.get_id();
			                Log.e("iTag","Enter Correct 4");
			                Log.e("_id",_id ); 
			                
			                
			            	String Action = _id  ;//((TextView) view.findViewById(R.id.lv_tv_note)).getText().toString();
			    			
			   			 
			    			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
			    			final TextView input = new TextView(context);
			    			
			    			input.setTag(us);
			    			
			    			
			    			
			    			alert.setTitle( "Are you sure you want to delete this post?" );
			    		    alert.setView(input);
			    		    
			    		    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			    		        public void onClick(DialogInterface dialog, int whichButton) {
			    		            
			    		        		
			    		        		ChatMainOnline us = (ChatMainOnline) input.getTag();
			    		        		us.DeletePost();
			    		        		
			    		        		
			    		      	  			
			    		            
			    		        }
			    		    });

			    		    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			    		        public void onClick(DialogInterface dialog, int whichButton) {
			    		            dialog.cancel();
			    		        }
			    		    });
			    		    
			    		    alert.show();
		
					}
				});
				
				if (position == getCount()-1)
				{
					holder.txtChatOnlineLoadMsg.setVisibility(View.VISIBLE);
					holder.txtChatOnlineLoadMsg.setTag(user._id);
				}
				else
				{
					holder.txtChatOnlineLoadMsg.setVisibility(View.GONE);
					holder.txtChatOnlineLoadMsg.setTag("");
				}
			 
		return row;

	}

	static class UserHolder {
		TextView txtChatterName;
		TextView tvDateTIme;
		TextView txtChatPost;
		TextView txtChatId;
		TextView txtChatreply;
		ImageView imgChatDelete;
		ImageView imgChatPoster;
		ImageView imgChatIcoLeft;
		ImageView img_chatonline_PM;
		TextView txtChatOnlineLoadMsg;
		
	}
	@Override
    public Filter getFilter() {
		  return new Filter() {
			  @Override
		        protected FilterResults performFiltering(CharSequence constraint) {
		             FilterResults oReturn = new FilterResults();
		             ArrayList<ChatMainOnline> results = new ArrayList<ChatMainOnline>();
		          
		            ArrayList<ChatMainOnline> orig = new ArrayList<ChatMainOnline>();
		            Log.e("orig","empty");
		           
		            	Log.e("orig","true");
		            	orig = data;
		            	Log.e("orig","false");
		            	
		            Log.e("orig",String.valueOf(orig.size()));
		            if (constraint != null) {
		                if (orig != null && orig.size() > 0) {
		                    for (final ChatMainOnline g : orig) {
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
			                    	
			                    	else if (g.getreply().toLowerCase()
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
		        	
		        	  ArrayList<ChatMainOnline> items;
		        	  Log.e("orig","notifyDataSetChanged");
		        	  
		            items = (ArrayList<ChatMainOnline>) results.values;
		           
		            Log.e("orig","notifyDataS items etChanged");
		            notifyDataSetChanged(items);
		        }
		    };
		}

		public void notifyDataSetChanged(ArrayList<ChatMainOnline> performfilter) {
		    super.notifyDataSetChanged();
		    this.filterdata = performfilter;
		   // notifyChanged = true;
		}
}
