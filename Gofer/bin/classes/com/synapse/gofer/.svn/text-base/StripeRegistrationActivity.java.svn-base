package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import com.stripe.android.*;

import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.util.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StripeRegistrationActivity extends Activity {
	Button saveButton;
	EditText cardNumber, cvc_numbar, exp_date, email;
	String card_number_val, cvc_val;
	ProgressBar stripe_progressBar;
	int exp_year_val, exp_mnth_val;
	JSONArray arr;
	String token_id;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int USER_NOT_EXIST = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_stripe_registration);
		init();
	}

	public void init() {
		this.cardNumber = (EditText) findViewById(R.id.card_number);
		this.cvc_numbar = (EditText) findViewById(R.id.cvc_number);
		this.exp_date = (EditText) findViewById(R.id.exp_date);
		this.email = (EditText) findViewById(R.id.email);
		this.stripe_progressBar = (ProgressBar) findViewById(R.id.stripe_progressBar);
		email.setText(Constants.uemail);
	}

	public void Stripe_register(View view) {

		card_number_val = cardNumber.getText().toString();
		cvc_val = cvc_numbar.getText().toString();
		cvc_val = cvc_val.trim();
		String exp_date_val = exp_date.getText().toString();
		if (!card_number_val.equals("") && !cvc_val.equals("")
				&& !exp_date_val.equals("")) {
			try{
				card_number_val = card_number_val.replaceAll("\\s+", "");
				cvc_val = cvc_val.trim();
				String[] parts = exp_date.getText().toString().split("/");
				exp_mnth_val = Integer.parseInt(parts[0], 10);
				exp_year_val = Integer.parseInt(parts[1], 10);
				//Log.d("card details",card_number_val+"  "+cvc_val+"   "+exp_mnth_val+"  "+exp_year_val);
				saveCreditCard();
			}catch(NumberFormatException e){
				showAlertDialog("Please fill valid details!");
			}
		} else {
			showAlertDialog("Please fill all fields!");
		}

	}

	public void saveCreditCard() {

		Card card = new Card(card_number_val, exp_mnth_val, exp_year_val,
				cvc_val);

		boolean validation = card.validateCard();
		if (validation) {
			stripe_progressBar.setVisibility(View.VISIBLE);

			new Stripe().createToken(card, Constants.STRIPE_PUBLISHABLE_KEY,
					new TokenCallback() {
						@SuppressLint("NewApi")
						public void onSuccess(Token token) {

							submitToken(token.getId());

						}

						public void onError(Exception error) {
							stripe_progressBar.setVisibility(View.INVISIBLE);
							Log.d("error", "" + error);
							showAlertDialog(error.getMessage());
						}
					});
		} else {

			Log.d("not valid", "card");
			showAlertDialog("Your Card Not Valid Please Enter Valid Information");
		}
	}

	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	/*
	 * To Submit Token.
	 */
	private void submitToken(final String token) {

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("stripeToken", token));
				nameValuePairs.add(new BasicNameValuePair("stripeEmail",
						Constants.uemail));
				DataPostParser parser1 = new DataPostParser(Constants.HTTP_HOST
						+ "creditCardRegistration");
				ResultData postdata1 = parser1.getParseData(nameValuePairs);
				Message msg = handler.obtainMessage();
				if ((postdata1.getAuthenticated().equals("success"))
						&& (postdata1.getMessage()
								.equals("customer_id created successfully."))) {
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);

			}
		});
		thr.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				stripe_progressBar.setVisibility(View.INVISIBLE);
				showAlertDialog("Your Card is successfully registered");
				Constants.hasStripeRegistered = true;
				onBackPressed();
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please Try Again Later!");
			}
		}
	};

}