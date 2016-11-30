package com.synapse.gofer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.gofer.rating.RatingProviderActivity;
import com.synapse.backgroud.MapUpdateService;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.notification;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Shared_Preferences;
import com.synapse.gofer.util.Utils;

public class SettingsActivity extends Activity implements OnClickListener {

	private Button btnRadius, btnMapRefresh, btnPofile, btnLogout,
			showTrafficBtn, btnRadiusOfPostedJobs, dispute;
	private TextView btnBack;
	private ToggleButton btnTip;
	/** Called when the activity is first created. */
	private String[] refreshRateArray = { "15 Seconds", "30 Seconds",
			"1 Minute", "2 Minutes", "5 Minutes", "10 Minutes", "20 Minutes",
			"30 Minutes" };
	private long[] refreshRateValue = { 15000, 30000, 60000, 120000, 300000,
			600000, 1200000, 1800000 };

	private String[] mapRadiousOfPostedJobsArray = { "5 Mile", "25 Miles",
			"50 Miles", "100 Miles" };
	private float[] mapRadiousOfPostedJobsVlaue = { 5000, 25000, 50000, 100000 };

	private String[] radiusArray = { "1 Mile", "2 Miles", "3 Miles", "5 Miles",
			"10 Miles", "15 Miles" };
	private float[] radiusVlaue = { 5, 8, 11, 14, 17, 20 };

	public static boolean showTraffic = false;
	public static boolean enableTrafficButton = false;
	ImageView sendFeedback;
	private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
	Shared_Preferences _share;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		
		setContentView(R.layout.activity_settings);
		_share = new Shared_Preferences(SettingsActivity.this);
		initViews();

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			receivers.add(mMessageReceiver);
		}
	}

	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {
		btnRadius = (Button) findViewById(R.id.btnRadius);
		btnMapRefresh = (Button) findViewById(R.id.btnMapRefresh);
		btnRadiusOfPostedJobs = (Button) findViewById(R.id.btnRadiusOfPostedJobs);

		btnPofile = (Button) findViewById(R.id.btnPofile);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		dispute = (Button) findViewById(R.id.dispute);
		btnBack = (TextView) findViewById(R.id.btnBack);
		btnTip = (ToggleButton) findViewById(R.id.togleTipofday);
		sendFeedback = (ImageView) findViewById(R.id.send_feedback);

		btnRadius.setOnClickListener(this);
		btnMapRefresh.setOnClickListener(this);
		btnPofile.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnTip.setOnClickListener(this);
		sendFeedback.setOnClickListener(this);
		btnRadiusOfPostedJobs.setOnClickListener(this);
		dispute.setOnClickListener(this);

		SharedPreferences pref = getSharedPreferences("goffer_pref",
				Context.MODE_PRIVATE);
		if (pref.getBoolean("tip_for_theday", false))
			btnTip.setBackgroundResource(R.drawable.off);
		else
			btnTip.setBackgroundResource(R.drawable.on);

		showTrafficBtn = (Button) findViewById(R.id.btnshow_traffic);
		showTrafficBtn.setOnClickListener(this);
		if (enableTrafficButton) {
			showTrafficBtn.setVisibility(View.VISIBLE);
			showTrafficBtn.setOnClickListener(this);

		}

		try {
			Log.e("Send", "Check > " + _share.getIsShowTraffic());
			if (_share.getIsShowTraffic().equals("ok")) {
				showTrafficBtn.setText("Hide traffic");
			} else {
				showTrafficBtn.setText("Show traffic");
			}
		} catch (Exception e) {
			Log.e("Send", "Catch Call > okay ");
			showTrafficBtn.setText("Show traffic");
			_share.setIsShowTraffic("no");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnRadius) {
			mapRadiusDailog();
		} else if (v == btnMapRefresh) {
			mapRefreshDailog();
		} else if (v == btnRadiusOfPostedJobs) {
			setRadiusOfPostedJobs();
		} else if (v == btnPofile) {
			startActivity(new Intent(SettingsActivity.this,
					ProfileActivity.class));
		} else if (v == btnLogout) {
			logOut();
		} else if (v == btnBack) {
			finish();

		} else if (v == btnTip) {
			SharedPreferences pref = getSharedPreferences("goffer_pref",
					Context.MODE_PRIVATE);
			if (btnTip.isChecked()) {
				btnTip.setBackgroundResource(R.drawable.on);
				pref.edit().putBoolean("tip_for_theday", false).commit();

			} else {
				btnTip.setBackgroundResource(R.drawable.off);
				pref.edit().putBoolean("tip_for_theday", true).commit();
				ShowAlertDialog(getString(R.string.txtmessagetip),
						getString(R.string.txtalerttip));
				// new TipForTheDay(this).execute("");
				// ShowAlertDialog(message, title);
			}
		
		} else if (v == sendFeedback) {
			String to = "feedback@crowdserviceinc.com";
			String subject = "Crowdservice Feedback";
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("text/html");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));
		} else if (v == showTrafficBtn) {

			// showTrafficBtn.setText("Hide traffic");
			// showTrafficBtn.setText("Show traffic");

			// showTraffic = !showTraffic;
			// String str = showTraffic ? "Traffic is on" : "Traffic is off";

			if (_share.getIsShowTraffic().equals("no")) {
				showTrafficBtn.setText("Hide traffic");
				_share.setIsShowTraffic("ok");
				ShowAlertDialog(getString(R.string.txtmessageshow),
						getString(R.string.app_name));
			} else {
				showTrafficBtn.setText("Show traffic");
				_share.setIsShowTraffic("no");
				ShowAlertDialog(getString(R.string.txtmessagereduced),
						getString(R.string.app_name));
			}
			// Toast t = Toast.makeText(SettingsActivity.this, str,
			// Toast.LENGTH_LONG);
			// t.setGravity(Gravity.CENTER, 0, 0);
			// t.show();
		} else if (v == dispute) {
			
			
			startActivity(new Intent(SettingsActivity.this,
					DisputeActivity.class));
					
			/*
			String to = "disputeresolution@crowdserviceinc.com";
			String subject = "Crowdservice Dispute";
			String 
			
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("text/html");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
			email.setType("message/rfc822");
			
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));
					*/
		}
	}

	private void setRadiusOfPostedJobs() {
		AlertDialog.Builder petbuilder = new AlertDialog.Builder(this);
		petbuilder.setTitle("Choose radius of posted jobs");

		// jobRadius
		// mapRadiousOfPostedJobsArray
		// mapRadiousOfPostedJobsVlaue
		int count = mapRadiousOfPostedJobsArray.length;
		int index = 0;
		for (int i = 0; i < count; i++) {
			if (Constants.jobRadius == mapRadiousOfPostedJobsVlaue[i]) {
				index = i;
				break;
			}
		}

		petbuilder.setSingleChoiceItems(mapRadiousOfPostedJobsArray, index,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Constants.jobRadius = mapRadiousOfPostedJobsVlaue[which];
						SharedPreferences pref = getSharedPreferences(
								"goffer_pref", Context.MODE_PRIVATE);
						pref.edit().putFloat("jobRadius", Constants.jobRadius)
								.commit();
						Toast t = Toast.makeText(SettingsActivity.this,
								"Your posted job radius updated "
										+ mapRadiousOfPostedJobsArray[which]
										+ " Successfully", Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

						dialog.dismiss();
					}
				});

		petbuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog petdialog = petbuilder.create();
		petdialog.show();
	}

	private void mapRefreshDailog() {
		AlertDialog.Builder petbuilder = new AlertDialog.Builder(this);
		petbuilder.setTitle("Choose map refresh rate");

		int count = refreshRateValue.length;
		int index = 0;
		for (int i = 0; i < count; i++) {
			if (Constants.mapRefreshRate == refreshRateValue[i]) {
				index = i;
				break;
			}
		}

		petbuilder.setSingleChoiceItems(refreshRateArray, index,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Constants.mapRefreshRate = refreshRateValue[which];
						SharedPreferences pref = getSharedPreferences(
								"goffer_pref", Context.MODE_PRIVATE);
						pref.edit()
								.putLong("maprefresh_rate",
										Constants.mapRefreshRate).commit();
						if (MapUpdateService.self != null)
							MapUpdateService.self
									.changeRefreshRate(Constants.mapRefreshRate);

						Toast t = Toast.makeText(SettingsActivity.this,
								"Your map refresh rate updated "
										+ refreshRateArray[which]
										+ " Successfully", Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

						dialog.dismiss();
					}
				});

		petbuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog petdialog = petbuilder.create();
		petdialog.show();
	}

	private void mapRadiusDailog() {
		AlertDialog.Builder petbuilder = new AlertDialog.Builder(this);
		petbuilder.setTitle("Choose map radius");

		int count = radiusArray.length;
		int index = 0;
		for (int i = 0; i < count; i++) {
			if (Constants.mapRadius == radiusVlaue[i]) {
				index = i;
				break;
			}
		}

		petbuilder.setSingleChoiceItems(radiusArray, index,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Constants.mapRadius = radiusVlaue[which];
						SharedPreferences pref = getSharedPreferences(
								"goffer_pref", Context.MODE_PRIVATE);
						pref.edit().putFloat("map_radius", Constants.mapRadius)
								.commit();
						Toast t = Toast.makeText(SettingsActivity.this,
								"Your map radius updated " + radiusArray[which]
										+ " Successfully", Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();
						dialog.dismiss();
					}
				});

		petbuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog petdialog = petbuilder.create();
		petdialog.show();
	}

	private void logOut() {

		LoginManager.getInstance().logOut();
		
		if (Constants.isNetAvailable(SettingsActivity.this)) {
			new LogoutUserFromServer().execute("");
		} else {
			Constants.NoInternetError(SettingsActivity.this);
		}
	}

	private class LogoutUserFromServer extends
			AsyncTask<String, String, String> {
		private ProgressDialog progressBar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar = new ProgressDialog(SettingsActivity.this);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = Constants.HTTP_HOST + "updatelogin";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("is_login", "0"));
			nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));

			HttpPostConnector conn = new HttpPostConnector(url, nameValuePairs);
			String response = conn.getResponseData();

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressBar.dismiss();

			String response = result;
			if (response != null && response.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.has("status")
							&& jsonObject.getString("status").equalsIgnoreCase(
									"success")) {
						Toast t = Toast.makeText(SettingsActivity.this,
								"You have log out successfuly",
								Toast.LENGTH_SHORT);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

						clearLoginData();
						
						Intent intent = new Intent(getApplicationContext(),
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						startActivity(intent);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	private void clearLoginData() {
		
		SharedPreferences loginDB = getSharedPreferences(
				LoginActivity.LOGINDATA, 0);
		
		SharedPreferences.Editor editor = loginDB.edit();
		editor.putString("UserId", "");
		editor.putInt("UserType", 0);
		editor.putString("ApproxAdd", "");
		editor.putString("TrueAdd", "");
		editor.putBoolean("isLogin", false);
		editor.putString("Cookie", "");
		editor.commit();
	}
	
	private void ShowAlertDialog(String message, String title) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get extra data included in the Intent

			notification _notification = (notification) intent
					.getSerializableExtra("notification");

			String message = intent.getStringExtra("message");

			if (_notification.getMsgType().equals("11")) {
				Utils.displayCustomOkAlertRedirect(SettingsActivity.this,
						_notification.getMessage(),
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("12")) {
				Utils.displayCustomAgreeAlert(SettingsActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("10")) {
				Utils.displayCustomOkAlert(SettingsActivity.this, message,
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("0")) {
				Utils.displayCustomOkAlert(SettingsActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("1")) {
				Utils.displayCustomOkAlert(SettingsActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_1));
			} else if (_notification.getMsgType().equals("6")) {
				Intent jobCompleteIntent = new Intent(SettingsActivity.this,
						RatingProviderActivity.class);
				jobCompleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				String jobId = _notification.getJobid();
				String toRateId = _notification.getTo_id();

				jobCompleteIntent.putExtra("completedJobId", jobId);
				jobCompleteIntent.putExtra("completedJobuserId", toRateId);
				context.startActivity(jobCompleteIntent);
			}

			// Toast.makeText(getApplicationContext(), "msg >> " + message,
			// Toast.LENGTH_SHORT).show();
		}
	};

	public boolean isReceiverRegistered(BroadcastReceiver receiver) {
		boolean registered = receivers.contains(receiver);
		Log.i("Send", "is receiver " + receiver + " registered? " + registered);
		return registered;
	}
}