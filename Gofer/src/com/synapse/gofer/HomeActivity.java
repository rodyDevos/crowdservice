package com.synapse.gofer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class HomeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);	
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_home);
	}

}