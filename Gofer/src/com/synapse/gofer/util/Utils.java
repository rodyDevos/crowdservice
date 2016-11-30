package com.synapse.gofer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.util.Log;

import com.gofer.rating.RatingCustomerActivity;
import com.gofer.rating.RatingProviderActivity;
import com.synapse.gofer.ContactActivity;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.notification;

//created by:jksharma
//created on:12-Jul-2011
public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void displayOkAlert(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Tip of the day");
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();
	}

	public static void displayCustomAgreeAlert(final Context context,
			String message, String title, final notification model) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				doCancelRequest(context, model.getJobid());
			}
		});
		builder.setNegativeButton("Agree", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// doAgreeRequest(context, model); // Not use
				doCompleteRequest(context, model);// temp
				Log.e("Send", "Check Agree Data > FI > " + model.getTo_id()
						+ " UID " + Constants.uid);
			}
		});

		builder.create().show();
	}

	public static void displayCustomOkAlert(Context context, String message,
			String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	// Cancel Request with redirect Contact screen
	public static void displayCustomOkAlertRedirect(final Context context,
			String message, String title, final notification _notification) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(context, ContactActivity.class);
				intent.putExtra("data", _notification.getJobid());
				context.startActivity(intent);
			}
		});
		builder.create().show();
	}

	public static String getfullAddres(Address add) {
		String strAdd = "";
		if (add.getAddressLine(0) != null)
			strAdd += add.getAddressLine(0);
		if (add.getLocality() != null)
			strAdd = strAdd + "," + add.getLocality();
		if (add.getAdminArea() != null)
			strAdd = strAdd + "," + add.getAdminArea();
		if (add.getCountryName() != null)
			strAdd = strAdd + "," + add.getCountryName();
		return strAdd;
	}

	public static void displayCustomOkAlert(Context context, String message) {

	}

	public static void doCancelRequest(Context context, final String jobid) {
 
		final ProgressDialog progressBar = new ProgressDialog(context);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					reqEntity.addPart("job_id", new StringBody(jobid));
					reqEntity.addPart("user_id", new StringBody(Constants.uid));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("VIPIII", "Error 3> " + e.toString());
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "disagreeComplete");
				Log.d("Send", "Check Data > " + Constants.HTTP_HOST
						+ "disagreeComplete");
				Log.d("Send", "job_id Data > " + jobid
						+ " disagreeComplete " + " > user_id > "
						+ Constants.uid);
				
				httppost.setHeader("Cookie", Constants.cookie);
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
					Log.d("VIPIII", "Error> " + e.toString());
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
					Log.d("VIPIII", "Error 1> " + e.toString());
				}
				progressBar.dismiss();

				// Message msg = handler.obtainMessage();
				if (resultData != null) {
					// msg.obj = resultData;
					// msg.arg1 = SUCCESS;

				} else {
					// msg.arg1 = FAILURE;

				}
				// handler.sendMessage(msg);
			}
		});
		thr.start();
	}
	
	public static void doCancelRequest1(Context context, final String jobid) {

		final ProgressDialog progressBar = new ProgressDialog(context);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					reqEntity.addPart("job_id", new StringBody(jobid));
					reqEntity.addPart("user_id", new StringBody(Constants.uid));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("VIPIII", "Error 3> " + e.toString());
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "completeJobCancelNotification");
				Log.d("Send", "Check Data > " + Constants.HTTP_HOST
						+ "completeJobCancelNotification");
				Log.d("Send", "job_id Data > " + jobid
						+ " completeJobCancelNotification " + " > user_id > "
						+ Constants.uid);
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
					Log.d("VIPIII", "Error> " + e.toString());
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
					Log.d("VIPIII", "Error 1> " + e.toString());
				}
				progressBar.dismiss();

				// Message msg = handler.obtainMessage();
				if (resultData != null) {
					// msg.obj = resultData;
					// msg.arg1 = SUCCESS;

				} else {
					// msg.arg1 = FAILURE;

				}
				// handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	public static void doAgreeRequest(final Context context,
			final notification _notification) {

		final ProgressDialog progressBar = new ProgressDialog(context);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					reqEntity.addPart("job_id",
							new StringBody(_notification.getJobid()));
					reqEntity.addPart("user_id", new StringBody(Constants.uid));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("VIPIII", "Error 3> " + e.toString());
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "completeJobAgreeNotification");
				Log.d("Send", "Check Data > " + Constants.HTTP_HOST
						+ "completeJobAgreeNotification");
				Log.d("Send", "job_id Data > " + _notification.getJobid()
						+ " requestComplete " + " > user_id > " + Constants.uid);
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
					Log.d("VIPIII", "Error> " + e.toString());
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
					Log.d("VIPIII", "Error 1> " + e.toString());
				}
				progressBar.dismiss();

				// Message msg = handler.obtainMessage();
				if (resultData != null) {
					// msg.obj = resultData;
					// msg.arg1 = SUCCESS;
					RedirectFeedbackScreen(context, _notification);
				} else {
					// msg.arg1 = FAILURE;
				}
				// handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.e("HttpReaderException", ">>>" + e.getMessage());
		}
		return sb.toString();
	}

	public static void RedirectFeedbackScreen(Context context,
			notification _notification) {
		Intent jobCompleteIntent = new Intent(context,
				RatingCustomerActivity.class);
		String jobId = _notification.getJobid();
		String toRateId = _notification.getTo_id();

		jobCompleteIntent.putExtra("completedJobId", jobId);
		jobCompleteIntent.putExtra("completedJobuserId", toRateId);
		context.startActivity(jobCompleteIntent);
	}

	private void handlePushIntent(Context context, notification _notification) {

		int msgType = Integer.parseInt(_notification.getMsgType());
		if (msgType == 5) {
			String isReply = _notification.getReply();
			// if (isReply.equalsIgnoreCase("YES"))
			// displayReplyDialog();
			// else
			// displayNoReplyDialog();

		} else if (msgType == 3) {
			String jobName = _notification.getJobname();
			String userName = _notification.getUser_name();
			String msg = _notification.getMessage();

			// displayPushAlert(jobName, msg);
		} else if (msgType == 6) {

			String messageToDisplay = _notification.getFrom_name()
					+ "has completed " + _notification.getJobname();

			String status = _notification.getStatus();
			if (status.equalsIgnoreCase("pending")) {
				// displayPushAlert(_notification.getJobname(),
				// messageToDisplay);
			} else {
				Intent jobCompleteIntent = new Intent(context,
						RatingProviderActivity.class);
				String jobId = _notification.getJobid();
				String toRateId = _notification.getTo_id();

				jobCompleteIntent.putExtra("completedJobId", jobId);
				jobCompleteIntent.putExtra("completedJobuserId", toRateId);
				context.startActivity(jobCompleteIntent);

			}

		}

	}

	public static void doCompleteRequest(final Context context,
			final notification _notification) {

		final ProgressDialog progressBar = new ProgressDialog(context);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				String ownerid;
				if (Constants.uid.equals(_notification.getOwnerId())) {
					ownerid = Constants.uid;
				} else {
					ownerid = _notification.getOwnerId();
				}

				try {
					reqEntity.addPart("job_id",
							new StringBody(_notification.getJobid()));
					 reqEntity.addPart("user_id", new
					 StringBody(Constants.uid));

					// New condition for pass owner id
					//reqEntity.addPart("user_id", new StringBody(ownerid));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("VIPIII", "Error 3> " + e.toString());
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "requestComplete");
				Log.d("Send", "Check Data > " + Constants.HTTP_HOST
						+ "requestComplete");
				Log.d("Send", "job_id Data > " + _notification.getJobid()
						+ " requestComplete " + " > user_id > " + Constants.uid
						+ " < OwnerID >" + ownerid);
				httppost.setHeader("Cookie", Constants.cookie);
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
					Log.d("Send", "Error> " + e.toString());
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					Log.d("Send", "RES> " + strResponse);
					JSONObject jsonresponse = new JSONObject(strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
					} catch (Exception ex) {
						ex.printStackTrace();
						Log.d("Send", "ERROR > " + ex.toString());
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Send", "Error 1> " + e.toString());
				}
				progressBar.dismiss();
				Log.d("Send", "Final MSG > " + resultData.getMessage());
				// Message msg = handler.obtainMessage();
				if (resultData != null) {
					// msg.obj = resultData;
					// msg.arg1 = SUCCESS;
					RedirectFeedbackScreen(context, _notification);
				} else {
					// msg.arg1 = FAILURE;
				}
				// handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
	     try {
	         //Decode image size
	         BitmapFactory.Options o = new BitmapFactory.Options();
	         o.inJustDecodeBounds = true;
	         BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	         //The new size we want to scale to
	         final int REQUIRED_WIDTH=WIDTH;
	         final int REQUIRED_HIGHT=HIGHT;
	         //Find the correct scale value. It should be the power of 2.
	         int scale=1;
	         while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
	             scale*=2;

	         //Decode with inSampleSize
	         BitmapFactory.Options o2 = new BitmapFactory.Options();
	         o2.inSampleSize=scale;
	         return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	     } catch (FileNotFoundException e) {
	    	 e.printStackTrace();
	     }
	     return null;
	 }
	
	public static Bitmap decodeUri(Context context, Uri selectedImage,int WIDTH,int HIGHT){
		
		try {
	         //Decode image size
	         BitmapFactory.Options o = new BitmapFactory.Options();
	         o.inJustDecodeBounds = true;
	         BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

	         //The new size we want to scale to
	         final int REQUIRED_WIDTH=WIDTH;
	         final int REQUIRED_HIGHT=HIGHT;
	         //Find the correct scale value. It should be the power of 2.
	         int scale=1;
	         while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
	             scale*=2;

	         //Decode with inSampleSize
	         BitmapFactory.Options o2 = new BitmapFactory.Options();
	         o2.inSampleSize=scale;
	         return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
	     } catch (FileNotFoundException e) {
	    	 e.printStackTrace();
	     }
	     return null;
	     
	     /*
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
        */
    }
}
