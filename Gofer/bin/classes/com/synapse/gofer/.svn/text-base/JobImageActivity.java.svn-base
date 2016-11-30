package com.synapse.gofer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.synapse.gofer.util.ImageLoader;

public class JobImageActivity extends Activity implements OnClickListener {

	private ImageView imageView = null;
	private ProgressBar bar = null;
	private String imageUrl = "";
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_job_image);

		TextView back = (TextView) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);

		title.setText(getIntent().getStringExtra("job_name"));

		bar = (ProgressBar) findViewById(R.id.progress_bar);
		imageView = (ImageView) findViewById(R.id.job_image);

		imageUrl = getIntent().getStringExtra("job_image_url");

		ImageLoader loadr = ImageLoader.getInstance(this);

		if (imageUrl.contains("http")) {
			loadr.displayImage(imageUrl, imageView, bar);
		} else {
			loadr.displayImage("file://" + imageUrl, imageView, bar);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnBack) {
			finish();
			overridePendingTransition(0, R.anim.slide_top_to_bottom);
		}
	}

}
