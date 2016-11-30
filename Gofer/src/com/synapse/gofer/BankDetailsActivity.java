package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.util.Constants;

public class BankDetailsActivity extends FragmentActivity implements OnClickListener {
	private BackListener backListner = null;
	private EditText edit_legal_name,edit_bnk_dtl,edit_rtn;
	private Button btnSubmit,btnreset;
	private WebView webview ; 
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	  public ProgressDialog progressBar;
	
//	public static final int FORGOTPASS = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bank_details);
		initViews();
	}

	
	private void initViews() {
		TextView back = (TextView)findViewById(R.id.viewjob_btnBack);
		back.setOnClickListener(this);
		edit_legal_name = (EditText)findViewById(R.id.edit_legal_name);
		webview = (WebView)findViewById(R.id.webView_support);
		edit_bnk_dtl = (EditText)findViewById(R.id.edit_bnk_dtl);
		edit_rtn = (EditText)findViewById(R.id.edit_rtn1);
		btnSubmit = (Button) findViewById(R.id.submit_paypal_email);
		btnSubmit.setOnClickListener(this);
		btnreset = (Button) findViewById(R.id.reset_form);		
		btnreset.setOnClickListener(this);
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLoadsImagesAutomatically(true);
		webview.setWebViewClient(new MyBrowser());
	
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.viewjob_btnBack) {
			
			if (backListner != null) {
				backListner.backPressed();
			} else {
				if (webview.getVisibility() == View.VISIBLE) {
					webview.setVisibility(View.GONE);
				} else {
					finish();
				}
			}
		} else if (v == btnSubmit) {

			if (validate(BankDetailsActivity.this)) {
				btnSubmit.setEnabled(false);
				doSubmit(edit_legal_name.getText().toString(), edit_bnk_dtl
						.getText().toString(), edit_rtn.getText().toString());
			}

		} else if (v == btnreset) {
			edit_legal_name.setText("");
			edit_bnk_dtl.setText("");
			edit_rtn.setText("");
		}
	}
	
	public interface BackListener {
		
		public void backPressed();
	}

	public void setBackListener(BackListener listener) {
		
		backListner = listener;
	}
	
	boolean validate(Context cont) {
		if ((edit_legal_name.getText().toString() == null) || (edit_legal_name.getText().toString().equals(""))) {
			showAlertDialog("Please enter legal name.");
			return false;
		} else if ((edit_bnk_dtl.getText().toString() == null) || (edit_bnk_dtl.getText().toString().equals(""))) {
			showAlertDialog("Please enter bank account number.");
			return false;
		}else if ((edit_rtn.getText().toString() == null) || (edit_rtn.getText().toString().equals(""))) {
			showAlertDialog("Please enter routine number.");
			return false;
		}
		
		return true;
	}
	
	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}
	
	
	/*
	 * To Submit Record.
	 */
	private void doSubmit(final String legal_name,final String acnt_no,final String rtn_no) {
		final ProgressDialog progressBar = new ProgressDialog(BankDetailsActivity.this);
		progressBar.setMessage("Please wait while loading...");
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() 
			{				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userID",Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("lName",legal_name));
				nameValuePairs.add(new BasicNameValuePair("bnkAccno",acnt_no));
				nameValuePairs.add(new BasicNameValuePair("routineno",rtn_no));
				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST+"bnkRegister");
				ResultData postdata=parser.getParseData(nameValuePairs);
				Message msg = handler.obtainMessage();
				if ((postdata.getAuthenticated().equals("success")) && (postdata.getMessage().equals("recipient created successfully."))) {
					progressBar.dismiss();
					msg.arg1 = SUCCESS;
					handler.sendMessage(msg);
					
				}else{
					progressBar.dismiss();
					msg.arg1 = FAILURE;
					handler.sendMessage(msg);
				
					
				}
			}
		});
		thr.start();
	}	
	
	
	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {	
				Constants.hasPayPalRegistered = true;
				btnSubmit.setEnabled(true);
				onBackPressed();
				
			}
			else if (msg.arg1 == FAILURE) {	
				btnSubmit.setEnabled(true);
				openGoferSupport();
			}
		}
	};
	
	public void openGoferSupport(){
		final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
		
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.dailog_gofer_support, null);
		viewDialog.setView(dialogView);
		final Dialog dailog = viewDialog.create();
		dailog.show();
		
		//viewDialog.
		
		
		Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
		
		Button btnContact = (Button) dialogView.findViewById(R.id.btnContact);
		btnContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dailog.dismiss();
				webview.setVisibility(View.VISIBLE);
				
				webview.loadUrl("http://www.crowdserviceinc.com/Gofer/Support");
				
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
		
	}
	
	  private class MyBrowser extends WebViewClient {
		  
		  @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            // TODO Auto-generated method stub
	            super.onPageStarted(view, url, favicon);
	            progressBar = new ProgressDialog(BankDetailsActivity.this);
	            progressBar.setMessage("Please wait while loading...");
	    		progressBar.setCancelable(false);
	            progressBar.show();
	        }
		  
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         Log.d("s1","s1");
	         
	         return true;
	      }
	      
	      @Override
	        public void onPageFinished(WebView view, String url) {
	            // TODO Auto-generated method stub
	            super.onPageFinished(view, url);
	            progressBar.cancel();	
	           // progressBar.setVisibility(View.GONE);
	        }
	   }
	
	
}
