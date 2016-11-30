package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.util.Constants;

public class AlertDialogFragment extends Fragment {

	private RatingBar ratingBar = null;
	private TextView review = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_customer_detail_alert_dialog,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		Button donebtn = (Button) getView().findViewById(
				R.id.customerdetail_alert_done);
		donebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				if (fm.getBackStackEntryCount() > 0) {
					fm.popBackStack();
				}
			}
		});

		ratingBar = (RatingBar) getView().findViewById(R.id.user_rating);

		review = (TextView) getView().findViewById(R.id.user_review);

		String userId = getArguments().getString("user_id");
		String jobId = getArguments().getString("job_id");

		if (Constants.isNetAvailable(getActivity())) {
			new GetUserRating().execute(userId, jobId);
		} else {
			Constants.NoInternetError(getActivity());
		}
	}

	private class GetUserRating extends AsyncTask<String, String, Float> {
		private ProgressDialog progressDialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Please wait...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Float doInBackground(String... params) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
			nameValuePairs.add(new BasicNameValuePair("job_id", params[1]));
			String url = Constants.HTTP_HOST + "viewRating";

			HttpPostConnector conn = new HttpPostConnector(url, nameValuePairs);
			String response = conn.getResponseData();
			Float rating = 0.0f;
			try {
				if (response != null && response.length() > 0) {
					JSONObject jsonResponse = new JSONObject(response);
					if (jsonResponse.has("status")
							&& jsonResponse.getString("status")
									.equalsIgnoreCase("fail")) {
						Toast t = Toast.makeText(getActivity(),
								"No rating found for this job",
								Toast.LENGTH_LONG);
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();

						ratingBar.setRating(0);

						review.setText("");
					} else if (jsonResponse.has("Rating")) {
						JSONObject jsonObject = jsonResponse
								.getJSONObject("Rating");
						float rate = 0;
						try {
							rate = Float.parseFloat(jsonObject
									.getString("rate"));
						} catch (Exception e) {
							rate = 0;
						}
						ratingBar.setRating(rate);

						review.setText(jsonObject.getString("feedback"));
					}
				}

			} catch (Exception e) {
			}

			return rating;
		}

		@Override
		protected void onPostExecute(Float result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result != null) {
			}
		}
	}

}
