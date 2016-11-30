package com.synapse.gofer.jobandcourier;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.synapse.gofer.CustomerDetailFragment;
import com.synapse.gofer.JobDetailFragment;
import com.synapse.gofer.JobImageActivity;
import com.synapse.gofer.PaypalWebviewAcivity;
import com.synapse.gofer.R;
import com.synapse.gofer.ViewJobsActivity;
import com.synapse.gofer.ViewJobsActivity.BackListener;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.parser.FundPostParser;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;

public class JobAndCourierDetailBaseFragment extends Fragment implements
		BackListener, OnClickListener {
	private JobBean job = null;
	// private JobData jobDetail = null;
	private static final int SUCCESS = 101;
	private int x = 0;
	private static final int APPLY_JOB = 102;
	private String jobId = "";
	public static String courierId = "";
	private String tempcourierId = "";
	private RadioButton customerbtn = null;
	private RadioButton jobDetailBtn = null;
	private LinearLayout header = null;
	private Button acceptBid = null, attachment = null, btn_contact;
	private Boolean jobAccept = false;
	RelativeLayout relative_option;
	public static JobAndCourierDetailBaseFragment self = null;

	private boolean paypalShow = false;
	private String mMerch, mService, mPayKey, Status = "";
	boolean onlyJob;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("SAMPLE", " JobAndCourierDetailBaseFragment CALL ");
		return inflater.inflate(R.layout.fragment_job_and_courier_detail_base,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;
		iniUi(getArguments());

		// ((ViewJobsActivity) getActivity()).setBackListener(this);

	}

	private void iniUi(Bundle arguments) {
		jobId = arguments.getString("data");
		tempcourierId = arguments.getString("courierId");
		courierId = arguments.getString("courierId");
		x = arguments.getInt("bidCount");
		jobAccept = arguments.getBoolean("jobAccept", false);
		Status = arguments.getString("status");

		mMerch = arguments.getString("merch", "");
		mService = arguments.getString("service", "");

		onlyJob = arguments.getBoolean("onlyJobDetail");
		btn_contact = (Button) getView().findViewById(R.id.btn_contact);
		header = (LinearLayout) getView().findViewById(R.id.header);
		acceptBid = (Button) getView().findViewById(R.id.accept_for_bid);
		acceptBid.setOnClickListener(this);
		customerbtn = (RadioButton) getView().findViewById(
				R.id.btnCustomerDetails);
		customerbtn.setOnClickListener(this);
		relative_option = (RelativeLayout) getView().findViewById(
				R.id.relative_option);
		attachment = (Button) getView().findViewById(R.id.attachment);
		attachment.setOnClickListener(this);

		jobDetailBtn = (RadioButton) getView().findViewById(R.id.btnJobDetails);
		jobDetailBtn.setOnClickListener(this);

		btn_contact.setOnClickListener(this);
		// Show Contact Button or not
		CheckStatus();

		if (onlyJob) {
			hideAcceptBid(true);
			hideHeader(true);
		}
		jobDetailBtn.performClick();
	}

	private void CheckStatus() {
		if (Status.equals("A")) {
			btn_contact.setVisibility(View.VISIBLE);
			acceptBid.setVisibility(View.GONE);
		}
	}

	@Override
	public void backPressed() {

		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		} else
			getActivity().finish();

		((ViewJobsActivity) getActivity()).setBackListener(null);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.accept_for_bid) {

			if (!paypalShow) {
				paypalShow = true;
			} else {
				return;
			}

			try {

				if (Constants.isNetAvailable(getActivity())) {
					ServerCommunicateFund servicefund = new ServerCommunicateFund();
					servicefund.execute();
				} else {
					Constants.NoInternetError(getActivity());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// ServerCommuniBidjob download=new ServerCommuniBidjob();
			// download.execute(new String[]{""});
		} else if (v == attachment) {

			Intent intent = new Intent(getActivity(), JobImageActivity.class);
			intent.putExtra("job_image_url", (String) attachment.getTag());
			intent.putExtra("job_name", "");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_bottom_to_top,
					0);
		} else if (v.getId() == R.id.btnCustomerDetails) {

			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new CustomerDetailFragment();
			Bundle bundle = new Bundle();

			if (!onlyJob && tempcourierId != "")
				courierId = tempcourierId;
			bundle.putString("UserId", courierId);
			bundle.putString("UserType", "3");
			bundle.putBoolean("onlyjob", onlyJob);
			fragmentpublic.setArguments(bundle);
			ft.replace(R.id.job_courier_detail_container, fragmentpublic,
					"CustomerDetailFragment");
			ft.commit();

			jobDetailBtn.setSelected(false);
			customerbtn.setSelected(true);
			relative_option.setVisibility(View.GONE);
			// customerbtn.setBackgroundResource(R.drawable.rightcorner);
			// jobDetailBtn
			// .setBackgroundResource(R.drawable.leftcorner_unselected);
			// ((ViewJobsActivity) getActivity()).setTitle("Courier Details");
			((ViewJobsActivity) getActivity())
					.setTitle("Jobs and couriers Details");
			// if (x == 0)
			// ((ViewJobsActivity) getActivity()).SetBackText("Back");
			// else
			// ((ViewJobsActivity) getActivity())
			// .SetBackText("List of Couriers");
		} else if (v.getId() == R.id.btnJobDetails) {
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new JobDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data", jobId);
			bundle.putInt("biduser", x);
			bundle.putBoolean("jobAccept", jobAccept);
			fragmentpublic.setArguments(bundle);
			ft.replace(R.id.job_courier_detail_container, fragmentpublic,
					"JobDetailFragment");
			ft.commit();

			// customerbtn
			// .setBackgroundResource(R.drawable.rightcorner_unselected);
			// jobDetailBtn.setBackgroundResource(R.drawable.leftcorner);
			relative_option.setVisibility(View.VISIBLE);
			jobDetailBtn.setSelected(true);
			customerbtn.setSelected(false);

			((ViewJobsActivity) getActivity()).setTitle("JobDetails");
		}
		if (v == btn_contact) {
			Intent intent = new Intent(getActivity(),
					com.synapse.gofer.ContactActivity.class);
			intent.putExtra("data", jobId);
			Log.e("VIPVIP", "Redirect " + jobId);
			startActivity(intent);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("VIPI", "Call handleMessage");
			if (msg.what == APPLY_JOB && msg.arg1 == SUCCESS) {
				backPressed();
				// backPressed();
			}
		}
	};

	private class ServerCommuniBidjob extends AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
			nameValuePairs.add(new BasicNameValuePair("user_id", courierId));
			nameValuePairs.add(new BasicNameValuePair("pay_key", mPayKey));
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "UpdateBid");
			String dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.what = APPLY_JOB;
			msg.obj = dataList;
			handler.sendMessage(msg);
		}
	}

	public void hideHeader(boolean flag) {
		if (flag)
			header.setVisibility(View.GONE);
		else
			header.setVisibility(View.VISIBLE);
	}

	public void hideAcceptBid(boolean flag) {
		if (flag)
			acceptBid.setVisibility(View.GONE);
		else
			acceptBid.setVisibility(View.VISIBLE);
	}

	public void enableAttachment(boolean flag, String url) {
		if (flag) {
			if (url != null && url.length() > 0) {
				attachment.setVisibility(View.VISIBLE);
				attachment.setTag(url);
			}

		} else {
			attachment.setVisibility(View.GONE);
			attachment.setTag(url);
		}
	}

	private class ServerCommunicateFund extends
			AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
			nameValuePairs.add(new BasicNameValuePair("user_id", courierId));
			FundPostParser parser = new FundPostParser(Constants.HTTP_HOST
					+ "fund");
			String dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			if (!dataList.equals("")) {
				Intent intent = new Intent(getActivity(),
						PaypalWebviewAcivity.class);
				intent.putExtra("url", dataList);
				startActivity(intent);
			}
		}
	}
}
