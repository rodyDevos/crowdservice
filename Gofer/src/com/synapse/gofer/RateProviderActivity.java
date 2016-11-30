package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.util.Constants;

public class RateProviderActivity extends ParentActivity implements OnClickListener {

	public ImageView rating_star21, rating_star22, rating_star23,
			rating_star24, rating_star25;
	public ImageView rating_star31, rating_star32, rating_star33,
			rating_star34, rating_star35;
	public ImageView rating_star41, rating_star42, rating_star43,
			rating_star44, rating_star45;
	public int rating2 = 0, rating3 = 0, rating4 = 0;
	Button btn_send_message;
	EditText user_feedback;
	public int Result_ok = 101;
	String user_type;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratecustomerlayout);

		init();
	}

	private void init() {

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

		LinearLayout ll4 = (LinearLayout) findViewById(R.id.rating_for_user_layout4);
		rating_star41 = (ImageView) ll4.findViewById(R.id.rating_star1);
		rating_star42 = (ImageView) ll4.findViewById(R.id.rating_star2);
		rating_star43 = (ImageView) ll4.findViewById(R.id.rating_star3);
		rating_star44 = (ImageView) ll4.findViewById(R.id.rating_star4);
		rating_star45 = (ImageView) ll4.findViewById(R.id.rating_star5);

		rating_star41.setOnClickListener(this);
		rating_star42.setOnClickListener(this);
		rating_star43.setOnClickListener(this);
		rating_star44.setOnClickListener(this);
		rating_star45.setOnClickListener(this);

		btn_send_message = (Button) findViewById(R.id.btn_send_message);
		user_feedback = (EditText) findViewById(R.id.user_feedback);
		btn_send_message.setOnClickListener(this);

		if (Constants.userType == 2) {
			user_type = "courier";
		} else {
			user_type = "customer";
		}

	}

	@Override
	public void onClick(View v) {

		if (v == rating_star21) {
			rating2 = 1;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star);
			rating_star23.setBackgroundResource(R.drawable.star);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star22) {
			rating2 = 2;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star23) {
			rating2 = 3;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star);
			rating_star25.setBackgroundResource(R.drawable.star);
		}
		if (v == rating_star24) {
			rating2 = 4;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star_highlighted);
			rating_star25.setBackgroundResource(R.drawable.star);
		}
		if (v == rating_star25) {
			rating2 = 5;
			rating_star21.setBackgroundResource(R.drawable.star_highlighted);
			rating_star22.setBackgroundResource(R.drawable.star_highlighted);
			rating_star23.setBackgroundResource(R.drawable.star_highlighted);
			rating_star24.setBackgroundResource(R.drawable.star_highlighted);
			rating_star25.setBackgroundResource(R.drawable.star_highlighted);

		}
		if (v == rating_star31) {
			rating3 = 1;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star);
			rating_star33.setBackgroundResource(R.drawable.star);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star32) {
			rating3 = 2;

			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star33) {
			rating3 = 3;

			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star);
			rating_star35.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star34) {
			rating3 = 4;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star_highlighted);
			rating_star35.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star35) {
			rating3 = 5;
			rating_star31.setBackgroundResource(R.drawable.star_highlighted);
			rating_star32.setBackgroundResource(R.drawable.star_highlighted);
			rating_star33.setBackgroundResource(R.drawable.star_highlighted);
			rating_star34.setBackgroundResource(R.drawable.star_highlighted);
			rating_star35.setBackgroundResource(R.drawable.star_highlighted);
		}
		if (v == rating_star41) {
			rating4 = 1;
			rating_star41.setBackgroundResource(R.drawable.star_highlighted);
			rating_star42.setBackgroundResource(R.drawable.star);
			rating_star44.setBackgroundResource(R.drawable.star);
			rating_star44.setBackgroundResource(R.drawable.star);
			rating_star45.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star42) {
			rating4 = 2;

			rating_star41.setBackgroundResource(R.drawable.star_highlighted);
			rating_star42.setBackgroundResource(R.drawable.star_highlighted);
			rating_star43.setBackgroundResource(R.drawable.star);
			rating_star44.setBackgroundResource(R.drawable.star);
			rating_star45.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star43) {
			rating4 = 4;

			rating_star41.setBackgroundResource(R.drawable.star_highlighted);
			rating_star42.setBackgroundResource(R.drawable.star_highlighted);
			rating_star43.setBackgroundResource(R.drawable.star_highlighted);
			rating_star44.setBackgroundResource(R.drawable.star);
			rating_star45.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star44) {
			rating4 = 4;
			rating_star41.setBackgroundResource(R.drawable.star_highlighted);
			rating_star42.setBackgroundResource(R.drawable.star_highlighted);
			rating_star43.setBackgroundResource(R.drawable.star_highlighted);
			rating_star44.setBackgroundResource(R.drawable.star_highlighted);
			rating_star45.setBackgroundResource(R.drawable.star);

		}
		if (v == rating_star45) {
			rating4 = 5;
			rating_star41.setBackgroundResource(R.drawable.star_highlighted);
			rating_star42.setBackgroundResource(R.drawable.star_highlighted);
			rating_star43.setBackgroundResource(R.drawable.star_highlighted);
			rating_star44.setBackgroundResource(R.drawable.star_highlighted);
			rating_star45.setBackgroundResource(R.drawable.star_highlighted);
		}

		if (v == btn_send_message) {
			doCompleteRequest();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_CANCELED) {
			finish();
		}
	}

	private void doCompleteRequest() {

		final ProgressDialog progressBar = new ProgressDialog(
				RateProviderActivity.this);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					reqEntity.addPart("communicationrate", new StringBody(
							rating4 + ""));
					reqEntity.addPart("feedback", new StringBody(user_feedback
							.getText().toString() + ""));
					reqEntity.addPart("professionrate", new StringBody(rating3
							+ ""));
					reqEntity.addPart("promptrate",
							new StringBody(rating2 + ""));
					reqEntity.addPart("user_type", new StringBody(user_type));
					// from_user_id, job_id, to_user_id
					reqEntity.addPart("from_rate_id", new StringBody(
							getIntent().getStringExtra("from_user_id")));
					reqEntity.addPart("job_id", new StringBody(getIntent()
							.getStringExtra("job_id")));
					reqEntity.addPart("to_rate_id", new StringBody(getIntent()
							.getStringExtra("to_user_id")));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("VIPIII", "E>" + e.toString());
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "rateUsers");
				httppost.setEntity(reqEntity);
				// Execute HTTP Post Request
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					Log.d("VIPIII", "RES> " + strResponse);
					JSONObject jsonresponse = new JSONObject(strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressBar.dismiss();

				Message msg = handler1.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler1.sendMessage(msg);
			}
		});
		thr.start();
	}

	/*
	 * Handler.
	 */
	private Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getMessage().equals("refund failed")) {
						showAlertDialog("Rating failed.");
					} else {
						showAlertDialog(data.getMessage());
					}
					// Intent returnIntent = new Intent();
					// setResult(Activity.RESULT_CANCELED, returnIntent);
					// finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
				} else {
					String message = (String) msg.obj;
					if (message.equals("refund failed")) {
						showAlertDialog("Rating failed.");

					} else {
						showAlertDialog(message);
					}
					// Intent returnIntent = new Intent();
					// setResult(Activity.RESULT_CANCELED, returnIntent);
					// finish();
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(RateProviderActivity.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}

	private void showAlertDialog(String s) {
		Context context = RateProviderActivity.this.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();

		Intent intent = new Intent(RateProviderActivity.this,
				karmaquestion.class);

		intent.putExtra("from_user_id",
				getIntent().getStringExtra("from_user_id"));
		intent.putExtra("job_id", getIntent().getStringExtra("job_id"));
		intent.putExtra("to_user_id", getIntent().getStringExtra("to_user_id"));

		startActivityForResult(intent, Result_ok);
	}

}
