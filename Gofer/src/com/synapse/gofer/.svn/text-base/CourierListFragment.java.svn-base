package com.synapse.gofer;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.synapse.gofer.adapter.ViewCustomerJobListingAdapter;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.util.Constants;

public class CourierListFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	private final int SUCCESS = 100;
	private final int FAILURE = 101;

	private ListView mListView = null;
	private ViewCustomerJobListingAdapter mJobListingAdapter = null;
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
		return inflater.inflate(R.layout.fragment_courier_list, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		mJobListingAdapter = new ViewCustomerJobListingAdapter(null,
				getActivity().getLayoutInflater());
		mListView = (ListView) getView().findViewById(
				R.id.courier_list_listview);
		mListView.setAdapter(mJobListingAdapter);

		mListView.setOnItemClickListener(this);
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}
	}

	@Override
	public void onClick(View v) {

	}

	/*
	 * To download data from server.
	 */
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
			JobsPostParser parser = new JobsPostParser(Constants.HTTP_HOST
					+ "viewCustomerJobs");
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
				ArrayList<JobsModel> dataList = (ArrayList<JobsModel>) msg.obj;
				JobsModel model = dataList.get(0);
				if (model.getStatus().equalsIgnoreCase("success")) {

					ArrayList<JobData> JobDatalist = new ArrayList<JobData>(
							Arrays.asList(model.getJobData()));
					mJobListingAdapter.refereshAdapter(JobDatalist);
				}
				// else
				// showAlertDialog(model.getMessage());
			}
			/*
			 * else if(msg.what == CATEGORY_LIST && msg.arg1 == SUCCESS) { if
			 * (msg.obj instanceof ArrayList) {
			 * 
			 * catList = (ArrayList<Category>)msg.obj; if (catList!= null){
			 * categoryNames = convertArrayListToArray(catList); }
			 * AlertDialog.Builder alertDialogBuilder = new
			 * AlertDialog.Builder(getActivity());
			 * alertDialogBuilder.setTitle("Select category");
			 * 
			 * alertDialogBuilder.setItems(categoryNames,new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { nameValuePairs = new ArrayList<NameValuePair>();
			 * nameValuePairs.add(new
			 * BasicNameValuePair("user_id",Constants.uid));
			 * nameValuePairs.add(new BasicNameValuePair("category_id",
			 * catList.get(which).getId())); ServerCommunication download=new
			 * ServerCommunication(); download.execute(new String[]{""}); } });
			 * alertDialogBuilder.create().show();
			 * 
			 * 
			 * } }
			 */
			else if (msg.arg1 == FAILURE) {
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		JobData job = mJobListingAdapter.getDataSource().get(position);

		BiddingDetail bid = job.getBidDetail();
		int x = Integer.parseInt(bid.getCount());
		if (x > 1) {

		}
		FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragmentpublic = new JobDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", job);
		fragmentpublic.setArguments(bundle);
		transaction.replace(R.id.mainContainer, fragmentpublic,
				"JobDetailFragment");
		transaction.addToBackStack("JobDetailFragment");
		transaction.commit();
	}
}
