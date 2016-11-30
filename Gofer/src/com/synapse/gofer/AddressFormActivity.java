package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.GradientDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressFormActivity extends Activity implements OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	
	TextView lbDescription1;
	TextView txtBack;
	
	EditText txtTruesAddress1, txtTruesAddress2, txtTruesAddress3, txtTruesAddress4, txtTruesAddress5;
	EditText txtApproxAddress1, txtApproxAddress2, txtApproxAddress3, txtApproxAddress4, txtApproxAddress5;
    
	Button btnSubmit;
	
	private int requestForService = 1;
	
	public static final String LOGINDATA = "GoferLogin";
	private SharedPreferences loginDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_form);
		
		requestForService = getIntent().getIntExtra("requestForService", 1);
		
		initViews();		
	}
	
	public void initViews(){
		lbDescription1 = (TextView) findViewById(R.id.lbDescription1);
		txtBack = (TextView) findViewById(R.id.btnBack);
		txtTruesAddress1 = (EditText) findViewById(R.id.trueAddress1);
		txtTruesAddress2 = (EditText) findViewById(R.id.trueAddress2);
		txtTruesAddress3 = (EditText) findViewById(R.id.trueAddress3);
		txtTruesAddress4 = (EditText) findViewById(R.id.trueAddress4);
		txtTruesAddress5 = (EditText) findViewById(R.id.trueAddress5);
		
		txtApproxAddress1 = (EditText) findViewById(R.id.approxAddress1);
		txtApproxAddress2 = (EditText) findViewById(R.id.approxAddress2);
		txtApproxAddress3 = (EditText) findViewById(R.id.approxAddress3);
		txtApproxAddress4 = (EditText) findViewById(R.id.approxAddress4);
		txtApproxAddress5 = (EditText) findViewById(R.id.approxAddress5);
		
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		
		txtBack.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		
		String htmlString = "All job listings are absolutely free. And when your job is complete, you will not be charged anything over the agreed bid amount.\n\nOf course, when you provide a service, our <font color='red'>commissions</font> are the lowest in the business.";
		lbDescription1.setText(Html.fromHtml(htmlString));
	}
	
	private void doSubmit(final String billing_add1, final String truesAddress2,
			final String truesAddress3, final String truesAddress4,
			final String truesAddress5, final String approxAddress1,
			final String approxAddress2, final String approxAddress3,
			final String approxAddress4, final String approxAddress5) {
		final ProgressDialog progressBar = new ProgressDialog(
				AddressFormActivity.this);
		progressBar.setMessage("Please wait while updating...");
		progressBar.setCancelable(false);
		progressBar.show();

		Log.d("userId=", Constants.uid);
		Log.d("userEmail=", Constants.uemail);
		
		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					
					reqEntity.addPart("billing_add1", new StringBody(
							billing_add1));
					reqEntity.addPart("address2", new StringBody(truesAddress2));
					reqEntity.addPart("address3", new StringBody(truesAddress3));
					reqEntity.addPart("address4", new StringBody(truesAddress4));
					reqEntity.addPart("address5", new StringBody(truesAddress5));
					reqEntity.addPart("aprox_address", new StringBody(
							approxAddress1));
					reqEntity.addPart("aprox_address2", new StringBody(
							approxAddress2));
					reqEntity.addPart("aprox_address3", new StringBody(
							approxAddress3));
					reqEntity.addPart("aprox_address4", new StringBody(
							approxAddress4));
					reqEntity.addPart("aprox_address5", new StringBody(
							approxAddress5));

					reqEntity.addPart("id", new StringBody(
							Constants.uid));
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "editAddress");
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
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					Log.d("Response", strResponse);
					JSONObject jsonresponse = new JSONObject(strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					try {
						resultData.setUserid(jsonresponse.getString("id"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
				
			}
		});
		thread.start();
	}
	
	private void doRequestForService(String message) {
		final ProgressDialog progressBar = new ProgressDialog(AddressFormActivity.this);
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
				requestHandler.sendMessage(msg);
			}
		});
		thr.start();
	}
	
	public String convertStreamToString(InputStream is) {
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
	
	private Handler requestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getAuthenticated() != null
							&& data.getAuthenticated().equalsIgnoreCase(
									"success")) {
						Intent intent = new Intent(AddressFormActivity.this,
								FindGoferActivity.class);
						intent.putExtra("requestForService", requestForService);
						startActivity(intent);

						finish();
						
					} else {
						showAlertDialog(data.getMessage());
					}
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(AddressFormActivity.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	/*
	 * Edit Address handler.
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
						
						String trueAddress = txtTruesAddress1.getText().toString() + ", " + 
											 txtTruesAddress2.getText().toString() + ", " + 
											 txtTruesAddress3.getText().toString() + ", " + 
											 txtTruesAddress4.getText().toString() + ", " + 
											 txtTruesAddress5.getText().toString();
						
						String approxAddress = 	txtApproxAddress1.getText().toString() + ", " + 
												txtApproxAddress2.getText().toString() + ", " + 
												txtApproxAddress3.getText().toString() + ", " + 
												txtApproxAddress4.getText().toString() + ", " + 
												txtApproxAddress5.getText().toString();
						
						Constants.approxAddress = approxAddress;
						Constants.trueAddress = trueAddress;
						
						loginDB = getSharedPreferences(LOGINDATA, 0);
						SharedPreferences.Editor editor = loginDB.edit();
						editor.putString("ApproxAdd", Constants.approxAddress);
						editor.putString("TrueAdd", Constants.trueAddress);
						editor.commit();
						
						Log.d("TRUE Address", Constants.trueAddress);
						
						
						if(Constants.isNetAvailable(AddressFormActivity.this)){
							doRequestForService("Making you customer");
						}else{
							Constants.NoInternetError(AddressFormActivity.this);
						}
						/*
						startActivity(new Intent(AddressFormActivity.this,
								MapActivity.class));
						finish();
						*/
						
					} else {
						showAlertDialog(data.getMessage());
					}
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};
	
	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	@Override
	public void onClick(View view) {
		if (view == txtBack) {
			AddressFormActivity.this.finish();
		}else if(view == btnSubmit) {
			
			hideSoftKeyboard(AddressFormActivity.this);
			
			if(txtTruesAddress1.getText().length() == 0){
				showAlertDialog("Please enter true address1.");
			}else{
				
				if(Constants.isNetAvailable(AddressFormActivity.this)){
					
					doSubmit(txtTruesAddress1.getText().toString(), txtTruesAddress2.getText().toString(),
							txtTruesAddress3.getText().toString(), txtTruesAddress4.getText().toString(),
							txtTruesAddress5.getText().toString(), txtApproxAddress1.getText().toString(),
							txtApproxAddress2.getText().toString(), txtApproxAddress3.getText().toString(),
							txtApproxAddress4.getText().toString(), txtApproxAddress5.getText().toString());
				}else{
					Constants.NoInternetError(AddressFormActivity.this);
				}
				
			}
		}
	}
}
