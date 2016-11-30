package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.model.Card;
import com.synapse.gofer.parser.CardPostParser;
import com.synapse.gofer.util.Constants;

public class ProfilePersonalFragment extends Fragment implements
		OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private Button btnCardChange, btnBankDetailChange;
	private TextView bnkaccntno, creaditcardno;

	// private EditText cardNumber = null, cardCvv = null, cardExpiry = null;
	// private Button submit = null;
	// private boolean editFlag = false;

	public ProfilePersonalFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_personalprofile,
				container, false);
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {

		if (Constants.isNetAvailable(getActivity())) {
			new ServerCommunication().execute("", "", "");
		} else {
			Constants.NoInternetError(getActivity());
		}
		creaditcardno = (TextView) getView().findViewById(R.id.creaditcardno);
		creaditcardno.setText(Constants.cardInfo);
		bnkaccntno = (TextView) getView().findViewById(R.id.bnkaccntno);
		bnkaccntno.setText(Constants.BankInfo);
		btnCardChange = (Button) getView().findViewById(R.id.btnCardChange);
		btnBankDetailChange = (Button) getView().findViewById(
				R.id.btnBankDetailChange);
		btnCardChange.setOnClickListener(this);
		btnBankDetailChange.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == btnCardChange) {

			Intent intent = new Intent(getActivity(),
					StripeRegistrationActivity.class);
			startActivity(intent);

		} else if (v == btnBankDetailChange) {
			Intent intent = new Intent(getActivity(), BankDetailsActivity.class);
			startActivity(intent);
		}
	}

	private class ServerCommunication extends AsyncTask<String, String, Card> {
		ProgressDialog progressBar = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.setCancelable(false);
			progressBar.show();
		}

		@Override
		protected Card doInBackground(String... params) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			CardPostParser parser = null;
			/*
			 * if (editFlag) { nameValuePairs.add(new
			 * BasicNameValuePair("cardNumber",
			 * cardNumber.getText().toString())); nameValuePairs.add(new
			 * BasicNameValuePair("cardCvvNumber",
			 * cardCvv.getText().toString())); String str =
			 * cardExpiry.getText().toString(); String month = ""; String year =
			 * ""; if (str.length() > 0) { month = str.split("/")[0]; year =
			 * str.split("/")[1]; } nameValuePairs.add(new
			 * BasicNameValuePair("dcExpMonth", month)); nameValuePairs.add(new
			 * BasicNameValuePair("dcExpYear", year)); parser = new
			 * CardPostParser(Constants.HTTP_HOST + "EditCardInfo"); } else
			 */
			parser = new CardPostParser(Constants.HTTP_HOST + "ViewCardInfo");

			Card card = parser.getParseData(nameValuePairs);

			return card;
		}

		@Override
		protected void onPostExecute(Card result) {
			super.onPostExecute(result);
			progressBar.dismiss();

			// if (result != null && !editFlag) {
			// cardNumber.setText(result.getCardNumber());
			// cardCvv.setText(result.getCardCvvNumber());
			// String str = result.getDcExpMonth() + "/"
			// + result.getDcExpYear();
			// cardExpiry.setText(str);
			// } else if (result != null && editFlag) {
			// editFlag = false;

			if (result != null) {
				showAlertDialog(result.getMessage());
			}

			// }
		}

	}

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();

	}

	/*
	 * http://www.sampatti.com/gofer/services/EditCardInfo user_id cardNumber
	 * cardCvvNumber dcExpMonth
	 */

}
