package com.shifa.kent;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
public class UserCustomKentAdapter extends ArrayAdapter<User> implements Filterable  {
	Context context;
	int layoutResourceId;
	DBclass db1;

	UserHolder holder = null;
	boolean filterResult = true;
	ArrayList<User> data = new ArrayList<User>();
	ArrayList<User> filterdata = new ArrayList<User>();
	public UserCustomKentAdapter(Context context, int layoutResourceId,
			ArrayList<User> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.filterdata = data;
		this.data = data;
		 
		db1 = new DBclass(context);
		Log.e("USercustomadapter constructor","Data Populate done");
	
	}
	@Override
	public int getCount() {
	    return filterdata.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("convertView","convertView");
		
		View row = convertView;
		Log.e("row","Start");

		if (row == null) {
			Log.e("cache","false");
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new UserHolder();
			holder.textName = (TextView) row.findViewById(R.id.tv_kent_overview_sympptoms);
		
			holder.chkHolderBox = (CheckBox) row.findViewById(R.id.chckKentOverviewidbox);
			holder.chkHolderBox.setTag(R.string.RemediesShowHide, holder);
			
			
			
			holder.txtCounter = (TextView) row.findViewById(R.id.tv_kent_overview_Count);
			holder.txtRemedies = (TextView) row.findViewById(R.id.tv_kent_overview_Remedies);
			holder.imgRemOverview = (ImageView) row.findViewById(R.id.imgRemOverview);
			
			row.setTag(holder);
		} else {
			Log.e("cache","true");
			
			holder = (UserHolder) row.getTag();
		}
			
				Log.e("Getview",String.valueOf(position));
				try
				{
				User user = filterdata.get(position);
				
				
				
				holder.chkHolderBox.setTag(R.string.ListPosition, position);
				holder.chkHolderBox.setTag(R.string.RemediesShowHide, user.getCounter());
				
			
				
				String name = user.getName().replace("|", ", ");
				
				String sCounter = user.getCounter();
				Log.e("Getview","progress 20%");
				 //Log.e("getId check ", String.valueOf(user.getRemediesShowHide()));
				 int sRemedies = user.getRemediesShowHide();
				 
				 Log.e("counter","ic_cateogryminus");
				 String HtmlRem = getRemediesColor(user.getremedies(),user.book);
				 if (HtmlRem.equals("-"))
				 {
					 name = "<b><u>" + name + "</u></b>";
					 holder.txtRemedies.setVisibility(View.GONE);
					 holder.txtCounter.setVisibility(View.GONE);
					 holder.imgRemOverview.setBackgroundResource(R.drawable.ic_launcher);
				 }
				 else
				 {
		         	 holder.txtRemedies.setText(Html.fromHtml(HtmlRem), TextView.BufferType.SPANNABLE);
		         	 holder.txtRemedies.setVisibility(View.VISIBLE);
		         	 holder.txtCounter.setText(sCounter);
		         	 holder.imgRemOverview.setBackgroundResource(R.drawable.ic_kent_overview);
		         	 holder.txtCounter.setVisibility(View.VISIBLE);
				 }
				 
				 holder.textName.setText(Html.fromHtml(name), TextView.BufferType.SPANNABLE);
				
				 int iSublevel = user.getSubLevel();
				 if (iSublevel == 2 || iSublevel == 8)
				 {
					 sCounter = "";
					 holder.chkHolderBox.setEnabled (false);
					
				 }
				 else 
				 {
					
					 holder.chkHolderBox.setEnabled(true);
				
				 }
				
				 
				 if (user.getSelected().equals("1"))
				 {
					 Log.e("holder.chkHolderBox","Setselected trye");
					 holder.chkHolderBox.setChecked(true);
				 }
				 else
				 {
					 Log.e("holder.chkHolderBox","Setselected false");
					 holder.chkHolderBox.setChecked(false);
				 }
				 holder.chkHolderBox.setTag(R.string.RemediesShowHide, user.getID());
				 
				 
				 holder.chkHolderBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				        	
			                    
				        
				        	int iTag = (Integer) buttonView.getTag(R.string.ListPosition);
			                Log.e("iTag",String.valueOf(iTag));
			                User us = filterdata.get(iTag);
			                Log.e("us"," user us filled ");
			                int _id = (Integer) buttonView.getTag(R.string.RemediesShowHide);
			                Log.e("_id", String.valueOf(_id));
				            if(isChecked) {
				            	
				            	
				            		us.setSelected("1");
				            		Log.e("Check","CHECKED1");
				            		db1.KentItemSeleted("1", _id);
				            		Log.e("Check","CHECKED");
				            } else {   
				            		us.setSelected("0");
				            	  	Log.e("Check","UNCHECKED");
				            	  	db1.KentItemSeleted("0", _id);
				            	  	Log.e("Check","UNCHECKED");
				            }
				        }               
				    });		
				 
				 Log.e("Getview","progress 100%");
				
		return row;
				}
				catch(Exception exx)
				{
					Log.e("error",exx.toString());
					return row;
					
				}
	}
	/**
	         * Implementing the Filterable interface.
	         */
	
	public String getColorOnRemedies(String r)
	{
		
	    //window.MyCls.log(remedies);
	    return "";
	}
	static class UserHolder {
		TextView textName;
		
		TextView txtCounter;
		ImageView imgRemOverview;
		TextView txtRemedies;
		CheckBox chkHolderBox;
	}
	@Override
    public Filter getFilter() {
		  return new Filter() {
			  @Override
		        protected FilterResults performFiltering(CharSequence constraint) {
		             FilterResults oReturn = new FilterResults();
		             ArrayList<User> results = new ArrayList<User>();
		          
		            ArrayList<User> orig = new ArrayList<User>();
		            Log.e("orig","empty");
		           
		            	Log.e("orig","true");
		            	orig = data;
		            	Log.e("orig","false");
		            	
		            Log.e("orig",String.valueOf(orig.size()));
		            if (constraint != null) {
		                if (orig != null && orig.size() > 0) {
		                    for (final User g : orig) {
		                    	String[] sWords = constraint.toString().split(" ");
		                    	int iMatched = 0;
		                    	for (String sWrd : sWords) {
		                    		
		                    		Log.e("sWrd ", sWrd);
		                    		if (g.getName().toLowerCase()
			                                .contains(sWrd.toLowerCase().toString()))
			                        {
		                    			iMatched++;
			                        	

			                        }
			                    	else if (g.getAddress().toLowerCase()
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
		        	
		        	  ArrayList<User> items;
		        	  Log.e("orig","notifyDataSetChanged");
		        	  
		            items = (ArrayList<User>) results.values;
		           
		            Log.e("orig","notifyDataS items etChanged");
		            notifyDataSetChanged(items);
		        }
		    };
		}

		public void notifyDataSetChanged(ArrayList<User> performfilter) {
		    super.notifyDataSetChanged();
		    this.filterdata = performfilter;
		   // notifyChanged = true;
		}
		public String getRemediesColor(String r, String book)
		{
			Log.e("getRemediesColor", book);
			if (book.equalsIgnoreCase("Boenninghausens")){
				return getRemediesBoenninghausens(r);
			}
			else{
				return getRemediesKent(r);
			}
			
			
		}
		public String getRemediesKent(String r)
		{
			Log.e("getRemediesKent", "I am in");
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
		public String getRemediesBoenninghausens(String r)
		{
			Log.e("getRemediesBoenninghausens", "I am in");
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
			            str = "<font color='black'>" + spi[0] + "</font>, ";
			        }
			        else if (spi[1].equals("2"))
			        {
			            str = "<font color='#009933'>" + spi[0] + "</font>, ";
			        }
			        else if (spi[1].equals("3"))
			        {
			            str = "<font color='blue'><i>" + spi[0] + "</i></font>, ";
			        }
			        else if (spi[1].equals("4"))
			        {
			            str = "<font color='red'><b>" + spi[0] + "</b></font>, ";
			        }
			        else if (spi[1].equals("5"))
			        {
			            str = "<font color='#000080'><u><b>" + spi[0] + "</b></u></font>, ";
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
}



