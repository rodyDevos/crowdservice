package com.gofer.rating;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.synapse.gofer.MapActivity;
import com.synapse.gofer.ParentActivity;
import com.synapse.gofer.R;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.util.Constants;

//import com.google.android.gms.internal.di;

public class RatingQuestionsActivity extends ParentActivity {

	private TextView question = null, question2 = null;
	private Button yesRadio = null, noRadio = null, yesRadio2 = null,
			noRadio2 = null, feedback_answer_done;
	private int currentDisplayedQuestion = 0;
	private ArrayList<RatingQuestion> questionList = null;
	Drawable img_select, img_unselect;

	private String jobId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.rating_questions_activity);

		iniUi();
		jobId = getIntent().getStringExtra("jobID");
		if (Constants.isNetAvailable(RatingQuestionsActivity.this)) {
			new GetQuestions().execute("");
		} else {
			Constants.NoInternetError(RatingQuestionsActivity.this);
		}
	}

	private void iniUi() {
		question = (TextView) findViewById(R.id.rating_question);
		yesRadio = (Button) findViewById(R.id.rating_answer_yes);
		noRadio = (Button) findViewById(R.id.rating_answer_no);

		question2 = (TextView) findViewById(R.id.rating_question2);
		yesRadio2 = (Button) findViewById(R.id.rating_answer_yes2);
		noRadio2 = (Button) findViewById(R.id.rating_answer_no2);

		feedback_answer_done = (Button) findViewById(R.id.feedback_answer_done);

		img_select = getResources().getDrawable(R.drawable.radio_button_sel);
		img_unselect = getResources()
				.getDrawable(R.drawable.radio_button_unsel);

		feedback_answer_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Constants.isNetAvailable(RatingQuestionsActivity.this)) {
					new SubmitAnswer().execute("");
				} else {
					Constants.NoInternetError(RatingQuestionsActivity.this);
				}
				startActivity(new Intent(RatingQuestionsActivity.this,
						MapActivity.class));
			}
		});

		yesRadio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				noRadio.setCompoundDrawablesWithIntrinsicBounds(img_unselect,
						null, null, null);
				yesRadio.setCompoundDrawablesWithIntrinsicBounds(img_select,
						null, null, null);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// manageAndSendAnswer(true, yesRadio);
						((RatingQuestion) questionList.get(0)).setAnswer("1");
					}
				}, 500);
			}
		});

		noRadio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				noRadio.setCompoundDrawablesWithIntrinsicBounds(img_select,
						null, null, null);
				yesRadio.setCompoundDrawablesWithIntrinsicBounds(img_unselect,
						null, null, null);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// manageAndSendAnswer(false, noRadio);
						((RatingQuestion) questionList.get(0)).setAnswer("0");
					}
				}, 500);
			}
		});

		yesRadio2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				noRadio2.setCompoundDrawablesWithIntrinsicBounds(img_unselect,
						null, null, null);
				yesRadio2.setCompoundDrawablesWithIntrinsicBounds(img_select,
						null, null, null);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// manageAndSendAnswer(true, yesRadio);
						((RatingQuestion) questionList.get(1)).setAnswer("1");
					}
				}, 500);
			}
		});

		noRadio2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				noRadio2.setCompoundDrawablesWithIntrinsicBounds(img_select,
						null, null, null);
				yesRadio2.setCompoundDrawablesWithIntrinsicBounds(img_unselect,
						null, null, null);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// manageAndSendAnswer(false, noRadio);
						((RatingQuestion) questionList.get(1)).setAnswer("0");
					}
				}, 500);
			}
		});

		TextView back = (TextView) findViewById(R.id.btnBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RatingProviderActivity.BACK);
				finish();
			}
		});

		questionList = new ArrayList<RatingQuestion>(5);
	}

	private void manageAndSendAnswer(boolean answer, Button button) {
		if (currentDisplayedQuestion < questionList.size())
			((RatingQuestion) questionList.get(currentDisplayedQuestion))
					.setAnswer(answer ? "1" : "0");

		currentDisplayedQuestion = currentDisplayedQuestion + 1;
		if (currentDisplayedQuestion < questionList.size()) {
			RatingQuestion ques = questionList.get(currentDisplayedQuestion);

			question.setText(ques.getQuestion());

			Drawable img = getResources().getDrawable(
					R.drawable.radio_button_unsel);
			button.setCompoundDrawablesWithIntrinsicBounds(img, null, null,
					null);
		} else {
			new SubmitAnswer().execute("");
		}
	}

	private class GetQuestions extends AsyncTask<String, Integer, String> {
		ProgressDialog progressBar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(RatingQuestionsActivity.this);
			progressBar.setMessage("Getting Questions");
			progressBar.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String url = Constants.HTTP_HOST + "getQuestion";
			HttpPostConnector conn = new HttpPostConnector(url, null);
			HttpClient client = new DefaultHttpClient();
			HttpGet httpRequest = new HttpGet(url);
			HttpResponse response;
			try {
				response = client.execute(httpRequest);
				if (response != null) {
					String responseJson = EntityUtils.toString(
							response.getEntity(), "UTF-8");

					JSONObject jsonObject = new JSONObject(responseJson);
					if (jsonObject.has("RatingQuestion")) {
						JSONArray jsonArray = jsonObject
								.getJSONArray("RatingQuestion");
						int count = jsonArray.length();
						for (int i = 0; i < count; i++) {
							RatingQuestion question = new RatingQuestion();
							question.setId(jsonArray.getJSONObject(i)
									.getString("id"));
							question.setQuestion(jsonArray.getJSONObject(i)
									.getString("question"));

							questionList.add(question);
						}
					}

				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Sucess";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.dismiss();
			if (questionList.size() > 0) {
				RatingQuestion ques = questionList.get(0);
				RatingQuestion ques1 = questionList.get(1);

				question.setText(ques.getQuestion());
				question2.setText(ques1.getQuestion());
			}

		}
	}

	private class SubmitAnswer extends AsyncTask<String, Integer, String> {
		ProgressDialog progressBar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(RatingQuestionsActivity.this);
			progressBar.setMessage("Submitting Answers");
			progressBar.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String url = Constants.HTTP_HOST + "save_rating_question";
			String responseJson = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
			nameValuePairs
					.add(new BasicNameValuePair("from_id", Constants.uid));
			
			nameValuePairs
			.add(new BasicNameValuePair("to_id", Constants.uid));
			nameValuePairs
			.add(new BasicNameValuePair("request_from", "android"));
			
			// JSONArray answerArray = new JSONArray();
			JSONObject mJsonObject1 = new JSONObject();
			int i = 1;
			for (RatingQuestion q : questionList) {
				try {
					JSONObject answer = new JSONObject();
					answer.put("question_id", q.getId());
					answer.put("answer", q.getAnswer());
					// answerArray.put(answer);
					mJsonObject1.put(String.valueOf(i), answer);
					i++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			JSONObject mJsonObject = new JSONObject();
			try {
				mJsonObject.put("question", mJsonObject1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				nameValuePairs.add(new BasicNameValuePair("rating_question",
						mJsonObject.toString()));
				Log.d("test ashish", "" + mJsonObject.toString() + " job_id  "
						+ jobId + "  from_id  " + Constants.uid);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPostConnector conn = new HttpPostConnector(url,
						nameValuePairs);
				HttpPost httppost = new HttpPost(url);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response;

				response = httpclient.execute(httppost);

				if (response != null) {
					responseJson = EntityUtils.toString(response.getEntity(),
							"UTF-8");
					Log.d("Response", responseJson);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseJson;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("Result", result);
			progressBar.dismiss();

			if (result != null && result.length() > 0) {
				try {
					JSONObject mJsonObject = new JSONObject(result);

					Log.d("result1", mJsonObject.toString());
					if (mJsonObject.has("status")
							&& !mJsonObject.getString("status")
									.equalsIgnoreCase("fail")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RatingQuestionsActivity.this);
						builder.setTitle("Gofer");
						builder.setMessage("Your answers submited successfully");
						builder.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										setResult(RatingProviderActivity.DONE);
										// startActivity(new
										// Intent(RatingQuestionsActivity.this,MapActivity.class));
										finish();
									}
								});
						builder.create().show();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RatingQuestionsActivity.this);
						builder.setTitle("Gofer");
						builder.setMessage("Your answers not submited successfully");
						builder.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}

				} catch (Exception e) {
					Log.d("asassaasas", "asasasasas");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
