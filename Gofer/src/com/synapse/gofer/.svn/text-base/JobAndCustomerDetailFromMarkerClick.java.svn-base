package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;

public class JobAndCustomerDetailFromMarkerClick extends Fragment implements
		BackListener, OnClickListener {
	private JobBean job = null;
	private JobData jobDetail = null;
	private static final int SUCCESS = 101;

	private static final int APPLY_JOB = 102;
	private Button btn = null, attachment = null;

	public static JobAndCustomerDetailFromMarkerClick self = null;

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
		iniUi(getArguments());
		if (getActivity() instanceof FindGoferActivity)
			((FindGoferActivity) getActivity()).setBackListener(this);
	}

	private void iniUi(Bundle arguments) {
		// jobDetail = (JobData) arguments.getSerializable("data");
		// bidflag = arguments.getBoolean("displaybid");
		//
		// job = jobDetail.getJob();
		// if(getActivity() instanceof FindGoferActivity)
		// ((FindGoferActivity)getActivity()).setTitle(job.getName());

		questionBtn = (Button) getView().findViewById(R.id.ques_job);
		questionBtn.setVisibility(View.GONE);

		btn = (Button) getView().findViewById(R.id.apply_for_job);
		btn.setVisibility(View.GONE);
		// servicePrice = (Button)getView().findViewById(R.id.service_price);
		// servicePrice.setOnClickListener(this);
		attachment = (Button) getView().findViewById(R.id.attachment);
		attachment.setOnClickListener(this);

		customerbtn = (Button) getView().findViewById(R.id.btnCustomerDetails);
		customerbtn.setVisibility(View.GONE);

		jobDetailBtn = (Button) getView().findViewById(R.id.btnJobDetails);
		jobDetailBtn.setVisibility(View.GONE);

		jobDetailBtn.performClick();

		// enablebidOnJob(bidflag);

		int displayType = arguments.getInt("displayType");
		String ID = arguments.getString("id");
		if (displayType == MapActivity.JOB_MARKER) {
			displayJobDetail(ID);
		} else if (displayType == MapActivity.ACTIVE_COURIER
				|| displayType == MapActivity.COURIER_MARKER) {
			displayCoustomerCourierDetail("3", ID);
			attachment.setVisibility(View.GONE);
		} else {
			displayCoustomerCourierDetail("2", ID);
			attachment.setVisibility(View.GONE);
		}

	}

	private void displayCoustomerCourierDetail(String userType, String userId) {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();

		Fragment fragmentpublic = new CustomerDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("UserId", userId);
		bundle.putString("UserType", userType);

		fragmentpublic.setArguments(bundle);

		ft.replace(R.id.job_customer_detail_container, fragmentpublic,
				"CustomerDetailFragment");
		ft.commit();
	}

	private void displayJobDetail(String jobID) {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();

		Fragment fragmentpublic = new JobDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("data", jobID);
		fragmentpublic.setArguments(bundle);

		ft.replace(R.id.job_customer_detail_container, fragmentpublic,
				"JobDetailFragment");
		ft.commit();
	}

	@Override
	public void backPressed() {

		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
		((FindGoferActivity) getActivity()).setBackListener(null);
		((FindGoferActivity) getActivity()).setTitle("Jobs Listing");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.apply_for_job) {
			if (jobPrice == null || jobPrice.length() == 0
					|| jobPrice.equals(""))
				jobPriceAlert(true);
			else {
				ServerCommuniBidjob download = new ServerCommuniBidjob();
				download.execute(new String[] { "" });
			}
		}
		/*
		 * else if(v.getId() == R.id.service_price) { jobPriceAlert(false); }
		 */
		else if (v.getId() == R.id.btnCustomerDetails) {
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new CustomerDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("UserId", job.getUserId());
			Log.e("VIP", "Local Logic 2 ");
			bundle.putString("UserType", "2");

			fragmentpublic.setArguments(bundle);

			ft.replace(R.id.job_customer_detail_container, fragmentpublic,
					"CustomerDetailFragment");
			ft.commit();

			customerbtn.setBackgroundResource(R.drawable.rightcorner);
			jobDetailBtn
					.setBackgroundResource(R.drawable.leftcorner_unselected);

		} else if (v.getId() == R.id.btnJobDetails) {
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			Fragment fragmentpublic = new JobDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data", job.getId());
			fragmentpublic.setArguments(bundle);

			ft.replace(R.id.job_customer_detail_container, fragmentpublic,
					"JobDetailFragment");
			ft.commit();

			customerbtn
					.setBackgroundResource(R.drawable.rightcorner_unselected);
			jobDetailBtn.setBackgroundResource(R.drawable.leftcorner);
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
					JobAndCustomerDetailFromMarkerClick.this.getActivity(),
					ContactEditDialogActivity.class);
			intent.putExtra("from_id", Constants.uid);
			intent.putExtra("to_id", job.getUserId());
			intent.putExtra("job_id", job.getId());
			intent.putExtra("from", "jobdetails");
			intent.putExtra("ws", "messaging");
			getActivity().startActivity(intent);
		}
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

	private void jobPriceAlert(final boolean flag) {
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

		ll.setGravity(Gravity.CENTER);
		ll.addView(input);

		alert.setView(ll);
		alert.setMessage("Please enter job price");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				if (input.getText().toString().length() > 1) {
					jobPrice = input.getText().toString();
					if (flag) {
						if (Constants.isNetAvailable(getActivity())) {
							ServerCommuniBidjob download = new ServerCommuniBidjob();
							download.execute(new String[] { "" });
						} else {
							Constants.NoInternetError(getActivity());
						}

					}
				} else {
					Toast t = Toast.makeText(getActivity(),
							"Please enter valid amount", Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
				}

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
