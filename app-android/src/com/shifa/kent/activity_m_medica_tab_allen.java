package com.shifa.kent;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
 
public class activity_m_medica_tab_allen extends Activity {
    /** Called when the activity is first created. */
	ScrollView scroll;
	TextView textView;
	int SearchIndexOf = 0;
	String value = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 

        Bundle extras = getIntent().getExtras();
		String SearchKeyWord = "";
		
		if (extras != null) {
			
			SearchKeyWord = extras.getString("SearchKeyWord");
			
			if (!SearchKeyWord.equals(""))
			{
				SearchKeyWord = SearchKeyWord.trim();
				SearchKeyWord  = SearchKeyWord.replace(",", " ");
				SearchKeyWord = SearchKeyWord.replace("*,*", " ");
				SearchKeyWord = SearchKeyWord.replace("*", "");
			}
			String ssk[] = SearchKeyWord.split(" ");
			
			value = extras.getString("allen_data");
			if (!SearchKeyWord.equals(""))
			{
				for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
				{
					if (!ssk[iMulC].trim().equals("")) 
					{
						value = value.toLowerCase().replaceAll(ssk[iMulC].toLowerCase(),
							"<b><font size='12' color='#009900'>" + ssk[iMulC] + "</font></b>");
					
					}
				}
			}
		
		}
        /* First Tab Content */
         scroll = new ScrollView(this);
         textView = new TextView(this);
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new  ScrollingMovementMethod().getInstance());
        textView.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);
        scroll.addView(textView);
        setContentView(scroll);
/* Main Layout button */
		
		
		LinearLayout ll  = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		/************** Button Search */
		if (value.indexOf("009900") != -1)
		{
			LinearLayout llButton  = new LinearLayout(this);	
			llButton.setOrientation(LinearLayout.HORIZONTAL);
			Button btn1 = createNewButton("Prev");
			 
			btn1.setOnClickListener(new View.OnClickListener() {
	    	  @Override
	    	  public void onClick(View view) {
	    		  SearchIndexOf =value.lastIndexOf("009900",SearchIndexOf-1);
	    		  if( SearchIndexOf == -1) return;
	    		  int line = textView.getLayout().getLineForOffset(SearchIndexOf);
	    		  makeScroll(line); 
	    	  }
	
	    	});
			llButton.addView(btn1);
			    
		        
			Button btn2 = createNewButton("Next");
			btn2.setOnClickListener(new View.OnClickListener() {
		    	  @Override
		    	  public void onClick(View view) {
		    		  SearchIndexOf =value.indexOf("009900",SearchIndexOf+1);
		    		  if( SearchIndexOf == -1) return;
		    		  int line = textView.getLayout().getLineForOffset(SearchIndexOf);
		    		  makeScroll(line);
		    			
		    	  }
	
		    	});
			        
			 
			llButton.addView(btn2);
		//	ll.addView(llButton);
		}
		/*********************************/
        /* First Tab Content */;
        LinearLayout llScroll  = new LinearLayout(this);
        scroll = new ScrollView(this);
         textView = new TextView(this);
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new  ScrollingMovementMethod().getInstance());
        textView.setText(Html.fromHtml(value), TextView.BufferType.SPANNABLE);

        scroll.addView(textView);
        llScroll.addView(scroll);
        ll.addView(llScroll);
        setContentView(ll);
        
       
        
        
    }

    private Button createNewButton(String btnCaption) {
        final LayoutParams lparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final Button button = new Button(this);
        button.setLayoutParams(lparams);
        button.setText(btnCaption);
        return button;
    }
    private void makeScroll(final int go) {
        scroll.post(new Runnable() {
            public void run() {
                scroll.scrollTo(0, go * textView.getLineHeight());
            }
        });
    }
}