package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.util.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PayPalDetailsActivity extends FragmentActivity implements OnClickListener {
	private BackListener backListner = null;
	private EditText ppl_email,cppl_email;
	private Button btnSubmit,btnreset;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
//	public static final int FORGOTPASS = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pay_pal_details);
		initViews();
	}

	
	private void initViews() {
		TextView back = (TextView)findViewById(R.id.viewjob_btnBack);
		back.setOnClickListener(this);
		ppl_email = (EditText)findViewById(R.id.edit_paypal_email);
		cppl_email = (EditText)findViewById(R.id.confirm_paypal_email);
		btnSubmit = (Button) findViewById(R.id.submit_paypal_email);
		btnSubmit.setOnClickListener(this);
		btnreset = (Button) findViewById(R.id.reset_form);		
		btnreset.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.viewjob_btnBack)
		{
			
			if(backListner != null){
				backListner.backPressed();
			}else
				finish();
		}else if(v == btnSubmit){
			
			if(validate(PayPalDetailsActivity.this)){
				btnSubmit.setEnabled(false);
				doSubmit(ppl_email.getText().toString());
			}
			
		}else if(v == btnreset){
			ppl_email.setText("");
			cppl_email.setText("");
		}
	}
	
	public interface BackListener {
		
		public void backPressed();
	}

	public void setBackListener(BackListener listener) {
		
		backListner = listener;
	}
	
	boolean validate(Context cont) {
		if ((ppl_email.getText().toString() == null) || (ppl_email.getText().toString().equals(""))) {
			showAlertDialog("Please enter email address.");
			return false;
		} else if (!isValidEmail(ppl_email.getText().toString())) {
			showAlertDialog("Please enter valid email address.");
			return false;
		} else if ((cppl_email.getText().toString() == null) || (cppl_email.getText().toString().equals(""))) {
			showAlertDialog("Please enter confirm email address.");
			return false;
		} else if (!isValidEmail(cppl_email.getText().toString())) {
			showAlertDialog("Please enter valid confirm email address.");
			return false;
		} else if (!(ppl_email.getText().toString().equals(cppl_email.getText().toString()))) {
			showAlertDialog("Email does not match!.");
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
	 *To validate.  
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	
	/*
	 * To Submit Record.
	 */
	private void doSubmit(final String email) {
		final ProgressDialog progressBar = new ProgressDialog(PayPalDetailsActivity.this);
		progressBar.setMessage("Please wait while loading...");
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() 
			{				
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userID",Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("paypalemail",email));
				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST+"paypalRegister");
				ResultData postdata=parser.getParseData(nameValuePairs);
				Message msg = handler.obtainMessage();
				if ((postdata.getAuthenticated().equals("success")) && (postdata.getMessage().equals("paypal email regstered successfully."))) {
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
				showAlertDialog("Please try later!");
			}
		}
	};
	
	
}
