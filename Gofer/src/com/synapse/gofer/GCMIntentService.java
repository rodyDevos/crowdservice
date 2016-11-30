package com.synapse.gofer;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.synapse.gofer.model.notification;
import com.synapse.gofer.util.Constants;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private Context mContext = null;

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		Constants.gcmRegistrationId = registrationId;
		mContext = context;
		ServerUtilities.register(context, Constants.gcmAppName,
				Constants.gcmEmail, registrationId);
	}

	/**
	 * \
	 * 
	 * Method called on device un registred
	 * */
	
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		// displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i("VIPMSG", "Received message");
		mContext = context;
		String message = intent.getExtras().getString("message");
		// notifies user
		try {

			generateNotification(context, message);

		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
			Log.e("Send", "Error Come " + e.toString());
		}
	}

	/*
	 * Method called on receiving a deleted message
	 */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		mContext = context;
		String message = getString(R.string.gcm_deleted, total);
		// displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		// displayMessage(context,context.getResources().getString(R.string.gcm_recoverable_error),errorId);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		String title = context.getString(R.string.app_name);

		String msgArray[] = message.split("~");
		int msgType = Integer.parseInt(msgArray[0]);
		Log.e("Send", "1. MSG REC TYP > " + msgType);
		String messageToDisplay = message;

		Intent notificationIntent = null;
		notification _notification = new notification();
		SharedPreferences loginDB = mContext.getSharedPreferences(
				LoginActivity.LOGINDATA, 0);
		boolean name = loginDB.getBoolean("isLogin", false);
		if (name) {
			Constants.uid = loginDB.getString("UserId", "0");
			Log.e("VIPII", "1." + loginDB.getInt("UserType", 2));
			Constants.userType = loginDB.getInt("UserType", 2);
			Constants.approxAddress = loginDB.getString("ApproxAdd", "");
			Constants.trueAddress = loginDB.getString("TrueAdd", "");
			notificationIntent = new Intent(context, MapActivity.class);
		} else {
			notificationIntent = new Intent(context, LoginActivity.class);
		}

		Log.e("Send", "2. MSG  > " + message);
		_notification.setMsgType(Integer.toString(msgType));
		if (msgType == 5) {
			// type~from_id~full_name~job_id~reply~message~to_id~job_name
			notificationIntent.putExtra("from_id", msgArray[1]);
			notificationIntent.putExtra("username", msgArray[2]);
			notificationIntent.putExtra("job_id", msgArray[3]);
			notificationIntent.putExtra("reply", msgArray[4]);
			notificationIntent.putExtra("message", msgArray[5]);
			notificationIntent.putExtra("to_id", msgArray[6]);
			notificationIntent.putExtra("jobName", msgArray[7]);

			notificationIntent.putExtra("isFromPush", true);
			notificationIntent.putExtra("msgType", 5);

			_notification.setFrom_id(msgArray[1]);
			_notification.setUser_name(msgArray[2]);
			_notification.setJobid(msgArray[3]);
			_notification.setReply(msgArray[4]);
			_notification.setMessage(msgArray[5]);
			_notification.setTo_id(msgArray[6]);
			_notification.setJobname(msgArray[7]);

			messageToDisplay = msgArray[5];
		} else if (msgType == 0 || msgType == 4 || msgType == 1) {
			// type~message
			Log.e("Send", "3. Inside  > " + message);
			messageToDisplay = msgArray[1];

			_notification.setMessage(msgArray[1]);

		} else if (msgType == 3) {
			// type~job_name~full_name~message_all

			notificationIntent.putExtra("jobName", msgArray[1]);
			notificationIntent.putExtra("username", msgArray[2]);
			notificationIntent.putExtra("message", msgArray[3]);
			notificationIntent.putExtra("msgType", 3);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobname(msgArray[1]);
			_notification.setUser_name(msgArray[2]);
			_notification.setMessage(msgArray[3]);

			messageToDisplay = msgArray[3];
		} else if (msgType == 6) {
			
			// both party agrees to complete job
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[3]);
			notificationIntent.putExtra("from_name", msgArray[4]);
			notificationIntent.putExtra("to_id", msgArray[5]);
			notificationIntent.putExtra("to_name", msgArray[6]);
			notificationIntent.putExtra("status", msgArray[7]);
			notificationIntent.putExtra("msgType", 6);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[3]);
			_notification.setFrom_name(msgArray[4]);
			_notification.setTo_id(msgArray[5]);
			_notification.setTo_name(msgArray[6]);
			_notification.setStatus(msgArray[7]);

			messageToDisplay = msgArray[4] + " has agreed to end " + msgArray[2];
		} else if (msgType == 10) {
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[3]);
			notificationIntent.putExtra("from_name", msgArray[4]);
			notificationIntent.putExtra("to_id", msgArray[5]);
			notificationIntent.putExtra("to_name", msgArray[6]);
			// notificationIntent.putExtra("status", msgArray[7]);
			notificationIntent.putExtra("msgType", 10);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[3]);
			_notification.setFrom_name(msgArray[4]);
			_notification.setTo_id(msgArray[5]);
			_notification.setTo_name(msgArray[6]);

			messageToDisplay = msgArray[4] + " has complete " + msgArray[2];
		} else if (msgType == 11) {
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[3]);
			notificationIntent.putExtra("from_name", msgArray[4]);
			notificationIntent.putExtra("to_id", msgArray[5]);
			notificationIntent.putExtra("to_name", msgArray[6]);
			// notificationIntent.putExtra("status", msgArray[7]);
			notificationIntent.putExtra("msgType", 11);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[3]);
			_notification.setFrom_name(msgArray[4]);
			_notification.setTo_id(msgArray[5]);
			_notification.setTo_name(msgArray[6]);

			messageToDisplay = msgArray[4]
					+ " does not agree to your complete request. Please contact them to resolve any issues";
			_notification.setMessage(messageToDisplay);
		} else if (msgType == 12) {
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[3]);
			notificationIntent.putExtra("from_name", msgArray[4]);
			notificationIntent.putExtra("to_id", msgArray[5]);
			notificationIntent.putExtra("to_name", msgArray[6]);
			// notificationIntent.putExtra("status", msgArray[7]);
			notificationIntent.putExtra("ownerid", msgArray[8]);

			notificationIntent.putExtra("msgType", 12);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[3]);
			_notification.setFrom_name(msgArray[4]);
			_notification.setTo_id(msgArray[5]);
			_notification.setTo_name(msgArray[6]);
			_notification.setOwnerId(msgArray[8]);

			messageToDisplay = msgArray[4] + " would like to complete your job "
					+ msgArray[2];
		} else if (msgType == 8) {
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[4]);
			notificationIntent.putExtra("from_name", msgArray[5]);
			notificationIntent.putExtra("to_id", msgArray[6]);
			notificationIntent.putExtra("to_name", msgArray[7]);
			notificationIntent.putExtra("status", msgArray[8]);
			//notificationIntent.putExtra("ownerid", msgArray[8]);

			notificationIntent.putExtra("msgType", 8);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[4]);
			_notification.setFrom_name(msgArray[5]);
			_notification.setTo_id(msgArray[6]);
			_notification.setTo_name(msgArray[7]);
			_notification.setStatus(msgArray[8]);

			messageToDisplay = msgArray[4] + " would like to complete your job "
					+ msgArray[2];
			
		} else if (msgType == 9) {
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			
			notificationIntent.putExtra("job_id", msgArray[1]);
			notificationIntent.putExtra("jobName", msgArray[2]);
			notificationIntent.putExtra("from_id", msgArray[3]);
			notificationIntent.putExtra("from_name", msgArray[4]);
			notificationIntent.putExtra("to_id", msgArray[5]);
			notificationIntent.putExtra("to_name", msgArray[6]);
			//notificationIntent.putExtra("status", msgArray[8]);
			//notificationIntent.putExtra("ownerid", msgArray[8]);

			notificationIntent.putExtra("msgType", 9);
			notificationIntent.putExtra("isFromPush", true);

			_notification.setJobid(msgArray[1]);
			_notification.setJobname(msgArray[2]);
			_notification.setFrom_id(msgArray[3]);
			_notification.setFrom_name(msgArray[4]);
			_notification.setTo_id(msgArray[5]);
			_notification.setTo_name(msgArray[6]);
			//_notification.setStatus(msgArray[8]);

			messageToDisplay = msgArray[4] + " disagrees to complete your job "
					+ msgArray[2];
			
		}
		
		else if (msgType == 7) {
			
			// 7~mgdurand has just cancelled your job, 'Refund Test 1-1', and you have received a full refund.
			// type~jobid~jobname~from_id~from_name~to_id~to_name~status
			
			messageToDisplay = msgArray[4] + " has just cancelled your job, " + msgArray[2];
		}
		
		if (!isMyApplication()) {

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(icon,
					messageToDisplay, when);

			Intent launchIntent;

			if (msgType == 12 || msgType == 8) { // Redirect with show alert dialog
				launchIntent = new Intent(this, MapActivity.class);
				// Pass required value
				launchIntent.putExtra("ismap", "yes");
				launchIntent.putExtra("message", messageToDisplay);
				launchIntent.putExtra("status", Integer.toString(msgType));
				launchIntent.putExtra("notification", _notification);

			} else if (msgType == 6) { // Redirect feedback screen
				launchIntent = new Intent(this, MapActivity.class);
				launchIntent.putExtra("ismap", "yes");
				launchIntent.putExtra("message", messageToDisplay);
				launchIntent.putExtra("status", Integer.toString(msgType));
				launchIntent.putExtra("notification", _notification);
			} else { // Simple Redirect map screen
				launchIntent = new Intent(this, MapActivity.class);

				Constants.uid = loginDB.getString("UserId", "0");

				launchIntent.putExtra("ismap", "yes");
				launchIntent.putExtra("message", messageToDisplay);
				launchIntent.putExtra("status", Integer.toString(msgType));
				launchIntent.putExtra("notification", _notification);

				Constants.userType = loginDB.getInt("UserType", 2);
				Constants.approxAddress = loginDB.getString("ApproxAdd", "");
				Constants.trueAddress = loginDB.getString("TrueAdd", "");
			}

			// set intent so it does not start a new activity
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent = PendingIntent.getActivity(context, 0,
					launchIntent, PendingIntent.FLAG_ONE_SHOT);
			notification.setLatestEventInfo(context, title, messageToDisplay,
					intent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// Play default notification sound
			notification.defaults |= Notification.DEFAULT_SOUND;
			// notification.sound = Uri.parse("android.resource://" +
			// context.getPackageName() + "your_sound_file_name.mp3");
			// Vibrate if vibrate is enabled
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(0, notification);
		} else {

			Intent intent = new Intent("custom-event-name");
			// You can also include some extra data.
			intent.putExtra("message", messageToDisplay);
			intent.putExtra("status", msgType);
			intent.putExtra("notification", _notification);
			LocalBroadcastManager.getInstance(getApplicationContext())
					.sendBroadcast(intent);
		}
	}

	private boolean isMyApplication() {

		ActivityManager mActivityManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		String mPackageName = "";
		if (Build.VERSION.SDK_INT > 20) {
			mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;

		} else {
			mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity
					.getPackageName();

		}

		Log.e("Send", "Output > " + getApplicationContext().getPackageName());
		if (mPackageName.equals(getApplicationContext().getPackageName())) {
			return true;
		}
		return false;
	}

}
