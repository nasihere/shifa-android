package com.shifa.kent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
 
public class activity_m_medica_tab_borieck extends Activity {
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
			
			value = extras.getString("medica_data");
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
	    		  SearchIndexOf =value.lastIndexOf("009900",SearchIndexOf);
	    		  if( SearchIndexOf == -1) return;
	    		  SearchIndexOf--;
	    		  int line = textView.getLayout().getLineForOffset(SearchIndexOf);
	    		  makeScroll(line); 
	    	  }
	
	    	});
			llButton.addView(btn1);
			    
		        
			Button btn2 = createNewButton("Next");
			btn2.setOnClickListener(new View.OnClickListener() {
		    	  @Override
		    	  public void onClick(View view) {
		    		 
		    		  SearchIndexOf =value.indexOf("009900",SearchIndexOf);
		    		  
		    		  if( SearchIndexOf == -1) return;
		    		  SearchIndexOf++;
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
            	
            	Log.e("Line no", String.valueOf(go));
            	setScrollSpot(go);
                //scroll.scrollTo(0, go * textView.getLineHeight());
            }
        });
    }
    private float getScrollSpot() {
        int y = scroll.getScrollY();
        Layout layout = textView.getLayout();
        int topPadding = -layout.getTopPadding();
        if (y <= topPadding) {
            return (float) (topPadding - y) / textView.getLineHeight();
        }

        int line = layout.getLineForVertical(y - 1) + 1;
        int offset = layout.getLineStart(line);
        int above = layout.getLineTop(line) - y;
        return offset + (float) above / textView.getLineHeight();
    }

    private void setScrollSpot(float spot) {
        int offset = (int) spot;
        int above = (int) ((spot - offset) * textView.getLineHeight());
        Layout layout = textView.getLayout();
        int line = layout.getLineForOffset(offset);
        int y = (line == 0 ? -layout.getTopPadding() : layout.getLineTop(line))
            - above;
        scroll.scrollTo(0, y);
    }

}