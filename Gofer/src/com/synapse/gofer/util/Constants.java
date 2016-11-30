package com.synapse.gofer.util;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.UserDetail;

public class Constants {
	private final static String TAG = "Constants";
	// true for Developing the Application
	public static boolean DEVELOPING_MODE = false;
	public static boolean DEVELOPING_MODE_ALERT = false;
	// em and phone
	public static String DEVELOPING_MODE_DEVICE = "phone";

	// PAYPAL

	final public static String PAYPAL_APPID = "APP-80W284485P519543T";

	// final public static String PAYPAL_APPID = "APP-7FG37046VP222471C";
	final public static String PAYPAL_MERCHANT_EMAIL = "CrowdserviceInc-facilitator@gmail.com";
	// final public static int PAYPAL_MODE = PayPal.ENV_SANDBOX;
	final public static int PAYPAL_REQUEST_CODE = 10001;

	// For Live the Application get_HTTP_URL()
	public static final String HTTP_HOST = get_HTTP_URL();
	
	public static final String API_UPDATE_MAPMARKER = get_API_URL("getNearbyUsers");
	
	public static String STRIPE_PUBLISHABLE_KEY = "pk_test_bSxDwqU7rFFUDSWVO93d21bI";

	public static String FACEBOOK_ACCESS_TOKEN = "accesstoken";
	
	public static int USER_TYPE_CUSTOMER = 2; 
	public static int USER_TYPE_PROVIDER = 3; 
	

	public static final int MAP_MODE_EVERYONE = 101;
	public static final int MAP_MODE_MYJOBS = 102;
	public static final int MAP_MODE_OPENJOBS = 103;
	
	public static final int SUCCESS = 100;
	public static final int FAILURE = 101;
	
	public static final SimpleDateFormat serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat deviceFormate = new SimpleDateFormat("MMM dd");
	
	public static int runonetime;
	public static String lat = "33.921043";
	public static String username = "";
	public static String lon = "-118.330182";
	public static String locationAdd = "";
	public static String deviceid = "0";
	public static String uid = "0";
	public static String tempuserid = "0";
	public static int userType = 2;
	public static String approxAddress = "";
	public static String trueAddress = "";
	public static Bitmap imageComment = null;
	public static ArrayList<JobsModel> jobsModelList = null;
	public static String TempAddress = "";
	public static String TempTime = "";

	public static boolean traffic = false;

	public static String TempEndAddress = "";
	public static String TempEndTime = "";

	public static String TempImagePath = "";

	public static ArrayList<UserDetail> workingCustomerArray = null;
	public static ArrayList<UserDetail> details = null;
	public static ArrayList<UserDetail> workingCourierArray = null;
	public static ArrayList<UserDetail> customerArray = null;
	public static ArrayList<UserDetail> courierArray = null;
	public static ArrayList<JobData> jobsArray = null;
	public static long mapRefreshRate = 60000;
	public static float mapRadius = 14;
	public static float jobRadius = 100000;

	public static String gcmAppName = "Gofer";
	public static String gcmEmail = "syn.androidteam@gmail.com";
	public static String gcmRegistrationId = "";
	public static String gcmProjectID = "peak-sorter-512";
	public static String gcmProjectNumber = "30138520990";

	public static Boolean hasStripeRegistered = false;
	public static Boolean hasPayPalRegistered = false;
	public static Boolean hasjob_address = false;
	public static String uemail = "";
	public static String cardInfo = "Register Card";
	public static String BankInfo = "Register Bank";
	public static String Paypal_id = "";

	public static String job_address1 = "";
	public static String job_address2 = "";
	public static String job_address3 = "";
	public static String job_address4 = "";
	public static String job_address5 = "";
	public static String strsValue = "";
	public static int display_width = 0;
	public static int display_height = 0;
	public static boolean is_customer = true;

	public static boolean hasActiveJobs = false;
	
	public static int PREFERED_PROFILE_WIDTH = 400;
	public static int PREFERED_PROFILE_HEIGHT = 400;
	
	public static String cookie;
	
	public static int currentMapCondition = MAP_MODE_MYJOBS;
	
	public static AlertDialog alertDialog = null;
	/*
	 * List of categories of Courier & Home sections of a User
	 */
	public static ArrayList<String> courierCategories = new ArrayList<String>();
	public static ArrayList<String> homeCategories = new ArrayList<String>();

	public static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
	
	public Constants() {
	}

	public static String getFormattedDate(String str) {
		String strD = str;
		try {
			strD = str.substring(0, str.indexOf(" "));
		} catch (Exception ex) {
		}
		return strD;
	}

	public static Boolean isNetAvailable(Context con) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean hasTrueAddress(){
		
		String str = trueAddress.replaceAll(" ", "").replaceAll(",", "");
		return str.length() > 0;
	}
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
		Bitmap result = null;
		try {
			result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, 200, 200);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawCircle(50, 50, 50, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (NullPointerException e) {
		} catch (OutOfMemoryError o) {
		}
		return result;
	}

	public static String getCurrentDateTime() {
		final Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH);
		int mhour = c.get(Calendar.HOUR_OF_DAY);
		int mminute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
//		String date = String.valueOf(cday) + "/" + String.valueOf(cmonth) + "/"
//				+ String.valueOf(cyear) + " " + String.valueOf(mhour) + ":"
//				+ String.valueOf(mminute) + ":" + String.valueOf(sec);
		
		String date = String.valueOf(cmonth) + "/" + String.valueOf(cday) + "/" 
				+ String.valueOf(cyear) + " " + String.valueOf(mhour) + ":"
				+ String.valueOf(mminute);
				
		return date;
	}

	public static String getCurrentDateTimeYYMMDD() {
		final Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH);
		int mhour = c.get(Calendar.HOUR_OF_DAY);
		int mminute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		String date = String.valueOf(cyear) + "-" + String.valueOf(cmonth)
				+ "-" + String.valueOf(cday) + " " + String.valueOf(mhour)
				+ ":" + String.valueOf(mminute) + ":" + String.valueOf(sec);
		return date;
	}

	public static String getCurrentDateTime(int hour) {
		final Calendar c = Calendar.getInstance();
		long x = 1000 * 60 * 60 * 12;
		c.setTimeInMillis(c.getTimeInMillis() + x);
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH);
		int mhour = c.get(Calendar.HOUR_OF_DAY);
		int mminute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		String date = String.valueOf(cday) + "/" + String.valueOf(cmonth) + "/"
				+ String.valueOf(cyear) + " " + String.valueOf(mhour) + ":"
				+ String.valueOf(mminute) + ":" + String.valueOf(sec);
		return date;
	}

	public static String getCurrentDateTimeForBid() {
		final Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH) + 1;
		int mhour = c.get(Calendar.HOUR_OF_DAY);
		int mminute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		String date = String.valueOf(cmonth) + "/" + String.valueOf(cday) + "/"
				+ String.valueOf(cyear) + " " + String.valueOf(mhour) + ":"
				+ String.valueOf(mminute) + ":" + String.valueOf(sec);
		return date;
	}

	// "MM/dd/yyyy HH:mm:ss"];

	public static long getMilliseconds(String date) {
		if (date != null && date.length() > 0) {
			long milli = 0;
			SimpleDateFormat formater = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			try {
				Date date1 = formater.parse(date);
				milli = date1.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return milli;
		} else
			return 0;
	}

	public static String get_HTTP_URL() {
		if (DEVELOPING_MODE) {
			Log.d("Test : DEVELOPING_MODE is : " + DEVELOPING_MODE,
					"Service Url is : http://192.168.1.81:9000/api/services/ ");
			return "http://192.168.1.81:9000/api/services/";
		} else {
			Log.d("Live : DEVELOPING_MODE is : " + DEVELOPING_MODE,
					"Service Url is : http://54.186.18.31/api/services/ ");
			// return "http://54.186.18.31/api/v1/";
			return "http://admin.crowdserviceinc.com/api/v1/";
		}
	}
	
	public static String get_API_URL(String method) {
		return get_HTTP_URL() + method;
	}

	public static String getCurrentTime() {
		final Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH) + 1;
		int mhour = c.get(Calendar.HOUR_OF_DAY);
		int mminute = c.get(Calendar.MINUTE);
		
		int sec = c.get(Calendar.SECOND);
		String date = String.valueOf(mhour) + ":" + String.valueOf(mminute);
		return date;
	}

	/**
	 * Getting round corner Bitmap image
	 * 
	 * @param BitMap
	 *            and ImageButton
	 * @return round corner bitmap
	 * @Developer Ashish
	 * */

	public static Bitmap getRoundedCornerImage(Bitmap bitmap, int targetWidth,
			int targetHeight) {

		int targetWidth11 = bitmap.getWidth();
		int targetHeight11 = bitmap.getHeight();
		if (targetWidth == 0)
			targetWidth = 115;
		if (targetHeight == 0)
			
			targetHeight = 115;

		Bitmap output = Bitmap.createBitmap(targetWidth, targetHeight,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, targetWidth, targetHeight);
		final RectF rectF = new RectF(rect);
		final float roundPx = 100;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap,
				new Rect(0, 0, targetWidth11, targetHeight11), rect, paint);

		return output;

	}

	/**
	 * Getting image Bitmap from Internet URl
	 * 
	 * @param Internet
	 *            url of image in String format
	 * @return bitmap
	 * @author ASHISH
	 * */

	public static Bitmap downloadBitmap(String url) {
		// Initialize the default HTTP client object
		final DefaultHttpClient client = new DefaultHttpClient();

		// forming a HttoGet request
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			// check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(TAG + "ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

					return bitmap;
				} finally {

					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.e(TAG + ": Error ", "" + e.toString());
		}
		return null;
	}

	public static void NoInternetError(final Context context) {
		
		if(alertDialog != null && alertDialog.isShowing()){
			alertDialog.dismiss();
		}

		alertDialog = new AlertDialog.Builder(context).create();
		
		alertDialog.setTitle("Network Error");
		alertDialog.setMessage("The Interent connection appears to be lost. Please ensure that you have an active connection prior to listing or viewing jobs.");
		alertDialog.setIcon(android.R.drawable.ic_menu_delete);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Exit",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

}