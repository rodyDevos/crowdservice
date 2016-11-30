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
import org.json.JSONArray;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.util.Constants;

public class karmaquestion extends Activity implements OnClickListener {

	TextView rating_question, rating_question2;
	RadioButton rating_answer_yes, rating_answer_no, rating_answer_yes2,
			rating_answer_no2;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	JSONObject jsonresponse;
	Button btn_send_message;
	String user_type = "", from_user_id, job_id, to_user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.karmalayout);
		init();
		doFeedbackSubmit();
	}

	private void init() {
		rating_question = (TextView) findViewById(R.id.rating_question);
		rating_question2 = (TextView) findViewById(R.id.rating_question2);

		rating_answer_yes = (RadioButton) findViewById(R.id.rating_answer_yes);
		rating_answer_no = (RadioButton) findViewById(R.id.rating_answer_no);
		rating_answer_yes2 = (RadioButton) findViewById(R.id.rating_answer_yes2);
		rating_answer_no2 = (RadioButton) findViewById(R.id.rating_answer_no2);

		btn_send_message = (Button) findViewById(R.id.btn_send_message);

		rating_answer_no.setOnClickListener(this);
		rating_answer_no2.setOnClickListener(this);
		rating_answer_yes.setOnClickListener(this);
		rating_answer_yes2.setOnClickListener(this);
		btn_send_message.setOnClickListener(this);

		from_user_id = getIntent().getStringExtra("from_user_id");
		job_id = getIntent().getStringExtra("job_id");
		to_user_id = getIntent().getStringExtra("to_user_id");

		if (Constants.userType == 2) {
			user_type = "courier";
		} else {
			user_type = "customer";
		}
		CreateJson();
	}

	private void doFeedbackSubmit() {

		final ProgressDialog progressBar = new ProgressDialog(
				karmaquestion.this);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "getQuestion");
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
					jsonresponse = new JSONObject(strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

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

	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getMessage().equals("refund failed")) {
						showAlertDialog("get question failed.");
					} else {
						fillData();
						showAlertDialog(data.getMessage());
					}
					// finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
				} else {
					String message = (String) msg.obj;
					if (message.equals("refund failed")) {
						showAlertDialog("get question failed.");

					} else {
						fillData();
						showAlertDialog(message);
					}
					// finish();
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(karmaquestion.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void fillData() {
		try {
			// resultData.setMessage(jsonresponse.getString("message"));
			JSONArray RatingQuestionAraay = jsonresponse
					.getJSONArray("RatingQuestion");
			for (int i = 0; i < RatingQuestionAraay.length(); i++) {
				JSONObject ratingQuestionObject = RatingQuestionAraay
						.getJSONObject(i);
				if (i == 0) {
					rating_question.setText(RatingQuestionAraay
							.getJSONObject(i).getString("question"));
				} else {
					rating_question2.setText(RatingQuestionAraay.getJSONObject(
							i).getString("question"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void showAlertDialog(String s) {
		Context context = karmaquestion.this.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	@Override
	public void onClick(View v) {
		if (v == btn_send_message) {
			CreateJson();
			 doCompleteRequest();
		}
		if (v == rating_answer_yes) {
			if (!rating_answer_yes.isChecked() && !rating_answer_no.isChecked()) {

			} else {
				rating_answer_no.setChecked(false);
			}
		}
		if (v == rating_answer_no) {
			if (!rating_answer_yes.isChecked() && !rating_answer_no.isChecked()) {

			} else {
				rating_answer_yes.setChecked(false);
			}
		}

		if (v == rating_answer_yes2) {
			if (!rating_answer_yes2.isChecked()
					&& !rating_answer_no2.isChecked()) {

			} else {
				rating_answer_no2.setChecked(false);
			}
		}
		if (v == rating_answer_no2) {
			if (!rating_answer_yes2.isChecked()
					&& !rating_answer_no2.isChecked()) {

			} else {
				rating_answer_yes2.setChecked(false);
			}
		}

	}

	private void doCompleteRequest() {

		final ProgressDialog progressBar = new ProgressDialog(
				karmaquestion.this);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					reqEntity.addPart("user_type", new StringBody(user_type));
					// from_user_id, job_id, to_user_id
					reqEntity.addPart("from_user_id", new StringBody(
							getIntent().getStringExtra("from_user_id")));
					reqEntity.addPart("job_id", new StringBody(getIntent()
							.getStringExtra("job_id")));
					reqEntity.addPart("to_user_id", new StringBody(getIntent()
							.getStringExtra("to_user_id")));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "save_rating_question");
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
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_CANCELED, returnIntent);
					finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
				} else {
					String message = (String) msg.obj;
					if (message.equals("refund failed")) {
						showAlertDialog("Rating failed.");

					} else {
						showAlertDialog(message);
					}
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_CANCELED, returnIntent);
					finish();
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(karmaquestion.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void CreateJson() {
		JSONObject sub_parent = new JSONObject();
		JSONObject jsonObject = new JSONObject();

		// Question 1
		JSONObject question1 = new JSONObject();
		// Question 2
		JSONObject question2 = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		try {
			// Question Add
			question1.put("answer", "1");
			question1.put("question_id", "1");

			question2.put("answer", "0");
			question2.put("question_id", "2");

			jsonArray.put(question1);
			jsonArray.put(question2);

			sub_parent.put("from_id", "271");
			sub_parent.put("job_id", "529");

			jsonObject.put("question", jsonArray);

			jsonObject.put("request_from", "android");
			jsonObject.put("to_id", "287");

			sub_parent.put("rating_question", jsonObject);

			Log.e("VIPIII", "Json Test >> " + sub_parent.toString());
		} catch (Exception ex) {
		}
	}
}
