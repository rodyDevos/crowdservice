package com.gofer.rating;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.synapse.gofer.MapActivity;
import com.synapse.gofer.ParentActivity;
import com.synapse.gofer.R;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.util.Constants;

public class RatingProviderActivity extends ParentActivity implements OnClickListener {

	private RatingBar ratingBar = null, ratingBar1 = null, ratingBar2 = null;
	private EditText feedback = null;
	private Button done = null;
	private Button btnLater = null;
	private String jobId = "10";
	private String toRateId = "17";
	public static final int BACK = 101;
	public static final int DONE = 100;
	public ImageView rating_star11, rating_star12, rating_star13,
			rating_star14, rating_star15;
	public ImageView rating_star21, rating_star22, rating_star23,
			rating_star24, rating_star25;
	public ImageView rating_star31, rating_star32, rating_star33,
			rating_star34, rating_star35;
	public int rating = 0, rating2 = 0, rating3 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.rating_activity);
		initUi();
	}

	private void initUi() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.rating_for_user_layout);
		rating_star11 = (ImageView) ll.findViewById(R.id.rating_star1);
		rating_star12 = (ImageView) ll.findViewById(R.id.rating_star2);
		rating_star13 = (ImageView) ll.findViewById(R.id.rating_star3);
		rating_star14 = (ImageView) ll.findViewById(R.id.rating_star4);
		rating_star15 = (ImageView) ll.findViewById(R.id.rating_star5);

		rating_star11.setOnClickListener(this);
		rating_star12.setOnClickListener(this);
		rating_star13.setOnClickListener(this);
		rating_star14.setOnClickListener(this);
		rating_star15.setOnClickListener(this);

		LinearLayout ll2 = (LinearLayout) findViewById(R.id.rating_for_user_layout2);
		rating_star21 = (ImageView) ll2.findViewById(R.id.rating_star1);
		rating_star22 = (ImageView) ll2.findViewById(R.id.rating_star2);
		rating_star23 = (ImageView) ll2.findViewById(R.id.rating_star3);
		rating_star24 = (ImageView) ll2.findViewById(R.id.rating_star4);
		rating_star25 = (ImageView) ll2.findViewById(R.id.rating_star5);

		rating_star21.setOnClickListener(this);
		rating_star22.setOnClickListener(this);
		rating_star23.setOnClickListener(this);
		rating_star24.setOnClickListener(this);
		rating_star25.setOnClickListener(this);

		LinearLayout ll3 = (LinearLayout) findViewById(R.id.rating_for_user_layout3);
		rating_star31 = (ImageView) ll3.findViewById(R.id.rating_star1);
		rating_star32 = (ImageView) ll3.findViewById(R.id.rating_star2);
		rating_star33 = (ImageView) ll3.findViewById(R.id.rating_star3);
		rating_star34 = (ImageView) ll3.findViewById(R.id.rating_star4);
		rating_star35 = (ImageView) ll3.findViewById(R.id.rating_star5);

		rating_star31.setOnClickListener(this);
		rating_star32.setOnClickListener(this);
		rating_star33.setOnClickListener(this);
		rating_star34.setOnClickListener(this);
		rating_star35.setOnClickListener(this);

		// ratingBar = (RatingBar)findViewById(R.id.rating_for_user);
		// ratingBar1 = (RatingBar)findViewById(R.id.rating_for_user2);
		// ratingBar2 = (RatingBar)findViewById(R.id.rating_for_user3);
		feedback = (EditText) findViewById(R.id.user_feedback);
		done = (Button) findViewById(R.id.feedback_done);

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constants.isNetAvailable(RatingProviderActivity.this)) {
					new SendFeedback().execute("");
				} else {
					Constants.NoInternetError(RatingProviderActivity.this);
				}
			}
		});

		btnLater = (Button) findViewById(R.id.feedback_later);

		btnLater.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(RatingProviderActivity.this,
						MapActivity.class));
				
				finish();
			}
		});
		TextView back = (TextView) findViewById(R.id.btnBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		jobId = getIntent().getStringExtra("completedJobId");
		toRateId = getIntent().getStringExtra("completedJobuserId");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == rating_star11) {
			rating = 1;
			rating_star11.setBackgroundResource(R.drawable.star_highlighted);
			rating_star12.setBackgroundResource(R.drawable.star);
			rating_star13.setBackgroundResource(R.drawable.star);
			rating_star14.setBackgroundResource(R.drawable.star);
			rating_star15.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star12) {
			rating = 2;
			rating_star11.setBackgroundResource(R.drawable.star_highlighted);
			rating_star12.setBackgroundResource(R.drawable.star_highlighted);
			rating_star13.setBackgroundResource(R.drawable.star);
			rating_star14.setBackgroundResource(R.drawable.star);
			rating_star15.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star13) {
			rating = 3;
			rating_star11.setBackgroundResource(R.drawable.star_highlighted);
			rating_star12.setBackgroundResource(R.drawable.star_highlighted);
			rating_star13.setBackgroundResource(R.drawable.star_highlighted);
			rating_star14.setBackgroundResource(R.drawable.star);
			rating_star15.setBackgroundResource(R.drawable.star);
		} else if (v == rating_star14) {
			rating = 4;
			rating_star11.setBackgroundResource(R.drawable.star_highlighted);
			rating_star12.setBackgroundResource(R.drawable.star_highlighted);
			rating_star13.setBackgroundResource(R.drawable.star_highlighted);
			rating_star14.setBackgroundResource(R.drawable.star_highlighted);
			rating_star15.setBackgroundResource(R.drawable.star);
		} else if (v == rating_star15) {
			rating = 5;
			rating_star11.setBackgroundResource(R.drawable.star_highlighted);
			rating_star12.setBackgroundResource(R.drawable.star_highlighted);
			rating_star13.setBackgroundResource(R.drawable.star_highlighted);
			rating_star14.setBackgroundResource(R.drawable.star_highlighted);
			rating_star15.setBackgroundResource(R.drawable.star_highlighted);

		} else if (v == rating_star21) {
			rating2 = 1;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star);
			rating_star23.setBackgroundResource(R.drawable.star);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star22) {
			rating2 = 2;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star23) {
			rating2 = 3;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);
		} else if (v == rating_star24) {
			rating2 = 4;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star_highlighted);
			rating_star25.setBackgroundResource(R.drawable.star);
		} else if (v == rating_star25) {
			rating2 = 5;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star_highlighted);
			rating_star25.setBackgroundResource(R.drawable.star_highlighted);

		} else if (v == rating_star31) {
			rating3 = 1;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star);
			rating_star33.setBackgroundResource(R.drawable.star);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star32) {
			rating3 = 2;

			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star33) {
			rating3 = 3;

			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star34) {
			rating3 = 4;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star_highlighted);
			rating_star35.setBackgroundResource(R.drawable.star);

		} else if (v == rating_star35) {
			rating3 = 5;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star_highlighted);
			rating_star35.setBackgroundResource(R.drawable.star_highlighted);
		}

	}

	private class SendFeedback extends AsyncTask<String, Integer, String> {
		ProgressDialog progressBar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(RatingProviderActivity.this);
			progressBar.setMessage("Sending Feedback");
			progressBar.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String url = Constants.HTTP_HOST + "rateUsers";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
			nameValuePairs.add(new BasicNameValuePair("user_type", String
					.valueOf(Constants.userType)));
			nameValuePairs.add(new BasicNameValuePair("from_rate_id",
					Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("to_rate_id", toRateId));
			nameValuePairs
					.add(new BasicNameValuePair("promptrate", "" + rating));
			nameValuePairs.add(new BasicNameValuePair("professionrate", ""
					+ rating2));
			nameValuePairs.add(new BasicNameValuePair("communicationrate", ""
					+ rating3));
			nameValuePairs.add(new BasicNameValuePair("feedback", String
					.valueOf(feedback.getText().toString())));
			nameValuePairs.add(new BasicNameValuePair("date", Constants
					.getCurrentDateTimeYYMMDD()));

			HttpPostConnector conn = new HttpPostConnector(url, nameValuePairs);
			String responseJson = conn.getResponseData();

			/*
			 * HttpClient client = new DefaultHttpClient(); HttpGet httpRequest
			 * = new HttpGet(url); HttpResponse response; try { response =
			 * client.execute(httpRequest); if(response != null) { String
			 * responseJson = EntityUtils.toString(response.getEntity(),
			 * "UTF-8"); Log.d("responseJson",""+responseJson); } } catch
			 * (ClientProtocolException e) { e.printStackTrace();
			 * 
			 * } catch (IOException e) { e.printStackTrace();
			 * 
			 * } catch (Exception e) { e.printStackTrace();
			 * 
			 * }
			 */

			return "Sucess";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.dismiss();

			Intent intent = new Intent(RatingProviderActivity.this,
					RatingQuestionsActivity.class);
			intent.putExtra("jobID", jobId);
			startActivityForResult(intent, 200);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 200) {
			if (resultCode == DONE)
				finish();
		}
	}

}
