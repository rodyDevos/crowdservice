package com.synapse.gofer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.gofer.rating.RatingProviderActivity;
import com.synapse.gofer.jobandcourier.CourierJobsListingFragment;
import com.synapse.gofer.model.notification;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

@SuppressLint("Recycle")
public class ViewJobsActivity extends FragmentActivity implements
		OnClickListener {

	private FragmentManager fragmentManager;
	private BackListener backListner = null;
	private TextView title = null;
	private TextView viewjob_btnBack = null;
	private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_viewjobs);
		Log.e("VIP", "ViewJobsActivity");
		initViews();

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			receivers.add(mMessageReceiver);
		}

	}

	private void initViews() {
		TextView back = (TextView) findViewById(R.id.viewjob_btnBack);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.viewjob_title);

		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragmentpublic = null;
		Log.e("VIPVIP", "Constants.userType  >" + Constants.userType);
		if (Constants.userType == 2) {
			title.setText("Active Jobs");
			fragmentpublic = new ViewCustomerJobFragment();
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"ViewCustomerJobFragment");
		} else if (Constants.userType == 3) {
			title.setText("Active Jobs");
			fragmentpublic = new CourierJobsListingFragment();
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"CourierJobsListingFragment");

		}
		transaction.commit();

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.viewjob_btnBack) {
			if (backListner != null)
				backListner.backPressed();
			else
				finish();
		}
	}

	public void setTitle(String titleStr) {
		title.setText(titleStr);
	}

	// public void SetBackText(String backStr) {
	// viewjob_btnBack.setText(backStr);
	// }

	public interface BackListener {
		public void backPressed();
	}

	public void setBackListener(BackListener listener) {
		backListner = listener;
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get extra data included in the Intent

			notification _notification = (notification) intent
					.getSerializableExtra("notification");

			String message = intent.getStringExtra("message");

			if (_notification.getMsgType().equals("11")) {
				Utils.displayCustomOkAlertRedirect(ViewJobsActivity.this,
						_notification.getMessage(),
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("8")) {
				Utils.displayCustomAgreeAlert(ViewJobsActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			}else if (_notification.getMsgType().equals("12")) {
				Utils.displayCustomAgreeAlert(ViewJobsActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("9")) {
				Utils.displayCustomOkAlertRedirect(ViewJobsActivity.this,
						message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
				
			}else if (_notification.getMsgType().equals("10")) {
				Utils.displayCustomOkAlert(ViewJobsActivity.this, message,
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("0")) {
				Utils.displayCustomOkAlert(ViewJobsActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("1")) {
				Utils.displayCustomOkAlert(ViewJobsActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_1));
			} else if (_notification.getMsgType().equals("6")) {
				
//				Intent jobCompleteIntent = new Intent(ViewJobsActivity.this,
//						RatingProviderActivity.class);
//				jobCompleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				String jobId = _notification.getJobid();
//				String toRateId = _notification.getTo_id();
//
//				jobCompleteIntent.putExtra("completedJobId", jobId);
//				jobCompleteIntent.putExtra("completedJobuserId", toRateId);
//				context.startActivity(jobCompleteIntent);
				
				Utils.displayCustomOkAlert(ViewJobsActivity.this,
						message,
						getString(R.string.app_alert_title));
				
			} else{
				Utils.displayCustomOkAlert(ViewJobsActivity.this,
						message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname());
			}

			// Toast.makeText(getApplicationContext(), "msg >> " + message,
			// Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			
			receivers.add(mMessageReceiver);
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
		if (isReceiverRegistered(mMessageReceiver)) {
			receivers.remove(mMessageReceiver);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public boolean isReceiverRegistered(BroadcastReceiver receiver) {
		boolean registered = receivers.contains(receiver);
		Log.i("Send", "is receiver " + receiver + " registered? " + registered);
		return registered;
	}

}
