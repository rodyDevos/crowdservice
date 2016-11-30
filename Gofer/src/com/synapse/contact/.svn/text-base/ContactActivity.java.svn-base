package com.synapse.contact;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gofer.rating.RatingProviderActivity;
import com.gofer.rating.RatingCustomerActivity;
import com.synapse.gofer.MapActivity;
import com.synapse.gofer.PaypalWebviewAcivity;
import com.synapse.gofer.R;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.parser.FundPostParser;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;
import com.synapse.gofer.util.Utils;

public class ContactActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	final static private String TAG = "ContactActivity";

	private ImageView imageView = null;
	private Button one = null, two = null, three = null, four = null,
			five = null, completeTransaction = null;
	private TextView miles, contact_title, txtChooseJob, txtJobName = null;
	private EditText enterText = null;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SUCCESS_1 = 3;
	public static final int FAILURE_1 = 4;
	private RatingBar ratingBar = null;
	private RatingBar ratingBar1 = null;
	private RatingBar ratingBar2 = null;
	private RatingBar ratingBar3 = null;
	private SeekBar cancelSeekbar = null;
	private String uid = "0";
	private String userName = "";
	private String jobid = "0";
	private ArrayList jobsModelList = null;
	private String message = "", shieldType = "blue";
	private ImageView camera, txtshield;

	private ArrayList<JobBean> jobsArray = null;
	ArrayList<UserDetail> userarray = null;

	private boolean paypalShow = false;
	private String mExtraAmount = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("VIP", "ContactActivity");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact);
		iniUi();

	}

	private void needExpenseStatus() {
		{
			Thread thr = new Thread(new Runnable() {
				// @Override
				public void run() {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

					nameValuePairs.add(new BasicNameValuePair("job_id", jobid));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							Constants.uid));

					DataPostParser parser = new DataPostParser(
							Constants.HTTP_HOST + "extraAmountStatus");
					ResultData postdata1 = parser.getParseData(nameValuePairs);
					Message msg = handler.obtainMessage();
					if ((postdata1.getAuthenticated().equals("success"))) {

						msg.obj = postdata1;
						Log.d("as", "as" + msg.obj);
						msg.arg1 = SUCCESS;

					} else if ((postdata1.getAuthenticated().equals("fail"))) {

						msg.arg1 = FAILURE;

					}
					handler.sendMessage(msg);
				}
			});
			thr.start();
		}
	}

	private void checkStatus(final int from) {
		{
			Thread thr = new Thread(new Runnable() {
				// @Override
				public void run() {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					Log.d("a1s", "1as");
					nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

					DataPostParser parser = new DataPostParser(
							Constants.HTTP_HOST + "needExpenseStatus");
					ResultData postdata1 = parser.getParseData(nameValuePairs);
					Log.d("a1s", "1as" + postdata1);
					Message msg = handler.obtainMessage();
					if ((postdata1.getAuthenticated().equals("success"))) {

						msg.obj = postdata1;
						Log.d("as", "as" + msg.obj);
						msg.arg1 = SUCCESS;
						msg.arg2 = from;

					} else if ((postdata1.getAuthenticated().equals("fail"))) {

						msg.obj = postdata1;
						Log.d("as", "as" + msg.obj);
						msg.arg1 = FAILURE;
						msg.arg2 = from;
					}
					handler.sendMessage(msg);
				}
			});
			thr.start();
		}
	}

	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);

		toast.show();
	}

	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d("a1s2", "1as2" + msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getMessage().equals("Extra Amount Needed")) {
						mExtraAmount = data.getAmount();
						dailogbox(
								data.getMessage(),
								"Your Provider Needed Some Extra Amount of "
										+ data.getAmount() + " for "
										+ data.getReason());
						// showAlertDialog("Extra Amount Needed");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals("over Amount Needed")) {
						// dailogbox(data.getMessage(),"Your Provider Want to return Amount of "+data.getAmount()+" for "+data.getReason());
						dailogbox(
								data.getMessage(),
								"Your provider returned amount of "
										+ data.getAmount() + " for "
										+ data.getReason());
						// showAlertDialog("over Amount Needed");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals("NeedExpenses Active")) {
						int from = msg.arg2;
						if (from == 4) {
							createIntent(Gravity.CENTER, false,
									"" + four.getText(), true);
						} else if (from == 5) {
							createIntent(Gravity.CENTER, false,
									"" + five.getText(), true);
						}
					} else if (data.getMessage().equals("Extra Amount Charged")) {
						showAlertDialog("You have successfully charged the amount");
						// }else if
						// (data.getMessage().equals("Recipient Id found ")) {
						// // STRIPE
						// transferOverPayAmount();
					} else if (data.getMessage().equals("Paypal Id found")) { // PAYPAL
						transferOverPayAmount();
						// showAlertDialog("You have successfully charged the amount");
					} else if (data.getMessage().equals(
							"Bank money transfer successfully.")) {
						showAlertDialog("Bank money transfer successfully");

					} else if (data.getMessage().equals(
							"PayPal money transfer successfully.")) {
						showAlertDialog("PayPal money transfer successfully.");
						updateJob(data.getUserid());
					}
				}
			} else if (msg.arg1 == FAILURE) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getMessage().equals("NeedExpenses Deactive")) {
						showAlertDialog("You have already sent request for Need Expensive");
					} else if (data.getMessage().equals(
							"Extra Amount not Needed")) {
						showAlertDialog("Extra Amount not Needed");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals(
							"Customer id not found.")) {
						showAlertDialog("Please register your credit card first");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals("No Result Found")) {
						// showAlertDialog("Extra Amount not Needed");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals("account not Charged.")) {
						showAlertDialog("account not Charged.");
						// showAlertDialog("Extra Amount not Needed");
						// createIntent(Gravity.CENTER,false,""+four.getText(),true);
					} else if (data.getMessage().equals(
							"Recipient Id Not found.")) {
						// showAlertDialog("You have successfully charged the amount");

					} else if (data.getMessage().equals(
							"Bank money not transfer.")) {
						showAlertDialog("Bank money not transfer.");

					} else if (data.getMessage().equals(
							"PayPal money not transfer.")) {
						showAlertDialog("PayPal money not transfer.");
					}

				}
			}
		}
	};

	private void updateJob(final String job_id) {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "updateOverPayStatus");
				parser.getParseData(nameValuePairs);
			}
		});
		thr.start();

	}

	private void ExtraAmountCharge() {
		if (!paypalShow) {
			paypalShow = true;
		} else {
			return;
		}
		// ProgressDialog progressBar = new ProgressDialog(this);
		// progressBar.setCancelable(false);
		// progressBar.setMessage("Please wait while loading...");
		// progressBar.show();

		try {
			if (Constants.isNetAvailable(ContactActivity.this)) {
				ServerCommunicateFund servicefund = new ServerCommunicateFund();
				servicefund.execute();
			} else {
				Constants.NoInternetError(ContactActivity.this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) {
	 * 
	 * if (requestCode == Constants.PAYPAL_REQUEST_CODE) { switch (resultCode) {
	 * case Activity.RESULT_OK: String payKey = data
	 * .getStringExtra(PayPalActivity.EXTRA_PAY_KEY); Log.d(TAG, "PayKey: " +
	 * payKey);
	 * 
	 * ExtraAmountChargeContinue(payKey); break; case Activity.RESULT_CANCELED:
	 * break; case PayPalActivity.RESULT_FAILURE: String errorID = data
	 * .getStringExtra(PayPalActivity.EXTRA_ERROR_ID); String errorMessage =
	 * data .getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE); Log.d(TAG,
	 * "Error: " + errorMessage); } paypalShow = false; } }
	 */

	private void ExtraAmountChargeContinue(final String payKey) {

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("user_id", ""
						+ Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("job_id", jobid));
				nameValuePairs.add(new BasicNameValuePair("pay_key", payKey));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "chargeExtraAmount");
				ResultData parser_data = parser.getParseData(nameValuePairs);
				Log.d("parser_data",
						"parser_data" + parser_data.getAuthenticated());
				Log.d("parser_data", "parser_data" + parser_data.getMessage());
				Message msg = handler.obtainMessage();
				if ((parser_data.getAuthenticated().equals("success"))) {

					msg.obj = parser_data;
					Log.d("as", "as" + msg.obj);
					msg.arg1 = SUCCESS;

				} else if ((parser_data.getAuthenticated().equals("fail"))) {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	private void transferOverPayAmount() {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				Log.d("assss", "sssas");
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("user_id", ""
						+ Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "transferOverPayAmount");
				ResultData parser_data = parser.getParseData(nameValuePairs);
				Log.d("parser_data",
						"parser_data" + parser_data.getAuthenticated());
				Log.d("parser_data", "parser_data" + parser_data.getMessage());
				Message msg = handler.obtainMessage();
				if ((parser_data.getAuthenticated().equals("success"))) {

					msg.obj = parser_data;
					Log.d("as", "as" + msg.obj);
					msg.arg1 = SUCCESS;

				} else if ((parser_data.getAuthenticated().equals("fail"))) {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	private void CheckRecipientID() {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("user_id", ""
						+ Constants.uid));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "getPaypalId");
				ResultData parser_data = parser.getParseData(nameValuePairs);
				Log.d("parser_data",
						"parser_data" + parser_data.getAuthenticated());
				Log.d("parser_data", "parser_data" + parser_data.getMessage());
				Message msg = handler.obtainMessage();
				if ((parser_data.getAuthenticated().equals("success"))) {
					msg.obj = parser_data;
					msg.arg1 = SUCCESS;
				} else if ((parser_data.getAuthenticated().equals("fail"))) {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	private void iniUi() {
		((TextView) findViewById(R.id.contact_btnBack))
				.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.contact_profile_image);
		miles = (TextView) findViewById(R.id.contact_miles);
		ratingBar = (RatingBar) findViewById(R.id.contact_rating_bar);
		/*
		 * ratingBar1 =(RatingBar)findViewById(R.id.ratingBar1); ratingBar2
		 * =(RatingBar)findViewById(R.id.ratingBar2); ratingBar3
		 * =(RatingBar)findViewById(R.id.ratingBar3);
		 */
		txtshield = (ImageView) findViewById(R.id.txtshield);
		contact_title = (TextView) findViewById(R.id.contact_title);
		enterText = (EditText) findViewById(R.id.contact_entertext);
		txtChooseJob = (TextView) findViewById(R.id.txtChooseJob);
		txtChooseJob.setOnClickListener(this);
		txtJobName = (TextView) findViewById(R.id.txtJobName);
		one = (Button) findViewById(R.id.contact_one);
		two = (Button) findViewById(R.id.contact_two);
		three = (Button) findViewById(R.id.contact_three);
		four = (Button) findViewById(R.id.contact_four);
		five = (Button) findViewById(R.id.contact_five);
		camera = (ImageView) findViewById(R.id.camera);
		completeTransaction = (Button) findViewById(R.id.contact_complete_transaction);
		completeTransaction.setOnClickListener(this);
		cancelSeekbar = (SeekBar) findViewById(R.id.contact_seekbar_cancel_transaction);
		cancelSeekbar.setOnSeekBarChangeListener(this);

		ratingBar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		if (Constants.userType == 2) {

			((TextView) findViewById(R.id.contact_title))
					.setText("Contact Courier");
			one.setText("Hello?");
			two.setText("Change Merchandize");
			three.setText("Send Payment");
			four.setText("Change Location");
			five.setVisibility(View.GONE);
		} else if (Constants.userType == 3) {
			((TextView) findViewById(R.id.contact_title))
					.setText("Contact Customer");
			one.setText("En Route");
			two.setText("Merch Purchased");
			three.setText("Running Late");
			four.setText("Need Expenses");
			five.setText("Return Payment");
		}

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		enterText.setOnClickListener(this);
		camera.setOnClickListener(this);

		userName = getIntent().getStringExtra("UserName");
		uid = getIntent().getStringExtra("UserID");

		shieldType = getIntent().getStringExtra("ShieldType");
		if (shieldType.equals("blue"))
			txtshield.setImageResource(R.drawable.shieldx);
		else if (shieldType.equals("silver"))
			txtshield.setImageResource(R.drawable.shield_silver);
		else if (shieldType.equals("gold"))
			txtshield.setImageResource(R.drawable.shield_gold);

		/*
		 * int[] viewLocation = new int[2]; int[] viewLocation1 = new int[2];
		 * txtshield.getLocationInWindow(viewLocation);
		 * txtshield.getLocationOnScreen(viewLocation1);
		 * 
		 * txtshield.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		 * 
		 * Log.d("WHIELS WIDRTH"+txtshield.getMeasuredHeightAndState(),"A "+
		 * txtshield.getMeasuredWidth());
		 * Log.d("WHIELS getScrollX"+viewLocation[0],"A "+viewLocation[1]);
		 * 
		 * Log.d("WHIELS getScrollX"+viewLocation1[0],"A "+viewLocation1[1]);
		 */

		String imagePath = getIntent().getStringExtra("profileimage");
		userarray = new ArrayList<UserDetail>();
		jobsArray = new ArrayList<JobBean>();
		contact_title.setText(userName);
		if (Constants.isNetAvailable(ContactActivity.this)) {
			new ViewJobProfile().execute();
		} else {
			Constants.NoInternetError(ContactActivity.this);
		}

		try {
			if (imagePath != null && imagePath != "") {
				Bitmap bitmap = ImageProcessing.downloadImage(imagePath);
				if (bitmap != null)
					imageView.setImageBitmap(bitmap);

			}
		} catch (Exception e) {

		}

	}

	@Override
	public void onClick(View v) {
		if (v == one) {
			createIntent(Gravity.CENTER, false, "" + one.getText(), false);
		} else if (v == two) {
			createIntent(Gravity.CENTER, false, "" + two.getText(), false);
		} else if (v == three) {
			createIntent(Gravity.CENTER, false, "" + three.getText(), false);
		} else if (v == four) {
			if (Constants.userType == 2)
				createIntent(Gravity.CENTER, false, "" + four.getText(), false);
			else
				checkStatus(4);
			// createIntent(Gravity.CENTER,false,""+four.getText(),true);
		} else if (v == five) {
			checkStatus(5);
			// createIntent(Gravity.CENTER,false,""+five.getText(),true);
		} else if (v.getId() == R.id.camera) {
			createIntent(Gravity.BOTTOM, true, "" + enterText.getText(), false);
		} else if (v == completeTransaction) {
			// createIntent(Gravity.CENTER,false);
			// Log.d("Job Details"," Login UserType : "+Constants.userType+" * Service Provicer User : "+uid+" * Login user : "+Constants.uid+" *  Job No. : "+jobid);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure want to complete this job?");
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							if (Constants.isNetAvailable(ContactActivity.this)) {
								new CompleteTransaction().execute("");
							} else {
								Constants.NoInternetError(ContactActivity.this);
							}

						}
					});
			builder.create().show();

		} else if (v == enterText) {
			createIntent(Gravity.BOTTOM, true, "" + enterText.getText(), false);
		} else if (v.getId() == R.id.contact_btnBack) {
			finish();
		} else if (v == txtChooseJob)
			openJobsList();
	}

	private void createIntent(int gravity, boolean isPhoto, String message,
			Boolean isAmount) {
		Intent intent = new Intent(this, ContactEditDialogActivity.class);
		intent.putExtra("dialogGravity", gravity);
		intent.putExtra("UserID", uid);
		intent.putExtra("JobId", jobid);
		intent.putExtra("isPhoto", isPhoto);
		intent.putExtra("source_message", message);
		intent.putExtra("isAmount", isAmount);
		intent.putExtra("textChange", message);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_bottom_to_top, 0);
	}

	private void transferAmount() {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("user_id", ""
						+ Constants.userType));
				nameValuePairs.add(new BasicNameValuePair("from_rate_id", uid));
				nameValuePairs.add(new BasicNameValuePair("to_rate_id",
						Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "transferAmount");
				parser.getParseData(nameValuePairs);
			}
		});
		thr.start();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser && progress == 100) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelSeekbar.setProgress(0);
							dialog.dismiss();
						}
					});
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {

								if (Constants
										.isNetAvailable(ContactActivity.this)) {
									new CancleTransaction().execute("");
								} else {
									Constants
											.NoInternetError(ContactActivity.this);
								}
							} catch (Exception exp) {

							}
						}
					});
			builder.setMessage("Are you sure to cancel transaction?");
			builder.create().show();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private class ViewJobProfile extends AsyncTask<String, String, ArrayList> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("login_user_id",
					Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("login_user_type", String
					.valueOf(Constants.userType)));
			nameValuePairs.add(new BasicNameValuePair("other_user_id", uid));
			nameValuePairs.add(new BasicNameValuePair("status", "A"));
			JobsPostParser jobsParser = new JobsPostParser(Constants.HTTP_HOST
					+ "viewUserjobs");
			jobsModelList = jobsParser.getParseData(nameValuePairs);

			return jobsModelList;
		}

		@Override
		protected void onPostExecute(ArrayList result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				jobsModelList = result;
				JobsModel model = (JobsModel) result.get(0);
				if (model.getJobData() != null) {
					JobData data[] = model.getJobData();
					if (data != null && data.length > 0) {

						JobData selectedUserJobData = null;
						int count = data.length;
						for (int i = 0; i < count; i++) {
							JobBean jobBean = data[i].getJob();

							UserDetail userdetail = data[i].getUser();
							userarray.add(userdetail);

							jobsArray.add(jobBean);
							if (data[i].getBidDetail().getJobPostedById()
									.equals(uid)) {
								selectedUserJobData = data[i];

							}
						}
						if (selectedUserJobData == null) {
							txtJobName.setText(data[0].getJob().getName());
							jobid = data[0].getJob().getId();
						} else {
							txtJobName.setText(selectedUserJobData.getJob()
									.getName());
							jobid = selectedUserJobData.getJob().getId();
							shieldType = selectedUserJobData.getUser()
									.getSecurity();
							if (shieldType.equals("blue"))
								txtshield.setImageResource(R.drawable.shieldx);
							else if (shieldType.equals("silver"))
								txtshield
										.setImageResource(R.drawable.shield_silver);
							else if (shieldType.equals("gold"))
								txtshield
										.setImageResource(R.drawable.shield_gold);
						}
						needExpenseStatus();

						if (data.length > 1) {
							txtChooseJob.setVisibility(View.VISIBLE);
						} else {
							txtChooseJob.setVisibility(View.INVISIBLE);
						}
					}
				}
			}
			// @Ashish
			if (!Constants.strsValue.equals("")) {
				String[] parts = Constants.strsValue.split("#");
				/*
				 * ratingBar.setRating(Float.parseFloat(parts[0]));
				 * ratingBar1.setRating(Float.parseFloat(parts[1]));
				 * ratingBar2.setRating(Float.parseFloat(parts[2]));
				 * ratingBar3.setRating(Float.parseFloat(parts[3]));
				 */
				starRating(Float.parseFloat(parts[0]));
			}
			// -Ashish-
		}

	}

	public void starRating(Float a) {

		int b = Math.round(a);
		int c = (int) b;
		Log.d("a", String.valueOf(a));
		Log.d("c", String.valueOf(c));
		if (c == 5)

		{
			((ImageView) findViewById(R.id.star_ratin1))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin2))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin3))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin4))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin5))
					.setImageResource(R.drawable.yellowstar);

		} else if (c == 4) {
			((ImageView) findViewById(R.id.star_ratin1))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin2))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin3))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin4))
					.setImageResource(R.drawable.yellowstar);
		} else if (c == 3) {
			((ImageView) findViewById(R.id.star_ratin1))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin2))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin3))
					.setImageResource(R.drawable.yellowstar);

		} else if (c == 2) {
			((ImageView) findViewById(R.id.star_ratin1))
					.setImageResource(R.drawable.yellowstar);
			((ImageView) findViewById(R.id.star_ratin2))
					.setImageResource(R.drawable.yellowstar);

		}

		else if (c == 1) {
			((ImageView) findViewById(R.id.star_ratin1))
					.setImageResource(R.drawable.yellowstar);

		}
	}

	protected void showNotificationDetails() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContactActivity.this);
		builder.setTitle("Task Details");
		LinearLayout layout = new LinearLayout(ContactActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final EditText edt = new EditText(ContactActivity.this);
		edt.setMaxLines(3);
		builder.setView(layout);
		builder.setPositiveButton("Send",
				new DialogInterface.OnClickListener() {
					// @Override
					public void onClick(DialogInterface dialog, int whichButton) {

						return;
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					// @Override
					public void onClick(DialogInterface dialog, int whichButton) {

						return;
					}
				});
		builder.show();

	}

	private void openJobsList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Choose Job");

		final String jobs[] = new String[jobsArray.size()];
		for (int i = 0; i < jobsArray.size(); i++) {
			jobs[i] = jobsArray.get(i).getName();
		}
		builder.setItems(jobs, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UserDetail user = userarray.get(which);

				txtJobName.setText(jobsArray.get(which).getName());

				contact_title.setText(user.getUsername().toString());

				jobid = jobsArray.get(which).getId();
				Log.d("Jobid", "Jobid" + jobid);
				needExpenseStatus();
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog petdialog = builder.create();
		petdialog.show();
	}

	private class CompleteTransaction extends AsyncTask<String, String, String> {
		private ProgressDialog progressBar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(ContactActivity.this);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// 2. URL: http://www.sampatti.com/pb101/services/completeJob
			// Parameters :user_id, job_id
			String url = Constants.HTTP_HOST + "completeJob";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

			HttpPostConnector conn = new HttpPostConnector(url, nameValuePairs);
			String response = conn.getResponseData();

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String response = result;
			if (true) {
				// {"bid_status":"Pending","status":"success","message":"message sended successfully"}

				if (response != null && response.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.has("status")
								&& jsonObject.getString("status")
										.equalsIgnoreCase("success")) {
							if (jsonObject.has("bid_status")
									&& !jsonObject.getString("bid_status")
											.equalsIgnoreCase("Pending")) {
								progressBar.dismiss();
								transferAmount();

								Intent jobCompleteIntent = new Intent(
										ContactActivity.this,
										RatingProviderActivity.class);
								// String jobId =
								// getIntent().getStringExtra("job_id");
								// String toRateId =
								// getIntent().getStringExtra("to_id");

								jobCompleteIntent.putExtra("completedJobId",
										jobid);
								jobCompleteIntent.putExtra(
										"completedJobuserId", uid);
								startActivity(jobCompleteIntent);

							} else if (jsonObject.has("bid_status")
									&& jsonObject.getString("bid_status")
											.equalsIgnoreCase("Pending")) {
								progressBar.dismiss();
								Intent jobCompleteIntent = new Intent(
										ContactActivity.this,
										RatingCustomerActivity.class);
								// String jobId =
								// getIntent().getStringExtra("job_id");
								// String toRateId =
								// getIntent().getStringExtra("to_id");

								jobCompleteIntent.putExtra("completedJobId",
										jobid);
								jobCompleteIntent.putExtra(
										"completedJobuserId", uid);
								startActivity(jobCompleteIntent);

							}
						} else {
							progressBar.dismiss();
							Utils.displayOkAlert(ContactActivity.this,
									jsonObject.getString("message"));
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else
					progressBar.dismiss();
			}
		}

	}

	public void dailogbox(final String source, String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContactActivity.this);
		if (source.equals("Extra Amount Needed")) {

			builder.setMessage(message)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// FIRE ZE MISSILES!
									if (source.equals("Extra Amount Needed")) {
										cnfirm_dailog();
									}/*
									 * else { CheckRecipientID(); }
									 */
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancelled the dialog
								}
							});
			builder.show();

		} else if (source.equals("over Amount Needed")) {
			builder.setMessage(message).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							updateJob(jobid);
						}
					});

			builder.show();
		}

		// Create the AlertDialog object and return it
		// return builder.create();
	}

	public void cnfirm_dailog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContactActivity.this);
		builder.setMessage("Are you sure to confirm to pay extra amount")
				.setPositiveButton("Confirm",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// FIRE ZE MISSILES!
								ExtraAmountCharge();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});

		builder.show();
	}

	private class CancleTransaction extends AsyncTask<String, String, String> {
		ProgressDialog progressDialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ContactActivity.this);
			progressDialog.setMessage("Please wait...");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = Constants.HTTP_HOST + "cancel_job";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

			HttpPostConnector conn = new HttpPostConnector(url, nameValuePairs);
			String response = conn.getResponseData();

			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response);
			progressDialog.dismiss();

			if (response != null && response.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.has("status")
							&& jsonObject.getString("status").equalsIgnoreCase(
									"success")) {
						Utils.displayOkAlert(ContactActivity.this,
								"Transaction cancelled successfully");
						Intent jobCompleteIntent = new Intent(
								ContactActivity.this, MapActivity.class);
						startActivity(jobCompleteIntent);
					} else {
						Utils.displayOkAlert(ContactActivity.this,
								jsonObject.getString("message"));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private class ServerCommunicateFund extends
			AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				ContactActivity.this);

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobid));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			FundPostParser parser = new FundPostParser(Constants.HTTP_HOST
					+ "fund");
			String dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			if (!dataList.equals("")) {
				Intent intent = new Intent(ContactActivity.this,
						PaypalWebviewAcivity.class);
				intent.putExtra("url", dataList);
				startActivity(intent);
			}
		}
	}

}
