package com.synapse.gofer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePhotoActivity extends Activity implements OnClickListener {

	private ImageView imgPhoto;
	private Bitmap bitmap;
	private TextView btnBack;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profilepoto);
		byte[] byteArray = getIntent().getByteArrayExtra("photo");
		bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		initViews();
	}

	/*
	 * Called to initialize to user interface.
	 */
	private void initViews() {
		btnBack = (TextView) findViewById(R.id.btnBack);
		imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
		btnBack.setOnClickListener(this);
		if (bitmap != null) {
			Drawable d = getResources().getDrawable(R.drawable.profile);
			int maxHeight = d.getIntrinsicHeight();
			int maxWidth = d.getIntrinsicWidth();
			Bitmap resized = Bitmap.createScaledBitmap(bitmap, maxWidth,
					maxHeight, true);
			imgPhoto.setMaxHeight(maxHeight);
			imgPhoto.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnBack) {
			finish();
		}
	}

}
