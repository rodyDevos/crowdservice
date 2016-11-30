package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.synapse.gofer.ViewJobsActivity.BackListener;
import com.synapse.gofer.adapter.ViewCustomerJobListingAdapter;
import com.synapse.gofer.jobandcourier.JobAndCourierDetailBaseFragment;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.swipetodelete.SwipeMenu;
import com.synapse.gofer.swipetodelete.SwipeMenuCreator;
import com.synapse.gofer.swipetodelete.SwipeMenuItem;
import com.synapse.gofer.swipetodelete.SwipeMenuListView;
import com.synapse.gofer.swipetodelete.SwipeMenuListView.OnMenuItemClickListener;
import com.synapse.gofer.util.Constants;

public class ViewCustomerJobFragment extends Fragment implements
		OnClickListener, OnItemClickListener, BackListener,
		OnMenuItemClickListener {

	private final int SUCCESS = 100;
	private final int FAILURE = 101;

	private SwipeMenuListView mListView = null, new_ListView;
	private ViewCustomerJobListingAdapter mJobListingAdapter = null;
	private ArrayList<NameValuePair> nameValuePairs = null;

	private static final int CATEGORY_LIST = 102;
	private static final int JOB_LIST = 103;

	RadioButton btncustomer, btnprovider;

	private ArrayList<Category> catList = null;
	private String[] categoryNames = null;
	ArrayList<String> values;
	private Spinner spinnerCategory;
	boolean is_Customer = true;
	String CustomerAPI = "viewCustomerJobs", ProviderAPI = "viewCouriersJobs";
	boolean is_single_option = false;
	public ArrayList<JobData> JobDatalist;
	SwipeMenuCreator creator;

	int last_job = -1;
	SwipeMenuItem ArchiveItem, DeleteItem;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_customer_jobs,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		try {
			Log.e("VIPIIIV", "initUi OnLoad Call 1. " + btncustomer.isChecked()
					+ " 2. " + btnprovider.isChecked());
			is_single_option = true;
		} catch (Exception e) {
			Log.e("VIPIIIV", "Error>> " + e.toString());
			is_single_option = false;
		}
		mJobListingAdapter = new ViewCustomerJobListingAdapter(null,
				getActivity().getLayoutInflater());

		mListView = (SwipeMenuListView) getView().findViewById(
				R.id.view_customer_job_listview);
		new_ListView = (SwipeMenuListView) getView().findViewById(
				R.id.view_provider_job_listview);

		mListView.setOnItemClickListener(this);
		new_ListView.setOnItemClickListener(this);

		// step 2. listener item click event

		btncustomer = (RadioButton) getView().findViewById(R.id.btncustomer);
		btnprovider = (RadioButton) getView().findViewById(R.id.btnprovider);

		spinnerCategory = (Spinner) getView().findViewById(R.id.spinner);
		spinnerCategory.setVisibility(View.GONE);
		
		mListView.setAdapter(mJobListingAdapter);
		new_ListView.setAdapter(mJobListingAdapter);

		((ViewJobsActivity) getActivity()).setBackListener(this);
		mListView.setOnItemClickListener(this);
		new_ListView.setOnItemClickListener(this);

		mListView.setOnMenuItemClickListener(this);
		new_ListView.setOnMenuItemClickListener(this);

		btncustomer.setOnClickListener(this);
		btnprovider.setOnClickListener(this);

		values = new ArrayList<String>();
		
		values.add("All");
		values.add("Bid");
		values.add("Active");
		values.add("Complete");
		values.add("Archive");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		spinnerCategory.setAdapter(adapter);
		
		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				nameValuePairs.clear();
				
				if (position == 0)
					nameValuePairs.add(new BasicNameValuePair("job_status",
							"all"));
				else if (position == 1)
					nameValuePairs
							.add(new BasicNameValuePair("job_status", "P"));
				else if (position == 2)
					nameValuePairs
							.add(new BasicNameValuePair("job_status", "A"));
				else if (position == 3)
					nameValuePairs
							.add(new BasicNameValuePair("job_status", "C"));
				else if (position == 4)
					nameValuePairs
							.add(new BasicNameValuePair("job_status", "V"));
				
				nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
				
				Log.e("VIP", "onItemSelected -> " + position + " 0."
						+ nameValuePairs.get(0));
				ServerCommunication download;
				if (is_Customer) {
					download = new ServerCommunication(CustomerAPI);
				} else {
					download = new ServerCommunication(ProviderAPI);
				}

				if (Constants.isNetAvailable(getActivity())) {
					download.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(getActivity());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

//		if (Constants.isNetAvailable(getActivity())) {
//			ServerCommunication download = new ServerCommunication(CustomerAPI);
//			download.execute(new String[] { "" });
//		} else {
//			Constants.NoInternetError(getActivity());
//		}
		
		//spinnerCategory.setSelection(0);
		
		btncustomer.performClick();
	}

	@Override
	public void onClick(View v) {
		if (v == btncustomer) {

			is_Customer = true;
			is_single_option = false;
			Constants.is_customer = true;

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication download = new ServerCommunication(
						CustomerAPI);
				download.execute(new String[] { "" });
			} else {
				Constants.NoInternetError(getActivity());
			}

		}
		if (v == btnprovider) {

			is_Customer = false;
			is_single_option = true;
			Constants.is_customer = false;

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication download = new ServerCommunication(
						ProviderAPI);
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
		String apitype;

		public ServerCommunication(String apitype) {
			// TODO Auto-generated constructor stub
			this.apitype = apitype;
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<JobsModel> doInBackground(String... strParams) {
			
			if (is_Customer) {
				apitype = CustomerAPI;
			}else{
				apitype = ProviderAPI;
			}
			JobsPostParser parser = new JobsPostParser(Constants.HTTP_HOST
					+ apitype);
			
			nameValuePairs = new ArrayList<NameValuePair>();
			
			nameValuePairs.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("job_status",
					"all"));
			
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

				if (dataList == null)
					return;
				JobsModel model = dataList.get(0);
				
				if (model.getStatus().equalsIgnoreCase("fail")) {
					
					mListView.setVisibility(View.GONE);
					new_ListView.setVisibility(View.GONE);
					return;
				}
				
				try {
					Log.e("VIPI", "model  " + model.getJobData().length);
					mListView.setVisibility(View.VISIBLE);
					new_ListView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					mListView.setVisibility(View.GONE);
					new_ListView.setVisibility(View.GONE);
				}
				if (model.getStatus().equalsIgnoreCase("success")) {
					JobDatalist = new ArrayList<JobData>(Arrays.asList(model
							.getJobData()));
					mJobListingAdapter.refereshAdapter(JobDatalist);
				}
				Log.e("VIPVIP", " is_single_option ... " + is_single_option);
				if (is_single_option) {
					// step 1. create a MenuCreator
					creator = new SwipeMenuCreator() {

						@Override
						public void create(SwipeMenu menu) {
							menu.removeMenuItem(ArchiveItem);
							menu.removeMenuItem(DeleteItem);
							Log.e("VIPVIP", "btnprovider>> Size "
									+ menu.getMenuItems().size());

							// create "open" item
							DeleteItem = new SwipeMenuItem(getActivity());
							// set item background
							DeleteItem.setBackground(new ColorDrawable(Color
									.rgb(0xF9, 0x3F, 0x25)));
							// set item width
							DeleteItem.setWidth(dp2px(90));
							// set item title
							DeleteItem.setTitle("Delete");
							// set item title fontsize
							DeleteItem.setTitleSize(18);
							// set item title font color
							DeleteItem.setTitleColor(Color.WHITE);
							// add to menu
							menu.addMenuItem(DeleteItem);
							Log.e("VIPVIP", "btnprovider >> A "
									+ menu.getMenuItems().size());

						}
					};

					mListView.setVisibility(View.GONE);
					new_ListView.setVisibility(View.VISIBLE);
					// set creator
					new_ListView.setMenuCreator(creator);
				} else {

					// step 1. create a MenuCreator
					creator = new SwipeMenuCreator() {

						@Override
						public void create(SwipeMenu menu) {
							menu.removeMenuItem(ArchiveItem);
							menu.removeMenuItem(DeleteItem);
							Log.e("VIPVIP", " btncustomer >> Size "
									+ menu.getMenuItems().size());

							// create "open" item
							ArchiveItem = new SwipeMenuItem(getActivity());
							// set item background
							ArchiveItem.setBackground(new ColorDrawable(Color
									.rgb(0xC9, 0xC9, 0xCE)));
							// set item width
							ArchiveItem.setWidth(dp2px(90));
							// set item title
							ArchiveItem.setTitle("Archive");
							// set item title fontsize
							ArchiveItem.setTitleSize(18);
							// set item title font color
							ArchiveItem.setTitleColor(Color.WHITE);
							// add to menu
							menu.addMenuItem(ArchiveItem);

							// create "open" item
							DeleteItem = new SwipeMenuItem(getActivity());
							// set item background
							DeleteItem.setBackground(new ColorDrawable(Color
									.rgb(0xF9, 0x3F, 0x25)));
							// set item width
							DeleteItem.setWidth(dp2px(90));
							// set item title
							DeleteItem.setTitle("Delete");
							// set item title fontsize
							DeleteItem.setTitleSize(18);
							// set item title font color
							DeleteItem.setTitleColor(Color.WHITE);
							// add to menu
							menu.addMenuItem(DeleteItem);

						}
					};

					mListView.setVisibility(View.VISIBLE);
					new_ListView.setVisibility(View.GONE);
					// set creator
					mListView.setMenuCreator(creator);
				}

				try {
					if (dataList.get(0).getJobData().length == 0) {
						mListView.setVisibility(View.GONE);
						new_ListView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					Log.e("VIPI", "E." + e.toString());
					e.getStackTrace();
					mListView.setVisibility(View.GONE);
					new_ListView.setVisibility(View.GONE);
				}

			} else if (msg.arg1 == FAILURE) {
				// showAlertDialog("No Jobs found");
			}
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
		} else
			getActivity().finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		JobData job = mJobListingAdapter.getDataSource().get(position);

		BiddingDetail bid = job.getBidDetail();
		int x = 0;
		try {
			x = Integer.parseInt(bid.getCount());
		} catch (Exception e) {
			x = 0;
		}

		if (bid.getLinkStatus().contains("A")
				|| bid.getLinkStatus().contains("C")) {
			Log.d("test", "test1");
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment fragmentpublic = new JobAndCourierDetailBaseFragment();
			Bundle bundle = new Bundle();
			Log.e("SAMPLE", " Constants.userType " + Constants.userType);
			if (Constants.userType == 2) {
				Log.e("VIPVIPP", " Job> " + job.getJob().getUserId());
				if (bid.getLinkStatus().contains("A")) {

				} else {
					Constants.tempuserid = job.getJob().getUserId();
				}

			} else {

				Log.e("VIPVIPP", "B User " + job.getJob().getUserId());
				Constants.tempuserid = job.getJob().getUserId();

			}

			Log.e("VIPVIPP", " Redirect> temp." + Constants.tempuserid
					+ " .uid. " + Constants.uid);

			bundle.putString("data", job.getJob().getId());
			// bundle.putString("courierId", ""); // crash

			try {
				Log.e("VIPIII", "uid > " + Constants.uid + " .. "
						+ job.getJob().getId());
				// Log.e("VIPIII", "putString > " + job.getUser().getId()
				// + " Blank " + bid.getLinkStatus().contains("C"));
				if (bid.getLinkStatus().contains("C")) {
					bundle.putString("courierId", ""); // crash
				} else {
					bundle.putString("courierId", job.getUser().getId()); // crash
				}
			} catch (Exception ex) {
				Log.e("VIPIII", "putString > crash " + ex.toString());
				ex.printStackTrace();
				bundle.putString("courierId", ""); // crash
			}
			bundle.putInt("bidCount", 1);
			bundle.putString("status", bid.getLinkStatus());
			bundle.putBoolean("onlyJobDetail", false);
			fragmentpublic.setArguments(bundle);
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"JobAndCourierDetailBaseFragment");
			transaction.addToBackStack("JobAndCourierDetailBaseFragment");

			transaction.commit();
		} else if (x == 0) {
			Log.d("test", "test3");
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment fragmentpublic = new JobAndCourierDetailBaseFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data", job.getJob().getId());
			// bundle.putString("courierId", "");
			try {
				bundle.putString("courierId", job.getUser().getId());
			} catch (Exception ex) {
				bundle.putString("courierId", "");// crash
			}
			bundle.putInt("bidCount", 0);
			bundle.putString("status", bid.getLinkStatus());
			bundle.putBoolean("jobAccept", true);
			bundle.putBoolean("onlyJobDetail", true);
			bundle.putSerializable("job_id", job.getJob().getId());
			bundle.putSerializable("merch", job.getJob().getAmount());
			fragmentpublic.setArguments(bundle);
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"JobAndCourierDetailBaseFragment");
			transaction.addToBackStack("JobAndCourierDetailBaseFragment");

			transaction.commit();
		} else {
			Log.d("test", "test2");
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment fragmentpublic = new ViewBidUserfragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("job_id", job.getJob().getId());
			bundle.putSerializable("job_merch", job.getJob().getAmount());
			fragmentpublic.setArguments(bundle);
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"ViewBidUserfragment");
			transaction.addToBackStack("ViewBidUserfragment");
			transaction.commit();
		}

	}

	@Override
	public void backPressed() {
		onBack();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void doDelete(final String jobid, String message, final int position) {
		final ProgressDialog progressBar = new ProgressDialog(getActivity());
		progressBar.setMessage(message);
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					Log.e("VIPI", "PARAM >> " + Constants.uid + " > " + jobid);
					reqEntity.addPart("user_id", new StringBody(Constants.uid));
					reqEntity.addPart("job_id", new StringBody(jobid));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "deleteJob");
				httppost.setEntity(reqEntity);
				
				httppost.setHeader("Cookie", Constants.cookie);
				// Execute HTTP Post Request
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					JSONObject jsonresponse = new JSONObject(strResponse);
					Log.e("VIPI", "Response Delete> " + strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
						resultData.setJobName(jsonresponse.getString("job_name"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
					last_job = position;

				} else {
					msg.arg1 = FAILURE;
				}
				deletehandler.sendMessage(msg);
			}
		});
		thread.start();
	}

	private void doDeleteBid(final String bidId, String message, final int position) {
		final ProgressDialog progressBar = new ProgressDialog(getActivity());
		progressBar.setMessage(message);
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					Log.e("VIPI", "PARAM >> " + Constants.uid + " > " + bidId);
					reqEntity.addPart("bid_id", new StringBody(bidId));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "deleteBid");
				httppost.setEntity(reqEntity);
				
				httppost.setHeader("Cookie", Constants.cookie);
				// Execute HTTP Post Request
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					JSONObject jsonresponse = new JSONObject(strResponse);
					Log.e("VIPI", "Response Delete> " + strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
						resultData.setJobName(jsonresponse.getString("job_name"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
					last_job = position;

				} else {
					msg.arg1 = FAILURE;
				}
				deletehandler.sendMessage(msg);
			}
		});
		thread.start();
	}
	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.e("HttpReaderException", ">>>" + e.getMessage());
		}
		return sb.toString();
	}

	/*
	 * Delete Job handler.
	 */
	private Handler deletehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.arg1 == SUCCESS) {

				if (msg.obj instanceof ResultData) {
					Log.e("VIPI", "Job Complete>> " + last_job);
					if (last_job != -1) {
						JobDatalist.remove(last_job);
					}
					// JobData[] mStringArray = new JobData[JobDatalist.size()];
					// mStringArray = JobDatalist.toArray(mStringArray);
					Log.e("VIPI", " JobDatalist REF >> " + JobDatalist.size());
					mJobListingAdapter.refereshAdapter(JobDatalist);
					ResultData data = (ResultData) msg.obj;
					showAlertDialog(data.getMessage());
					
					String resultMsg = "";
					if(is_Customer){
						resultMsg = "You've deleted the job, '" + data.getJobName() + "' .";
						ShowAlertPopup(resultMsg);
					}
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			if (is_single_option) {
				ShowDeleteOption(position, JobDatalist.get(position)
						.getBidDetail().getLinkStatus(), true);
			} else {
				ShowDeleteOption(position, JobDatalist.get(position)
						.getBidDetail().getLinkStatus(), false);
			}
			break;
		case 1:
			ShowDeleteOption(position, JobDatalist.get(position).getBidDetail()
					.getLinkStatus(), true);
		}

		return false;
	}

	private void doArchive(final String jobid, String message,
			final int position) {
		final ProgressDialog progressBar = new ProgressDialog(getActivity());
		progressBar.setMessage(message);
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					reqEntity.addPart("job_id", new StringBody(jobid));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "archiveJob");
				httppost.setEntity(reqEntity);

				// Execute HTTP Post Request
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					JSONObject jsonresponse = new JSONObject(strResponse);
					Log.e("VIPI", "Response Delete> " + strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
						resultData.setJobName(jsonresponse.getString("job_name"));
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressBar.dismiss();

				Message msg = archivehandler.obtainMessage();
				if (resultData != null) {
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
					last_job = position;

				} else {
					msg.arg1 = FAILURE;
				}
				archivehandler.sendMessage(msg);
			}
		});
		thread.start();
	}

	private void ShowDeleteOption(final int position, String status,
			final boolean is_delete) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				(is_delete) ? getString(R.string.txtsuredelete)
						: getString(R.string.txtsurearchive))
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (is_delete) {
									if (JobDatalist.get(position)
											.getBidDetail().getLinkStatus()
											.equals("P")) {
										
										if(is_Customer)
											doDelete(
													JobDatalist.get(position)
															.getJob().getId(),
													"Please wait until job deleted on server",
													position);
										else{
											
											BiddingDetail bidDetail = JobDatalist.get(position).getBidDetail();
											
											Log.d("Bid Detail", bidDetail.toString());
											doDeleteBid(
													JobDatalist.get(position)
															.getBidDetail().getId(),
													"Please wait until bid deleted on server",
													position);
										}
											
									} else {
										ShowAlertPopup(getString(R.string.txtshowdeletemsg));
									}
								} else {
									if (JobDatalist.get(position)
											.getBidDetail().getLinkStatus()
											.equals("C")) {
										doArchive(
												JobDatalist.get(position)
														.getJob().getId(),
												"Please wait until job deleted on server",
												position);
									} else {
										ShowAlertPopup(getString(R.string.txtshowarchive));
									}
								}

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void ShowAlertPopup(String str) {
		
		Log.d("Alert", str);
		
		new AlertDialog.Builder(getActivity()).setTitle("Job Delete")
		.setMessage(str)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// continue with delete
				dialog.dismiss();
			}
		}).setIcon(android.R.drawable.ic_dialog_info).show();
		
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setTitle("Job Delete")
//				.setMessage(str).setCancelable(false)
//				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						dialog.dismiss();
//					}
//				}).setIcon(android.R.drawable.ic_dialog_info).show();
//		
//		AlertDialog alert = builder.create();
//		alert.show();
	}

	/*
	 * Archive Job handler.
	 */
	private Handler archivehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.arg1 == SUCCESS) {

				if (msg.obj instanceof ResultData) {
					if (last_job != -1) {
						BiddingDetail bidDetail = JobDatalist.get(last_job)
								.getBidDetail();
						bidDetail.setLinkStatus("V");
						JobDatalist.get(last_job).setBidDetail(bidDetail);
					}
					mJobListingAdapter.refereshAdapter(JobDatalist);
					ResultData data = (ResultData) msg.obj;
					showAlertDialog(data.getMessage());

				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};

}