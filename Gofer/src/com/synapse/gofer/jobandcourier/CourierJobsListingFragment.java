package com.synapse.gofer.jobandcourier;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

import com.synapse.gofer.JobAndCustomerDetailBaseFragment;
import com.synapse.gofer.R;
import com.synapse.gofer.ViewCustomerJobFragment;
import com.synapse.gofer.ViewJobsActivity;
import com.synapse.gofer.ViewJobsActivity.BackListener;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.parser.CategoryPostParser;
import com.synapse.gofer.parser.JobsPostParser;
import com.synapse.gofer.swipetodelete.SwipeMenu;
import com.synapse.gofer.swipetodelete.SwipeMenuCreator;
import com.synapse.gofer.swipetodelete.SwipeMenuItem;
import com.synapse.gofer.swipetodelete.SwipeMenuListView;
import com.synapse.gofer.util.Constants;

public class CourierJobsListingFragment extends Fragment implements
		OnClickListener, OnItemClickListener, BackListener {

	private final int SUCCESS = 100;
	private final int FAILURE = 101;

	private SwipeMenuListView mListView = null;
	private SwipeMenuListView newListView = null;
	private CourierJobListingAdapter mJobListingAdapter = null;
	private ArrayList<NameValuePair> nameValuePairs = null;

	private static final int CATEGORY_LIST = 102;
	private static final int JOB_LIST = 103;

	private ArrayList<Category> customerList = null;

	private String[] categoryNames = null;
	RadioButton btncustomer, btnprovider;
	String CustomerAPI = "viewCustomerJobs", ProviderAPI = "viewCouriersJobs";
	private Spinner spinnerCategory;
	boolean is_Customer = true;
	ArrayList<String> values;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_courier_jobs_listing,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi() {
		Log.e("VIPI", "initUi OnLoad Call is_Customer " + is_Customer);
		if (is_Customer) {
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment fragmentpublic = null;
			// title.setText("Customer Jobs");
			fragmentpublic = new ViewCustomerJobFragment();
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"ViewCustomerJobFragment");
			transaction.commit();
		} else {
			mJobListingAdapter = new CourierJobListingAdapter(null,
					getActivity().getLayoutInflater(), null);
			mListView = (SwipeMenuListView) getView().findViewById(
					R.id.view_customer_job_listview);
			newListView = (SwipeMenuListView) getView().findViewById(
					R.id.view_job_listview);

			// step 1. create a MenuCreator
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {
					// create "open" item
					SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
					// set item background
					openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
							0xC9, 0xCE)));
					// set item width
					openItem.setWidth(dp2px(90));
					// set item title
					openItem.setTitle("Archive");
					// set item title fontsize
					openItem.setTitleSize(18);
					// set item title font color
					openItem.setTitleColor(Color.WHITE);
					// add to menu
					menu.addMenuItem(openItem);

					// create "open" item
					SwipeMenuItem openItem2 = new SwipeMenuItem(getActivity());
					// set item background
					openItem2.setBackground(new ColorDrawable(Color.rgb(0xF9,
							0x3F, 0x25)));
					// set item width
					openItem2.setWidth(dp2px(90));
					// set item title
					openItem2.setTitle("Delete");
					// set item title fontsize
					openItem2.setTitleSize(18);
					// set item title font color
					openItem2.setTitleColor(Color.WHITE);
					// add to menu
					menu.addMenuItem(openItem2);

				}
			};

			// set creator
			Log.e("VIPVIP", "setMenuCreator >");
			mListView.setMenuCreator(creator);

			// step 2. listener item click event
			mListView
					.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(int position,
								SwipeMenu menu, int index) {
							switch (index) {
							case 0:
								// open
								Toast.makeText(getActivity(), "Archive",
										Toast.LENGTH_LONG).show();
								break;
							case 1:
								// delete
								// delete(item);
								Toast.makeText(getActivity(), "Delete",
										Toast.LENGTH_LONG).show();
							}
							return false;
						}
					});

			btncustomer = (RadioButton) getView()
					.findViewById(R.id.btncustomer);
			btnprovider = (RadioButton) getView()
					.findViewById(R.id.btnprovider);
			spinnerCategory = (Spinner) getView().findViewById(R.id.spinner);
			mListView.setAdapter(mJobListingAdapter);

			mListView.setOnItemClickListener(this);
			btncustomer.setOnClickListener(this);
			btnprovider.setOnClickListener(this);
			values = new ArrayList<String>();
			values.add("All");
			values.add("Bid");
			values.add("Active");
			values.add("Complete");
			values.add("Archive");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1,
					android.R.id.text1, values);
			spinnerCategory.setAdapter(adapter);
			spinnerCategory
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							if (position == 0)
								nameValuePairs.add(new BasicNameValuePair(
										"job_status", "all"));
							else if (position == 1)
								nameValuePairs.add(new BasicNameValuePair(
										"job_status", "P"));
							else if (position == 2)
								nameValuePairs.add(new BasicNameValuePair(
										"job_status", "A"));
							else if (position == 3)
								nameValuePairs.add(new BasicNameValuePair(
										"job_status", "C"));
							else if (position == 4)
								nameValuePairs.add(new BasicNameValuePair(
										"job_status", "V"));
							Log.e("VIP", "onItemSelected -> " + position);
							ServerCommunication download;
							if (is_Customer) {
								download = new ServerCommunication(CustomerAPI);
							} else {
								download = new ServerCommunication(ProviderAPI);
							}
							download.execute(new String[] { "" });
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});

			((ViewJobsActivity) getActivity()).setBackListener(this);

			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			Log.e("VIP", "user_id>> " + Constants.uid);
			ServerCommunication download = new ServerCommunication(CustomerAPI);
			download.execute(new String[] { "" });
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btncustomer) {
			is_Customer = true;
			ServerCommunication download = new ServerCommunication(CustomerAPI);
			download.execute(new String[] { "" });

		}
		if (v == btnprovider) {
			is_Customer = false;
			ServerCommunication download = new ServerCommunication(ProviderAPI);
			download.execute(new String[] { "" });
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
			Log.e("TAGG", "REQ Courier > " + Constants.HTTP_HOST + apitype
					+ " Size " + nameValuePairs.size());
			JobsPostParser parser = new JobsPostParser(Constants.HTTP_HOST
					+ apitype);
			ArrayList<JobsModel> dataList = parser.getParseData(nameValuePairs);
			Log.e("VIP", "COUR > " + dataList.size());

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
			if (dataList.size() == 0) {
				mListView.setVisibility(View.GONE);
			} else {
				mListView.setVisibility(View.VISIBLE);
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
				if (dataList == null)
					return;
				JobsModel model = dataList.get(0);
				if (model.getStatus().equalsIgnoreCase("success")) {
					mJobListingAdapter.refereshAdapter(model.getJobData(),
							model.getCheckBids());
				}
				// else
				// showAlertDialog(model.getMessage());
			} else if (msg.what == CATEGORY_LIST && msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ArrayList) {

					customerList = (ArrayList<Category>) msg.obj;
					if (customerList != null) {
						categoryNames = convertArrayListToArray(customerList);
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
											"category_id", customerList.get(
													which).getId()));
									ServerCommunication download;
									if (is_Customer) {
										download = new ServerCommunication(
												CustomerAPI);
									} else {
										download = new ServerCommunication(
												ProviderAPI);
									}

									if (Constants.isNetAvailable(getActivity())) {
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
				// showAlertDialog("Please try again.");
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
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<Category> doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// nameValuePairs.add(new BasicNameValuePair("id",Constants.uid));
			Log.e("VIP", "HTTP_HOST " + Constants.HTTP_HOST + "viewcategories");
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
		} else
			getActivity().finish();
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
		bundle.putBoolean("displaybid", false);
		fragmentpublic.setArguments(bundle);
		transaction.replace(R.id.mainContainer, fragmentpublic,
				"JobAndCustomerDetailBaseFragment");
		transaction.addToBackStack("JobAndCustomerDetailBaseFragment");

		transaction.commit();

		/*
		 * FragmentManager fragmentManager = getActivity()
		 * .getSupportFragmentManager(); FragmentTransaction transaction =
		 * fragmentManager.beginTransaction(); Fragment fragmentpublic = null;
		 * 
		 * // title.setText("Customer Jobs"); fragmentpublic = new
		 * ViewCustomerJobFragment(); transaction.replace(R.id.mainContainer,
		 * fragmentpublic, "ViewCustomerJobFragment"); transaction.commit();
		 */
	}

	@Override
	public void backPressed() {
		onBack();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
