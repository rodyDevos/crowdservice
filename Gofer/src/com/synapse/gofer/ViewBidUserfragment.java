package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.synapse.gofer.FindGoferActivity.BackListener;
import com.synapse.gofer.adapter.ViewBidUserAdapter;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.CourierModel;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.CourierListParser;
import com.synapse.gofer.parser.FundPostParser;
import com.synapse.gofer.util.Constants;

public class ViewBidUserfragment extends Fragment implements OnClickListener,
		BackListener {

	final private static String TAG = "ViewBidUserfragment";
	private final int SUCCESS = 100;
	private final int FAILURE = 101;

	private ListView mListView = null;
	private ViewBidUserAdapter mJobListingAdapter = null;
	private ArrayList<NameValuePair> nameValuePairs = null;

	private static final int CATEGORY_LIST = 102;
	private static final int JOB_LIST = 103;

	private ArrayList<Category> catList = null;
	private String[] categoryNames = null;
	private String jobid = "";
	private String mJobMerch = "";

	private boolean paypalShow = false;

	// public static JobData jobDetail;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_courier_list, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		jobid = getArguments().getString("job_id");
		mJobMerch = getArguments().getString("job_merch");
		// jobDetail = (JobData) getArguments().getSerializable("data");
		mJobListingAdapter = new ViewBidUserAdapter(null, getActivity()
				.getLayoutInflater(), jobid, getFragmentManager(),
				getActivity());
		mJobListingAdapter.setMerch(mJobMerch);
		mJobListingAdapter
				.setAcceptListener(new ViewBidUserAdapter.OnAcceptListener() {

					@Override
					public void onClick(UserDetail user, BiddingDetail bd) {
						if (!paypalShow) {
							paypalShow = true;
						} else {
							return;
						}
						try {

							if (Constants.isNetAvailable(getActivity())) {
								ServerCommunicateFund servicefund = new ServerCommunicateFund(
										user.getId());
								servicefund.execute();
							} else {
								Constants.NoInternetError(getActivity());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

		mListView = (ListView) getView().findViewById(
				R.id.courier_list_listview);
		mListView.setAdapter(mJobListingAdapter);

		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("job_id", jobid));

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}

		((ViewJobsActivity) getActivity()).setTitle("List of Couriers");
		// ((ViewJobsActivity) getActivity()).SetBackText("Active jobs");
	}

	@Override
	public void onClick(View v) {

	}

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, ArrayList<CourierModel>> {
		private ProgressDialog progressBar = new ProgressDialog(getActivity());

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<CourierModel> doInBackground(String... strParams) {
			CourierListParser parser = new CourierListParser(
					Constants.HTTP_HOST + "ViewBidUsers");
			Log.e("VIPIII", "URL View> " + Constants.HTTP_HOST + "ViewBidUsers");

			ArrayList<CourierModel> dataList = parser
					.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ArrayList<CourierModel> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.what = JOB_LIST;
			msg.obj = dataList;
			handler.sendMessage(msg);
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
				ArrayList<CourierModel> dataList = (ArrayList<CourierModel>) msg.obj;
				CourierModel model = dataList.get(0);
				if (model.getStatus().equalsIgnoreCase("success")) {
					mJobListingAdapter.refereshAdapter(dataList);
				} else
					showAlertDialog(model.getMessage());
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

	public void onBack() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			((ViewJobsActivity) getActivity()).setTitle("Active Jobs");
		} else
			getActivity().finish();

	}

	@Override
	public void backPressed() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
	}

	private class ServerCommunicateFund extends
			AsyncTask<String, String, String> {

		String userid;

		public ServerCommunicateFund(String userid) {
			this.userid = userid;
		}

		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		@Override
		protected String doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobid));
			nameValuePairs.add(new BasicNameValuePair("user_id", userid));
			Log.e("VIPIII", "pass > " + jobid + " uid " + userid);
			FundPostParser parser = new FundPostParser(Constants.HTTP_HOST
					+ "fund");
			String dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setCancelable(false);
			progressBar.setCancelable(false);
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			onBack();
			Log.e("VIPIII", "dataList > " + dataList);
			if (!dataList.equals("")) {
				Intent intent = new Intent(getActivity(),
						PaypalWebviewAcivity.class);
				intent.putExtra("url", dataList);
				startActivity(intent);
			}
		}
	}

	// public void onBack() {
	// FragmentManager fm = getFragmentManager();
	// if (fm.getBackStackEntryCount() > 0) {
	// fm.popBackStack();
	// } else
	// getActivity().finish();
	// }

}
