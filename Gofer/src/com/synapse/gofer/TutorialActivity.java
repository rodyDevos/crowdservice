package com.synapse.gofer;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.widget.Button;
import com.synapse.gofer.adapter.DescriptionPagerAdapter;

public class TutorialActivity extends Activity implements OnClickListener{

	private Button btnDone;
	private WebView webView;
	
	private String tutorialUrl = "http://m.crowdserviceinc.com/#!tutorial/x94q4";
	/**Called when the activity is first created.*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    // TODO Auto-generated method stub
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);
		
		initViews();
	}
	
	/*
	 * Called To initialize to user interface. 
	 */
	private void initViews()
	{		
		btnDone= (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);
		
		webView = (WebView) findViewById(R.id.webView1);
		
		webView.setWebViewClient(new MyBrowser());
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.loadUrl(tutorialUrl);
        
	}

	private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	   }
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==btnDone){
			finish();
		}
	}
		 

}
