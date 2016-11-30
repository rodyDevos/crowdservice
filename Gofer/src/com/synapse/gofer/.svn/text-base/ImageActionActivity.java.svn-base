package com.synapse.gofer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageActionActivity extends Activity implements OnClickListener{

	private ImageView imageView = null;
	public static Bitmap bitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_action);
		
		
		TextView back = (TextView)findViewById(R.id.btndelete);
		back.setOnClickListener(this);
		
		TextView done = (TextView)findViewById(R.id.btn_done);
		done.setOnClickListener(this);
		TextView title = (TextView)findViewById(R.id.btn_text);
		
		
		imageView = (ImageView)findViewById(R.id.job_image);
		if(bitmap !=null)
		imageView.setImageBitmap(bitmap);
		
		
		String txt = getIntent().getStringExtra("imgno");
		
		title.setText(txt);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_done)
		{
			finish();
			overridePendingTransition(0, R.anim.slide_top_to_bottom);
		}
		else if(v.getId() == R.id.btndelete)
		{
			
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);			 	     
			alertDialog.setTitle("Do you want to delete image");       	       
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which){
					finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
					ProfileJobsFragment.self.deleteImage(ProfileJobsFragment.clickedImageNumber);
					
				}
			});	 
			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to invoke NO event           
					dialog.dismiss();
				}
			});	 
			//Showing Alert Message
			alertDialog.show();
			
		}
	}
	

	
}
