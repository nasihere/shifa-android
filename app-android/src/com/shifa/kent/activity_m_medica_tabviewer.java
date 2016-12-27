package com.shifa.kent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
 
public class activity_m_medica_tabviewer extends TabActivity {
    /** Called when the activity is first created. */
	 String SearchKeyWord = "";
	 TabHost tabHost;
	 
	 String[] StrAutoCompleteMap = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_materia_medica);
 
        final MultiAutoCompleteTextView mt  = (MultiAutoCompleteTextView)
        		findViewById(R.id.multiAutoCompleteSearchMateriaMedica);
        
        //String[] str={"Andoid","Jelly Bean","Froyo",
        	///	"Ginger Bread","Eclipse Indigo","Eclipse Juno"};
        
        
        
        final ImageButton btnSearch = (ImageButton) findViewById(R.id.imgBtnSearchMateriaMedica);
        btnSearch.setOnClickListener(new View.OnClickListener() {

        	  @Override
        	  public void onClick(View view) {
        		 
        			
        		 
        		  SearchKeyWord = mt.getText().toString();
        		  
        		  
        		  Bundle extras = getIntent().getExtras();
        		  Bundle bundle = new Bundle();
        		  Intent Intent = new Intent(activity_m_medica_tabviewer.this,
        				  activity_m_medica_tabviewer.class);
        		  
        		  bundle.putStringArray("StrAutoCompleteMap", StrAutoCompleteMap);
        		  Intent.putExtras(bundle);     
        		  Intent.putExtra("SearchKeyWord", SearchKeyWord);
        		  Intent.putExtra("medica_data",  extras.getString("medica_data"));
        		  Intent.putExtra("kent_data",  extras.getString("kent_data"));
        		  Intent.putExtra("allen_data",  extras.getString("allen_data"));
        		  startActivity(Intent);
        		  finish();
        		  
        	  }

        	});
        
//        bundle read parameter
        Bundle extras = getIntent().getExtras();
        String value = "";
        SearchKeyWord = extras.getString("SearchKeyWord");
        //Set multi autocomplete text before replace
         //mt=(MultiAutoCompleteTextView)
        	//	findViewById(R.id.multiAutoCompleteSearchMateriaMedica);
        mt.setText(SearchKeyWord);
        
        SearchKeyWord = SearchKeyWord.replace(":(Title)",".__" );
        Log.e("SearchKeyWord",SearchKeyWord);
        
        
//      bundle read parameter boeriek data
        
        value = extras.getString("medica_data");
        Intent intentBorieck = new Intent(activity_m_medica_tabviewer.this,
        		activity_m_medica_tab_borieck.class);
		
        intentBorieck.putExtra("SearchKeyWord", SearchKeyWord);
        intentBorieck.putExtra("medica_data", value);
		
//      bundle read parameter kent data        
        value = extras.getString("kent_data");
        Intent intentKent = new Intent(activity_m_medica_tabviewer.this,
        		activity_m_medica_tab_kent.class);
        intentKent.putExtra("SearchKeyWord", SearchKeyWord);
        intentKent.putExtra("kent_data", value);
		
//      bundle read parameter allen data
        value = extras.getString("allen_data");
        Intent intentAllen = new Intent(activity_m_medica_tabviewer.this,
        		activity_m_medica_tab_allen.class);
        intentAllen.putExtra("SearchKeyWord", SearchKeyWord);
        intentAllen.putExtra("allen_data", value);
        
        
	//	intent.putExtra("allen_data", sDataAllen);
	//	intent.putExtra("kent_data", sDataKent);
		
		
		
        /** TabHost will have Tabs */
         tabHost = (TabHost)findViewById(android.R.id.tabhost);
 
        /** TabSpec used to create a new tab.
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */
 
        /** tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec firstTabSpec = tabHost.newTabSpec("tab_id1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tab_id2");
        TabSpec thirdTabSpec = tabHost.newTabSpec("tab_id3");
        
        
        /* Trimgin Cleaing Data */
        String MultipleListWordsBorieck = extras.getString("medica_data").replaceAll("\\<[^>]*>","").toLowerCase().toString();
        String MultipleListWordsKent  = extras.getString("kent_data").replaceAll("\\<[^>]*>","").toLowerCase().toString();
        String MultipleListWordsAllen  =  extras.getString("allen_data").replaceAll("\\<[^>]*>","").toLowerCase().toString();
      
        
      
        /** Search Count index **/
        int SearchCountBorieck = 0;
        String SearchCounterBorieckText = ""; 
       
        String[]  tmpSearch = SearchKeyWord.split(",");
    		
    	
        
        for(int i=0;i<=tmpSearch.length -1 ;i++)
        {
        	Log.e("tmpSearch[i]",tmpSearch[i]);
        	if (tmpSearch[i].trim().equals("")) break;
        	SearchCountBorieck  += MultipleListWordsBorieck.split(tmpSearch[i].toLowerCase(), -1).length -1;
        	SearchCounterBorieckText = " (" + String.valueOf(SearchCountBorieck) + ")";
        }
        int SearchCountKent = 0;
        String SearchCountKentText = ""; 
        for(int i=0;i<=tmpSearch.length -1 ;i++)
        {
        	Log.e("tmpSearch[i] kent",tmpSearch[i]);
        	if (tmpSearch[i].trim().equals("")) break;
        	SearchCountKent  += MultipleListWordsKent.split(tmpSearch[i].toLowerCase(), -1).length -1;
        	SearchCountKentText = " (" + String.valueOf(SearchCountKent) + ")";
        }
        int SearchCountAllen = 0;
        String SearchCountAllenText = "";
        for(int i=0;i<=tmpSearch.length -1 ;i++)
        {
        	if (tmpSearch[i].trim().equals("")) break;
        	SearchCountAllen  += MultipleListWordsAllen.split(tmpSearch[i].toLowerCase(), -1).length -1;
        	SearchCountAllenText = " (" + String.valueOf(SearchCountAllen) + ")";
        }
        
        /** TabSpec setIndicator() is used to set name for the tab. */
        /** TabSpec setContent() is used to set content for a particular tab. */
        firstTabSpec.setIndicator("Boericke" + SearchCounterBorieckText).setContent(intentBorieck);
        secondTabSpec.setIndicator("J.K Kent" + SearchCountKentText).setContent(intentKent);
        thirdTabSpec.setIndicator("Allen" + SearchCountAllenText).setContent(intentAllen);
 
        /** Add tabSpec to the TabHost to display. */
        if (!extras.getString("medica_data").equals(""))
        	tabHost.addTab(firstTabSpec);
        if (!extras.getString("kent_data").equals(""))
        	tabHost.addTab(secondTabSpec);
        if (!extras.getString("allen_data").equals(""))
        	tabHost.addTab(thirdTabSpec);
 
        
        
        
        
        
        /**Autocomplete text box data check earlier*/
        Intent i = getIntent();
        Bundle intentExtra=i.getExtras();
        if (intentExtra != null)
        	StrAutoCompleteMap = i.getStringArrayExtra("StrAutoCompleteMap");
        
        String MultipleListWords = MultipleListWordsBorieck + MultipleListWordsKent + MultipleListWordsAllen;
        MultipleListWords = deDup(MultipleListWords.toLowerCase());
        MultipleListWords = MultipleListWords.replace(".__", ":(Title) ");
        
        if (StrAutoCompleteMap == null)
        {
        	StrAutoCompleteMap   = MultipleListWords.split(" ");
        	
       }
            mt.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	        
	    	ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
	     		android.R.layout.simple_dropdown_item_1line,StrAutoCompleteMap);
	     
	    	mt.setThreshold(1);
	    	mt.setAdapter(adp);
        //}
        
        
        
    }
    public String deDup(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split(" "))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", " ");
    }
   
 
}
