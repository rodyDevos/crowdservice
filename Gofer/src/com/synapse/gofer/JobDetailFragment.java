package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.jobandcourier.JobAndCourierDetailBaseFragment;
import com.synapse.gofer.model.JobBean;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.util.Constants;

public class JobDetailFragment extends Fragment implements OnClickListener {
	// private JobBean job = null;
	private String jobId = "";
	private int viewBidUser = 0;
	JobData jobDetail = null;
	private static final int SUCCESS = 101;

	private static final int APPLY_JOB = 102;

	private boolean jobAccept = true;

	private ArrayList<NameValuePair> nameValuePairs = null;

	private TextView category = null, jobName = null, jobPrice = null,
			startAdd = null, endAdd = null, endDate = null,
			comment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_job_detail, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		iniUi(getArguments());
	}

	private void iniUi(Bundle arguments) {
		jobId = arguments.getString("data");
		viewBidUser = arguments.getInt("biduser");
		Log.d("jobAccept1", "jobAccept" + jobAccept);
		jobAccept = arguments.getBoolean("jobAccept", false);
		Log.d("jobAccept2", "jobAccept" + jobAccept);
		// job = jobDetail.getJob();
		// ((FindGoferActivity)getActivity()).setTitle(job.getName());
		category = (TextView) getView().findViewById(R.id.category_name);
		jobName = (TextView) getView().findViewById(R.id.job_name);
		jobPrice = (TextView) getView().findViewById(R.id.job_price);
		startAdd = (TextView) getView().findViewById(R.id.job_starting_address);
//		startDate = (TextView) getView().findViewById(R.id.job_starting_date);
		endAdd = (TextView) getView().findViewById(R.id.job_end_address);
		endDate = (TextView) getView().findViewById(R.id.job_end_date);
		comment = (TextView) getView().findViewById(
				R.id.job_comment_instructions);

		nameValuePairs = new ArrayList<NameValuePair>();
		ServerCommunication download = new ServerCommunication();
		download.execute(new String[] { "" });
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnCustomerDetails) {
			Toast t = Toast.makeText(getActivity(), "Under Devlopment",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
	}

	/*
	 * private Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * super.handleMessage(msg); if(msg.what == APPLY_JOB && msg.arg1 ==
	 * SUCCESS) { Toast t= Toast.makeText(getActivity(), ""+ msg.obj,
	 * Toast.LENGTH_LONG); t.setGravity(Gravity.CENTER, 0, 0); t.show();
	 * 
	 * } } };
	 */

	public String linkStatus;

	private class ServerCommunication extends
			AsyncTask<String, String, ArrayList<JobsModel>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<JobsModel> doInBackground(String... strParams) {
			JobsPostParser parser = null;
			Log.e("VIPIII", "Temp Call >> " + viewBidUser);
			if (viewBidUser == 0) {
				nameValuePairs.add(new BasicNameValuePair("user_id",
						Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("id", jobId));
				parser = new JobsPostParser(Constants.HTTP_HOST + "viewjob");
			} else {
				nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
				parser = new JobsPostParser(Constants.HTTP_HOST
						+ "ViewBidUsers");
			}
			ArrayList<JobsModel> dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ArrayList<JobsModel> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			if (dataList == null || dataList.size() == 0)
				return;
			JobsModel model = dataList.get(0);
			if (model != null) {
				String jobApplied = model.getJobApplied();
				String jobImg = model.getJobImage();
				if (viewBidUser > 0) {
					UserDetail user = model.getJobData()[0].getUser();
					if (getActivity() instanceof ViewJobsActivity)
						JobAndCourierDetailBaseFragment.courierId = user
								.getId();
					viewBidUser = 0;
					linkStatus = model.getJobData()[0].getBidDetail()
							.getLinkStatus();

					if (Constants.isNetAvailable(getActivity())) {
						ServerCommunication download = new ServerCommunication();
						download.execute(new String[] { "" });
					} else {
						Constants.NoInternetError(getActivity());
					}

				} else if (model.getStatus().equalsIgnoreCase("success")
						&& model.getJobData()[0] != null) {
					JobBean job = model.getJobData()[0].getJob();

					category.setText(model.getJobData()[0].getCategory()
							.getName());
					jobName.setText(job.getName());

					if (getActivity() instanceof FindGoferActivity)
						((FindGoferActivity) getActivity()).setTitle(job
								.getName());
					else if (getActivity() instanceof ActivityForMarkerClick) {

					} else
						((ViewJobsActivity) getActivity())
								.setTitle("Job and couriers details");
					// ((ViewJobsActivity) getActivity())
					// .SetBackText("List of Couriers");

					jobPrice.setText(job.getAmount());
					startAdd.setText(job.getFromLocation());
//					startDate.setText(job.getStartDate());
					endAdd.setText(job.getToLocation());
					endDate.setText(job.getEndDate());
					comment.setText(job.getDescription());

					if (getActivity() instanceof ViewJobsActivity) {
						if (Constants.userType == 3) {
							Log.e("VIPVIP", " Constants.userType > "
									+ Constants.userType + " jobApplied> "
									+ jobApplied);
							if (jobApplied.equalsIgnoreCase("Yes")) {
								JobAndCourierDetailBaseFragment.self
										.hideAcceptBid(true);
							} else {
								JobAndCourierDetailBaseFragment.self
										.hideAcceptBid(false);
							}
							if (jobImg != null && jobImg.length() > 0) {
								JobAndCourierDetailBaseFragment.self
										.enableAttachment(true, jobImg);
							} else {
								JobAndCourierDetailBaseFragment.self
										.enableAttachment(false, jobImg);
							}
						} else {
							if ((linkStatus != null && linkStatus.contains("A"))
									|| (linkStatus != null && linkStatus
											.contains("C"))) {
								JobAndCourierDetailBaseFragment.self
										.hideAcceptBid(true);
							} else {
								if (jobAccept)
									JobAndCourierDetailBaseFragment.self
											.hideAcceptBid(true);
								else
									JobAndCourierDetailBaseFragment.self
											.hideAcceptBid(false);
							}
							if (jobImg != null && jobImg.length() > 0) {
								JobAndCourierDetailBaseFragment.self
										.enableAttachment(true, jobImg);
							} else
								JobAndCourierDetailBaseFragment.self
										.enableAttachment(false, jobImg);
						}

					} else if (getActivity() instanceof ActivityForMarkerClick) {
						if (jobImg != null && jobImg.length() > 0) {
							JobAndCustomerDetailFromMarkerClick.self
									.enableAttachment(true, jobImg);
						} else
							JobAndCustomerDetailFromMarkerClick.self
									.enableAttachment(false, jobImg);
					} else {
						if (jobApplied.equalsIgnoreCase("Yes")) {
							JobAndCustomerDetailBaseFragment.self
									.enablebidOnJob(false);
						} else
							JobAndCustomerDetailBaseFragment.self
									.enablebidOnJob(true);

						if (jobImg != null && jobImg.length() > 0) {
							JobAndCustomerDetailBaseFragment.self
									.enableAttachment(true, jobImg);
						} else
							JobAndCustomerDetailBaseFragment.self
									.enableAttachment(false, jobImg);
					}
				}

			}

		}
	}
}
