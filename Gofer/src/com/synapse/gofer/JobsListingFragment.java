package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.synapse.gofer.adapter.JobListingAdapter;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.parser.CategoryPostParser;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.util.Constants;

public class JobsListingFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	private final int SUCCESS = 100;
	private final int FAILURE = 101;

	private Button jobTypeBtn = null, jobPostedBtn = null, jobTime = null,
			jobDistance = null;
	private ListView mListView = null;
	private JobListingAdapter mJobListingAdapter = null;
	private ArrayList<NameValuePair> nameValuePairs = null;

	private static final int CATEGORY_LIST = 102;
	private static final int JOB_LIST = 103;

	private ArrayList<Category> catList = null;
	private String[] categoryNames = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_jobs_listing, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		jobTypeBtn = (Button) getView().findViewById(R.id.job_type);
		jobTypeBtn.setOnClickListener(this);
		jobPostedBtn = (Button) getView().findViewById(R.id.job_posted);
		jobPostedBtn.setOnClickListener(this);
		jobTime = (Button) getView().findViewById(R.id.job_time);
		jobTime.setOnClickListener(this);
		jobDistance = (Button) getView().findViewById(R.id.job_distance);
		jobDistance.setOnClickListener(this);
		mJobListingAdapter = new JobListingAdapter(null, getActivity()
				.getLayoutInflater(), null);
		mListView = (ListView) getView().findViewById(R.id.job_listview);
		mListView.setAdapter(mJobListingAdapter);
		mListView.setOnItemClickListener(this);
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
		nameValuePairs.add(new BasicNameValuePair("date", "date"));
		Log.e("SAMPLE", "PASS UID " + Constants.uid);

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}

		jobPostedBtn.setSelected(true);
	}

	@Override
	public void onClick(View v) {

		if (v == jobTypeBtn) {
			jobTypeBtn.setSelected(true);
			jobPostedBtn.setSelected(false);
			jobTime.setSelected(false);
			jobDistance.setSelected(false);

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommuniCategory download = new ServerCommuniCategory();
				download.execute(new String[] { "" });
			} else {
				Constants.NoInternetError(getActivity());
			}

		} else if (v == jobPostedBtn) {
			jobTypeBtn.setSelected(false);
			jobPostedBtn.setSelected(true);
			jobTime.setSelected(false);
			jobDistance.setSelected(false);
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("date", "date"));

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication download = new ServerCommunication();
				download.execute(new String[] { "" });
			} else {
				Constants.NoInternetError(getActivity());
			}

		} else if (v == jobTime) {
			jobTypeBtn.setSelected(false);
			jobPostedBtn.setSelected(false);
			jobTime.setSelected(true);
			jobDistance.setSelected(false);
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("time", "time"));

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication download = new ServerCommunication();
				download.execute(new String[] { "" });
			} else {
				Constants.NoInternetError(getActivity());
			}

		} else if (v == jobDistance) {
			jobTypeBtn.setSelected(false);
			jobPostedBtn.setSelected(false);
			jobTime.setSelected(false);
			jobDistance.setSelected(true);
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("distance", "distance"));

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication download = new ServerCommunication();
				download.execute(new String[] { "" });
			} else {
				Constants.NoInternetError(getActivity());
			}

		}
	}

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, ArrayList<JobsModel>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public ServerCommunication() {
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<JobsModel> doInBackground(String... strParams) {
			JobsPostParser parser = new JobsPostParser(Constants.HTTP_HOST
					+ "viewjobs");
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
			
			if(progressBar != null && progressBar.isShowing())
				progressBar.dismiss();
			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.what = JOB_LIST;
			msg.obj = dataList;
			handler.sendMessage(msg);
			try {
				if (dataList.size() != 0) {
					if (dataList.get(0).getJobData().length != 0) {
						mListView.setVisibility(View.VISIBLE);
					} else {
						mListView.setVisibility(View.GONE);
					}
				}
			} catch (Exception ex) {
				mListView.setVisibility(View.GONE);
			}
		}
	}

	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == JOB_LIST && msg.arg1 == SUCCESS) {
				ArrayList<JobsModel> dataList = (ArrayList<JobsModel>) msg.obj;
				if (dataList != null && dataList.size() > 0) {
					JobsModel model = dataList.get(0);
					if (model.getStatus().equalsIgnoreCase("success")) {
						mJobListingAdapter.refereshAdapter(model.getJobData(),
								model.getCheckBids());
					}
				}
				// else
				// showAlertDialog(model.getMessage());
			} else if (msg.what == CATEGORY_LIST && msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ArrayList) {
					catList = (ArrayList<Category>) msg.obj;
					if (catList != null) {
						categoryNames = convertArrayListToArray(catList);
					}
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());
					alertDialogBuilder.setTitle("Select category");
					alertDialogBuilder.setItems(categoryNames,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair(
											"user_id", Constants.uid));
									nameValuePairs.add(new BasicNameValuePair(
											"category_id", catList.get(which)
													.getId()));

									if (Constants.isNetAvailable(getActivity())) {
										ServerCommunication download = new ServerCommunication();
										download.execute(new String[] { "" });
									} else {
										Constants
												.NoInternetError(getActivity());
									}

								}
							});
					alertDialogBuilder.create().show();
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}

		private String[] convertArrayListToArray(ArrayList<Category> catList) {
			String[] strArr = new String[catList.size()];
			for (int i = 0; i < catList.size(); i++)
				strArr[i] = catList.get(i).getName();
			return strArr;
		}
	};

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	private class ServerCommuniCategory extends
			AsyncTask<String, String, ArrayList<Category>> {

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public ServerCommuniCategory() {
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<Category> doInBackground(String... strParams) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// nameValuePairs.add(new BasicNameValuePair("id",Constants.uid));
			CategoryPostParser parser = new CategoryPostParser(
					Constants.HTTP_HOST + "viewcategories");
			ArrayList<Category> dataList = parser.getParseData(nameValuePairs);
			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Category> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.what = CATEGORY_LIST;
			msg.obj = dataList;
			handler.sendMessage(msg);

		}
	}

	public void onBack() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		JobData job = mJobListingAdapter.getDataSource()[position];
		FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragmentpublic = new JobAndCustomerDetailBaseFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", job);
		bundle.putBoolean("displaybid", true);
		fragmentpublic.setArguments(bundle);
		transaction.replace(R.id.mainContainer, fragmentpublic,
				"JobAndCustomerDetailBaseFragment");
		transaction.addToBackStack("JobAndCustomerDetailBaseFragment");
		transaction.commit();
	}
}