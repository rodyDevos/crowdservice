package com.synapse.gofer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.login.LoginManager;
import com.google.android.gcm.GCMRegistrar;
import com.synapse.backgroud.MapUpdateService;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

public class SplashActivity extends Activity implements LocationListener{

	public static final int SPLASHTIME = 3000;
	private static final int STOPSPLASH = 0;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;

	/** Push Notification */
	AsyncTask<Void, Void, Void> mRegisterTask;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	ProgressDialog progressBar;
	private LocationManager mlocManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		// Message msg = new Message();
		// msg.what = STOPSPLASH;
		// splashHandler.sendMessageDelayed(msg,SPLASHTIME);
		// msg.arg1 = SUCCESS;
		getScreenScales();
		
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
				0, this);
		
		setPush();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.arg1 == SUCCESS) {
				/*
				 * Intent i= new Intent(SplashActivity.this,
				 * GPSLoggerService.class); startService(i);
				 */

				startService(new Intent(SplashActivity.this,
						MapUpdateService.class));
				SharedPreferences loginDB = getSharedPreferences(
						LoginActivity.LOGINDATA, 0);

				boolean name = loginDB.getBoolean("isLogin", false);

				if (name) {
					Constants.uid = loginDB.getString("UserId", "0");
					Log.e("VIPII", "2." + loginDB.getInt("UserType", 2));
					Constants.userType = loginDB.getInt("UserType", 2);
					Constants.approxAddress = loginDB
							.getString("ApproxAdd", "");
					Constants.trueAddress = loginDB.getString("TrueAdd", "");
					Constants.cookie = loginDB.getString("Cookie", "");
					
					checkActiveJobs();
				} else {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
				}
			} else if (msg.arg1 == FAILURE) {

			}
		}
	};

	private void setPush() {
		
		
//		cd = new ConnectionDetector(getApplicationContext());
//		// Check if Internet present
//		if (!cd.isConnectingToInternet()) {
//			// Internet Connection is not present
//			alert.showAlertDialog(SplashActivity.this,
//					"Internet Connection Error",
//					"Please connect to working Internet connection", false);
//			// stop executing code by return
//			return;
//		}
		
		if (!Constants.isNetAvailable(SplashActivity.this)) {
			Constants.NoInternetError(SplashActivity.this);
			return;
		}
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));
		// Get GCM registration id
		final String regId = GCMRegistrar
				.getRegistrationId(SplashActivity.this);
		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(SplashActivity.this,
					CommonUtilities.SENDER_ID);
			Constants.gcmRegistrationId = GCMRegistrar
					.getRegistrationId(SplashActivity.this);

			Message msg = new Message();
			msg.what = STOPSPLASH;
			msg.arg1 = SUCCESS;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);

		} else {
			Constants.gcmRegistrationId = regId;

			Message msg = new Message();
			msg.what = STOPSPLASH;
			msg.arg1 = SUCCESS;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);

			// Device is already registered on GCM

		}
	}

	@Override
	protected void onDestroy() {
		unRegister();
		mlocManager.removeUpdates(this);
		super.onDestroy();
	}

	/*
	 * Receiving push messages
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			// Showing received message
			// lblMessage.append(newMessage + "\n");
			Toast.makeText(getApplicationContext(),
					"New Message: " + newMessage, Toast.LENGTH_LONG).show();
			// Releasing wake lock
			WakeLocker.release();
		}
	};

	private void unRegister() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	}

	// @Ashish
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

	// -Ashish-

	
	private void checkActiveJobs() {
		
		if(progressBar == null){
			progressBar = new ProgressDialog(
					SplashActivity.this);
			progressBar.setMessage("Loading...");
			progressBar.setCancelable(false);
			progressBar.show();
		}
		
		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				Location loc = mlocManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				Geocoder geocoder = new Geocoder(SplashActivity.this);
				try {
					if(loc != null){
						Constants.lat = String.valueOf(loc.getLatitude());
						Constants.lon = String.valueOf(loc.getLongitude());
						Constants.locationAdd = Utils.getfullAddres((geocoder
								.getFromLocation(loc.getLatitude(),
										loc.getLongitude(), 1)).get(0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("latitude",
						Constants.lat));
				nameValuePairs.add(new BasicNameValuePair("longitude",
						Constants.lon));
				nameValuePairs.add(new BasicNameValuePair("location",
						Constants.locationAdd));
				nameValuePairs.add(new BasicNameValuePair("distance","10000000"));
				nameValuePairs.add(new BasicNameValuePair("condition","related"));
				
				HttpPostConnector conn=new HttpPostConnector(Constants.API_UPDATE_MAPMARKER, nameValuePairs);
				
				String response=conn.getResponseData();	
				
				if(response != null && response.length() > 0)
				{
					try {
						
						JSONArray responseArray = new JSONArray(response);
						
						int count = responseArray.length();
						Log.d("Total Users : ",""+count);
						
						Constants.hasActiveJobs = (count > 0);
						
						Message msg = handler.obtainMessage();
						handler.sendMessage(msg);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(progressBar.isShowing()){
				progressBar.dismiss();
			}
			startActivity(new Intent(SplashActivity.this,
					MapActivity.class));
			finish();
		}
	};
	
		
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
