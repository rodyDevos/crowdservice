package com.synapse.gofer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gofer.rating.RatingProviderActivity;
import com.synapse.gofer.model.notification;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

public class FindGoferActivity extends FragmentActivity implements
		OnClickListener {

	private FragmentManager fragmentManager;
	private BackListener backListner = null;
	private TextView title = null;
	private TextView back = null;
	private TextView settng = null;
	private final int SUCCESS = 101;

	int requestForService = 120;
	private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_findgofer);
		Log.e("VIP", "FindGoferActivity");

		initViews();

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			receivers.add(mMessageReceiver);
		}
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get extra data included in the Intent

			notification _notification = (notification) intent
					.getSerializableExtra("notification");

			String message = intent.getStringExtra("message");

			if (_notification.getMsgType().equals("11")) {
				Utils.displayCustomOkAlertRedirect(FindGoferActivity.this,
						_notification.getMessage(),
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("12")) {
				Utils.displayCustomAgreeAlert(FindGoferActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("10")) {
				Utils.displayCustomOkAlert(FindGoferActivity.this, message,
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("0")) {
				Utils.displayCustomOkAlert(FindGoferActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("1")) {
				Utils.displayCustomOkAlert(FindGoferActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_1));
			} else if (_notification.getMsgType().equals("6")) {
				Intent jobCompleteIntent = new Intent(FindGoferActivity.this,
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

	/*
	 * Called to initialize to user interface.
	 */
	private void initViews() {
		back = (TextView) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		settng = (TextView) findViewById(R.id.btnSetting);
		settng.setOnClickListener(this);
		requestForService = getIntent().getIntExtra("requestForService", 1);
		if (requestForService == 1) {
			back.setText("Map");
			settng.setVisibility(View.VISIBLE);
			title.setText("Request Service");
		} else if (requestForService == 0) {
			back.setText("Back");
			settng.setVisibility(View.GONE);
			title.setText("Open For Bids");
		}

		if (Constants.isNetAvailable(FindGoferActivity.this)) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });

		} else {
			Constants.NoInternetError(FindGoferActivity.this);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnBack) {
			if (backListner != null)
				backListner.backPressed();
			else
				finish();
		}
		if (v.getId() == R.id.btnSetting) {
			startActivity(new Intent(getApplicationContext(),
					SettingsActivity.class));

		}
	}

	public void setTitle(String titleStr) {
		title.setText(titleStr);
	}

	public interface BackListener {
		public void backPressed();
	}

	public void setBackListener(BackListener listener) {
		backListner = listener;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("VIP", "msg.arg1 --> " + msg.arg1);
			if (msg.arg1 == SUCCESS) {
				fragmentManager = getSupportFragmentManager();
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				Fragment fragmentpublic = null;
				if (requestForService == 1) {
					fragmentpublic = new CategoryFragment();
					transaction.replace(R.id.mainContainer, fragmentpublic,
							"Category");
				} else if (requestForService == 0) {
					fragmentpublic = new JobsListingFragment();
					transaction.replace(R.id.mainContainer, fragmentpublic,
							"JobsListingFragment");
				}
				transaction.commit();
			} else
				Toast.makeText(FindGoferActivity.this, "Problem",
						Toast.LENGTH_LONG).show();
		}
	};

	private class ServerCommunication extends AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				FindGoferActivity.this);

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));
			String x = requestForService == 1 ? "2" : "3";
			Log.e("TAGG", "REQ Parser >> " + Constants.uid);
			nameValuePairs.add(new BasicNameValuePair("is_customer", x));
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "updateuser");

			String dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			String str = requestForService == 1 ? "Making you customer"
					: "Making you as provider";
			progressBar.setMessage(str);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			if (dataList.equalsIgnoreCase("success")) {
				msg.arg1 = SUCCESS;
			} else
				msg.arg1 = 100;
			msg.obj = dataList;
			handler.sendMessage(msg);
		}
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

}
