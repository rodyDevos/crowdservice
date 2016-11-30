package com.synapse.gofer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gofer.rating.RatingProviderActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.synapse.backgroud.MapUpdateListner;
import com.synapse.backgroud.MapUpdateService;
import com.synapse.contact.ContactActivity;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.model.notification;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.service.GPSTracker;
import com.synapse.gofer.service.MyLocation;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Shared_Preferences;
import com.synapse.gofer.util.Utils;
import com.synapse.gofer.widget.BadgeButton;

public class MapActivity extends FragmentActivity implements OnClickListener,
		MapUpdateListner, OnInfoWindowClickListener {

	// Google Map
	private GoogleMap googleMap;
	Handler handle;
	private TextView txtMailbox, txtContact, textRequest, textProvide,
			btnClickThrough;
	private Button btnEveryone, btnOpenJobs, btnMyJobs, btnSettings; 
	private BadgeButton txtViewJobs;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private int requestForService = 1;
	private MyLocation myLocation;
	Context context;

	public static final int CUSTOMER_MARKER = 200;
	public static final int COURIER_MARKER = 201;
	public static final int JOB_MARKER = 203;
	public static final int ACTIVE_COURIER = 204;
	public static final int ACTIVE_CUSTOMER = 205;
	public static final int ADDRESS_FILL = 301;
	public static final int PROVIDER_MARKER = 206;
	
	private ArrayList<JobsModel> jobsModelList;
	private ProgressDialog progressBar = null;

	private Timer activeJobUpdateTimer = null;
	private boolean activityVisible = true;
	private String number_of_activeJob = "";
	private LinearLayout layoutDialogGofer = null;
	Dialog dialogMarkerUserOption = null;
	private boolean isActiveJob = false;
	Handler mapservicehandler;
	private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
	GPSTracker _gps_tracker;
	Shared_Preferences _shared;

	Toast msgToast = null;
	
	// /**Called when the activity is first created.*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_map);

		context = this;
		Log.e("Send", "User ID " + Constants.uid + " Name "
				+ Constants.username);
		
		msgToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		getScreenScales();
		if (MapUpdateService.self == null) {

			startService(new Intent(MapActivity.this, MapUpdateService.class));
		}
		
		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			receivers.add(mMessageReceiver);
		}
		
		// Manage Current Location Object

		_gps_tracker = new GPSTracker(MapActivity.this);

		Constants.lat = Double.toString(_gps_tracker.getLatitude());
		Constants.lon = Double.toString(_gps_tracker.getLongitude());

		Log.e("Send", "Show Location > " + _gps_tracker.getLatitude()
				+ " Long " + _gps_tracker.getLongitude());

		// Close current location

		// Getting screen width and height
		_shared = new Shared_Preferences(MapActivity.this);
		initViews();
		
		if(Constants.hasActiveJobs){
			btnMyJobs.performClick();
		}

		SharedPreferences pref = getSharedPreferences("goffer_pref",
				Context.MODE_PRIVATE);
		long flag = pref.getLong("tipDisplayDate", -1);

		if (Constants.isNetAvailable(MapActivity.this)) {
			if (flag != -1) {
				Date currentDate = new Date();
				Date lastDate = new Date(flag);
				if (lastDate.compareTo(currentDate) > 0)
					new TipForTheDay(this).execute("");
			} else {
				new TipForTheDay(this).execute("");
			}
		} else {
			Constants.NoInternetError(MapActivity.this);
		}
		
		progressBar = new ProgressDialog(this);

		handlePushIntent();

		if (Constants.DEVELOPING_MODE && !Constants.DEVELOPING_MODE_ALERT) {
			Toast.makeText(MapActivity.this, "Welcome to Development Mode",
					Toast.LENGTH_LONG).show();
			Constants.DEVELOPING_MODE_ALERT = true;
		}
		
		try {
			if (getIntent().getStringExtra("ismap").equals("yes")) {

				notification _notification = (notification) getIntent()
						.getSerializableExtra("notification");

				String message = getIntent().getStringExtra("message");
				
				if (getIntent().getStringExtra("status").equals("12")) {

					Utils.displayCustomAgreeAlert(MapActivity.this, message,
							getString(R.string.app_alert_title) + "-"
									+ _notification.getJobname(), _notification);
				} else if (getIntent().getStringExtra("status").equals("8")) {

					Utils.displayCustomAgreeAlert(MapActivity.this, message,
							getString(R.string.app_alert_title) + "-"
									+ _notification.getJobname(), _notification);
				} else if (getIntent().getStringExtra("status").equals("6")) {

//					Intent jobCompleteIntent = new Intent(context,
//							RatingProviderActivity.class);
//					String jobId = _notification.getJobid();
//					String toRateId = _notification.getTo_id();
//
//					jobCompleteIntent.putExtra("completedJobId", jobId);
//					jobCompleteIntent.putExtra("completedJobuserId", toRateId);
//					context.startActivity(jobCompleteIntent);
					
					Utils.displayCustomOkAlert(MapActivity.this,
							message,
							getString(R.string.app_alert_title));
					
				} else {
					
					if (_notification.getMsgType().equals("11")) {
						Utils.displayCustomOkAlertRedirect(MapActivity.this,
								_notification.getMessage(),
								getString(R.string.app_alert_title) + "-"
										+ _notification.getJobname(),
								_notification);
					} else if (_notification.getMsgType().equals("9")) {
						Utils.displayCustomOkAlertRedirect(MapActivity.this,
								message,
								getString(R.string.app_alert_title) + "-"
										+ _notification.getJobname(), _notification);
						
					} else if (_notification.getMsgType().equals("10")) {
						Utils.displayCustomOkAlert(MapActivity.this, message,
								getString(R.string.txtalert_bid_0));
					} else if (_notification.getMsgType().equals("0")) {
						Utils.displayCustomOkAlert(MapActivity.this,
								_notification.getMessage(),
								getString(R.string.txtalert_bid_0));
					} else if (_notification.getMsgType().equals("1")) {
						Utils.displayCustomOkAlert(MapActivity.this,
								_notification.getMessage(),
								getString(R.string.txtalert_bid_1));
					} else{
						Utils.displayCustomOkAlert(MapActivity.this,
								message,
								getString(R.string.app_alert_title) + "-"
										+ _notification.getJobname());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				Utils.displayCustomOkAlertRedirect(MapActivity.this,
						_notification.getMessage(),
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("8")) {
				Utils.displayCustomAgreeAlert(MapActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			}else if (_notification.getMsgType().equals("12")) {
				Utils.displayCustomAgreeAlert(MapActivity.this, message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
			} else if (_notification.getMsgType().equals("9")) {
				Utils.displayCustomOkAlertRedirect(MapActivity.this,
						message,
						getString(R.string.app_alert_title) + "-"
								+ _notification.getJobname(), _notification);
				
			}else if (_notification.getMsgType().equals("10")) {
				Utils.displayCustomOkAlert(MapActivity.this, message,
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("0")) {
				Utils.displayCustomOkAlert(MapActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_0));
			} else if (_notification.getMsgType().equals("1")) {
				Utils.displayCustomOkAlert(MapActivity.this,
						_notification.getMessage(),
						getString(R.string.txtalert_bid_1));
			} else if (_notification.getMsgType().equals("6")) {
				
//				Intent jobCompleteIntent = new Intent(MapActivity.this,
//						RatingProviderActivity.class);
//				jobCompleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				String jobId = _notification.getJobid();
//				String toRateId = _notification.getTo_id();
//
//				jobCompleteIntent.putExtra("completedJobId", jobId);
//				jobCompleteIntent.putExtra("completedJobuserId", toRateId);
//				context.startActivity(jobCompleteIntent);
				
				
				Utils.displayCustomOkAlert(MapActivity.this,
						message,
						getString(R.string.app_alert_title));				
				
			} else{
				Utils.displayCustomOkAlert(MapActivity.this,
						message,
						getString(R.string.app_alert_title));
			}

			// Toast.makeText(getApplicationContext(), "msg >> " + message,
			// Toast.LENGTH_SHORT).show();
		}
	};

	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
		Log.e("Send", "Call Here " + (googleMap != null));
		if (googleMap != null) {
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setInfoWindowAdapter(new MarkerInfoWindow());
			googleMap.setOnInfoWindowClickListener(this);
			googleMap.setMyLocationEnabled(true);

			// Change Position of current position

			try {
				if (_shared.getIsShowTraffic().equals("ok")) {
					googleMap.setTrafficEnabled(true);
				} else {
					googleMap.setTrafficEnabled(false);
				}
			} catch (Exception e) {
				googleMap.setTrafficEnabled(false);
				// TODO: handle exception
			}

			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Find myLocationButton view
			View myLocationButton = mapFragment.getView().findViewById(0x2);

			if (myLocationButton != null
					&& myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
				// ZoomControl is inside of RelativeLayout
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton
						.getLayoutParams();
				// Align it to - parent BOTTOM|LEFT
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

				// Update margins, set to 10dp
				final int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
								.getDisplayMetrics());
				params.setMargins(margin, margin, margin, margin);

				myLocationButton.setLayoutParams(params);
			}
			
			// Change Position of current position

			final LatLng latLng = new LatLng(_gps_tracker.getLatitude(),
					_gps_tracker.getLongitude());
			Log.e("Send", "Other 1 >");
			// googleMap.addMarker(new MarkerOptions().position(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChange(CameraPosition arg0) {
					Log.e("Send", " Listner " + latLng.latitude + " long "
							+ latLng.longitude);
					// Move camera.
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					// Remove listener to prevent position reset on camera move.
					googleMap.setOnCameraChangeListener(null);
				}
			});
		}

		textRequest = (TextView) findViewById(R.id.textRequest);
		textProvide = (TextView) findViewById(R.id.textProvide);
		txtMailbox = (TextView) findViewById(R.id.labelSettings);
		btnSettings = (Button) findViewById(R.id.btnSettings);

		txtContact = (TextView) findViewById(R.id.labelContact);
		txtViewJobs = (BadgeButton) findViewById(R.id.labelViewjobs);
		btnClickThrough = (TextView) findViewById(R.id.badgebtn_btn);
		
		btnEveryone = (Button) findViewById(R.id.btnEveryone);
		btnMyJobs = (Button) findViewById(R.id.btnMyJobs);
		btnOpenJobs = (Button) findViewById(R.id.btnOpenJobs);
		
		textRequest.setOnClickListener(this);
		textProvide.setOnClickListener(this);
		txtMailbox.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		
		txtContact.setOnClickListener(this);
		// txtViewJobs.setOnClickListener(this);
		btnClickThrough.setOnClickListener(this);

		btnEveryone.setOnClickListener(this);
		btnMyJobs.setOnClickListener(this);
		btnOpenJobs.setOnClickListener(this);
		// @Ashish
		LinearLayout linearlayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
		RelativeLayout badgebtn_rl = (RelativeLayout) findViewById(R.id.badgebtn_rl);
		FrameLayout.LayoutParams head_params = (FrameLayout.LayoutParams) badgebtn_rl
				.getLayoutParams();
		head_params.setMargins(0, 3, Constants.display_width / 14, 0); // substitute
																		// parameters
																		// for
																		// left,
																		// top,
																		// right,
																		// bottom
		badgebtn_rl.setLayoutParams(head_params);
		linearlayout1.getLayoutParams().height = Constants.display_height / 10;

		txtMailbox.getLayoutParams().width = Constants.display_width / 4;
		textRequest.getLayoutParams().width = Constants.display_width / 4;
		textProvide.getLayoutParams().width = Constants.display_width / 4;
		txtViewJobs.getLayoutParams().width = Constants.display_width / 4;

	}

	@Override
	protected void onResume() {
		super.onResume();
		activityVisible = true;

		Constants.TempEndAddress = "";
		Constants.TempEndTime = "";
		Constants.TempAddress = "";
		Constants.TempTime = "";

		// googleMap.setMyLocationEnabled(true);
		if (Constants.lat != null && !Constants.lat.equals("")
				&& Constants.lon != null && !Constants.lon.equals("")) {
			if (!Constants.DEVELOPING_MODE
					|| Constants.DEVELOPING_MODE_DEVICE.equals("phone")) // if
																			// application
																			// in
																			// developing
																			// mode
																			// then
																			// below
																			// line
																			// of
																			// code
																			// will
																			// not
																			// execute.
				if (_shared != null) {
					try {
						if (_shared.getIsShowTraffic().equals("ok")) {
							googleMap.setTrafficEnabled(true);
						} else {
							googleMap.setTrafficEnabled(false);
						}
					} catch (Exception e) {
						googleMap.setTrafficEnabled(false);
					}
				}
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(Double.parseDouble(Constants.lat), Double
							.parseDouble(Constants.lon)), Constants.mapRadius));

		}

		if (!isReceiverRegistered(mMessageReceiver)) {
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter("custom-event-name"));
			receivers.add(mMessageReceiver);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityVisible = false;
		if (dialogMarkerUserOption != null) {
			dialogMarkerUserOption.dismiss();
		}
		
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
		if (isReceiverRegistered(mMessageReceiver)) {
			receivers.remove(mMessageReceiver);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();

//		if (Constants.isNetAvailable(MapActivity.this)) {
//			//new ViewUserContacts().execute("", "", "");
//		} else {
//			Constants.NoInternetError(MapActivity.this);
//		}

		if (MapUpdateService.self != null){
			MapUpdateService.self.setOnUpdateListener(MapActivity.this);
			MapUpdateService.self.changeRefreshRate(Constants.mapRefreshRate, 0);
		}
		
		if (googleMap != null) {
			if (!Constants.DEVELOPING_MODE
					|| Constants.DEVELOPING_MODE_DEVICE.equals("phone")) // if
																			// application
																			// in
																			// developing
																			// mode
																			// then
																			// below
																			// line
																			// of
																			// code
																			// will
																			// not
																			// execute.
				googleMap.clear();
		}
	}

	@Override
	protected void onStop() {
		if (MapUpdateService.self != null)
			MapUpdateService.self.setOnUpdateListener(null);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		if (MapUpdateService.self != null) {
			MapUpdateService.self.stopSelf();
		}
		super.onDestroy();
	}
	
	private void handlePushIntent() {
		boolean isFromPush = getIntent().getBooleanExtra("isFromPush", false);
		if (isFromPush) {
			int msgType = getIntent().getIntExtra("msgType", -1);
			if (msgType == 5) {
				String isReply = getIntent().getStringExtra("reply");
				if (isReply.equalsIgnoreCase("YES"))
					displayReplyDialog();
				else
					displayNoReplyDialog();

			} else if (msgType == 3) {
				String jobName = getIntent().getStringExtra("jobName");
				String userName = getIntent().getStringExtra("username");
				String msg = getIntent().getStringExtra("message");

				displayPushAlert(jobName, msg);
			} else if (msgType == 6) {

				String messageToDisplay = getIntent().getStringExtra(
						"from_name")
						+ "has completed "
						+ getIntent().getStringExtra("jobName");

				String status = getIntent().getStringExtra("status");
				if (status.equalsIgnoreCase("pending")) {
					displayPushAlert(getIntent().getStringExtra("jobName"),
							messageToDisplay);
				} else {
					Intent jobCompleteIntent = new Intent(MapActivity.this,
							RatingProviderActivity.class);
					jobCompleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					String jobId = getIntent().getStringExtra("job_id");
					String toRateId = getIntent().getStringExtra("to_id");

					jobCompleteIntent.putExtra("completedJobId", jobId);
					jobCompleteIntent.putExtra("completedJobuserId", toRateId);
					startActivity(jobCompleteIntent);

				}

			}

		}
	}

	private void displayReplyDialog() {

		final String fromId = getIntent().getStringExtra("from_id");
		String username = getIntent().getStringExtra("username");
		final String jobId = getIntent().getStringExtra("job_id");
		final String message = getIntent().getStringExtra("message");
		final String toId = getIntent().getStringExtra("to_id");
		final String jobName = getIntent().getStringExtra("jobName");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Gofer - " + jobName);
		builder.setMessage(username + "has asked query : " + message);

		final EditText input = new EditText(this);
		input.setHint("Enter Reply");

		LinearLayout ll = new LinearLayout(this);

		ll.setGravity(Gravity.CENTER);
		ll.addView(input);
		builder.setView(ll);

		builder.setPositiveButton("Reply",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (Constants.isNetAvailable(MapActivity.this)) {
							new ServerCommunicationSendMessage().execute(toId,
									fromId, jobId, input.getText().toString(),
									"NO");
						} else {
							Constants.NoInternetError(MapActivity.this);
						}

						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();

	}

	private void displayNoReplyDialog() {
		String fromId = getIntent().getStringExtra("from_id");
		String username = getIntent().getStringExtra("username");
		String jobId = getIntent().getStringExtra("job_id");
		String message = getIntent().getStringExtra("message");
		String toId = getIntent().getStringExtra("to_id");
		final String jobName = getIntent().getStringExtra("jobName");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Gofer - " + jobName);
		builder.setMessage(username + "has send reply" + message);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private void displayPushAlert(String jobName, String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Gofer - " + jobName);
		builder.setMessage(message);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == txtMailbox) {
			startActivity(new Intent(MapActivity.this, MailboxActivity.class));
		} else if (v == textRequest) {
			
			Log.d("True Address", Constants.trueAddress);
			
			if(Constants.hasTrueAddress()){
				if(Constants.isNetAvailable(MapActivity.this)){
					requestForService = 1;
					doSubmit("Making you customer");
				}else{
					Constants.NoInternetError(MapActivity.this);
				}
			}else{
				Intent intent = new Intent(MapActivity.this, AddressFormActivity.class);
				intent.putExtra("requestForService", 1);
				startActivityForResult(intent, ADDRESS_FILL);
			}
			
		} else if (v == textProvide) {
			if(Constants.hasTrueAddress()){
				if(Constants.isNetAvailable(MapActivity.this)){
					requestForService = 0;
					doSubmit("Making you courier");
				}else{
					Constants.NoInternetError(MapActivity.this);
				}
			}else{
				Intent intent = new Intent(MapActivity.this, AddressFormActivity.class);
				intent.putExtra("requestForService", 0);
				startActivityForResult(intent, ADDRESS_FILL);
			}
			
		} else if (v == btnClickThrough) {
			
			if(Constants.isNetAvailable(MapActivity.this)){
				startActivity(new Intent(MapActivity.this, ViewJobsActivity.class));
			}else{
				Constants.NoInternetError(MapActivity.this);
			}
			
			// startActivity(new Intent(MapActivity.this,
			// RatingActivity.class));
		} else if (v == txtContact) {
			openCC("", "");
		} else if (v == btnSettings) {
			
			startActivity(new Intent(MapActivity.this, SettingsActivity.class));
			
		} else if (v == btnEveryone) {
			
			if (MapUpdateService.self != null){
				MapUpdateService.self.setOnUpdateListener(MapActivity.this);
				MapUpdateService.self.changeMapCondition(Constants.MAP_MODE_EVERYONE, Constants.mapRefreshRate, 0);
			}
			
			showToastMessage("View all Users", Toast.LENGTH_SHORT);
			
		} else if (v == btnMyJobs) {
			
			if (MapUpdateService.self != null){
				MapUpdateService.self.setOnUpdateListener(MapActivity.this);
				MapUpdateService.self.changeMapCondition(Constants.MAP_MODE_MYJOBS, Constants.mapRefreshRate, 0);
			}

			showToastMessage("View Your co-party", Toast.LENGTH_SHORT);
			
		} else if (v == btnOpenJobs) {
			
			if (MapUpdateService.self != null){
				MapUpdateService.self.setOnUpdateListener(MapActivity.this);
				MapUpdateService.self.changeMapCondition(Constants.MAP_MODE_OPENJOBS, Constants.mapRefreshRate, 0);
			}

			showToastMessage("Jobs Open for Bidding", Toast.LENGTH_SHORT);
		}
	}

	private void openCC(String uId, String userName) {
		if (jobsModelList != null && jobsModelList.size() > 0) {
			JobsModel jobs = (JobsModel) jobsModelList.get(0);
			JobData data[] = jobs.getJobData();

			// Log.e("TAGG", "JobData>>  " + data.length);

			if (!uId.equals("") && !userName.equals("")) {
				Intent intent = new Intent(MapActivity.this,
						ContactActivity.class);
				intent.putExtra("UserName", userName);
				intent.putExtra("ShieldType", "");
				intent.putExtra("UserID", uId);
				intent.putExtra("profileimage", "");
				startActivity(intent);
			} else if (jobs.getJobData() != null
					&& jobs.getJobData().length == 1) {
				Intent intent = new Intent(MapActivity.this,
						ContactActivity.class);
				intent.putExtra("UserName", data[0].getUser().getUsername());
				intent.putExtra("ShieldType", data[0].getUser().getSecurity());
				intent.putExtra("UserID", data[0].getUser().getId());
				intent.putExtra("profileimage", data[0].getProfileImgUrl());
				startActivity(intent);
			} else {
				openUserList();
			}
		}

	}

	/*
	 * private void findGoferDialog() {
	 * layoutDialogGofer=(LinearLayout)findViewById(R.id.layoutDialogGofer);
	 * TextView textRequest = (TextView)findViewById(R.id.textRequest); TextView
	 * textProvide = (TextView)findViewById(R.id.textProvide);
	 * textRequest.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * layoutDialogGofer.setVisibility(View.INVISIBLE); requestForService = 1;
	 * doSubmit("Making you customer"); } }); textProvide.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * layoutDialogGofer.setVisibility(View.INVISIBLE); requestForService = 0;
	 * doSubmit("Making you courier");
	 * 
	 * } }); layoutDialogGofer.setVisibility(View.VISIBLE); }
	 */

	@Override
	public void onBackPressed() {
		if (layoutDialogGofer != null
				&& layoutDialogGofer.getVisibility() == View.VISIBLE)
			layoutDialogGofer.setVisibility(View.GONE);
		else
			super.onBackPressed();
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
					if (data.getAuthenticated() != null
							&& data.getAuthenticated().equalsIgnoreCase(
									"success")) {
						Intent intent = new Intent(MapActivity.this,
								FindGoferActivity.class);
						intent.putExtra("requestForService", requestForService);
						startActivity(intent);

					} else {
						// showAlertDialog(data.getMessage());
					}
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(MapActivity.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/*
	 * To Submit Record.
	 */
	private void doSubmit(String message) {
		final ProgressDialog progressBar = new ProgressDialog(MapActivity.this);
		progressBar.setMessage(message);
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("is_customer", "2"));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "updateuser");
				ResultData postdata = parser.getParseData(nameValuePairs);
				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (postdata != null) {
					msg.obj = postdata;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	private void showAlertDialog(String s) {
		Context context = MapActivity.this.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	

	@Override
	public void updateStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				 progressBar.setMessage("Getting new values...");
				 progressBar.show();
				
				//showToastMessage("Getting new values...", Toast.LENGTH_LONG);
				
				Log.d("Service", "Getting new values...");
				//showAlertDialog("Getting new values...");
			}
		});

	}

	@Override
	public void updateEnd() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if(progressBar.isShowing())
					progressBar.dismiss();
				
				if (googleMap != null) {
					googleMap.clear();
					
					displayNormalUser();
					if (SettingsActivity.showTraffic)
						displayActiveUser();
				}

			}
		});

	}

	private void showToastMessage(final String message, int type){
		
		msgToast.setGravity(Gravity.BOTTOM, 0, 300);
		msgToast.setText(message);
		
		if (activityVisible)
			msgToast.show();
	}
	
	private synchronized void displayNormalUser() {
		ArrayList<UserDetail> courierList = Constants.courierArray;
		if (courierList != null) {
			for (UserDetail userDetail : courierList) {
				
//				if(!userDetail.isLogin())
//					continue;
				
				String lat = userDetail.getLatitude();
				String lon = userDetail.getLongitude();
				String uName = userDetail.getUsername();
				String uid = userDetail.getId();
				String location = userDetail.getLocation();

				Log.e("Send", "Print 2 > " + uid + " >> " + lat + " >> " + lon);

				if (lat != null && !lat.equalsIgnoreCase("null")
						&& !lat.equalsIgnoreCase("") && lon != null
						&& !lon.equalsIgnoreCase("null")
						&& !lon.equalsIgnoreCase("")) {
					String snippet = location + "#" + userDetail.getId() + "#"
							+ PROVIDER_MARKER;// lat + ", " + lon;
					LatLng latLng = new LatLng(Double.parseDouble(lat),
							Double.parseDouble(lon));
					Log.e("Send", "Other 2 > " + uName + " UN "
							+ Constants.username);
					if (!uid.equals(Constants.uid)) {
						googleMap.addMarker(createMarker(uName, snippet,
								latLng, PROVIDER_MARKER));
					}

					// builder.include(latLng);
				}
			}

		}

		ArrayList<UserDetail> customerList = Constants.customerArray;
		if (customerList != null) {
			for (UserDetail userDetail : customerList) {

				String lat = userDetail.getLatitude();
				String lon = userDetail.getLongitude();
				String uName = userDetail.getUsername();
				String location = userDetail.getLocation();

				if (lat != null && !lat.equalsIgnoreCase("null")
						&& !lat.equalsIgnoreCase("") && lon != null
						&& !lon.equalsIgnoreCase("null")
						&& !lon.equalsIgnoreCase("")) {
					String snippet = location + "#" + userDetail.getId() + "#"
							+ CUSTOMER_MARKER;// lat + ", " + lon;
					LatLng latLng = new LatLng(Double.parseDouble(lat),
							Double.parseDouble(lon));
					Log.e("Send", "Other 4 >");
					googleMap.addMarker(createMarker(uName, snippet, latLng,
							CUSTOMER_MARKER));
					// builder.include(latLng);
				}
			}
		}
		
		if (!Constants.username.equalsIgnoreCase("")){
			
			int marker_type = PROVIDER_MARKER;
			if(Constants.userType == 2){
				marker_type = CUSTOMER_MARKER;
			}else{
				marker_type = PROVIDER_MARKER;
			}
			String snippet1 = Constants.locationAdd + "#" + Constants.uid
					+ "#" + marker_type;// lat + ", " + lon;
			LatLng latLng = new LatLng(Double.parseDouble(Constants.lat),
					Double.parseDouble(Constants.lon));
			Log.e("Send", "Other 5 >");
			googleMap.addMarker(createMarker(Constants.username, snippet1,
					latLng, marker_type));
		}
//		
//
//		ArrayList<JobData> jobsArray = Constants.jobsArray;
//		if (jobsArray != null) {
//			for (JobData jobData : jobsArray) {
//
//				JobBean bean = jobData.getJob();
//
//				String lat = bean.getStartLatitude();
//				String lon = bean.getStartlongitude();
//				String name = bean.getName();
//
//				if (lat != null && !lat.equalsIgnoreCase("null")
//						&& !lat.equalsIgnoreCase("") && lon != null
//						&& !lon.equalsIgnoreCase("null")
//						&& !lon.equalsIgnoreCase("")) {
//					String snippet = bean.getToLocation() + "#" + bean.getId()
//							+ "#" + JOB_MARKER;// lat + ", " + lon;
//					LatLng latLng = new LatLng(Double.parseDouble(lat),
//							Double.parseDouble(lon));
//					Log.e("Send", "Print 6 > " + name + " >> " + lat + " >> "
//							+ lon);
//
//					Log.e("Send", "Other 6 >");
//					googleMap.addMarker(createMarker(name, snippet, latLng,
//							JOB_MARKER));
//				}
//
//			}
//		}
	}

	private MarkerOptions createMarker(String title, String snippet,
			LatLng lng, int markerType) {
		MarkerOptions markeroption = new MarkerOptions();
		markeroption.position(lng);
		markeroption.snippet(snippet);
		markeroption.title(title);
		if (markerType == COURIER_MARKER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.courier_map_marker));

		} else if (markerType == CUSTOMER_MARKER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.customer_map_marker));

		} else if (markerType == JOB_MARKER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.job_map_marker));
			markeroption.visible(false);
		} else if (markerType == ACTIVE_COURIER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.active_courier));
		} else if (markerType == ACTIVE_CUSTOMER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.provider_map_marker));
		} else if (markerType == PROVIDER_MARKER) {
			markeroption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.provider_map_marker));

		}
		return markeroption;
	}

	private class ViewUserContacts extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			if (Constants.userType == 3)
				nameValuePairs.add(new BasicNameValuePair("search_type",
						"job_apply_by_id"));
			else if (Constants.userType == 2)
				nameValuePairs.add(new BasicNameValuePair("search_type",
						"job_posted_by_id"));

			HttpPostConnector conn = new HttpPostConnector(Constants.HTTP_HOST
					+ "checkuserjob", nameValuePairs);

			String response = conn.getResponseData();

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("Job Result", result);

			if (result != null && result.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String str = "No";
					if (jsonObject.has("is_active"))
						str = jsonObject.getString("is_active");
					// @Ashish
					if (jsonObject.has("number_of_activeJob")) {
						number_of_activeJob = jsonObject
								.getString("number_of_activeJob");
						if (!number_of_activeJob.equals(""))
							showButtonWithText(R.id.labelViewjobs,
									jsonObject.getString("number_of_activeJob"));
					}
					// -@-
					if (str.equalsIgnoreCase("No")) {
						txtContact.setVisibility(View.INVISIBLE);
						isActiveJob = false;
						if (MapUpdateService.self != null) {
							MapUpdateService.self
									.setOnUpdateListener(MapActivity.this);
						}

					} else {
						txtContact.setVisibility(View.VISIBLE);
						isActiveJob = true;
						if (MapUpdateService.self != null) {
							MapUpdateService.self.setOnUpdateListener(null);
						}
						if (activeJobUpdateTimer == null)
							activeJobUpdateTimer = new Timer();
						else {
							activeJobUpdateTimer.cancel();
							activeJobUpdateTimer.purge();
							activeJobUpdateTimer = new Timer();
						}

						activeJobUpdateTimer.schedule(
								new MapUpdateWithActiveUser(), 1000,
								Constants.mapRefreshRate);
						SettingsActivity.enableTrafficButton = true;
						if (SettingsActivity.showTraffic) {
							if (MapUpdateService.self != null) {
								MapUpdateService.self
										.setOnUpdateListener(MapActivity.this);
								MapUpdateService.self.changeRefreshRate(
										Constants.mapRefreshRate, 0);
							}
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	}

	private void openUserList() {
		AlertDialog.Builder petbuilder = new AlertDialog.Builder(this);
		if (Constants.userType == 2) {
			petbuilder.setTitle("Choose Courrier");
		} else {
			petbuilder.setTitle("Choose Customer");
		}
		JobsModel jobs = (JobsModel) jobsModelList.get(0);

		final JobData data[] = jobs.getJobData();
		final String users[] = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			UserDetail detail = data[i].getUser();
			users[i] = detail.getUsername();
		}
		petbuilder.setItems(users, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UserDetail detail = data[which].getUser();
				Intent intent = new Intent(MapActivity.this,
						ContactActivity.class);
				intent.putExtra("UserName", detail.getUsername());
				intent.putExtra("UserID", detail.getId());
				intent.putExtra("profileimage", data[which].getProfileImgUrl());
				startActivity(intent);
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

	private class MapUpdateWithActiveUser extends TimerTask {
		// ProgressDialog progressBar1 = new ProgressDialog(MapActivity.this);
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// progressBar1.setMessage("Getting new values...");
					// progressBar1.show();
					Toast t = Toast.makeText(MapActivity.this,
							"Getting new values...", Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					if (activityVisible)
						t.show();
				}
			});

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("user_type", String
					.valueOf(Constants.userType)));
			nameValuePairs.add(new BasicNameValuePair("location",
					Constants.locationAdd));
			nameValuePairs
					.add(new BasicNameValuePair("latitude", Constants.lat));
			nameValuePairs.add(new BasicNameValuePair("longitude",
					Constants.lon));
			JobsPostParser jobsParser = new JobsPostParser(Constants.HTTP_HOST
					+ "viewUserContacts");
			jobsModelList = jobsParser.getParseData(nameValuePairs);
			Constants.jobsModelList = jobsModelList;
			if (jobsModelList != null && jobsModelList.size() > 0) {
				JobsModel model = (JobsModel) jobsModelList.get(0);

				final JobData data[] = model.getJobData();
				if (Constants.userType == 2)
					Constants.workingCourierArray = new ArrayList<UserDetail>();
				else
					Constants.workingCustomerArray = new ArrayList<UserDetail>();
				if (data != null && data.length > 0) {
					for (int i = 0; i < data.length; i++) {
						UserDetail detail = data[i].getUser();
						if (Constants.userType == 2)
							Constants.workingCourierArray.add(detail);
						else
							Constants.workingCustomerArray.add(detail);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (googleMap != null) {
							googleMap.clear();
							displayActiveUser();
							if (SettingsActivity.showTraffic)
								displayNormalUser();
						}
					}
				});
			}

		}

	}

	private synchronized void displayActiveUser() {

		if (Constants.userType == 3) {

			ArrayList<UserDetail> customerList = Constants.customerArray;
			if (customerList != null && customerList.size() > 0) {
				for (UserDetail userDetail : customerList) {

					String lat = userDetail.getLatitude();
					String lon = userDetail.getLongitude();
					String uName = userDetail.getUsername();
					String loc = userDetail.getLocation();
					if (lat != null && !lat.equalsIgnoreCase("null")
							&& !lat.equalsIgnoreCase("") && lon != null
							&& !lon.equalsIgnoreCase("null")
							&& !lon.equalsIgnoreCase("")) {
						String snippet = loc + "#" + userDetail.getId() + "#"
								+ CUSTOMER_MARKER;// lat + ", " + lon;
						LatLng latLng = new LatLng(Double.parseDouble(lat),
								Double.parseDouble(lon));
						Log.e("Send", "Add Flag 1 >" + uName);
						googleMap.addMarker(createMarker(uName, snippet,
								latLng, CUSTOMER_MARKER));
						// ----changes ACTIVE_customer instead of
						// Customer-Markser
					}
				}

				if (!Constants.username.equalsIgnoreCase("")) {
					String snippet1 = Constants.locationAdd + "#"
							+ Constants.uid + "#" + ACTIVE_CUSTOMER;// lat +
																	// ", " +
																	// lon;
					LatLng latLng = new LatLng(
							Double.parseDouble(Constants.lat),
							Double.parseDouble(Constants.lon));
					Log.e("Send", "Add Flag 2 >");
					googleMap.addMarker(createMarker(Constants.username,
							snippet1, latLng, ACTIVE_CUSTOMER));

				}
			}
		} else {
			ArrayList<UserDetail> courierList = Constants.workingCourierArray;
			if (courierList != null && courierList.size() > 0) {
				for (UserDetail userDetail : courierList) {

					String lat = userDetail.getLatitude();
					String lon = userDetail.getLongitude();
					String uName = userDetail.getUsername();
					String loc = userDetail.getLocation();
					if (lat != null && !lat.equalsIgnoreCase("null")
							&& !lat.equalsIgnoreCase("") && lon != null
							&& !lon.equalsIgnoreCase("null")
							&& !lon.equalsIgnoreCase("")) {
						String snippet = loc + "#" + userDetail.getId() + "#"
								+ ACTIVE_CUSTOMER;// lat + ", " + lon;
													// //Customer_arker set
													// instaed of Active_courier
						LatLng latLng = new LatLng(Double.parseDouble(lat),
								Double.parseDouble(lon));
						Log.e("Send", "Add Flag 3 >");
						googleMap.addMarker(createMarker(uName, snippet,
								latLng, ACTIVE_CUSTOMER));
					}
				}

				if (!Constants.username.equalsIgnoreCase("")) {
					String snippet1 = Constants.locationAdd + "#"
							+ Constants.uid + "#" + CUSTOMER_MARKER;// lat +
																	// ", " +
																	// lon;
					LatLng latLng = new LatLng(
							Double.parseDouble(Constants.lat),
							Double.parseDouble(Constants.lon));
					Log.e("Send", "Add Flag 4 >");
					googleMap.addMarker(createMarker(Constants.username,
							snippet1, latLng, CUSTOMER_MARKER));

				}
			}
		}
	}

	private class ServerCommunicationSendMessage extends
			AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				MapActivity.this);

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("from_id", strParams[0]));
			nameValuePairs.add(new BasicNameValuePair("to_id", strParams[1]));
			nameValuePairs.add(new BasicNameValuePair("job_id", strParams[2]));
			nameValuePairs.add(new BasicNameValuePair("message", strParams[3]));
			nameValuePairs.add(new BasicNameValuePair("reply", strParams[4]));
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "messaging");
			String response = parser.getParseData(nameValuePairs);
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String resp) {
			super.onPostExecute(resp);
			progressBar.dismiss();

			//showAlertDialog("Replay Send");
			
			showToastMessage("Replay Send", Toast.LENGTH_SHORT);
		}
	}

	private class MarkerInfoWindow implements InfoWindowAdapter {
		private View view = null;

		public MarkerInfoWindow() {
			view = getLayoutInflater()
					.inflate(R.layout.markerinfo_window, null);
		}

		@Override
		public View getInfoContents(Marker marker) {
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {

			RelativeLayout ll = (RelativeLayout) view;
			TextView title = (TextView) ll.findViewById(R.id.marker_title);
			//TextView snippet = (TextView) ll.findViewById(R.id.marker_snippet);

			String strSnippet = marker.getSnippet();

			title.setText(marker.getTitle());
			//snippet.setText(strSnippet.split("#")[0]);

			return view;
		}

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String[] strSnippet = marker.getSnippet().split("#");
		final String strId = strSnippet[1];
		final String strType = strSnippet[2];
		final String userName = marker.getTitle();

		Intent intent = new Intent(MapActivity.this, MapProfile.class);
		intent.putExtra("displayType", Integer.parseInt(strType));
		intent.putExtra("id", strId);

		startActivity(intent);
		
		
//		
//		dialogMarkerUserOption = new Dialog(MapActivity.this);
//
//		int divierId = dialogMarkerUserOption.getContext().getResources()
//				.getIdentifier("android:id/titleDivider", null, null);
//		View divider = dialogMarkerUserOption.findViewById(divierId);
//		divider.setBackgroundDrawable(new ColorDrawable(
//				android.graphics.Color.TRANSPARENT));
//		dialogMarkerUserOption.setContentView(R.layout.popup_marker_option);
//
//		dialogMarkerUserOption.getWindow().setBackgroundDrawable(
//				new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		Button btnProfile = (Button) dialogMarkerUserOption
//				.findViewById(R.id.btnMarkerProfile);
//		Button btnContact = (Button) dialogMarkerUserOption
//				.findViewById(R.id.btnMarkerContact);
//		View v1 = (View) dialogMarkerUserOption.findViewById(R.id.contactline);
//		if (userName.equalsIgnoreCase(Constants.username) || !isActiveJob) {
//			btnContact.setVisibility(View.GONE);
//		}
//		Button btnCancel = (Button) dialogMarkerUserOption
//				.findViewById(R.id.btnMarkerCancel);
//
//		btnProfile.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MapActivity.this, MapProfile.class);
//				intent.putExtra("displayType", Integer.parseInt(strType));
//				intent.putExtra("id", strId);
//
//				startActivity(intent);
//			}
//		});
//
//		btnCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialogMarkerUserOption.dismiss();
//			}
//		});
//
//		btnContact.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// openCC();
//
//				openCC(strId, userName);
//			}
//		});
//
//		dialogMarkerUserOption.show();

	}

	private void showButtonWithText(int id, String text) {

		BadgeButton btn = (BadgeButton) findViewById(id);
		btn.setBadgeText(text);
		if (!(text.equals("null")))
						
			btn.showBadge();
		else
			btn.hideBadge();

	}

	public boolean isReceiverRegistered(BroadcastReceiver receiver) {
		boolean registered = receivers.contains(receiver);
		Log.i("Send", "is receiver " + receiver + " registered? " + registered);
		return registered;
	}

	// -Azim-

	public void getScreenScales() {

		// WindowManager wm = (WindowManager)
		// context.getSystemService(Context.WINDOW_SERVICE);
		Display display = getWindowManager().getDefaultDisplay();
		// Display display = wm.getDefaultDisplay();
		Point size = new Point();
		Constants.display_width = display.getWidth();
		Constants.display_height = display.getHeight();
		Log.d("Display width is : ", "" + Constants.display_width);
		Log.d("Display height is : ", "" + Constants.display_height);
	}

}
