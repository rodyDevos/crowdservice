package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.synapse.contact.ContactEditDialogActivity;
import com.synapse.gofer.FindGoferActivity.BackListener;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;

public class JobAndCustomerDetailBaseFragment extends Fragment implements
		BackListener, OnClickListener {
	private JobBean job = null;
	private JobData jobDetail = null;
	private static final int SUCCESS = 101;
	private static final int FAIL = 104;
	private static final int IS_USER_EXIST = 103;
	private static final int APPLY_JOB = 102;
	private Button btn = null, attachment = null;
	private ProgressDialog progressBar1;
	public static JobAndCustomerDetailBaseFragment self = null;
	private boolean IS_JOB_PRICE_POPUP = false;

	private Button customerbtn = null;
	private Button jobDetailBtn = null;
	private String jobPrice = "";
	private boolean bidflag = false;
	private Button questionBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_job_and_customer_detai_basel,
				container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;
		Log.e("VIPI",
				"onActivityCreated >> JobAndCustomerDetailBaseFragment 1.");
		iniUi(getArguments());
		if (getActivity() instanceof FindGoferActivity)
			((FindGoferActivity) getActivity()).setBackListener(this);

	}

	private void iniUi(Bundle arguments) {
		Constants.hasPayPalRegistered = false;

		jobDetail = (JobData) arguments.getSerializable("data");
		bidflag = arguments.getBoolean("displaybid");

		job = jobDetail.getJob();
		if (getActivity() instanceof FindGoferActivity)
			((FindGoferActivity) getActivity()).setTitle(job.getName());

		questionBtn = (Button) getView().findViewById(R.id.ques_job);
		questionBtn.setOnClickListener(this);

		btn = (Button) getView().findViewById(R.id.apply_for_job);
		btn.setOnClickListener(this);
		// servicePrice = (Button)getView().findViewById(R.id.service_price);
		// servicePrice.setOnClickListener(this);
		attachment = (Button) getView().findViewById(R.id.attachment);
		attachment.setOnClickListener(this);

		customerbtn = (Button) getView().findViewById(R.id.btnCustomerDetails);
		customerbtn.setOnClickListener(this);

		jobDetailBtn = (Button) getView().findViewById(R.id.btnJobDetails);
		jobDetailBtn.setOnClickListener(this);

		jobDetailBtn.setSelected(true);
		jobDetailBtn.performClick();

		enablebidOnJob(bidflag);

	}

	@Override
	public void backPressed() {

		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
		((FindGoferActivity) getActivity()).setBackListener(null);
		((FindGoferActivity) getActivity()).setTitle("Open For Bids");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.apply_for_job) {
			Log.e("Send", "Click Ok Apply  Job");
			isUserRegister();
		}
		/*
		 * else if(v.getId() == R.id.service_price) { jobPriceAlert(false); }
		 */
		else if (v.getId() == R.id.btnCustomerDetails) {

			jobDetailBtn.setSelected(false);
			customerbtn.setSelected(true);
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new CustomerDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("UserId", job.getUserId());
			bundle.putString("UserType", "2");

			fragmentpublic.setArguments(bundle);

			ft.replace(R.id.job_customer_detail_container, fragmentpublic,
					"CustomerDetailFragment");
			ft.commit();

		} else if (v.getId() == R.id.btnJobDetails) {

			jobDetailBtn.setSelected(true);
			customerbtn.setSelected(false);

			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new JobDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data", job.getId());
			fragmentpublic.setArguments(bundle);

			ft.replace(R.id.job_customer_detail_container, fragmentpublic,
					"JobDetailFragment");
			ft.commit();

		} else if (v.getId() == R.id.attachment) {
			Intent intent = new Intent(getActivity(), JobImageActivity.class);
			intent.putExtra("job_image_url", (String) attachment.getTag());
			intent.putExtra("job_name", job.getName());
			startActivity(intent);

			getActivity().overridePendingTransition(R.anim.slide_bottom_to_top,
					0);
		} else if (v.getId() == R.id.ques_job) {

			// WS : messaging
			// Post var : from_id, to_id, job_id, message

			Intent intent = new Intent(
					JobAndCustomerDetailBaseFragment.this.getActivity(),
					ContactEditDialogActivity.class);
			intent.putExtra("from_id", Constants.uid);
			intent.putExtra("to_id", job.getUserId());
			intent.putExtra("job_id", job.getId());
			intent.putExtra("from", "jobdetails");
			intent.putExtra("ws", "messaging");
			intent.putExtra("textChange", "Ask the customer about this job");
			getActivity().startActivity(intent);
		}
	}

	private void isUserRegister() {

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("user_id",
						Constants.uid));
				// DataPostParser parser = new
				// DataPostParser(Constants.HTTP_HOST+"getRecipientid"); //
				// STRIPE
				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "getPaypalId"); // PAYPAL

				ResultData dataList = parser.getParseData(nameValuePairs);
				Message msg = handler.obtainMessage();
				msg.what = IS_USER_EXIST;
				// if((dataList.getAuthenticated().equals("success")) &&
				// (dataList.getMessage().equals("Recipient Id found "))) //
				// STRIPE
				if ((dataList.getAuthenticated().equals("success"))
						&& (dataList.getMessage().equals("Paypal Id found"))) // PAYPAL
					msg.arg1 = SUCCESS;
				else
					msg.arg1 = FAIL;
				handler.sendMessage(msg);
			}
		});
		thr.start();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == APPLY_JOB && msg.arg1 == SUCCESS) {
				// Toast t= Toast.makeText(getActivity(), ""+ msg.obj,
				// Toast.LENGTH_LONG);
				// t.setGravity(Gravity.CENTER, 0, 0);
				// t.show();

				enablebidOnJob(false);
				backPressed();
				((FindGoferActivity) getActivity()).setTitle("Open For Bids");
			} else if (msg.what == IS_USER_EXIST && msg.arg1 == SUCCESS) {

				if (jobPrice == null || jobPrice.length() == 0
						|| jobPrice.equals("")) {
					if (!IS_JOB_PRICE_POPUP)
						jobPriceAlert(true);

				} else {
					ServerCommuniBidjob download = new ServerCommuniBidjob();
					download.execute(new String[] { "" });
				}

			} else if (msg.what == IS_USER_EXIST && msg.arg1 == FAIL) {
				// accountRegisterAlert(); // STRIPE
				Intent intent = new Intent(getActivity(),
						PayPalDetailsActivity.class); // PAYPAL
				startActivity(intent);
			}
		}
	};

	private class ServerCommuniBidjob extends AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", job.getId()));
			nameValuePairs.add(new BasicNameValuePair("job_posted_by_id", job
					.getUserId()));
			nameValuePairs.add(new BasicNameValuePair("job_apply_by_id",
					Constants.uid));
			String date = Constants.getCurrentDateTimeForBid();
			nameValuePairs.add(new BasicNameValuePair("date", date));
			nameValuePairs.add(new BasicNameValuePair("amount", jobPrice));
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "bidjob");
			String dataList = parser.getParseData(nameValuePairs);
			// Log.d("nameValuePairs",""+nameValuePairs);

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

	/*
	 * Adding this method for registering paypal or bank details in server if
	 * not exist
	 */

	private void accountRegisterAlert() {
		final AlertDialog.Builder viewDialog = new AlertDialog.Builder(
				getActivity());

		LayoutInflater li = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		// li.setContentView(R.layout.messagewith_ok_cancle);
		View dialogView = li.inflate(R.layout.popup_paypal_detials, null);
		viewDialog.setView(dialogView);
		final Dialog dd = viewDialog.create();
		dd.show();
		progressBar1 = new ProgressDialog(getActivity());
		progressBar1.setMessage("Please wait while loading...");
		progressBar1.setCancelable(false);
		// viewDialog.

		Button btnPaypal = (Button) dialogView.findViewById(R.id.btnPaypal);

		Button btnBnk = (Button) dialogView.findViewById(R.id.btnBnk);
		btnBnk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dd.dismiss();
				progressBar1.show();
				Intent intent = new Intent(getActivity(),
						BankDetailsActivity.class);
				startActivity(intent);
			}
		});

		btnPaypal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dd.dismiss();
				progressBar1.show();
				Intent intent = new Intent(getActivity(),
						PayPalDetailsActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (progressBar1 != null) {
			if (progressBar1.isShowing())
				progressBar1.dismiss();
		}
		if (Constants.hasPayPalRegistered) {
			if (jobPrice == null || jobPrice.length() == 0
					|| jobPrice.equals("")) {
				if (!IS_JOB_PRICE_POPUP)
					jobPriceAlert(true);

			} else {

				if (Constants.isNetAvailable(getActivity())) {
					ServerCommuniBidjob download = new ServerCommuniBidjob();
					download.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(getActivity());
				}
			}

		}
	}

	private void jobPriceAlert(final boolean flag) {
		IS_JOB_PRICE_POPUP = true;
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		final EditText input = new EditText(getActivity());
		input.setHint("Enter job price");
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (input.getText().toString() != null
						&& input.getText().toString().length() == 0) {
					input.setText("$");
					input.setSelection(1);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		input.setInputType(InputType.TYPE_CLASS_NUMBER);

		LinearLayout ll = new LinearLayout(getActivity());
		input.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		ll.setGravity(Gravity.CENTER);
		ll.addView(input);

		alert.setView(ll);
		alert.setTitle("CrowdService");
		alert.setMessage("Please provide service price including an estimated merchandize price");
		alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				IS_JOB_PRICE_POPUP = false;
				if (input.getText().toString().length() > 1) {
					jobPrice = input.getText().toString();
					if (flag) {
						ServerCommuniBidjob download = new ServerCommuniBidjob();
						download.execute(new String[] { "" });

					}
				} else {
					Toast t = Toast.makeText(getActivity(),
							"Please enter valid amount", Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
				}

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alert.show();
	}

	private class ServerCommunicationJobDetail extends
			AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", job.getId()));
			nameValuePairs.add(new BasicNameValuePair("user_id", job
					.getUserId()));

			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "viewjob");
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

	public void enablebidOnJob(boolean flag) {
		if (bidflag && flag) {
			btn.setVisibility(View.VISIBLE);
			// servicePrice.setVisibility(View.VISIBLE);
		} else {
			btn.setVisibility(View.GONE);
			// servicePrice.setVisibility(View.GONE);
		}

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

}
